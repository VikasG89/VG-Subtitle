#ifndef VG_SUBTITLE_SPEECH_DETECTOR_H
#define VG_SUBTITLE_SPEECH_DETECTOR_H

namespace vg::subtitle::vad {

class SpeechDetector {
public:
    explicit SpeechDetector(float threshold);
    bool isSpeech(float probability) const;

private:
    float threshold_;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_SPEECH_DETECTOR_H
