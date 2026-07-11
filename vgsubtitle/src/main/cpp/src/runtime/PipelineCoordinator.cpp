#include "PipelineCoordinator.h"
#include "Logger.h"

namespace vg::subtitle {

PipelineCoordinator::PipelineCoordinator(EngineRuntime& runtime) : m_runtime(runtime) {}

std::vector<Segment> PipelineCoordinator::runFullPipeline(const std::string& pcmPath,
                                                         long long chunkDurationMs,
                                                         const std::string& language,
                                                         ProgressCallback progress) {
    Logger::i("Starting full pipeline for " + pcmPath);

    if (progress) progress(10);

    auto segments = m_runtime.generateSubtitles(pcmPath, chunkDurationMs, language);

    if (progress) progress(100);

    Logger::i("Pipeline completed with " + std::to_string(segments.size()) + " segments");
    return segments;
}

} // namespace vg::subtitle
