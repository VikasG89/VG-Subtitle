#include "SileroVadSession.h"
#include "SegmentBuilder.h"
#include <cmath>

namespace vg::subtitle::vad {

SileroVadSession::SileroVadSession(const VadConfig& config)
    : config_(config) {
}

std::vector<SpeechSegment> SileroVadSession::process(const float* audioData, size_t length) {
    // Silero usually expects 512 samples for 16kHz (32ms)
    const size_t frameSize = 512;
    buffer_.insert(buffer_.end(), audioData, audioData + length);

    std::vector<SpeechSegment> segments;
    SegmentBuilder builder(config_); // In real app, this might be a member

    size_t processed = 0;
    while (buffer_.size() >= frameSize) {
        // Mock inference: simple energy based for now as placeholder for Silero ONNX
        float energy = 0;
        for (size_t i = 0; i < frameSize; ++i) {
            energy += std::abs(buffer_[i]);
        }
        energy /= frameSize;

        bool isSpeech = energy > config_.speechThreshold * 0.1f; // Scaled threshold

        builder.addFrame(isSpeech, energy, currentMs_);

        currentMs_ += (frameSize * 1000) / config_.sampleRate;
        buffer_.erase(buffer_.begin(), buffer_.begin() + frameSize);
    }

    return builder.collectSegments(false);
}

void SileroVadSession::reset() {
    buffer_.clear();
    currentMs_ = 0;
    speechStarted_ = false;
}

std::vector<SpeechSegment> SileroVadSession::flush() {
    SegmentBuilder builder(config_);
    return builder.collectSegments(true);
}

} // namespace vg::subtitle::vad
