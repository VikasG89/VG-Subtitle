#include "AudioDecoder.h"
#include <fstream>

namespace vg::subtitle {

/**
 * @brief Decodes a WAV file into normalized float samples.
 *
 * Reads the 16-bit PCM data from the specified file path, skipping the 44-byte WAV header,
 * and converts each sample to a float by dividing by 32768.0.
 *
 * @param path The filesystem path to the WAV file.
 * @return std::vector<float> Normalized audio samples.
 */
std::vector<float> AudioDecoder::decodeWav(const std::string& path) {
    std::ifstream file(path, std::ios::binary);
    if (!file.is_open()) return {};

    char header[44];
    file.read(header, 44);

    if (std::string(header, 4) != "RIFF" || std::string(header + 8, 4) != "WAVE") return {};

    std::vector<int16_t> pcm16;
    file.seekg(0, std::ios::end);
    size_t size = file.tellg();
    if (size <= 44) return {};

    file.seekg(44, std::ios::beg);
    pcm16.resize((size - 44) / 2);
    file.read(reinterpret_cast<char*>(pcm16.data()), size - 44);

    std::vector<float> pcm32(pcm16.size());
    for (size_t i = 0; i < pcm16.size(); ++i) {
        pcm32[i] = static_cast<float>(pcm16[i]) / 32768.0f;
    }

    return pcm32;
}

} // namespace vg::subtitle
