#include <jni.h>
#include <string>
#include <iostream>

//声明命名空间std
using namespace std;

extern "C" {
    /**
     * 测试Jni双向通信：
     * Java层调用JNI层后，返回一个字符串，
     * 并且Native代码通过JNI修改Java代码的TAG变量的值。
     *
     * 方法名格式: Java_包名_类名_Java需要调用的方法名:
     * 包路径中的.要改成_
     * 下划线_要改成_1
     * @param env JNIEnv接口指针(提供原生方法修改和使用Java的引用类型)
     * @return 一个字符串
     */
    JNIEXPORT jstring JNICALL Java_com_zeke_utils_WildFireUtils_testNativeDuplex(
            JNIEnv *env, jobject clazz) {
//        从jobject clazz 参数中获取当前 JNI 函数所属的 Java 类对象
        jclass jcls = env->GetObjectClass(clazz);
//        可以使用 JNIEnv 的 GetFieldID、GetStaticFieldID、GetFieldID 等函数获取字段的 ID
        jfieldID tagFiledId = env->GetStaticFieldID(jcls, "TAG", "Ljava/lang/String;");
        env->DeleteLocalRef(jcls);
        if(tagFiledId != nullptr){
            // 获取TAG字段的值
            jstring value = (jstring)env->GetStaticObjectField(jcls, tagFiledId);
            // 将 jstring 转换为 C 字符串
            const char *str = env->GetStringUTFChars(value, nullptr);
            if (str != nullptr) {
                printf("TAG Field value: %s\n", str);// 使用字符串值
                // 释放字符串资源
                env->ReleaseStringUTFChars(value, str);
            }
            // 修改字段TAG的值
            jstring newValue = env->NewStringUTF("JNI ahs modify value.");
            env->SetStaticObjectField(jcls, tagFiledId, newValue);
        }

        std::string hello = "Hello I'm native content.";
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
    Java_com_zeke_utils_WildFireUtils_modifyArrayValue(JNIEnv *env, jobject thiz,
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

    JNIEXPORT jint JNICALL Java_com_zeke_utils_WildFireUtils__1native_1intFromJNI(
            JNIEnv *env,
            jobject) {
        return 1 << 8;
    }
}