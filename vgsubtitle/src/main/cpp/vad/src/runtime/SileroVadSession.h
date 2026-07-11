#ifndef VG_SUBTITLE_SILERO_VAD_SESSION_H
#define VG_SUBTITLE_SILERO_VAD_SESSION_H

#include "VadSession.h"
#include "VadConfig.h"
#include <vector>

namespace vg::subtitle::vad {

class SileroVadSession : public VadSession {
public:
    explicit SileroVadSession(const VadConfig& config);
    ~SileroVadSession() override = default;

    std::vector<SpeechSegment> process(const float* audioData, size_t length) override;
    void reset() override;
    std::vector<SpeechSegment> flush() override;

private:
    VadConfig config_;
    std::vector<float> buffer_;
    // Additional state for segmentation logic
    bool speechStarted_ = false;
    int64_t currentMs_ = 0;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_SILERO_VAD_SESSION_H
