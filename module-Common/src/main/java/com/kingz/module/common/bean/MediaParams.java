package com.kingz.module.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author：KingZ
 * date：2019/7/31
 * description：媒体资源参数Bean
 */
public class MediaParams implements Parcelable {
    public static final String PARAMS_KEY = "playParams";
    private String videoId;
    private String videoType;
    private String videoUrl;
    private String videoName;

    public MediaParams() {
    }

    protected MediaParams(Parcel in) {
        videoId = in.readString();
        videoType = in.readString();
        videoUrl = in.readString();
        videoName = in.readString();
    }

    /**
     * 反序列化
     */
    public static final Creator<MediaParams> CREATOR = new Creator<MediaParams>() {
        @Override
        public MediaParams createFromParcel(Parcel in) {
            return new MediaParams(in);
        }

        @Override
        public MediaParams[] newArray(int size) {
            return new MediaParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化过程
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(videoType);
        dest.writeString(videoUrl);
        dest.writeString(videoName);
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

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
