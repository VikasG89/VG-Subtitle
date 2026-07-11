#ifndef FFMPEG_RUNTIME_H
#define FFMPEG_RUNTIME_H

#include <string>
#include <functional>

namespace vg::subtitle::ffmpeg {

/**
 * High-level runtime for FFmpeg operations.
 */
class FFmpegRuntime {
public:
    using ProgressCallback = std::function<void(int percent)>;

    static bool extractAudio(const std::string& inputPath,
                            const std::string& outputPath,
                            ProgressCallback progress = nullptr);

    static long long getDurationMs(const std::string& path);
    static bool hasAudio(const std::string& path);
};

} // namespace vg::subtitle::ffmpeg

#endif // FFMPEG_RUNTIME_H
