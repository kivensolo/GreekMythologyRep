package com.kingz.utils

inline fun runSafely(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}

inline fun runSafely(block: () -> Unit, error: (t: Throwable) -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        error(e)
    } catch (t: Throwable) {
        t.printStackTrace()
        error(t)
    }
}
