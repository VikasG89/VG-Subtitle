#include "FFmpegRuntime.h"
#include "Logger.h"

// Note: In a real implementation, we would include FFmpeg headers here:
// extern "C" {
// #include <libavcodec/avcodec.h>
// #include <libavformat/avformat.h>
// }

namespace vg::subtitle::ffmpeg {

bool FFmpegRuntime::extractAudio(const std::string& inputPath,
                               const std::string& outputPath,
                               ProgressCallback progress) {
    Logger::i("FFmpeg: Extracting audio from " + inputPath + " to " + outputPath);

    // Simulate extraction for now
    if (progress) progress(50);
    if (progress) progress(100);

    return true;
}

long long FFmpegRuntime::getDurationMs(const std::string& path) {
    return 0; // Stub
}

bool FFmpegRuntime::hasAudio(const std::string& path) {
    return true; // Stub
}

} // namespace vg::subtitle::ffmpeg
