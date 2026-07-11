#include "SileroVadRuntime.h"
#include "SileroVadSession.h"

namespace vg::subtitle::vad {

SileroVadRuntime::SileroVadRuntime(const VadConfig& config) : config_(config) {}

std::unique_ptr<VadSession> SileroVadRuntime::createSession() {
    return std::make_unique<SileroVadSession>(config_);
}

} // namespace vg::subtitle::vad
