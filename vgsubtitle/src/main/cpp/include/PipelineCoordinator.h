#ifndef PIPELINE_COORDINATOR_H
#define PIPELINE_COORDINATOR_H

#include "EngineRuntime.h"
#include <functional>

namespace vg::subtitle {

class PipelineCoordinator {
public:
    using ProgressCallback = std::function<void(int percent)>;

    explicit PipelineCoordinator(EngineRuntime& runtime);

    std::vector<Segment> runFullPipeline(const std::string& pcmPath,
                                        long long chunkDurationMs,
                                        const std::string& language,
                                        ProgressCallback progress);

private:
    EngineRuntime& m_runtime;
};

} // namespace vg::subtitle

#endif // PIPELINE_COORDINATOR_H
