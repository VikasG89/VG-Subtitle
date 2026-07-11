#ifndef RESAMPLER_H
#define RESAMPLER_H

namespace vg::subtitle::ffmpeg {

class Resampler {
public:
    Resampler(int inputRate, int outputRate, int channels);
    ~Resampler();

    bool initialize();
};

} // namespace vg::subtitle::ffmpeg

#endif // RESAMPLER_H
