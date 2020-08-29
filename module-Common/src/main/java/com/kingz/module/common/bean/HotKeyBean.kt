package com.kingz.module.common.bean

class HotKeyBean {
    /**
     * id : 6
     * link :
     * name : 面试
     * order : 1
     * visible : 1
     */
    var id = 0
    var link: String? = null
    var name: String? = null
    var order = 0
    var visible = 0

    override fun toString(): String {
        return "HotKeyBean{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", visible=" + visible +
                '}'
    }
}