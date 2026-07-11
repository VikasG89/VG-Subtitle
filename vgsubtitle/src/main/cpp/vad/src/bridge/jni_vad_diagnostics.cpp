#include "jni_common.h"
#include "VadDiagnostics.h"

using namespace vg::subtitle::vad;

extern "C" JNIEXPORT jobject JNICALL
Java_com_vg_subtitle_native_bridge_NativeVadBridge_getVadStats(
        JNIEnv * env,
        jobject) {
    auto stats = VadDiagnostics::getStats();

    jclass hashMapClass = env->FindClass("java/util/HashMap");
    jmethodID hashMapInit = env->GetMethodID(hashMapClass, "<init>", "()V");
    jobject hashMapObj = env->NewObject(hashMapClass, hashMapInit);
    jmethodID putMethod = env->GetMethodID(hashMapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    for (auto const& [key, val] : stats) {
        env->CallObjectMethod(hashMapObj, putMethod, env->NewStringUTF(key.c_str()), env->NewStringUTF(val.c_str()));
    }

    return hashMapObj;
}
