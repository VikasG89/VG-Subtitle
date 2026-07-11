#ifndef WHISPER_RUNTIME_H
#define WHISPER_RUNTIME_H

#include <string>
#include <memory>
#include <vector>

namespace vg::subtitle::whisper {

struct WhisperSegment {
    int index;
    long long t0;
    long long t1;
    std::string text;
    float confidence;
};

/**
 * Main coordinator for the Whisper.cpp engine.
 */
class WhisperRuntime {
public:
    WhisperRuntime();
    ~WhisperRuntime();

    bool initialize(const std::string& modelPath);
    std::string detectLanguage(const std::vector<float>& pcm);
    std::vector<WhisperSegment> transcribe(const std::vector<float>& pcm, const std::string& language);

    void release();

private:
    class Impl;
    std::unique_ptr<Impl> m_impl;
};

} // namespace vg::subtitle::whisper

#endif // WHISPER_RUNTIME_H
