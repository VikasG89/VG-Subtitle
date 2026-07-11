#ifndef RUNTIME_CONTEXT_H
#define RUNTIME_CONTEXT_H

#include "whisper.h"
#include <memory>
#include <string>
#include <mutex>

namespace vg::subtitle {

/**
 * Encapsulates the state for a single inference session.
 * Managed by the EngineRuntime.
 */
class RuntimeContext {
public:
    explicit RuntimeContext(struct whisper_context* ctx);
    ~RuntimeContext();

    // Disable copy
    RuntimeContext(const RuntimeContext&) = delete;
    RuntimeContext& operator=(const RuntimeContext&) = delete;

    struct whisper_context* getWhisperContext() const { return m_ctx; }
    std::mutex& getMutex() { return m_mutex; }

private:
    struct whisper_context* m_ctx;
    std::mutex m_mutex;
};

} // namespace vg::subtitle

#endif // RUNTIME_CONTEXT_H
