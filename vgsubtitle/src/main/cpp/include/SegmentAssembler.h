#ifndef SEGMENT_ASSEMBLER_H
#define SEGMENT_ASSEMBLER_H

#include "WhisperRuntime.h"
#include <vector>

namespace vg::subtitle::whisper {

class SegmentAssembler {
public:
    static std::vector<WhisperSegment> assemble(struct whisper_context* ctx);
};

} // namespace vg::subtitle::whisper

#endif // SEGMENT_ASSEMBLER_H
