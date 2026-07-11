#include "Resampler.h"

namespace vg::subtitle::ffmpeg {

Resampler::Resampler(int inputRate, int outputRate, int channels) {}
Resampler::~Resampler() = default;
bool Resampler::initialize() { return true; }

} // namespace vg::subtitle::ffmpeg
