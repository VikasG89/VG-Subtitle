#include "TimestampGenerator.h"

namespace vg::subtitle::whisper {

long long TimestampGenerator::toMs(int64_t whisperTime) {
    // whisper.cpp uses centiseconds (10ms units)
    return whisperTime * 10;
}

} // namespace vg::subtitle::whisper
