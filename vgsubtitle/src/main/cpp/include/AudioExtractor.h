#ifndef AUDIO_EXTRACTOR_H
#define AUDIO_EXTRACTOR_H

#include <string>
#include <functional>

namespace vg::subtitle::ffmpeg {

class AudioExtractor {
public:
    using ProgressCallback = std::function<void(int percent)>;

    bool extract(const std::string& inputPath,
                 const std::string& outputPath,
                 ProgressCallback progress);
};

} // namespace vg::subtitle::ffmpeg

#endif // AUDIO_EXTRACTOR_H
