#ifndef WHISPER_LANGUAGE_DETECTOR_H
#define WHISPER_LANGUAGE_DETECTOR_H

#include "WhisperSession.h"
#include <string>
#include <vector>

namespace vg::subtitle::whisper {

class LanguageDetector {
public:
    explicit LanguageDetector(WhisperSession& session);
    std::string detect(const std::vector<float>& pcm);

private:
    WhisperSession& m_session;
};

} // namespace vg::subtitle::whisper

#endif // WHISPER_LANGUAGE_DETECTOR_H
