#include "SegmentBuilder.h"
#include <algorithm>

namespace vg::subtitle::vad {

SegmentBuilder::SegmentBuilder(const VadConfig& config) : config_(config) {}

void SegmentBuilder::addFrame(bool isSpeech, float confidence, int64_t timestampMs) {
    if (isSpeech) {
        if (!inSpeech_) {
            inSpeech_ = true;
            speechStartMs_ = std::max<int64_t>(0, timestampMs - config_.prePaddingMs);
        }
        lastSpeechMs_ = timestampMs;
    } else {
        if (inSpeech_) {
            if (timestampMs - lastSpeechMs_ >= config_.minSilenceDurationMs) {
                inSpeech_ = false;
                int64_t endMs = lastSpeechMs_ + config_.postPaddingMs;
                if (endMs - speechStartMs_ >= config_.minSpeechDurationMs) {
                    segments_.push_back({speechStartMs_, endMs, confidence});
                }
            }
        }
        lastSilenceMs_ = timestampMs;
    }
}

std::vector<SpeechSegment> SegmentBuilder::collectSegments(bool finalize) {
    if (finalize && inSpeech_) {
        int64_t endMs = lastSpeechMs_ + config_.postPaddingMs;
        if (endMs - speechStartMs_ >= config_.minSpeechDurationMs) {
            segments_.push_back({speechStartMs_, endMs, 0.9f}); // Placeholder confidence
        }
        inSpeech_ = false;
    }

    std::vector<SpeechSegment> result = std::move(segments_);
    segments_.clear();
    return result;
}

void SegmentBuilder::reset() {
    segments_.clear();
    inSpeech_ = false;
}

} // namespace vg::subtitle::vad
