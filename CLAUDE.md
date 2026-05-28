# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在本仓库中工作时提供指导。

## 项目概述

Android 学习/示例项目（"GreekMythologyRep"），涵盖模块化架构、自定义 View、媒体播放、网络请求、JNI/Native、协程及各类 Android 框架特性。使用 Kotlin 和 Java 编写。

## 构建系统

- **Gradle 7.5**，AGP 7.4.2，Kotlin 1.6.21
- **Gradle Wrapper** 使用腾讯云镜像分发
- 依赖版本集中管理于 `buildsystem/config.gradle` — 所有库版本、SDK 版本及测试依赖均通过 `ext` 属性定义
- 构建工具函数在 `buildsystem/utils.gradle`（如 `releaseTime()` 用于 APK 命名）
- 签名配置通过 `keystore.properties`（外部签名文件）
- **Product flavors**：`ChengDu`（com.demo.chengdu）和 `beijing`，维度为 "area"

### 常用命令

```bash
# 构建 debug APK（ChengDu 渠道）
./gradlew assembleChengDuDebug

# 构建 release APK
./gradlew assembleChengDuRelease

# 构建所有变体
./gradlew assemble

# 清理
./gradlew clean

# 运行指定模块的单元测试
./gradlew :app:testChengDuDebugUnitTest
./gradlew :library-network:test

# 运行插桩测试
./gradlew :app:connectedChengDuDebugAndroidTest
```

## 模块架构

### 应用模块（可安装 APK）
- `:app` — 主应用（com.kingz.customdemo），聚合所有功能模块
- `:app-playerdemo` — 独立播放器示例
- `:app-hlsCacheDemo`、`:app-bannerDemo`、`:app-Kotlin-Coroutines-Examples` — 专项示例应用
- `:app-pickerview` — 包含子模块 `:wheelview`、`:pickerview`、`:calendar_picker`

### 功能模块
- `:module-Home` — 首页，示例导航中心
- `:module-Demo` — 自定义 View 绘制、画笔效果、模糊、Canvas API 示例
- `:module-Login` — 登录认证 / 闪屏页
- `:module-Player` — 媒体播放器（基于 ExoPlayer，自定义解码器）
- `:module-Web` — WebView 集成
- `:module-Music` — 音乐播放功能
- `:module-Eyepetizer` — 视频详情 / 悬浮视频播放器
- `:module-Common` — 公共代码、路由配置、基础 Bean、工具类

### 核心模块
- `:base` — `BaseApplication`（继承 MultiDexApplication）
- `:database` — `DatabaseApplication`，Room 数据库初始化
- `:module-Native` — JNI/C++ 代码，通过 CMake 构建（WildFire native 库）

### 库模块（位于 `library/` 目录，在 settings.gradle 中映射）
- `:library`（`library/all`）— 聚合模块
- `:library-views`（`library/ui`）— 自定义 Drawable、UI 组件
- `:library-player`（`library/player`）— 播放器抽象层
- `:library-network`（`library/network`）— OkHttp/Retrofit 响应式 HTTP
- `:library-server`（`library/server`）— 本地服务器
- `:library-hlscache`（`library/hlscache`）— HLS 缓存
- `:library-unittest`（`library/unitTest`）— 测试工具
- `:library-floatwindow`（`library/floatwindow`）— 悬浮窗

### 外部依赖
- `:Akangaroo` — 当 `gradle.properties` 中 `directly_use_Akangraoo=true` 时条件引入，指向外部路径 `E:\GitHubProjects\AKangaroo\app`

## Application 类继承关系

```
MultiDexApplication
  └── BaseApplication (:base)
        └── DatabaseApplication (:database)
              ├── CommonApp (:module-Common) — 初始化 ARouter、ZLog、Bugly、SmartRefreshLayout
              └── HomeApplication (:module-Home)
```

## 模块间导航

- 使用 **ARouter**（阿里巴巴）进行跨模块页面路由
- 路由路径定义在 `module-Common/.../router/RouterConfig.kt`
- Activity 通过 `@Route(path = "...")` 注解标注
- 导航数据在 `module-Home/.../demo/NavigationData.kt` 中，将示例项映射到 Activity 类名或路由路径

## 模块功能开关

通过 `gradle.properties` 控制：
- `enableCoroutinesModule` — 控制协程示例模块
- `enableHomeModule` — 控制 module-Home（可独立作为 app 运行）
- `enableLoginModule` — 控制 module-Login
- `enableMusicModule` — 控制 module-Music

部分功能模块（如 `module-Home`）可根据开关在 library 和 application 插件间切换，支持独立开发调试。

## 主要依赖库

- ARouter — 组件路由
- Glide — 图片加载
- OkHttp 3 / Retrofit 2 — 网络请求
- RxJava 2 / RxAndroid — 响应式流
- Room — 数据库
- SmartRefreshLayout — 下拉刷新
- LeakCanary — 内存泄漏检测
- ImmersionBar — 沉浸式状态栏
- XXPermissions — 运行时权限
- Kotlin Coroutines 1.6.0

## Native/JNI

- CMake 最低版本 3.4.1，C++ 源码位于 `module-Native/src/main/cpp/`
- 构建产出 `WildFire` 动态库
- NDK ABI：arm64-v8a、x86、armeabi-v7a

## 测试

- 单元测试：JUnit 4、Mockito
- 插桩测试：AndroidX Test（runner 1.1.0）、Espresso 3.2.0
- 测试运行器：`androidx.test.runner.AndroidJUnitRunner`
- 测试目录：`src/test/`（单元测试）、`src/androidTest/`（插桩测试）

## SDK 版本

- compileSdk: 31，minSdk: 21，targetSdk: 23（新配置）/ 28（旧配置）
- Java source/target 兼容性：1.8
- 已启用 ViewBinding
