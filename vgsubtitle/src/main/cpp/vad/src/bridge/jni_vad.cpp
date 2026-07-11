#include "jni_common.h"
#include "VadSession.h"

using namespace vg::subtitle::vad;

static jobject create_java_vad_segment(JNIEnv* env, const SpeechSegment& segment) {
    jclass cls = env->FindClass("com/vg/subtitle/native/bridge/NativeVadSegment");
    jmethodID init = env->GetMethodID(cls, "<init>", "(JJF)V");
    return env->NewObject(cls, init, (jlong)segment.startMs, (jlong)segment.endMs, segment.confidence);
}

static jobjectArray segments_to_java_array(JNIEnv* env, const std::vector<SpeechSegment>& segments) {
    jclass cls = env->FindClass("com/vg/subtitle/native/bridge/NativeVadSegment");
    jobjectArray array = env->NewObjectArray((jsize)segments.size(), cls, nullptr);
    for (size_t i = 0; i < segments.size(); ++i) {
        env->SetObjectArrayElement(array, (jsize)i, create_java_vad_segment(env, segments[i]));
    }
    return array;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_vg_subtitle_native_bridge_NativeVadBridge_processAudio(
        JNIEnv * env,
        jobject,
        jlong session_handle,
        jfloatArray audio_data) {
    auto session = reinterpret_cast<VadSession *>(session_handle);
    if (!session || !audio_data) return nullptr;

    jfloat* pcm = env->GetFloatArrayElements(audio_data, nullptr);
    jsize len = env->GetArrayLength(audio_data);

    auto segments = session->process(pcm, (size_t)len);

    env->ReleaseFloatArrayElements(audio_data, pcm, JNI_ABORT);

    return segments_to_java_array(env, segments);
}

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeVadBridge_resetVad(
        JNIEnv * env,
        jobject,
        jlong session_handle) {
    auto session = reinterpret_cast<VadSession *>(session_handle);
    if (session) {
        session->reset();
    }
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_vg_subtitle_native_bridge_NativeVadBridge_flushVad(
        JNIEnv * env,
        jobject,
        jlong session_handle) {
    auto session = reinterpret_cast<VadSession *>(session_handle);
    if (!session) return nullptr;

    auto segments = session->flush();
    return segments_to_java_array(env, segments);
}
