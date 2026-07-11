#ifndef DEMUXER_H
#define DEMUXER_H

#include <string>

namespace vg::subtitle::ffmpeg {

class Demuxer {
public:
    explicit Demuxer(const std::string& path);
    ~Demuxer();

    bool open();
    void close();

private:
    std::string m_path;
};

} // namespace vg::subtitle::ffmpeg

#endif // DEMUXER_H
