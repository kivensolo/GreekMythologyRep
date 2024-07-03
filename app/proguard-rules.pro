# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class com.kingz.view.webview.core.XSystemWebView {
   public *;
}

# 保留用于调试堆栈跟踪的行号信息
-keepattributes SourceFile,LineNumberTable
# 隐藏原始源文件名 重命名为“SourceFile”字符串  重命名抛出异常时的文件名称
-renamesourcefileattribute SourceFile

# 代码混淆压缩比 ，在0~7之间 proguard对代码进行迭代优化的次数，Android一般为5
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写(默认规则中有)
-dontusemixedcaseclassnames
#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
-verbose
-ignorewarnings
#保持泛型
-keepattributes Signature

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
# google推荐混淆算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 避免混淆Annotation、内部类、泛型、匿名类
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod
#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

# 处理support包


#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment

-dontnote android.support.**
-dontwarn android.support.**
# 保留继承
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
-keep public class * extends androidx.appcompat.**
-keep public class * extends androidx.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}
# 保留四大组件，自定义的Application等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}
# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# 保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**

#fastjson混淆
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.**{*;}
-keep class com.alibaba.fastjson.**{*; }
-keep public class com.ninstarscf.ld.model.entity.**{*;}


# 忽略ijk的异常警告
-dontwarn tv.danmaku.ijk.media.exo.demo.**
-keep class tv.danmaku.ijk.media.exo.demo.**{*;}

# 忽略okHttp3的异常警告
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

# -------------- Arouter 混淆配置
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
# -keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
# -------------- Arouter 混淆配置
