#include "RuntimeContext.h"

namespace vg::subtitle {

RuntimeContext::RuntimeContext(struct whisper_context* ctx) : m_ctx(ctx) {
}

RuntimeContext::~RuntimeContext() {
    if (m_ctx) {
        whisper_free(m_ctx);
        m_ctx = nullptr;
    }
}

} // namespace vg::subtitle
