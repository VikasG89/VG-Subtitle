#include "jni_common.h"
#include "VadConfig.h"

using namespace vg::subtitle::vad;

VadConfig java_to_native_vad_config(JNIEnv* env, jobject config_obj) {
    VadConfig config;
    if (!config_obj) return config;

    jclass cls = env->GetObjectClass(config_obj);

    config.speechThreshold = env->GetFloatField(config_obj, env->GetFieldID(cls, "speechThreshold", "F"));
    config.minSpeechDurationMs = env->GetIntField(config_obj, env->GetFieldID(cls, "minSpeechDurationMs", "I"));
    config.minSilenceDurationMs = env->GetIntField(config_obj, env->GetFieldID(cls, "minSilenceDurationMs", "I"));
    config.prePaddingMs = env->GetIntField(config_obj, env->GetFieldID(cls, "prePaddingMs", "I"));
    config.postPaddingMs = env->GetIntField(config_obj, env->GetFieldID(cls, "postPaddingMs", "I"));
    config.maxSegmentLengthMs = env->GetIntField(config_obj, env->GetFieldID(cls, "maxSegmentLengthMs", "I"));
    config.segmentOverlapMs = env->GetIntField(config_obj, env->GetFieldID(cls, "segmentOverlapMs", "I"));
    config.confidenceThreshold = env->GetFloatField(config_obj, env->GetFieldID(cls, "confidenceThreshold", "F"));
    config.sampleRate = env->GetIntField(config_obj, env->GetFieldID(cls, "sampleRate", "I"));

    return config;
}
