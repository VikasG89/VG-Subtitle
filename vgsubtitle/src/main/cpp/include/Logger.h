#ifndef LOGGER_H
#define LOGGER_H

#include <string>
#include <android/log.h>

#define TAG "VGSubtitleNative"

namespace vg::subtitle {

class Logger {
public:
    static void i(const std::string& msg);
    static void w(const std::string& msg);
    static void e(const std::string& msg);
};

} // namespace vg::subtitle

#endif // LOGGER_H
