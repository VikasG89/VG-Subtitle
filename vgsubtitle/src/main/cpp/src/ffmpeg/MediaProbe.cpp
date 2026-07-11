#include "MediaProbe.h"

namespace vg::subtitle::ffmpeg {

MediaProbe::MediaProbe(const std::string& path) : m_path(path) {}

long long MediaProbe::getDurationMs() const { return 0; }
bool MediaProbe::hasAudio() const { return true; }
std::string MediaProbe::getFormat() const { return "unknown"; }

} // namespace vg::subtitle::ffmpeg
