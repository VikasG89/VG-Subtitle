#ifndef ENGINE_RUNTIME_H
#define ENGINE_RUNTIME_H

#include "RuntimeContext.h"
#include <vector>
#include <string>

namespace vg::subtitle {

struct Segment {
    int index;
    long long t0;
    long long t1;
    std::string text;
    std::string language;
    float confidence;
};

class EngineRuntime {
public:
    EngineRuntime();
    ~EngineRuntime();

    bool initialize(const std::string& modelPath);
    bool verifyModel(const std::string& modelPath);

    std::string detectLanguage(const std::string& pcmPath);
    std::vector<Segment> generateSubtitles(const std::string& pcmPath, long long chunkDurationMs, const std::string& language);

    void release();

private:
    std::unique_ptr<RuntimeContext> m_context;
};

} // namespace vg::subtitle

#endif // ENGINE_RUNTIME_H
