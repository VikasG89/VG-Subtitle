#ifndef DECODER_H
#define DECODER_H

namespace vg::subtitle::ffmpeg {

class Decoder {
public:
    Decoder();
    ~Decoder();

    bool initialize();
    void release();
};

} // namespace vg::subtitle::ffmpeg

#endif // DECODER_H
