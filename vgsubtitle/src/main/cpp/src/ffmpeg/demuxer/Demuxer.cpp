#include "Demuxer.h"

namespace vg::subtitle::ffmpeg {

Demuxer::Demuxer(const std::string& path) : m_path(path) {}
Demuxer::~Demuxer() = default;
bool Demuxer::open() { return true; }
void Demuxer::close() {}

} // namespace vg::subtitle::ffmpeg
