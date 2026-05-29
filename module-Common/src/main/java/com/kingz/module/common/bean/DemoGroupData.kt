package com.kingz.module.common.bean

import android.content.Context
import android.content.Intent
import com.kingz.module.common.router.DemoPageRegistry

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
 * @param registKey 路径注册key，或者路由path
 */
data class DemoSample(var name: String? = "unKnow",
                      var registKey: String = "") : ISampleEntity() {
    private val cacheClass = HashMap<String?, Class<*>>()

    override fun getDemoClass(): Class<*>? {
        val targetPath = DemoPageRegistry.getPath(registKey) ?: registKey

        if (cacheClass.containsKey(targetPath)) {
            return cacheClass[targetPath]
        }
        try {
            val clazzObj = Class.forName(targetPath)
            cacheClass[targetPath] = clazzObj
            return clazzObj
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun isRouterMode():Boolean{
        return registKey.startsWith("/")
    }
}