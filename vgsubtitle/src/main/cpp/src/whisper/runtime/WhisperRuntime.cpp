#include "WhisperRuntime.h"
#include "WhisperSession.h"
#include "SegmentAssembler.h"
#include "Logger.h"
#include <thread>
#include <algorithm>

namespace vg::subtitle::whisper {

class WhisperRuntime::Impl {
public:
    std::unique_ptr<WhisperSession> session;

    bool initialize(const std::string& modelPath) {
        whisper_context_params params = whisper_context_default_params();
        params.use_gpu = true;

        struct whisper_context* ctx = whisper_init_from_file_with_params(modelPath.c_str(), params);
        if (!ctx) {
            Logger::e("Failed to initialize whisper context from " + modelPath);
            return false;
        }

        session = std::make_unique<WhisperSession>(ctx);
        return true;
    }
};

WhisperRuntime::WhisperRuntime() : m_impl(std::make_unique<Impl>()) {}
WhisperRuntime::~WhisperRuntime() = default;

bool WhisperRuntime::initialize(const std::string& modelPath) {
    return m_impl->initialize(modelPath);
}

std::string WhisperRuntime::detectLanguage(const std::vector<float>& pcm) {
    if (!m_impl->session) return "und";

    auto ctx = m_impl->session->getContext();
    std::lock_guard<std::mutex> lock(m_impl->session->getMutex());

    int n_samples = std::min((int)pcm.size(), WHISPER_SAMPLE_RATE * 30);
    int n_threads = std::min(6u, std::max(1u, std::thread::hardware_concurrency()));

    if (whisper_pcm_to_mel(ctx, pcm.data(), n_samples, n_threads) != 0) return "und";

    int lang_id = whisper_lang_auto_detect(ctx, 0, n_threads, nullptr);
    if (lang_id < 0) return "und";

    return whisper_lang_str(lang_id);
}

std::vector<WhisperSegment> WhisperRuntime::transcribe(const std::vector<float>& pcm, const std::string& language) {
    if (!m_impl->session) return {};

    auto ctx = m_impl->session->getContext();
    std::lock_guard<std::mutex> lock(m_impl->session->getMutex());

    whisper_full_params params = whisper_full_default_params(WHISPER_SAMPLING_GREEDY);
    params.language = language == "auto" ? nullptr : language.c_str();
    params.n_threads = std::min(6u, std::max(1u, std::thread::hardware_concurrency()));
    params.audio_ctx = 448;

    if (whisper_full(ctx, params, pcm.data(), (int)pcm.size()) != 0) return {};

    return SegmentAssembler::assemble(ctx);
}

void WhisperRuntime::release() {
    m_impl->session.reset();
}

} // namespace vg::subtitle::whisper
