# 设置Cmake最小版本
cmake_minimum_required(VERSION 3.4.1)

# 编译出一个动态库 native-lib
add_library(WildFireLib SHARED
             # 提供到源代码文件的相对路径.
             src/main/cpp/BitmapTool.cpp
             src/main/cpp/JniTestDemo.cpp)

find_library(log-lib log)

# 关联library
target_link_libraries(WildFireLib  ${log-lib})

# 更多的脚本配置参考：
#   https://cmake.org/cmake/help/latest/guide/tutorial/index.html(英)
#   https://www.zybuluo.com/khan-lau/note/254724(中)