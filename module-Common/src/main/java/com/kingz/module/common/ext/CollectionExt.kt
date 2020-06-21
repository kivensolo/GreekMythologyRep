package com.kingz.module.common.ext

fun <T> Collection<T>.joinToString(
        separator: String = ",",
        prefix: String = "",
        postfix: String = "",
        transform: (T) -> String = { it.toString() }
        // transform： 声明一个以lambda为默认值的函数类型的参数
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }

    result.append(postfix)
    return result.toString()
}

fun <T> Collection<T>.joinToStringByInvoke(
        separator: String = ",",
        prefix: String = "",
        postfix: String = "",
        transform: ((T) -> String)? = null
        // transform： 声明一个函数类型的可空参数
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        // 使用安全调用语法调用函数
        val str = transform?.invoke(element)
                ?: element.toString() // 使用Elvis运算符处理回调没有被处理的情况
        result.append(str)
    }

    result.append(postfix)
    return result.toString()
}