#include "jni_common.h"

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_pause(
        JNIEnv * env,
        jobject,
        jlong context_ptr) {
    // Whisper.cpp doesn't have a native pause/resume for a single whisper_full call.
    // This state is typically managed in the Kotlin layer by pausing the audio feeder
    // or utilizing the abort_callback.
}

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_resume(
        JNIEnv * env,
        jobject,
        jlong context_ptr) {
}

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_cancel(
        JNIEnv * env,
        jobject,
        jlong context_ptr) {
    // Implementation would involve setting a flag checked by an abort_callback
}

extern "C" JNIEXPORT jint JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_getStatus(
        JNIEnv * env,
        jobject,
        jlong context_ptr) {
    return context_ptr != 0 ? 1 : 0;
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_getStatistics(
        JNIEnv * env,
        jobject,
        jlong context_ptr) {
    jclass map_cls = env->FindClass("java/util/HashMap");
    jmethodID map_ctor = env->GetMethodID(map_cls, "<init>", "()V");
    jmethodID map_put = env->GetMethodID(map_cls, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    jobject map_obj = env->NewObject(map_cls, map_ctor);

    // Placeholder statistics
    return map_obj;
}
