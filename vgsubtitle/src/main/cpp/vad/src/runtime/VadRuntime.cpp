#include "VadRuntime.h"
#include "SileroVadRuntime.h"

namespace vg::subtitle::vad {

std::unique_ptr<VadRuntime> VadRuntime::create(const VadConfig& config) {
    return std::make_unique<SileroVadRuntime>(config);
}

} // namespace vg::subtitle::vad
