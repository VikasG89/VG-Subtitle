#include "WhisperLanguageDetector.h"
#include <thread>
#include <algorithm>

namespace vg::subtitle::whisper {

LanguageDetector::LanguageDetector(WhisperSession& session) : m_session(session) {}

std::string LanguageDetector::detect(const std::vector<float>& pcm) {
    auto ctx = m_session.getContext();
    std::lock_guard<std::mutex> lock(m_session.getMutex());

    int n_samples = std::min((int)pcm.size(), WHISPER_SAMPLE_RATE * 30);
    int n_threads = std::min(6u, std::max(1u, std::thread::hardware_concurrency()));

    if (whisper_pcm_to_mel(ctx, pcm.data(), n_samples, n_threads) != 0) return "und";

    int lang_id = whisper_lang_auto_detect(ctx, 0, n_threads, nullptr);
    if (lang_id < 0) return "und";

    return whisper_lang_str(lang_id);
}

} // namespace vg::subtitle::whisper
