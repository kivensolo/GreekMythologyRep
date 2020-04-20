package com.zeke.ktx.modules.player.entity

import android.content.Context
import android.content.Intent
import java.util.*

/**
 * author：KingZ
 * date：2020/2/15
 * description： Demo展示页面的数据类
 */

// Demo分组的数据实例
data class DemoGroup(val title: String?,
                     val desc: String?,
                     val samples: MutableList<DemoSample> = ArrayList()){
    fun getSampleByIndex(index: Int): DemoSample {
        return samples[index]
    }
}

abstract class ISampleEntity {
    fun buildIntent(context: Context): Intent? {
        val targetClass = getDemoClass()
        return if (targetClass != null) Intent(context, targetClass) else null
    }

    protected abstract fun getDemoClass(): Class<*>?
}

// 每一个Demo实例的数据类
data class DemoSample(var name: String?,
                      private var classPath: String?) : ISampleEntity() {
    private val cacheClass = HashMap<String?, Class<*>>()

    override fun getDemoClass(): Class<*>? {
        if (cacheClass.containsKey(classPath)) {
            return cacheClass[classPath]
        }
        try {
            val clazzObj = Class.forName(classPath)
            cacheClass[classPath] = clazzObj
            return clazzObj
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}