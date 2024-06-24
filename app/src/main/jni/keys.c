#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_internshal_lucky_assessment_MainActivity_getWebKey(JNIEnv *env, jobject instance) {

    return (*env)-> NewStringUTF(env, "696985627226-g5qtl2ulg7bqhngvoti5htg4aos54u94.apps.googleusercontent.com");
}

