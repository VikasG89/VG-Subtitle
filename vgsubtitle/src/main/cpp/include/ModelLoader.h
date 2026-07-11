#ifndef MODEL_LOADER_H
#define MODEL_LOADER_H

#include <string>

namespace vg::subtitle::whisper {

/**
 * Handles model loading and verification.
 */
class ModelLoader {
public:
    static bool verifyChecksum(const std::string& path, const std::string& expectedSha256);
    static bool isGguf(const std::string& path);
};

} // namespace vg::subtitle::whisper

#endif // MODEL_LOADER_H
