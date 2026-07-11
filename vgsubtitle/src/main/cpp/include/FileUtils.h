#ifndef FILE_UTILS_H
#define FILE_UTILS_H

#include <string>
#include <vector>

namespace vg::subtitle {

class FileUtils {
public:
    static std::vector<float> readWavMono16k(const std::string& path);
};

} // namespace vg::subtitle

#endif // FILE_UTILS_H
