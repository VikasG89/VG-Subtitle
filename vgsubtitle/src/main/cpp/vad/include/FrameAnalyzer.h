#ifndef VG_SUBTITLE_FRAME_ANALYZER_H
#define VG_SUBTITLE_FRAME_ANALYZER_H

#include <vector>
#include <cstdint>

namespace vg::subtitle::vad {

class FrameAnalyzer {
public:
    virtual ~FrameAnalyzer() = default;
    virtual float analyzeFrame(const float* samples, size_t length) = 0;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_FRAME_ANALYZER_H
