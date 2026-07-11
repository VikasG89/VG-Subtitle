#include "InferenceEngine.h"
#include "SegmentAssembler.h"
#include <thread>
#include <algorithm>

namespace vg::subtitle::whisper {

InferenceEngine::InferenceEngine(WhisperSession& session) : m_session(session) {}

bool InferenceEngine::run(const std::vector<float>& pcm,
                         const std::string& language,
                         std::vector<WhisperSegment>& outSegments) {
    auto ctx = m_session.getContext();
    std::lock_guard<std::mutex> lock(m_session.getMutex());

    whisper_full_params params = whisper_full_default_params(WHISPER_SAMPLING_GREEDY);
    params.language = language == "auto" ? nullptr : language.c_str();
    params.n_threads = std::min(6u, std::max(1u, std::thread::hardware_concurrency()));

    if (whisper_full(ctx, params, pcm.data(), (int)pcm.size()) != 0) {
        return false;
    }

    outSegments = SegmentAssembler::assemble(ctx);
    return true;
}

} // namespace vg::subtitle::whisper
