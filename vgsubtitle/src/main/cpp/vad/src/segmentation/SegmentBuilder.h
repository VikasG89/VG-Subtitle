#ifndef VG_SUBTITLE_SEGMENT_BUILDER_H
#define VG_SUBTITLE_SEGMENT_BUILDER_H

#include "VadSession.h"
#include "VadConfig.h"
#include <vector>

namespace vg::subtitle::vad {

class SegmentBuilder {
public:
    explicit SegmentBuilder(const VadConfig& config);

    void addFrame(bool isSpeech, float confidence, int64_t timestampMs);
    std::vector<SpeechSegment> collectSegments(bool finalize);
    void reset();

private:
    VadConfig config_;
    std::vector<SpeechSegment> segments_;
    bool inSpeech_ = false;
    int64_t speechStartMs_ = 0;
    int64_t lastSpeechMs_ = 0;
    int64_t lastSilenceMs_ = 0;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_SEGMENT_BUILDER_H
