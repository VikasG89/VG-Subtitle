#ifndef INFERENCE_ENGINE_H
#define INFERENCE_ENGINE_H

#include "WhisperSession.h"
#include "WhisperRuntime.h"
#include <vector>

namespace vg::subtitle::whisper {

class InferenceEngine {
public:
    explicit InferenceEngine(WhisperSession& session);

    bool run(const std::vector<float>& pcm,
             const std::string& language,
             std::vector<WhisperSegment>& outSegments);

private:
    WhisperSession& m_session;
};

} // namespace vg::subtitle::whisper

#endif // INFERENCE_ENGINE_H
