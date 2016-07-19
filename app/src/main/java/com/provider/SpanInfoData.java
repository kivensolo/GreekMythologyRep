package com.provider;

import java.io.Serializable;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/7 15:34
 * description:
 */
public class SpanInfoData implements Serializable{

    private static final long serialVersionUID = 1L;

    public String itemTitle;
    public String spanText;
    public String spanDesc;
    public String rightText;

    @Override
    public String toString() {
        return "SpanInfoData{" +
                "itemTitle='" + itemTitle + '\'' +
                ", spanText='" + spanText + '\'' +
                ", spanDesc='" + spanDesc + '\'' +
                ", rightText='" + rightText + '\'' +
                '}';
    }
}
