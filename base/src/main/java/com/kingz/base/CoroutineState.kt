package com.kingz.base

/**
 * 协程状态
 */
enum class CoroutineState {
    START,      // 开始
    REFRESH,    // 刷新
    FINISH,     // 结束
    ERROR       // 异常
}