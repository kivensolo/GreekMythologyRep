package com.kingz.recyclerview.data;

import java.util.ArrayList;

/**
 * author：KingZ
 * date：2019/9/10
 * description：
 */
public class MgResponseBean {
    String code = "";
    Data data = new Data();
    String msg = "";
    String seqid = "";

    public class Data{
        ArrayList<MgPosterBean> hitDocs = new ArrayList<>();
        String totalHits = "";

        public ArrayList<MgPosterBean> getHitDocs() {
            return hitDocs;
        }

        public String getTotalHits() {
            return totalHits;
        }

        public void setHitDocs(ArrayList<MgPosterBean> hitDocs) {
            this.hitDocs = hitDocs;
        }

        public void setTotalHits(String totalHits) {
            this.totalHits = totalHits;
        }
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSeqid(String seqid) {
        this.seqid = seqid;
    }

    public String getCode() {
        return code;
    }

    public Data getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public String getSeqid() {
        return seqid;
    }
}
