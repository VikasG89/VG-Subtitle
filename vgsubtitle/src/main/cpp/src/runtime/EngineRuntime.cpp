#include "EngineRuntime.h"
#include "AudioDecoder.h"
#include <stdexcept>
#include <thread>
#include <algorithm>

namespace vg::subtitle {

EngineRuntime::EngineRuntime() = default;
EngineRuntime::~EngineRuntime() = default;

bool EngineRuntime::initialize(const std::string& modelPath) {
    whisper_context_params params = whisper_context_default_params();
    params.use_gpu = true;

    struct whisper_context* ctx = whisper_init_from_file_with_params(modelPath.c_str(), params);
    if (!ctx) return false;

    m_context = std::make_unique<RuntimeContext>(ctx);
    return true;
}

bool EngineRuntime::verifyModel(const std::string& modelPath) {
    FILE* f = fopen(modelPath.c_str(), "rb");
    if (f) {
        fclose(f);
        return true;
    }
    return false;
}

void EngineRuntime::release() {
    m_context.reset();
}

std::string EngineRuntime::detectLanguage(const std::string& pcmPath) {
    if (!m_context) return "und";

    std::vector<float> samples = AudioDecoder::decodeWav(pcmPath);
    if (samples.empty()) return "und";

    std::lock_guard<std::mutex> lock(m_context->getMutex());
    int n_samples = std::min((int)samples.size(), WHISPER_SAMPLE_RATE * 30);
    unsigned int cores = std::thread::hardware_concurrency();
    int n_threads = std::min(6u, std::max(1u, cores));

    if (whisper_pcm_to_mel(m_context->getWhisperContext(), samples.data(), n_samples, n_threads) != 0) {
        return "und";
    }

    int lang_id = whisper_lang_auto_detect(m_context->getWhisperContext(), 0, n_threads, nullptr);
    if (lang_id < 0) return "und";

    return whisper_lang_str(lang_id);
}

std::vector<Segment> EngineRuntime::generateSubtitles(const std::string& pcmPath, long long chunkDurationMs, const std::string& language) {
    if (!m_context) return {};

    std::vector<float> samples = AudioDecoder::decodeWav(pcmPath);
    if (chunkDurationMs > 0) {
        size_t max_samples = static_cast<size_t>(chunkDurationMs * WHISPER_SAMPLE_RATE / 1000);
        if (samples.size() > max_samples) samples.resize(max_samples);
    }

    std::lock_guard<std::mutex> lock(m_context->getMutex());
    whisper_full_params params = whisper_full_default_params(WHISPER_SAMPLING_GREEDY);
    params.language = language == "auto" ? nullptr : language.c_str();
    params.n_threads = std::min(6u, std::max(1u, std::thread::hardware_concurrency()));
    params.suppress_blank = true;
    params.suppress_nst = true;
    params.audio_ctx = 448;

    if (whisper_full(m_context->getWhisperContext(), params, samples.data(), (int)samples.size()) != 0) {
        return {};
    }

    std::vector<Segment> results;
    int count = whisper_full_n_segments(m_context->getWhisperContext());
    for (int i = 0; i < count; ++i) {
        const int64_t t0 = whisper_full_get_segment_t0(m_context->getWhisperContext(), i) * 10;
        const int64_t t1 = whisper_full_get_segment_t1(m_context->getWhisperContext(), i) * 10;
        const char* text = whisper_full_get_segment_text(m_context->getWhisperContext(), i);

        int n_tokens = whisper_full_n_tokens(m_context->getWhisperContext(), i);
        float sum_p = 0;
        for (int j = 0; j < n_tokens; ++j) {
            sum_p += whisper_full_get_token_p(m_context->getWhisperContext(), i, j);
        }
        float confidence = n_tokens > 0 ? sum_p / n_tokens : 0.0f;

        results.push_back({i, t0, t1, text, language, confidence});
    }

    return results;
}

} // namespace vg::subtitle
