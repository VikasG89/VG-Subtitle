#include "jni_common.h"
#include "ModelLoader.h"

using namespace vg::subtitle::whisper;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_vg_subtitle_native_bridge_NativeModelManager_verifyGguf(
        JNIEnv * env,
        jobject,
        jstring path) {
    std::string modelPath = jstring_to_string(env, path);
    return ModelLoader::isGguf(modelPath) ? JNI_TRUE : JNI_FALSE;
}
