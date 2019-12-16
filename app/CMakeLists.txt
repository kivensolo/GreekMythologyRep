# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.
cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name,
# specifies whether the library is STATIC or SHARED,
# and provides relative paths to the source code.
# You can define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.
# 编译出一个动态库 native-lib，源文件只有 src/main/cpp/native-lib.cpp
add_library( # Sets the name of the library.
             testNative-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

# 找到预编译库 log_lib 并link到动态库 native-lib 中
find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
target_link_libraries( # Specifies the target library.
                       testNative-lib
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )