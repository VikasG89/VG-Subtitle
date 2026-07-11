#ifndef VG_SUBTITLE_VAD_RUNTIME_H
#define VG_SUBTITLE_VAD_RUNTIME_H

#include <memory>
#include <vector>
#include "VadSession.h"
#include "VadConfig.h"

namespace vg::subtitle::vad {

class VadRuntime {
public:
    static std::unique_ptr<VadRuntime> create(const VadConfig& config);

    virtual ~VadRuntime() = default;

    virtual std::unique_ptr<VadSession> createSession() = 0;
};

} // namespace vg::subtitle::vad

#endif // VG_SUBTITLE_VAD_RUNTIME_H
