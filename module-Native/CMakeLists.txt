# CMake的构建脚本，作用相当于ndk-build中的Android.mk

# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

# 设置Cmake最小版本
cmake_minimum_required(VERSION 3.4.1)

# 可以通过添加多个add_library()命令来定义多个库,CMake会自动建造它们。
# 当构建应用时，Gradle会自动将共享库打包到APK中。.

# 编译出一个动态库 native-lib
add_library( # 设置library名称
             ZekeBitMap

             # 设置library模式
             # SHARED模式会编译so文件，点击AS的编译按钮，
             #  在app/build/intermediates/cmake/xxx/obj路径下会生成不同CPU架构对应的so文件
             # STATIC模式不会编译  但是ndk_build的方式，两种都会生成so文件
             SHARED

             # 提供到源代码文件的相对路径.
             src/main/cpp/BitmapTool.cpp
             src/main/cpp/JniTestDemo.cpp)

# 定位library log_lib 并link到动态库 native-lib 中
find_library( # Sets the name of the path variable.
              log-lib

              # 将library路径存储为一个变量，可以在其他地方用这个变量引用NDK库
              # 在这里设置变量名称
              log )

# 关联library
target_link_libraries( # 关联的library
                       ZekeBitMap
                       # Links the target library to the log library
                       # included in the NDK.
                       # 关联native-lib和log-lib
                       ${log-lib} )

# 更多的脚本配置参考：
#   https://cmake.org/cmake/help/latest/guide/tutorial/index.html(英)
#   https://www.zybuluo.com/khan-lau/note/254724(中)