package com.zeke.home.entity

import android.content.Context
import android.content.Intent
import java.util.*

/**
 * author：KingZ
 * date：2020/2/15
 * description： Demo展示页面的数据类
 */

// Demo分组的数据实例
data class DemoGroup(val title: String = "UnKnow",
                     val desc: String = "",
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

/**
 * 每一个Demo实例的数据类
 * @param path 页面路径 可以是AroutPath，也可以是直接的class包路径
 */
data class DemoSample(var name: String? = "unKnow",
                      var path: String = "") : ISampleEntity() {
    private val cacheClass = HashMap<String?, Class<*>>()

    override fun getDemoClass(): Class<*>? {
        if (cacheClass.containsKey(path)) {
            return cacheClass[path]
        }
        try {
            val clazzObj = Class.forName(path)
            cacheClass[path] = clazzObj
            return clazzObj
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun isRouterMode():Boolean{
        return path.startsWith("/")
    }
}