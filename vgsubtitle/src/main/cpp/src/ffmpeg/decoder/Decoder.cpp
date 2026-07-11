#include "Decoder.h"

namespace vg::subtitle::ffmpeg {

Decoder::Decoder() = default;
Decoder::~Decoder() = default;
bool Decoder::initialize() { return true; }
void Decoder::release() {}

} // namespace vg::subtitle::ffmpeg
