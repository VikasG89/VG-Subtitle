#ifndef TIMESTAMP_GENERATOR_H
#define TIMESTAMP_GENERATOR_H

#include "whisper.h"
#include <vector>

namespace vg::subtitle::whisper {

class TimestampGenerator {
public:
    static long long toMs(int64_t whisperTime);
};

} // namespace vg::subtitle::whisper

#endif // TIMESTAMP_GENERATOR_H
