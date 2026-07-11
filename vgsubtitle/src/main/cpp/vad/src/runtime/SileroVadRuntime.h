#ifndef VG_SUBTITLE_SILERO_VAD_RUNTIME_H
#define VG_SUBTITLE_SILERO_VAD_RUNTIME_H

#include "VadRuntime.h"
#include "VadConfig.h"

namespace vg::subtitle::vad {

class SileroVadRuntime : public VadRuntime {
public:
    explicit SileroVadRuntime(const VadConfig& config);
    ~SileroVadRuntime() override = default;

    std::unique_ptr<VadSession> createSession() override;

private:
    VadConfig config_;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_SILERO_VAD_RUNTIME_H
