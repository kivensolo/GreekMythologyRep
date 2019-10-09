package com.kingz.play;

import java.io.Serializable;

/**
 * author：KingZ
 * date：2019/10/9
 * description：影片信息JavaBean
 */
public class VideoInfo implements Serializable {
    public String videoId = "asdaskjdoijic";  //影片id
    public String mediaAssetsId = "100001";  //媒资id
    public String categoryId = "10086";  //栏目id
    public String videoName = "明日战记";  //影片名称
    public String summary = "《明日战记》是由吴炫辉导演，罗志良担任编剧，古天乐、刘青云、姜皓文、胡子彤、万国鹏等主演的动作科幻电影,该片讲述了各大国组成的“国际和平联盟”对抗神秘的机器人组织的故事。\n" +
            "该片于2017年2月12日开拍，有望于2019年秋季上映。";    //影片简介
    public int episodeCount = 512;  //总集数
    public int newEpisodeIndex = 10; //更新到多少集  从0开始
    public String newEpisodeTime; //最新级数的时间
    public String userScore = "9.9"; //影片评分
}
