#include "AudioExtractor.h"

namespace vg::subtitle::ffmpeg {

bool AudioExtractor::extract(const std::string& inputPath,
                            const std::string& outputPath,
                            ProgressCallback progress) {
    if (progress) progress(0);
    // Real FFmpeg logic would go here
    if (progress) progress(100);
    return true;
}

} // namespace vg::subtitle::ffmpeg
