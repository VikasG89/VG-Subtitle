#include "jni_common.h"
#include <stdexcept>

using namespace vg::subtitle;

extern "C" JNIEXPORT jstring JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_detectLanguage(
        JNIEnv * env,
        jobject,
        jlong context_ptr,
        jstring pcm_path) {
    try {
        auto runtime = reinterpret_cast<EngineRuntime *>(context_ptr);
        if (runtime == nullptr) throw std::runtime_error("EngineRuntime is null.");

        std::string path = jstring_to_string(env, pcm_path);
        std::string lang = runtime->detectLanguage(path);

        return env->NewStringUTF(lang.c_str());

    } catch (const std::exception & e) {
        throw_java(env, e.what(), 1);
        return nullptr;
    }
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_generateSubtitles(
        JNIEnv * env,
        jobject,
        jlong context_ptr,
        jstring pcm_path,
        jlong chunk_duration_ms,
        jstring language) {
    try {
        auto runtime = reinterpret_cast<EngineRuntime *>(context_ptr);
        if (runtime == nullptr) throw std::runtime_error("EngineRuntime is null.");

        std::string path = jstring_to_string(env, pcm_path);
        std::string lang = jstring_to_string(env, language);

        auto results = runtime->generateSubtitles(path, chunk_duration_ms, lang);

        jclass list_cls = env->FindClass("java/util/ArrayList");
        jmethodID list_ctor = env->GetMethodID(list_cls, "<init>", "()V");
        jmethodID list_add = env->GetMethodID(list_cls, "add", "(Ljava/lang/Object;)Z");
        jobject list_obj = env->NewObject(list_cls, list_ctor);

        jclass segment_cls = env->FindClass("com/vg/subtitle/api/model/Segment");
        jmethodID segment_ctor = env->GetMethodID(segment_cls, "<init>", "(IJJLjava/lang/String;Ljava/lang/String;F)V");

        for (const auto& s : results) {
            jobject segment = env->NewObject(segment_cls, segment_ctor,
                s.index, s.t0, s.t1,
                env->NewStringUTF(s.text.c_str()),
                env->NewStringUTF(s.language.c_str()),
                s.confidence);
            env->CallBooleanMethod(list_obj, list_add, segment);
            env->DeleteLocalRef(segment);
        }

        return list_obj;

    } catch (const std::exception & e) {
        throw_java(env, e.what(), 1);
        return nullptr;
    }
}
