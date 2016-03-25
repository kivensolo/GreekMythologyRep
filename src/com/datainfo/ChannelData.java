package com.datainfo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/23 23:59
 * description:
 */
public class ChannelData implements Serializable {

    private static final long serialVersionUID = 1L;

    public Bitmap img;
    public String channelName;
    public String playUrl;

    @Override
    public String toString() {
        return "ChannelData{" +
                "img=" + img +
                ", channelName='" + channelName + '\'' +
                ", playUrl='" + playUrl + '\'' +
                '}';
    }
}