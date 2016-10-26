package com.kingz.databean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/4/2 17:06
 * description:  海报分组信息
 */
public class PosterGroupInfo implements Serializable {


    public String id="";
    public String title="";
    public ArrayList<Poster> poster;

    @Override
    public String toString() {
        return "PosterGroupInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", poster=" + poster +
                '}';
    }

    public class Poster implements Serializable {
        public String poster_ur="";
        public String title=""; //海报标题

        @Override
        public String toString() {
            return "Poster{" +
                    "poster_ur='" + poster_ur + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
