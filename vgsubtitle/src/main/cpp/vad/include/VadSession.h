#ifndef VG_SUBTITLE_VAD_SESSION_H
#define VG_SUBTITLE_VAD_SESSION_H

#include <vector>
#include <cstdint>

namespace vg::subtitle::vad {

struct SpeechSegment {
    int64_t startMs;
    int64_t endMs;
    float confidence;
};

class VadSession {
public:
    virtual ~VadSession() = default;

    virtual std::vector<SpeechSegment> process(const float* audioData, size_t length) = 0;

    virtual void reset() = 0;

    virtual std::vector<SpeechSegment> flush() = 0;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_VAD_SESSION_H
