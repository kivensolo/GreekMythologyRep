package com.kingz.base

/**
 * @author zeke.wang
 * @date 2020/7/9
 * 协程状态
 */
enum class CoroutineState {
    START,      // 开始
    REFRESH,    // 刷新
    FINISH,     // 结束
    ERROR       // 异常
}