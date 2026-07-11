#include "Logger.h"

namespace vg::subtitle {

void Logger::i(const std::string& msg) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", msg.c_str());
}

void Logger::w(const std::string& msg) {
    __android_log_print(ANDROID_LOG_WARN, TAG, "%s", msg.c_str());
}

void Logger::e(const std::string& msg) {
    __android_log_print(ANDROID_LOG_ERROR, TAG, "%s", msg.c_str());
}

} // namespace vg::subtitle
