package com.zeke.network.retrofit.model;

import java.util.List;

/**
 * author: King.Z <br>
 * date:  2020/3/4 20:33 <br>
 * description:  <br>
 */
public class WeatherEntity {

    /**
     * data :
     * {
     * 	"yesterday": {
     * 		"date": "3日星期二",
     * 		"high": "高温 8℃",
     * 		"fx": "西北风",
     * 		"low": "低温 -3℃",
     * 		"fl": "<![CDATA[4-5级]]>",
     * 		"type": "晴"
     *        },
     * 	"city": "北京",
     * 	"forecast": [{
     * 		"date": "4日星期三",
     * 		"high": "高温 7℃",
     * 		"fengli": "<![CDATA[<3级]]>",
     * 		"low": "低温 -2℃",
     * 		"fengxiang": "西南风",
     * 		"type": "晴"
     *    }, {
     * 		"date": "5日星期四",
     * 		"high": "高温 11℃",
     * 		"fengli": "<![CDATA[3-4级]]>",
     * 		"low": "低温 -1℃",
     * 		"fengxiang": "南风",
     * 		"type": "晴"
     *    }, {
     * 		"date": "6日星期五",
     * 		"high": "高温 12℃",
     * 		"fengli": "<![CDATA[<3级]]>",
     * 		"low": "低温 1℃",
     * 		"fengxiang": "北风",
     * 		"type": "多云"
     *    }, {
     * 		"date": "7日星期六",
     * 		"high": "高温 15℃",
     * 		"fengli": "<![CDATA[<3级]]>",
     * 		"low": "低温 0℃",
     * 		"fengxiang": "东南风",
     * 		"type": "晴"
     *    }, {
     * 		"date": "8日星期天",
     * 		"high": "高温 11℃",
     * 		"fengli": "<![CDATA[<3级]]>",
     * 		"low": "低温 1℃",
     * 		"fengxiang": "南风",
     * 		"type": "多云"
     *    }],
     * 	"ganmao": "昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。",
     * 	"wendu": "3"
     * }
     * status : 1000
     * desc : OK
     */

    private DataBean data;
    private int status;
    private String desc;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static class DataBean {
        /**
         * yesterday : {"date":"3日星期二","high":"高温 8℃","fx":"西北风","low":"低温 -3℃","fl":"<![CDATA[4-5级]]>","type":"晴"}
         * city : 北京
         * forecast : [{"date":"4日星期三","high":"高温 7℃","fengli":"<![CDATA[<3级]]>","low":"低温 -2℃","fengxiang":"西南风","type":"晴"},{"date":"5日星期四","high":"高温 11℃","fengli":"<![CDATA[3-4级]]>","low":"低温 -1℃","fengxiang":"南风","type":"晴"},{"date":"6日星期五","high":"高温 12℃","fengli":"<![CDATA[<3级]]>","low":"低温 1℃","fengxiang":"北风","type":"多云"},{"date":"7日星期六","high":"高温 15℃","fengli":"<![CDATA[<3级]]>","low":"低温 0℃","fengxiang":"东南风","type":"晴"},{"date":"8日星期天","high":"高温 11℃","fengli":"<![CDATA[<3级]]>","low":"低温 1℃","fengxiang":"南风","type":"多云"}]
         * ganmao : 昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。
         * wendu : 3
         */

        private YesterdayBean yesterday;
        private String city;
        private String ganmao;
        private String wendu;
        private List<ForecastBean> forecast;

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayBean {
            /**
             * date : 3日星期二
             * high : 高温 8℃
             * fx : 西北风
             * low : 低温 -3℃
             * fl : <![CDATA[4-5级]]>
             * type : 晴
             */

            private String date;
            private String high;
            private String fx;
            private String low;
            private String fl;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class ForecastBean {
            /**
             * date : 4日星期三
             * high : 高温 7℃
             * fengli : <![CDATA[<3级]]>
             * low : 低温 -2℃
             * fengxiang : 西南风
             * type : 晴
             */

            private String date;
            private String high;
            private String fengli;
            private String low;
            private String fengxiang;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
