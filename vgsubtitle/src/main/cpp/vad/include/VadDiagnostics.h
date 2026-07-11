#ifndef VG_SUBTITLE_VAD_DIAGNOSTICS_H
#define VG_SUBTITLE_VAD_DIAGNOSTICS_H

#include <string>
#include <map>

namespace vg::subtitle::vad {

class VadDiagnostics {
public:
    static void logInferenceTime(long timeMs);
    static void logSegmentDetected(long startMs, long endMs);
    static std::map<std::string, std::string> getStats();
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_VAD_DIAGNOSTICS_H
