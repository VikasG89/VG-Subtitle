#include "SpeechDetector.h"

namespace vg::subtitle::vad {

SpeechDetector::SpeechDetector(float threshold) : threshold_(threshold) {}

bool SpeechDetector::isSpeech(float probability) const {
    return probability >= threshold_;
}

} // namespace vg::subtitle::vad
