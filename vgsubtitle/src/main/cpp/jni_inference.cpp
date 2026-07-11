#include "jni_common.h"
#include "WhisperRuntime.h"
#include "AudioDecoder.h"
#include <stdexcept>

using namespace vg::subtitle::whisper;

extern "C" JNIEXPORT jobject JNICALL
Java_com_vg_subtitle_native_bridge_NativeWhisperBridge_transcribeWhisper(
        JNIEnv * env,
        jobject,
        jlong runtime_ptr,
        jstring pcm_path,
        jstring language) {
    try {
        auto runtime = reinterpret_cast<WhisperRuntime *>(runtime_ptr);
        if (!runtime) throw std::runtime_error("WhisperRuntime is null");

        std::string path = jstring_to_string(env, pcm_path);
        std::string lang = jstring_to_string(env, language);

        auto samples = vg::subtitle::AudioDecoder::decodeWav(path);
        auto results = runtime->transcribe(samples, lang);

        jclass list_cls = env->FindClass("java/util/ArrayList");
        jmethodID list_ctor = env->GetMethodID(list_cls, "<init>", "()V");
        jmethodID list_add = env->GetMethodID(list_cls, "add", "(Ljava/lang/Object;)Z");
        jobject list_obj = env->NewObject(list_cls, list_ctor);

        jclass segment_cls = env->FindClass("com/vg/subtitle/api/model/Segment");
        jmethodID segment_ctor = env->GetMethodID(segment_cls, "<init>", "(IJJLjava/lang/String;Ljava/lang/String;F)V");

        jstring jLanguage = language; // or env->NewStringUTF(lang.c_str()) if you need a copy

        for (const auto &s : results) {
            jstring jText = env->NewStringUTF(s.text.c_str());

            jobject segment = env->NewObject(
                    segment_cls,
                    segment_ctor,
                    s.index,
                    s.t0,
                    s.t1,
                    jText,
                    jLanguage,
                    s.confidence
            );

            env->CallBooleanMethod(list_obj, list_add, segment);

            env->DeleteLocalRef(jText);
            env->DeleteLocalRef(segment);
        }

        return list_obj;

    } catch (const std::exception & e) {
        throw_java(env, e.what(), 1);
        return nullptr;
    }
}
