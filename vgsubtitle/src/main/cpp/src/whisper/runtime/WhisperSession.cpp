#include "WhisperSession.h"

namespace vg::subtitle::whisper {

WhisperSession::WhisperSession(struct whisper_context* ctx) : m_ctx(ctx) {}

WhisperSession::~WhisperSession() {
    if (m_ctx) {
        whisper_free(m_ctx);
        m_ctx = nullptr;
    }
}

} // namespace vg::subtitle::whisper
