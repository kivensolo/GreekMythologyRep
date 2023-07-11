#include <jni.h>
#include <string>
#include <iostream>

//声明命名空间std
using namespace std;

extern "C" {

/**
 * 测试基础类型数据(Stirng)在Java层和jni层通信：
 *
 * 方法名格式: Java_包名_类名_Java需要调用的方法名:
 * 包路径中的.要改成_
 * 下划线_要改成_1
 * @param env JNIEnv接口指针(提供原生方法修改和使用Java的引用类型)
 * @return
 */
JNIEXPORT jstring JNICALL Java_com_zeke_utils_MyNativeUtils_native_1get_1Hello(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str()); //生成一个的UTF-8编码字符串
}

/**
 * 测试数组数据在Java层和jni层通信。
 *
 * New<Type>Array函数可以创建数组实例，
 * Type可以是原生数据类型，也可以是Object，使用相应的API传递参数确定大小,如：
 * jintArray array = env->NewIntArray(env, 10);
 *
 * @param env       JNIEnv接口指针
 * @param thiz
 * @param srcArray  java数组对象(此处是int数组对象)
 * @return Java数组对象
 */
JNIEXPORT jintArray JNICALL
Java_com_zeke_utils_MyNativeUtils_modifyArrayValue(JNIEnv *env, jobject thiz,
                                                   jintArray srcArray) {
    cout << "native code invoked";

    jboolean isCopy; // 用作判断该函数返回的字符串是否是Java字符串的副本，还是直接指向Java字符串的内存
    // 获取数组长度
    jsize size = env->GetArrayLength(srcArray);
    jint* cArray = env->GetIntArrayElements(srcArray,&isCopy);

    //遍历数组，并将数组的每个元素加上100
    int i;
    for(i = 0; i < size; i++){
        cArray[i] += 100;
    }
    // 释放数组资源 0代表将内容复制到array中，并释放原生cArray
    env->ReleaseIntArrayElements(srcArray,cArray,0);
    return srcArray;
}

JNIEXPORT jint JNICALL Java_com_zeke_utils_MyNativeUtils__1native_1intFromJNI(
        JNIEnv *env,
        jobject) {
    return 1 << 8;
}
}