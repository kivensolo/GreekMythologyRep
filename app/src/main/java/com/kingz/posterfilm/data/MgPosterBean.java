package com.kingz.posterfilm.data;

/**
 * author：KingZ
 * date：2019/9/10
 * description：芒果tv海报数据实体
 */
public class MgPosterBean {
    String assetSource = "";
    int clipId = 0;
    String img = "";
    String title = "";
    String updateInfo = "";

    public String getAssetSource() {
        return assetSource;
    }

    public int getClipId() {
        return clipId;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public void setClipId(int clipId) {
        this.clipId = clipId;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
