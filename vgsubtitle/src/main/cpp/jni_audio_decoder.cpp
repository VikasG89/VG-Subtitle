#include "jni_common.h"
#include "AudioDecoder.h"

using namespace vg::subtitle;

/**
 * JNI implementation for NativeAudioDecoder.decodeToFloatArray.
 *
 * Converts a Java string path to a C++ string, decodes the WAV file using AudioDecoder,
 * and returns the normalized float samples as a jfloatArray.
 */
extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_vg_subtitle_native_bridge_NativeAudioDecoder_decodeToFloatArray(
        JNIEnv * env,
        jobject,
        jstring pcm_path) {
    std::string path = jstring_to_string(env, pcm_path);
    auto samples = AudioDecoder::decodeWav(path);

    if (samples.empty()) return nullptr;

    jfloatArray result = env->NewFloatArray(samples.size());
    env->SetFloatArrayRegion(result, 0, samples.size(), samples.data());
    return result;
}
