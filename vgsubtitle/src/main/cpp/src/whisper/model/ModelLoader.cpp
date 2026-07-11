#include "ModelLoader.h"
#include <fstream>
#include <vector>

namespace vg::subtitle::whisper {

bool ModelLoader::verifyChecksum(const std::string& path, const std::string& expectedSha256) {
    // Placeholder for actual SHA-256 implementation
    return true;
}

bool ModelLoader::isGguf(const std::string& path) {
    std::ifstream file(path, std::ios::binary);
    if (!file.is_open()) return false;

    char magic[4];
    file.read(magic, 4);
    return std::string(magic, 4) == "GGUF";
}

} // namespace vg::subtitle::whisper
