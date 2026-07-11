#ifndef AUDIO_DECODER_H
#define AUDIO_DECODER_H

#include <string>
#include <vector>

namespace vg::subtitle {

/**
 * @brief Provides functionality for decoding audio files.
 */
class AudioDecoder {
public:
    /**
     * @brief Decodes a WAV file into a vector of float samples.
     *
     * This method reads a 16-bit PCM WAV file and converts it to normalized 32-bit float samples.
     *
     * @param path The absolute path to the WAV file.
     * @return A vector of float samples normalized to the range [-1.0, 1.0].
     *         Returns an empty vector if the file cannot be opened or is not a valid WAV file.
     */
    static std::vector<float> decodeWav(const std::string& path);
};

} // namespace vg::subtitle

#endif // AUDIO_DECODER_H
