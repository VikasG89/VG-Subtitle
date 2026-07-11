#ifndef WHISPER_SESSION_H
#define WHISPER_SESSION_H

#include "whisper.h"
#include <mutex>

namespace vg::subtitle::whisper {

/**
 * Manages the whisper_context and thread-safe access.
 */
class WhisperSession {
public:
    explicit WhisperSession(struct whisper_context* ctx);
    ~WhisperSession();

    struct whisper_context* getContext() const { return m_ctx; }
    std::mutex& getMutex() { return m_mutex; }

private:
    struct whisper_context* m_ctx;
    std::mutex m_mutex;
};

} // namespace vg::subtitle::whisper

#endif // WHISPER_SESSION_H
