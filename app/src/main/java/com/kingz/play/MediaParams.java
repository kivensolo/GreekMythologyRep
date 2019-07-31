package com.kingz.play;

import java.io.Serializable;

/**
 * author：KingZ
 * date：2019/7/31
 * description：媒体资源参数样例
 */
public class MediaParams implements Serializable {
    private String videoId;

    public enum VideoType {
        VOD, LIVE
    }

    public enum QualityType {
        LOW("流畅 180P"), STD("标清 270P"), HD("高清 480P"), SD("超清 720P"), UHD("蓝光 1080P");

        String qualityName;

        QualityType(String qualityName) {
            this.qualityName = qualityName;
        }

        public String getQualityName() {
            return qualityName;
        }

        public static QualityType value(String quality) {
            if ("LOW".equalsIgnoreCase(quality)) {
                return LOW;
            } else if ("STD".equalsIgnoreCase(quality)) {
                return STD;
            } else if ("HD".equalsIgnoreCase(quality)) {
                return HD;
            } else if ("SD".equalsIgnoreCase(quality)) {
                return SD;
            } else if ("4K".equalsIgnoreCase(quality)) {
                return UHD;
            }
            return STD;
        }
    }

}
