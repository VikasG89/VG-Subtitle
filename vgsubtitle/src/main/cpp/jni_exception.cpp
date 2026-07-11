#include "jni_common.h"

void throw_java(JNIEnv *env, const char *message, int error_code) {
    jclass mapper_cls = env->FindClass("com/vg/subtitle/native/exception/NativeExceptionMapper");
    if (!mapper_cls) return;

    jmethodID throw_method = env->GetStaticMethodID(mapper_cls, "throwFromNative", "(ILjava/lang/String;)V");
    if (!throw_method) return;

    jstring jmsg = env->NewStringUTF(message);
    env->CallStaticVoidMethod(mapper_cls, throw_method, (jint)error_code, jmsg);
    env->DeleteLocalRef(jmsg);
}
