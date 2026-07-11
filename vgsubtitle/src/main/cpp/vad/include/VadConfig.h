#ifndef VG_SUBTITLE_VAD_CONFIG_H
#define VG_SUBTITLE_VAD_CONFIG_H

#include <cstdint>

namespace vg::subtitle::vad {

struct VadConfig {
    float speechThreshold = 0.5f;
    int32_t minSpeechDurationMs = 250;
    int32_t minSilenceDurationMs = 100;
    int32_t prePaddingMs = 32;
    int32_t postPaddingMs = 32;
    int32_t maxSegmentLengthMs = 30000;
    int32_t segmentOverlapMs = 0;
    float confidenceThreshold = 0.5f;
    int32_t sampleRate = 16000;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_VAD_CONFIG_H
