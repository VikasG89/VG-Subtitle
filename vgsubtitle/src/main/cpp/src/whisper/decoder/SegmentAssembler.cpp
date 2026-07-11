#include "SegmentAssembler.h"
#include "TimestampGenerator.h"
#include "whisper.h"

namespace vg::subtitle::whisper {

    std::vector<WhisperSegment> SegmentAssembler::assemble(struct ::whisper_context* ctx) {
    std::vector<WhisperSegment> segments;
    int n_segments = whisper_full_n_segments(ctx);

    for (int i = 0; i < n_segments; ++i) {
        long long t0 = TimestampGenerator::toMs(whisper_full_get_segment_t0(ctx, i));
        long long t1 = TimestampGenerator::toMs(whisper_full_get_segment_t1(ctx, i));
        const char* text = whisper_full_get_segment_text(ctx, i);

        // Basic confidence calculation based on token probabilities
        int n_tokens = whisper_full_n_tokens(ctx, i);
        float sum_p = 0;
        for (int j = 0; j < n_tokens; ++j) {
            sum_p += whisper_full_get_token_p(ctx, i, j);
        }
        float confidence = n_tokens > 0 ? sum_p / n_tokens : 0.0f;

        segments.push_back({i, t0, t1, text, confidence});
    }

    return segments;
}

} // namespace vg::subtitle::whisper
