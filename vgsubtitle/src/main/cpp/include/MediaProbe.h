#ifndef MEDIA_PROBE_H
#define MEDIA_PROBE_H

#include <string>

namespace vg::subtitle::ffmpeg {

class MediaProbe {
public:
    explicit MediaProbe(const std::string& path);

    long long getDurationMs() const;
    bool hasAudio() const;
    std::string getFormat() const;

private:
    std::string m_path;
};

} // namespace vg::subtitle::ffmpeg

#endif // MEDIA_PROBE_H
