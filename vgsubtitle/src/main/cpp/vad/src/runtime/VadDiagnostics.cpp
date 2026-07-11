#include "VadDiagnostics.h"
#include <mutex>
#include <vector>

namespace vg::subtitle::vad {

static std::mutex g_diag_mutex;
static long g_total_inference_time = 0;
static int g_inference_count = 0;
static int g_segments_count = 0;

void VadDiagnostics::logInferenceTime(long timeMs) {
    std::lock_guard<std::mutex> lock(g_diag_mutex);
    g_total_inference_time += timeMs;
    g_inference_count++;
}

void VadDiagnostics::logSegmentDetected(long startMs, long endMs) {
    std::lock_guard<std::mutex> lock(g_diag_mutex);
    g_segments_count++;
}

std::map<std::string, std::string> VadDiagnostics::getStats() {
    std::lock_guard<std::mutex> lock(g_diag_mutex);
    std::map<std::string, std::string> stats;
    stats["total_inference_time_ms"] = std::to_string(g_total_inference_time);
    stats["inference_count"] = std::to_string(g_inference_count);
    stats["segments_detected"] = std::to_string(g_segments_count);
    if (g_inference_count > 0) {
        stats["avg_inference_time_ms"] = std::to_string(g_total_inference_time / g_inference_count);
    }
    return stats;
}

} // namespace vg::subtitle::vad
