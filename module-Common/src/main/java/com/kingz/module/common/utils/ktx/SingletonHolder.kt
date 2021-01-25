package com.kingz.module.common.utils.ktx

/**
 * author：ZekeWang
 * date：2021/1/25
 * description：kotlin单例Holder泛型类
 */
open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

// 使用方式
//class SomeSingleton private constructor(context: Context) {
//    init {
//        // Init using context argument
//        context.getString(R.string.app_name)
//    }
//
//    companion object : SingletonHolder<SomeSingleton, Context>(::SomeSingleton)
//}