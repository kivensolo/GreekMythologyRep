package com.kingz.module.wanandroid.bean

import android.os.Parcel
import android.os.Parcelable

class Article() : Parcelable {
    /**
     * apkLink :
     * audit : 1
     * author :
     * canEdit : false
     * chapterId : 502
     * chapterName : 自助
     * collect : false
     * courseId : 13
     * desc :
     * descMd :
     * envelopePic :
     * fresh : false
     * id : 14755
     * link : https://juejin.im/post/6857457525764620302
     * niceDate : 1天前
     * niceShareDate : 1天前
     * origin :
     * prefix :
     * projectLink :
     * publishTime : 1597374671000
     * realSuperChapterId : 493
     * selfVisible : 0
     * shareDate : 1597374671000
     * shareUser : 飞洋
     * superChapterId : 494
     * superChapterName : 广场Tab
     * tags : []
     * title : 🔥Android进阶基础系列：Window和WindowManager ，全面理解！
     * type : 0
     * userId : 31360
     * visible : 1
     * zan : 0
     */
    val contentType: String = "artical"
    var apkLink: String? = null
    var audit = 0
    var author: String? = null
    var isCanEdit = false
    var chapterId = 0
    var chapterName: String? = null
    var isCollect = false
    var courseId = 0
    var desc: String? = null
    var descMd: String? = null
    var envelopePic: String? = null
    var isFresh = false
    var id = 0
    var link: String? = null
    var niceDate: String? = null
    var niceShareDate: String? = null
    var origin: String? = null
    var prefix: String? = null
    var projectLink: String? = null
    var publishTime: Long = 0
    var realSuperChapterId = 0
    var selfVisible = 0
    var shareDate: Long = 0
    var shareUser: String? = null
    var superChapterId = 0
    var superChapterName: String? = null
    var title: String? = null
    var type = 0
    var userId = 0
    var visible = 0
    var zan = 0
    var tags: List<*>? = null
    var isTop = false
    var originId = 0

    constructor(parcel: Parcel) : this() {
        apkLink = parcel.readString()
        audit = parcel.readInt()
        author = parcel.readString()
        isCanEdit = parcel.readByte() != 0.toByte()
        chapterId = parcel.readInt()
        chapterName = parcel.readString()
        isCollect = parcel.readByte() != 0.toByte()
        courseId = parcel.readInt()
        desc = parcel.readString()
        descMd = parcel.readString()
        envelopePic = parcel.readString()
        isFresh = parcel.readByte() != 0.toByte()
        id = parcel.readInt()
        link = parcel.readString()
        niceDate = parcel.readString()
        niceShareDate = parcel.readString()
        origin = parcel.readString()
        prefix = parcel.readString()
        projectLink = parcel.readString()
        publishTime = parcel.readLong()
        realSuperChapterId = parcel.readInt()
        selfVisible = parcel.readInt()
        shareDate = parcel.readLong()
        shareUser = parcel.readString()
        superChapterId = parcel.readInt()
        superChapterName = parcel.readString()
        title = parcel.readString()
        type = parcel.readInt()
        userId = parcel.readInt()
        visible = parcel.readInt()
        zan = parcel.readInt()
        isTop = parcel.readByte() != 0.toByte()
        originId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(apkLink)
        parcel.writeInt(audit)
        parcel.writeString(author)
        parcel.writeByte(if (isCanEdit) 1 else 0)
        parcel.writeInt(chapterId)
        parcel.writeString(chapterName)
        parcel.writeByte(if (isCollect) 1 else 0)
        parcel.writeInt(courseId)
        parcel.writeString(desc)
        parcel.writeString(descMd)
        parcel.writeString(envelopePic)
        parcel.writeByte(if (isFresh) 1 else 0)
        parcel.writeInt(id)
        parcel.writeString(link)
        parcel.writeString(niceDate)
        parcel.writeString(niceShareDate)
        parcel.writeString(origin)
        parcel.writeString(prefix)
        parcel.writeString(projectLink)
        parcel.writeLong(publishTime)
        parcel.writeInt(realSuperChapterId)
        parcel.writeInt(selfVisible)
        parcel.writeLong(shareDate)
        parcel.writeString(shareUser)
        parcel.writeInt(superChapterId)
        parcel.writeString(superChapterName)
        parcel.writeString(title)
        parcel.writeInt(type)
        parcel.writeInt(userId)
        parcel.writeInt(visible)
        parcel.writeInt(zan)
        parcel.writeByte(if (isTop) 1 else 0)
        parcel.writeInt(originId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}