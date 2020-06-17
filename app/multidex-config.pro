#-keep class com.example.MyClass 配置单个class
# 将bugly配置包下的类都打包到主Dex,以保证能正常初始化
-keep class com.tencent.bugly.crashreport.** { *; } # All classes in the com.tencent.bugly.crashreport