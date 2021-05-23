package com.zeke.music.bean

import java.io.Serializable

/**
 * author：KingZ
 * date：2019/10/9
 * description：歌曲信息数据类
 */
data class VideoInfo(
    var id:Int,            //影片id
    var videoName:String,  //影片名称
    var videoDesc:String,  //影片描述
    var artistName:String, //歌手名
    var videoImg:String,    //海报图片
    var videoUrl:String,    //影片播放url
    var videoType:Int,      //影片类型
    var createDate:String?,
    var delFlag:Int
): Serializable