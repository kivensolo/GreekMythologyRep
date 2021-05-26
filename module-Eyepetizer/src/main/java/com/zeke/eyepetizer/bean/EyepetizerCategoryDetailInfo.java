package com.zeke.eyepetizer.bean;

import java.util.List;

/**
 * author：ZekeWang
 * date：2021/5/24
 * description：开眼视频栏目详情数据
 *
 * 精简后的Url:
 * http://baobab.kaiyanapp.com/api/v5/index/tab/{tabId}?page={page}&udid=435865baacfc49499632ea13c5a78f944c2f28aa
 *
 * 参数分析：
 *
 * tabId: 【发现】【推荐】【日报】【社区】等栏目使用栏目英文名称，其余栏目使用栏目Id
 * page：页数
 * udid:这个参数不可省略，否则部分接口会出现403错误，值由特定的公式计算而出
 */
public class EyepetizerCategoryDetailInfo {

    //"模板"数据总数
    private int count;
    private int total;
    // 下一页数据API接口，实际上已不起作用，大部分是首页Url
    private String nextPageUrl;
    // 是否存在广告
    private boolean adExist;
    // "模板"数据列表
    private List<TemplateList> itemList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public boolean isAdExist() {
        return adExist;
    }

    public void setAdExist(boolean adExist) {
        this.adExist = adExist;
    }

    public List<TemplateList> getItemList() {
        return itemList;
    }

    public void setItemList(List<TemplateList> itemList) {
        this.itemList = itemList;
    }

    public static class TemplateList {
        /**
         * "模板"类型 不同的type对应下面不同的data结构,同时对应UI界面列表不同的Item项
         * horizontalScrollCard:代表横向滚动的列表Item项
         * textCard:代表简单文本的列表Item项
         * followCard: videoSmallCard:代表小型视频的列表Item项
         * briefCard:
         * squareCardCollection: 正方形卡片样式集合
         * videoCollectionWithBrief: 短片收集及简介
         * autoPlayFollowCard: ......
         */
        private String type;
        private TemplateData data;
        private Object trackingData;
        private Object tag;
        private int id;
        private int adIndex;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public TemplateData getData() {
            return data;
        }

        public void setData(TemplateData data) {
            this.data = data;
        }

        public Object getTrackingData() {
            return trackingData;
        }

        public void setTrackingData(Object trackingData) {
            this.trackingData = trackingData;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAdIndex() {
            return adIndex;
        }

        public void setAdIndex(int adIndex) {
            this.adIndex = adIndex;
        }

        /**
         * 具体的模板数据
         */
        public static class TemplateData {
            private String dataType;
            private TemplateHeader header;
            private int count;
            private Object adTrack;
            private Object footer;
            private List<TemplateItemList> itemList;

            public String getDataType() {
                return dataType;
            }

            public void setDataType(String dataType) {
                this.dataType = dataType;
            }

            public TemplateHeader getHeader() {
                return header;
            }

            public void setHeader(TemplateHeader header) {
                this.header = header;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public Object getAdTrack() {
                return adTrack;
            }

            public void setAdTrack(Object adTrack) {
                this.adTrack = adTrack;
            }

            public Object getFooter() {
                return footer;
            }

            public void setFooter(Object footer) {
                this.footer = footer;
            }

            public List<TemplateItemList> getItemList() {
                return itemList;
            }

            public void setItemList(List<TemplateItemList> itemList) {
                this.itemList = itemList;
            }

            public static class TemplateHeader {
                /**
                 * id : 5
                 * title : 开眼编辑精选
                 * font : bigBold
                 * subTitle : MONDAY, MAY 24
                 * subTitleFont : lobster
                 * textAlign : left
                 * cover : null
                 * label : null
                 * actionUrl : eyepetizer://feed?tabIndex=2
                 * labelList : null
                 * rightText : 查看往期
                 */
                private int id;
                private String title;
                private String font;
                private String subTitle;
                private String subTitleFont;
                private String textAlign;
                private Object cover;
                private Object label;
                private String actionUrl;
                private Object labelList;
                private String rightText;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getFont() {
                    return font;
                }

                public void setFont(String font) {
                    this.font = font;
                }

                public String getSubTitle() {
                    return subTitle;
                }

                public void setSubTitle(String subTitle) {
                    this.subTitle = subTitle;
                }

                public String getSubTitleFont() {
                    return subTitleFont;
                }

                public void setSubTitleFont(String subTitleFont) {
                    this.subTitleFont = subTitleFont;
                }

                public String getTextAlign() {
                    return textAlign;
                }

                public void setTextAlign(String textAlign) {
                    this.textAlign = textAlign;
                }

                public Object getCover() {
                    return cover;
                }

                public void setCover(Object cover) {
                    this.cover = cover;
                }

                public Object getLabel() {
                    return label;
                }

                public void setLabel(Object label) {
                    this.label = label;
                }

                public String getActionUrl() {
                    return actionUrl;
                }

                public void setActionUrl(String actionUrl) {
                    this.actionUrl = actionUrl;
                }

                public Object getLabelList() {
                    return labelList;
                }

                public void setLabelList(Object labelList) {
                    this.labelList = labelList;
                }

                public String getRightText() {
                    return rightText;
                }

                public void setRightText(String rightText) {
                    this.rightText = rightText;
                }
            }

            public static class TemplateItemList {
                /**
                 * type : followCard
                 * data : {"dataType":"FollowCard","header":{"id":254033,"title":"星球啊宇宙，皆是我碗里一抹灿烂的星火","font":null,"subTitle":null,"subTitleFont":null,"textAlign":"left","cover":null,"label":null,"actionUrl":"eyepetizer://pgc/detail/2161/?title=%E5%BC%80%E7%9C%BC%E5%88%9B%E6%84%8F%E7%B2%BE%E9%80%89&userType=PGC&tabIndex=1","labelList":null,"rightText":null,"icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg","iconType":"round","description":null,"time":1621818003000,"showHateVideo":false},"content":{"type":"video","data":{"dataType":"VideoBeanForClient","id":254033,"title":"星球啊宇宙，皆是我碗里一抹灿烂的星火","description":"Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo ","library":"DAILY","tags":[{"id":744,"name":"每日创意灵感","actionUrl":"eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","tagRecType":"IMPORTANT","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":94,"name":"实验性","actionUrl":"eyepetizer://tag/94/?title=%E5%AE%9E%E9%AA%8C%E6%80%A7","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/e1a1a2b35f6916636594fe6bff4c5050.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/4aae1da4cea59eb15007e8d306c6eaea.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":102,"name":"前卫","actionUrl":"eyepetizer://tag/102/?title=%E5%89%8D%E5%8D%AB","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":683,"name":"艺术","actionUrl":"eyepetizer://tag/683/?title=%E8%89%BA%E6%9C%AF","adTrack":null,"desc":"用形象纪录\u201c我思\u201d","bgPicture":"http://img.kaiyanapp.com/7f4b28deb406f7e6b78d4f70c5bec99b.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/52560bde4d8415af5944c93298c09ca4.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":15},{"id":684,"name":"设计","actionUrl":"eyepetizer://tag/684/?title=%E8%AE%BE%E8%AE%A1","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":546,"name":"宇宙","actionUrl":"eyepetizer://tag/546/?title=%E5%AE%87%E5%AE%99","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":548,"name":"星空","actionUrl":"eyepetizer://tag/548/?title=%E6%98%9F%E7%A9%BA","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":2,"name":"创意","actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/1b457058cf2b317304ff9f70543c040d.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/fdefdb34cbe3d2ac9964d306febe9025.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0}],"consumption":{"collectionCount":667,"shareCount":168,"replyCount":10,"realCollectionCount":454},"resourceType":"video","slogan":null,"provider":{"name":"Vimeo","alias":"vimeo","icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png"},"category":"创意","author":{"id":2161,"icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg","name":"开眼创意精选","description":"技术与审美结合，探索视觉的无限可能","link":"","latestReleaseTime":1621818003000,"videoNum":1193,"adTrack":null,"follow":{"itemType":"author","itemId":2161,"followed":false},"shield":{"itemType":"author","itemId":2161,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true,"recSort":0,"expert":false},"cover":{"feed":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/a9e85220b78aa2e54cc71a5f4313ecb9.png?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"},"playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=","thumbPlayUrl":null,"duration":75,"webUrl":{"raw":"http://www.eyepetizer.net/detail.html?vid=254033","forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=254033&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0"},"releaseTime":1621818003000,"playInfo":[{"height":428,"width":854,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=","size":18128955},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=ucloud&playUrlType=url_oss&udid=","size":18128955}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid="},{"height":640,"width":1280,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=","size":32731244},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=ucloud&playUrlType=url_oss&udid=","size":32731244}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid="}],"campaign":null,"waterMarks":null,"ad":false,"adTrack":[],"type":"NORMAL","titlePgc":null,"descriptionPgc":null,"remark":null,"ifLimitVideo":false,"searchWeight":0,"brandWebsiteInfo":null,"videoPosterBean":{"scale":0.725,"url":"http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/3ee1621d11fcdb12920dd2c053465f0d.mp4","fileSizeStr":"5.24MB"},"idx":0,"shareAdTrack":null,"favoriteAdTrack":null,"webAdTrack":null,"date":1621818000000,"promotion":null,"label":null,"labelList":[],"descriptionEditor":"Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo ","collected":false,"reallyCollected":false,"played":false,"subtitles":[],"lastViewTime":null,"playlists":null,"src":null,"recallSource":null,"recall_source":null},"trackingData":null,"tag":null,"id":0,"adIndex":-1},"adTrack":[]}
                 * trackingData : null
                 * tag : null
                 * id : 0
                 * adIndex : -1
                 */

                private String type;
                private DataBeanX data;
                private Object trackingData;
                private Object tag;
                private int id;
                private int adIndex;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public DataBeanX getData() {
                    return data;
                }

                public void setData(DataBeanX data) {
                    this.data = data;
                }

                public Object getTrackingData() {
                    return trackingData;
                }

                public void setTrackingData(Object trackingData) {
                    this.trackingData = trackingData;
                }

                public Object getTag() {
                    return tag;
                }

                public void setTag(Object tag) {
                    this.tag = tag;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getAdIndex() {
                    return adIndex;
                }

                public void setAdIndex(int adIndex) {
                    this.adIndex = adIndex;
                }

                public static class DataBeanX {
                    /**
                     * dataType : FollowCard
                     * header : {"id":254033,"title":"星球啊宇宙，皆是我碗里一抹灿烂的星火","font":null,"subTitle":null,"subTitleFont":null,"textAlign":"left","cover":null,"label":null,"actionUrl":"eyepetizer://pgc/detail/2161/?title=%E5%BC%80%E7%9C%BC%E5%88%9B%E6%84%8F%E7%B2%BE%E9%80%89&userType=PGC&tabIndex=1","labelList":null,"rightText":null,"icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg","iconType":"round","description":null,"time":1621818003000,"showHateVideo":false}
                     * content : {"type":"video","data":{"dataType":"VideoBeanForClient","id":254033,"title":"星球啊宇宙，皆是我碗里一抹灿烂的星火","description":"Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo ","library":"DAILY","tags":[{"id":744,"name":"每日创意灵感","actionUrl":"eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","tagRecType":"IMPORTANT","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":94,"name":"实验性","actionUrl":"eyepetizer://tag/94/?title=%E5%AE%9E%E9%AA%8C%E6%80%A7","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/e1a1a2b35f6916636594fe6bff4c5050.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/4aae1da4cea59eb15007e8d306c6eaea.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":102,"name":"前卫","actionUrl":"eyepetizer://tag/102/?title=%E5%89%8D%E5%8D%AB","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":683,"name":"艺术","actionUrl":"eyepetizer://tag/683/?title=%E8%89%BA%E6%9C%AF","adTrack":null,"desc":"用形象纪录\u201c我思\u201d","bgPicture":"http://img.kaiyanapp.com/7f4b28deb406f7e6b78d4f70c5bec99b.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/52560bde4d8415af5944c93298c09ca4.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":15},{"id":684,"name":"设计","actionUrl":"eyepetizer://tag/684/?title=%E8%AE%BE%E8%AE%A1","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":546,"name":"宇宙","actionUrl":"eyepetizer://tag/546/?title=%E5%AE%87%E5%AE%99","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":548,"name":"星空","actionUrl":"eyepetizer://tag/548/?title=%E6%98%9F%E7%A9%BA","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":2,"name":"创意","actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/1b457058cf2b317304ff9f70543c040d.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/fdefdb34cbe3d2ac9964d306febe9025.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0}],"consumption":{"collectionCount":667,"shareCount":168,"replyCount":10,"realCollectionCount":454},"resourceType":"video","slogan":null,"provider":{"name":"Vimeo","alias":"vimeo","icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png"},"category":"创意","author":{"id":2161,"icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg","name":"开眼创意精选","description":"技术与审美结合，探索视觉的无限可能","link":"","latestReleaseTime":1621818003000,"videoNum":1193,"adTrack":null,"follow":{"itemType":"author","itemId":2161,"followed":false},"shield":{"itemType":"author","itemId":2161,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true,"recSort":0,"expert":false},"cover":{"feed":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/a9e85220b78aa2e54cc71a5f4313ecb9.png?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"},"playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=","thumbPlayUrl":null,"duration":75,"webUrl":{"raw":"http://www.eyepetizer.net/detail.html?vid=254033","forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=254033&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0"},"releaseTime":1621818003000,"playInfo":[{"height":428,"width":854,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=","size":18128955},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=ucloud&playUrlType=url_oss&udid=","size":18128955}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid="},{"height":640,"width":1280,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=","size":32731244},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=ucloud&playUrlType=url_oss&udid=","size":32731244}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid="}],"campaign":null,"waterMarks":null,"ad":false,"adTrack":[],"type":"NORMAL","titlePgc":null,"descriptionPgc":null,"remark":null,"ifLimitVideo":false,"searchWeight":0,"brandWebsiteInfo":null,"videoPosterBean":{"scale":0.725,"url":"http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/3ee1621d11fcdb12920dd2c053465f0d.mp4","fileSizeStr":"5.24MB"},"idx":0,"shareAdTrack":null,"favoriteAdTrack":null,"webAdTrack":null,"date":1621818000000,"promotion":null,"label":null,"labelList":[],"descriptionEditor":"Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo ","collected":false,"reallyCollected":false,"played":false,"subtitles":[],"lastViewTime":null,"playlists":null,"src":null,"recallSource":null,"recall_source":null},"trackingData":null,"tag":null,"id":0,"adIndex":-1}
                     * adTrack : []
                     */

                    private String dataType;
                    private HeaderBeanX header;
                    private ContentBean content;
                    private List<?> adTrack;

                    public String getDataType() {
                        return dataType;
                    }

                    public void setDataType(String dataType) {
                        this.dataType = dataType;
                    }

                    public HeaderBeanX getHeader() {
                        return header;
                    }

                    public void setHeader(HeaderBeanX header) {
                        this.header = header;
                    }

                    public ContentBean getContent() {
                        return content;
                    }

                    public void setContent(ContentBean content) {
                        this.content = content;
                    }

                    public List<?> getAdTrack() {
                        return adTrack;
                    }

                    public void setAdTrack(List<?> adTrack) {
                        this.adTrack = adTrack;
                    }

                    public static class HeaderBeanX {
                        /**
                         * id : 254033
                         * title : 星球啊宇宙，皆是我碗里一抹灿烂的星火
                         * font : null
                         * subTitle : null
                         * subTitleFont : null
                         * textAlign : left
                         * cover : null
                         * label : null
                         * actionUrl : eyepetizer://pgc/detail/2161/?title=%E5%BC%80%E7%9C%BC%E5%88%9B%E6%84%8F%E7%B2%BE%E9%80%89&userType=PGC&tabIndex=1
                         * labelList : null
                         * rightText : null
                         * icon : http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg
                         * iconType : round
                         * description : null
                         * time : 1621818003000
                         * showHateVideo : false
                         */

                        private int id;
                        private String title;
                        private Object font;
                        private Object subTitle;
                        private Object subTitleFont;
                        private String textAlign;
                        private Object cover;
                        private Object label;
                        private String actionUrl;
                        private Object labelList;
                        private Object rightText;
                        private String icon;
                        private String iconType;
                        private Object description;
                        private long time;
                        private boolean showHateVideo;

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public Object getFont() {
                            return font;
                        }

                        public void setFont(Object font) {
                            this.font = font;
                        }

                        public Object getSubTitle() {
                            return subTitle;
                        }

                        public void setSubTitle(Object subTitle) {
                            this.subTitle = subTitle;
                        }

                        public Object getSubTitleFont() {
                            return subTitleFont;
                        }

                        public void setSubTitleFont(Object subTitleFont) {
                            this.subTitleFont = subTitleFont;
                        }

                        public String getTextAlign() {
                            return textAlign;
                        }

                        public void setTextAlign(String textAlign) {
                            this.textAlign = textAlign;
                        }

                        public Object getCover() {
                            return cover;
                        }

                        public void setCover(Object cover) {
                            this.cover = cover;
                        }

                        public Object getLabel() {
                            return label;
                        }

                        public void setLabel(Object label) {
                            this.label = label;
                        }

                        public String getActionUrl() {
                            return actionUrl;
                        }

                        public void setActionUrl(String actionUrl) {
                            this.actionUrl = actionUrl;
                        }

                        public Object getLabelList() {
                            return labelList;
                        }

                        public void setLabelList(Object labelList) {
                            this.labelList = labelList;
                        }

                        public Object getRightText() {
                            return rightText;
                        }

                        public void setRightText(Object rightText) {
                            this.rightText = rightText;
                        }

                        public String getIcon() {
                            return icon;
                        }

                        public void setIcon(String icon) {
                            this.icon = icon;
                        }

                        public String getIconType() {
                            return iconType;
                        }

                        public void setIconType(String iconType) {
                            this.iconType = iconType;
                        }

                        public Object getDescription() {
                            return description;
                        }

                        public void setDescription(Object description) {
                            this.description = description;
                        }

                        public long getTime() {
                            return time;
                        }

                        public void setTime(long time) {
                            this.time = time;
                        }

                        public boolean isShowHateVideo() {
                            return showHateVideo;
                        }

                        public void setShowHateVideo(boolean showHateVideo) {
                            this.showHateVideo = showHateVideo;
                        }
                    }

                    public static class ContentBean {
                        /**
                         * type : video
                         * data : {"dataType":"VideoBeanForClient","id":254033,"title":"星球啊宇宙，皆是我碗里一抹灿烂的星火","description":"Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo ","library":"DAILY","tags":[{"id":744,"name":"每日创意灵感","actionUrl":"eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","tagRecType":"IMPORTANT","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":94,"name":"实验性","actionUrl":"eyepetizer://tag/94/?title=%E5%AE%9E%E9%AA%8C%E6%80%A7","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/e1a1a2b35f6916636594fe6bff4c5050.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/4aae1da4cea59eb15007e8d306c6eaea.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":102,"name":"前卫","actionUrl":"eyepetizer://tag/102/?title=%E5%89%8D%E5%8D%AB","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":683,"name":"艺术","actionUrl":"eyepetizer://tag/683/?title=%E8%89%BA%E6%9C%AF","adTrack":null,"desc":"用形象纪录\u201c我思\u201d","bgPicture":"http://img.kaiyanapp.com/7f4b28deb406f7e6b78d4f70c5bec99b.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/52560bde4d8415af5944c93298c09ca4.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":15},{"id":684,"name":"设计","actionUrl":"eyepetizer://tag/684/?title=%E8%AE%BE%E8%AE%A1","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":546,"name":"宇宙","actionUrl":"eyepetizer://tag/546/?title=%E5%AE%87%E5%AE%99","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":548,"name":"星空","actionUrl":"eyepetizer://tag/548/?title=%E6%98%9F%E7%A9%BA","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":2,"name":"创意","actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/1b457058cf2b317304ff9f70543c040d.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/fdefdb34cbe3d2ac9964d306febe9025.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0}],"consumption":{"collectionCount":667,"shareCount":168,"replyCount":10,"realCollectionCount":454},"resourceType":"video","slogan":null,"provider":{"name":"Vimeo","alias":"vimeo","icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png"},"category":"创意","author":{"id":2161,"icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg","name":"开眼创意精选","description":"技术与审美结合，探索视觉的无限可能","link":"","latestReleaseTime":1621818003000,"videoNum":1193,"adTrack":null,"follow":{"itemType":"author","itemId":2161,"followed":false},"shield":{"itemType":"author","itemId":2161,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true,"recSort":0,"expert":false},"cover":{"feed":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/a9e85220b78aa2e54cc71a5f4313ecb9.png?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"},"playUrl":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=","thumbPlayUrl":null,"duration":75,"webUrl":{"raw":"http://www.eyepetizer.net/detail.html?vid=254033","forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=254033&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0"},"releaseTime":1621818003000,"playInfo":[{"height":428,"width":854,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=","size":18128955},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=ucloud&playUrlType=url_oss&udid=","size":18128955}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid="},{"height":640,"width":1280,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=","size":32731244},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=ucloud&playUrlType=url_oss&udid=","size":32731244}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid="}],"campaign":null,"waterMarks":null,"ad":false,"adTrack":[],"type":"NORMAL","titlePgc":null,"descriptionPgc":null,"remark":null,"ifLimitVideo":false,"searchWeight":0,"brandWebsiteInfo":null,"videoPosterBean":{"scale":0.725,"url":"http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/3ee1621d11fcdb12920dd2c053465f0d.mp4","fileSizeStr":"5.24MB"},"idx":0,"shareAdTrack":null,"favoriteAdTrack":null,"webAdTrack":null,"date":1621818000000,"promotion":null,"label":null,"labelList":[],"descriptionEditor":"Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo ","collected":false,"reallyCollected":false,"played":false,"subtitles":[],"lastViewTime":null,"playlists":null,"src":null,"recallSource":null,"recall_source":null}
                         * trackingData : null
                         * tag : null
                         * id : 0
                         * adIndex : -1
                         */

                        private String type;
                        private DataBean data;
                        private Object trackingData;
                        private Object tag;
                        private int id;
                        private int adIndex;

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }

                        public DataBean getData() {
                            return data;
                        }

                        public void setData(DataBean data) {
                            this.data = data;
                        }

                        public Object getTrackingData() {
                            return trackingData;
                        }

                        public void setTrackingData(Object trackingData) {
                            this.trackingData = trackingData;
                        }

                        public Object getTag() {
                            return tag;
                        }

                        public void setTag(Object tag) {
                            this.tag = tag;
                        }

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public int getAdIndex() {
                            return adIndex;
                        }

                        public void setAdIndex(int adIndex) {
                            this.adIndex = adIndex;
                        }

                        public static class DataBean {
                            /**
                             * dataType : VideoBeanForClient
                             * id : 254033
                             * title : 星球啊宇宙，皆是我碗里一抹灿烂的星火
                             * description : Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo
                             * library : DAILY
                             * tags : [{"id":744,"name":"每日创意灵感","actionUrl":"eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg","tagRecType":"IMPORTANT","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":94,"name":"实验性","actionUrl":"eyepetizer://tag/94/?title=%E5%AE%9E%E9%AA%8C%E6%80%A7","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/e1a1a2b35f6916636594fe6bff4c5050.jpeg?imageMogr2/quality/100","headerImage":"http://img.kaiyanapp.com/4aae1da4cea59eb15007e8d306c6eaea.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":102,"name":"前卫","actionUrl":"eyepetizer://tag/102/?title=%E5%89%8D%E5%8D%AB","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/0b311841ee68c2027bbedb2cb8f11a89.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":683,"name":"艺术","actionUrl":"eyepetizer://tag/683/?title=%E8%89%BA%E6%9C%AF","adTrack":null,"desc":"用形象纪录\u201c我思\u201d","bgPicture":"http://img.kaiyanapp.com/7f4b28deb406f7e6b78d4f70c5bec99b.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/52560bde4d8415af5944c93298c09ca4.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":15},{"id":684,"name":"设计","actionUrl":"eyepetizer://tag/684/?title=%E8%AE%BE%E8%AE%A1","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/35ad4c34a1504cd8d398b315453a0f69.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":546,"name":"宇宙","actionUrl":"eyepetizer://tag/546/?title=%E5%AE%87%E5%AE%99","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/7052c0f6e4267111b023d2541b1a7f07.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":548,"name":"星空","actionUrl":"eyepetizer://tag/548/?title=%E6%98%9F%E7%A9%BA","adTrack":null,"desc":null,"bgPicture":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/1c9468c6ade1524f09453d7aa547a30b.jpeg?imageMogr2/quality/60/format/jpg","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0},{"id":2,"name":"创意","actionUrl":"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F","adTrack":null,"desc":"技术与审美结合，探索视觉的无限可能","bgPicture":"http://img.kaiyanapp.com/1b457058cf2b317304ff9f70543c040d.png?imageMogr2/quality/60/format/jpg","headerImage":"http://img.kaiyanapp.com/fdefdb34cbe3d2ac9964d306febe9025.jpeg?imageMogr2/quality/100","tagRecType":"NORMAL","childTagList":null,"childTagIdList":null,"haveReward":false,"ifNewest":false,"newestEndTime":null,"communityIndex":0}]
                             * consumption : {"collectionCount":667,"shareCount":168,"replyCount":10,"realCollectionCount":454}
                             * resourceType : video
                             * slogan : null
                             * provider : {"name":"Vimeo","alias":"vimeo","icon":"http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png"}
                             * category : 创意
                             * author : {"id":2161,"icon":"http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg","name":"开眼创意精选","description":"技术与审美结合，探索视觉的无限可能","link":"","latestReleaseTime":1621818003000,"videoNum":1193,"adTrack":null,"follow":{"itemType":"author","itemId":2161,"followed":false},"shield":{"itemType":"author","itemId":2161,"shielded":false},"approvedNotReadyVideoCount":0,"ifPgc":true,"recSort":0,"expert":false}
                             * cover : {"feed":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","detail":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg","blurred":"http://img.kaiyanapp.com/a9e85220b78aa2e54cc71a5f4313ecb9.png?imageMogr2/quality/60/format/jpg","sharing":null,"homepage":"http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim"}
                             * playUrl : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=
                             * thumbPlayUrl : null
                             * duration : 75
                             * webUrl : {"raw":"http://www.eyepetizer.net/detail.html?vid=254033","forWeibo":"https://m.eyepetizer.net/u1/video-detail?video_id=254033&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0"}
                             * releaseTime : 1621818003000
                             * playInfo : [{"height":428,"width":854,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=","size":18128955},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=ucloud&playUrlType=url_oss&udid=","size":18128955}],"name":"标清","type":"normal","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid="},{"height":640,"width":1280,"urlList":[{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid=","size":32731244},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=ucloud&playUrlType=url_oss&udid=","size":32731244}],"name":"高清","type":"high","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=high&source=aliyun&playUrlType=url_oss&udid="}]
                             * campaign : null
                             * waterMarks : null
                             * ad : false
                             * adTrack : []
                             * type : NORMAL
                             * titlePgc : null
                             * descriptionPgc : null
                             * remark : null
                             * ifLimitVideo : false
                             * searchWeight : 0
                             * brandWebsiteInfo : null
                             * videoPosterBean : {"scale":0.725,"url":"http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/3ee1621d11fcdb12920dd2c053465f0d.mp4","fileSizeStr":"5.24MB"}
                             * idx : 0
                             * shareAdTrack : null
                             * favoriteAdTrack : null
                             * webAdTrack : null
                             * date : 1621818000000
                             * promotion : null
                             * label : null
                             * labelList : []
                             * descriptionEditor : Treedeo 工作室用丙烯颜料展现我们美丽星球上的山川大河、沙漠星辰。该工作室成立于 2005 年，致力于为不同的品牌和客户制作具有其强烈视觉特征的创意短片或标志，曾与 envato、iStock、POND5 等公司合作。From Treedeo
                             * collected : false
                             * reallyCollected : false
                             * played : false
                             * subtitles : []
                             * lastViewTime : null
                             * playlists : null
                             * src : null
                             * recallSource : null
                             * recall_source : null
                             */

                            private String dataType;
                            private int id;
                            private String title;
                            private String description;
                            private String library;
                            private ConsumptionBean consumption;
                            private String resourceType;
                            private Object slogan;
                            private ProviderBean provider;
                            private String category;
                            private AuthorBean author;
                            private CoverBean cover;
                            private String playUrl;
                            private Object thumbPlayUrl;
                            private int duration;
                            private WebUrlBean webUrl;
                            private long releaseTime;
                            private Object campaign;
                            private Object waterMarks;
                            private boolean ad;
                            private String type;
                            private Object titlePgc;
                            private Object descriptionPgc;
                            private Object remark;
                            private boolean ifLimitVideo;
                            private int searchWeight;
                            private Object brandWebsiteInfo;
                            private VideoPosterBeanBean videoPosterBean;
                            private int idx;
                            private Object shareAdTrack;
                            private Object favoriteAdTrack;
                            private Object webAdTrack;
                            private long date;
                            private Object promotion;
                            private Object label;
                            private String descriptionEditor;
                            private boolean collected;
                            private boolean reallyCollected;
                            private boolean played;
                            private Object lastViewTime;
                            private Object playlists;
                            private Object src;
                            private Object recallSource;
                            private Object recall_source;
                            private List<TagsBean> tags;
                            private List<PlayInfoBean> playInfo;
                            private List<?> adTrack;
                            private List<?> labelList;
                            private List<?> subtitles;

                            public String getDataType() {
                                return dataType;
                            }

                            public void setDataType(String dataType) {
                                this.dataType = dataType;
                            }

                            public int getId() {
                                return id;
                            }

                            public void setId(int id) {
                                this.id = id;
                            }

                            public String getTitle() {
                                return title;
                            }

                            public void setTitle(String title) {
                                this.title = title;
                            }

                            public String getDescription() {
                                return description;
                            }

                            public void setDescription(String description) {
                                this.description = description;
                            }

                            public String getLibrary() {
                                return library;
                            }

                            public void setLibrary(String library) {
                                this.library = library;
                            }

                            public ConsumptionBean getConsumption() {
                                return consumption;
                            }

                            public void setConsumption(ConsumptionBean consumption) {
                                this.consumption = consumption;
                            }

                            public String getResourceType() {
                                return resourceType;
                            }

                            public void setResourceType(String resourceType) {
                                this.resourceType = resourceType;
                            }

                            public Object getSlogan() {
                                return slogan;
                            }

                            public void setSlogan(Object slogan) {
                                this.slogan = slogan;
                            }

                            public ProviderBean getProvider() {
                                return provider;
                            }

                            public void setProvider(ProviderBean provider) {
                                this.provider = provider;
                            }

                            public String getCategory() {
                                return category;
                            }

                            public void setCategory(String category) {
                                this.category = category;
                            }

                            public AuthorBean getAuthor() {
                                return author;
                            }

                            public void setAuthor(AuthorBean author) {
                                this.author = author;
                            }

                            public CoverBean getCover() {
                                return cover;
                            }

                            public void setCover(CoverBean cover) {
                                this.cover = cover;
                            }

                            public String getPlayUrl() {
                                return playUrl;
                            }

                            public void setPlayUrl(String playUrl) {
                                this.playUrl = playUrl;
                            }

                            public Object getThumbPlayUrl() {
                                return thumbPlayUrl;
                            }

                            public void setThumbPlayUrl(Object thumbPlayUrl) {
                                this.thumbPlayUrl = thumbPlayUrl;
                            }

                            public int getDuration() {
                                return duration;
                            }

                            public void setDuration(int duration) {
                                this.duration = duration;
                            }

                            public WebUrlBean getWebUrl() {
                                return webUrl;
                            }

                            public void setWebUrl(WebUrlBean webUrl) {
                                this.webUrl = webUrl;
                            }

                            public long getReleaseTime() {
                                return releaseTime;
                            }

                            public void setReleaseTime(long releaseTime) {
                                this.releaseTime = releaseTime;
                            }

                            public Object getCampaign() {
                                return campaign;
                            }

                            public void setCampaign(Object campaign) {
                                this.campaign = campaign;
                            }

                            public Object getWaterMarks() {
                                return waterMarks;
                            }

                            public void setWaterMarks(Object waterMarks) {
                                this.waterMarks = waterMarks;
                            }

                            public boolean isAd() {
                                return ad;
                            }

                            public void setAd(boolean ad) {
                                this.ad = ad;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }

                            public Object getTitlePgc() {
                                return titlePgc;
                            }

                            public void setTitlePgc(Object titlePgc) {
                                this.titlePgc = titlePgc;
                            }

                            public Object getDescriptionPgc() {
                                return descriptionPgc;
                            }

                            public void setDescriptionPgc(Object descriptionPgc) {
                                this.descriptionPgc = descriptionPgc;
                            }

                            public Object getRemark() {
                                return remark;
                            }

                            public void setRemark(Object remark) {
                                this.remark = remark;
                            }

                            public boolean isIfLimitVideo() {
                                return ifLimitVideo;
                            }

                            public void setIfLimitVideo(boolean ifLimitVideo) {
                                this.ifLimitVideo = ifLimitVideo;
                            }

                            public int getSearchWeight() {
                                return searchWeight;
                            }

                            public void setSearchWeight(int searchWeight) {
                                this.searchWeight = searchWeight;
                            }

                            public Object getBrandWebsiteInfo() {
                                return brandWebsiteInfo;
                            }

                            public void setBrandWebsiteInfo(Object brandWebsiteInfo) {
                                this.brandWebsiteInfo = brandWebsiteInfo;
                            }

                            public VideoPosterBeanBean getVideoPosterBean() {
                                return videoPosterBean;
                            }

                            public void setVideoPosterBean(VideoPosterBeanBean videoPosterBean) {
                                this.videoPosterBean = videoPosterBean;
                            }

                            public int getIdx() {
                                return idx;
                            }

                            public void setIdx(int idx) {
                                this.idx = idx;
                            }

                            public Object getShareAdTrack() {
                                return shareAdTrack;
                            }

                            public void setShareAdTrack(Object shareAdTrack) {
                                this.shareAdTrack = shareAdTrack;
                            }

                            public Object getFavoriteAdTrack() {
                                return favoriteAdTrack;
                            }

                            public void setFavoriteAdTrack(Object favoriteAdTrack) {
                                this.favoriteAdTrack = favoriteAdTrack;
                            }

                            public Object getWebAdTrack() {
                                return webAdTrack;
                            }

                            public void setWebAdTrack(Object webAdTrack) {
                                this.webAdTrack = webAdTrack;
                            }

                            public long getDate() {
                                return date;
                            }

                            public void setDate(long date) {
                                this.date = date;
                            }

                            public Object getPromotion() {
                                return promotion;
                            }

                            public void setPromotion(Object promotion) {
                                this.promotion = promotion;
                            }

                            public Object getLabel() {
                                return label;
                            }

                            public void setLabel(Object label) {
                                this.label = label;
                            }

                            public String getDescriptionEditor() {
                                return descriptionEditor;
                            }

                            public void setDescriptionEditor(String descriptionEditor) {
                                this.descriptionEditor = descriptionEditor;
                            }

                            public boolean isCollected() {
                                return collected;
                            }

                            public void setCollected(boolean collected) {
                                this.collected = collected;
                            }

                            public boolean isReallyCollected() {
                                return reallyCollected;
                            }

                            public void setReallyCollected(boolean reallyCollected) {
                                this.reallyCollected = reallyCollected;
                            }

                            public boolean isPlayed() {
                                return played;
                            }

                            public void setPlayed(boolean played) {
                                this.played = played;
                            }

                            public Object getLastViewTime() {
                                return lastViewTime;
                            }

                            public void setLastViewTime(Object lastViewTime) {
                                this.lastViewTime = lastViewTime;
                            }

                            public Object getPlaylists() {
                                return playlists;
                            }

                            public void setPlaylists(Object playlists) {
                                this.playlists = playlists;
                            }

                            public Object getSrc() {
                                return src;
                            }

                            public void setSrc(Object src) {
                                this.src = src;
                            }

                            public Object getRecallSource() {
                                return recallSource;
                            }

                            public void setRecallSource(Object recallSource) {
                                this.recallSource = recallSource;
                            }

                            public Object getRecall_source() {
                                return recall_source;
                            }

                            public void setRecall_source(Object recall_source) {
                                this.recall_source = recall_source;
                            }

                            public List<TagsBean> getTags() {
                                return tags;
                            }

                            public void setTags(List<TagsBean> tags) {
                                this.tags = tags;
                            }

                            public List<PlayInfoBean> getPlayInfo() {
                                return playInfo;
                            }

                            public void setPlayInfo(List<PlayInfoBean> playInfo) {
                                this.playInfo = playInfo;
                            }

                            public List<?> getAdTrack() {
                                return adTrack;
                            }

                            public void setAdTrack(List<?> adTrack) {
                                this.adTrack = adTrack;
                            }

                            public List<?> getLabelList() {
                                return labelList;
                            }

                            public void setLabelList(List<?> labelList) {
                                this.labelList = labelList;
                            }

                            public List<?> getSubtitles() {
                                return subtitles;
                            }

                            public void setSubtitles(List<?> subtitles) {
                                this.subtitles = subtitles;
                            }

                            public static class ConsumptionBean {
                                /**
                                 * collectionCount : 667
                                 * shareCount : 168
                                 * replyCount : 10
                                 * realCollectionCount : 454
                                 */

                                private int collectionCount;
                                private int shareCount;
                                private int replyCount;
                                private int realCollectionCount;

                                public int getCollectionCount() {
                                    return collectionCount;
                                }

                                public void setCollectionCount(int collectionCount) {
                                    this.collectionCount = collectionCount;
                                }

                                public int getShareCount() {
                                    return shareCount;
                                }

                                public void setShareCount(int shareCount) {
                                    this.shareCount = shareCount;
                                }

                                public int getReplyCount() {
                                    return replyCount;
                                }

                                public void setReplyCount(int replyCount) {
                                    this.replyCount = replyCount;
                                }

                                public int getRealCollectionCount() {
                                    return realCollectionCount;
                                }

                                public void setRealCollectionCount(int realCollectionCount) {
                                    this.realCollectionCount = realCollectionCount;
                                }
                            }

                            public static class ProviderBean {
                                /**
                                 * name : Vimeo
                                 * alias : vimeo
                                 * icon : http://img.kaiyanapp.com/c3ad630be461cbb081649c9e21d6cbe3.png
                                 */

                                private String name;
                                private String alias;
                                private String icon;

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getAlias() {
                                    return alias;
                                }

                                public void setAlias(String alias) {
                                    this.alias = alias;
                                }

                                public String getIcon() {
                                    return icon;
                                }

                                public void setIcon(String icon) {
                                    this.icon = icon;
                                }
                            }

                            public static class AuthorBean {
                                /**
                                 * id : 2161
                                 * icon : http://img.kaiyanapp.com/f4a9aba1c6857ee0cefcdc5aee0a1fc9.png?imageMogr2/quality/60/format/jpg
                                 * name : 开眼创意精选
                                 * description : 技术与审美结合，探索视觉的无限可能
                                 * link :
                                 * latestReleaseTime : 1621818003000
                                 * videoNum : 1193
                                 * adTrack : null
                                 * follow : {"itemType":"author","itemId":2161,"followed":false}
                                 * shield : {"itemType":"author","itemId":2161,"shielded":false}
                                 * approvedNotReadyVideoCount : 0
                                 * ifPgc : true
                                 * recSort : 0
                                 * expert : false
                                 */

                                private int id;
                                private String icon;
                                private String name;
                                private String description;
                                private String link;
                                private long latestReleaseTime;
                                private int videoNum;
                                private Object adTrack;
                                private FollowBean follow;
                                private ShieldBean shield;
                                private int approvedNotReadyVideoCount;
                                private boolean ifPgc;
                                private int recSort;
                                private boolean expert;

                                public int getId() {
                                    return id;
                                }

                                public void setId(int id) {
                                    this.id = id;
                                }

                                public String getIcon() {
                                    return icon;
                                }

                                public void setIcon(String icon) {
                                    this.icon = icon;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getDescription() {
                                    return description;
                                }

                                public void setDescription(String description) {
                                    this.description = description;
                                }

                                public String getLink() {
                                    return link;
                                }

                                public void setLink(String link) {
                                    this.link = link;
                                }

                                public long getLatestReleaseTime() {
                                    return latestReleaseTime;
                                }

                                public void setLatestReleaseTime(long latestReleaseTime) {
                                    this.latestReleaseTime = latestReleaseTime;
                                }

                                public int getVideoNum() {
                                    return videoNum;
                                }

                                public void setVideoNum(int videoNum) {
                                    this.videoNum = videoNum;
                                }

                                public Object getAdTrack() {
                                    return adTrack;
                                }

                                public void setAdTrack(Object adTrack) {
                                    this.adTrack = adTrack;
                                }

                                public FollowBean getFollow() {
                                    return follow;
                                }

                                public void setFollow(FollowBean follow) {
                                    this.follow = follow;
                                }

                                public ShieldBean getShield() {
                                    return shield;
                                }

                                public void setShield(ShieldBean shield) {
                                    this.shield = shield;
                                }

                                public int getApprovedNotReadyVideoCount() {
                                    return approvedNotReadyVideoCount;
                                }

                                public void setApprovedNotReadyVideoCount(int approvedNotReadyVideoCount) {
                                    this.approvedNotReadyVideoCount = approvedNotReadyVideoCount;
                                }

                                public boolean isIfPgc() {
                                    return ifPgc;
                                }

                                public void setIfPgc(boolean ifPgc) {
                                    this.ifPgc = ifPgc;
                                }

                                public int getRecSort() {
                                    return recSort;
                                }

                                public void setRecSort(int recSort) {
                                    this.recSort = recSort;
                                }

                                public boolean isExpert() {
                                    return expert;
                                }

                                public void setExpert(boolean expert) {
                                    this.expert = expert;
                                }

                                public static class FollowBean {
                                    /**
                                     * itemType : author
                                     * itemId : 2161
                                     * followed : false
                                     */

                                    private String itemType;
                                    private int itemId;
                                    private boolean followed;

                                    public String getItemType() {
                                        return itemType;
                                    }

                                    public void setItemType(String itemType) {
                                        this.itemType = itemType;
                                    }

                                    public int getItemId() {
                                        return itemId;
                                    }

                                    public void setItemId(int itemId) {
                                        this.itemId = itemId;
                                    }

                                    public boolean isFollowed() {
                                        return followed;
                                    }

                                    public void setFollowed(boolean followed) {
                                        this.followed = followed;
                                    }
                                }

                                public static class ShieldBean {
                                    /**
                                     * itemType : author
                                     * itemId : 2161
                                     * shielded : false
                                     */

                                    private String itemType;
                                    private int itemId;
                                    private boolean shielded;

                                    public String getItemType() {
                                        return itemType;
                                    }

                                    public void setItemType(String itemType) {
                                        this.itemType = itemType;
                                    }

                                    public int getItemId() {
                                        return itemId;
                                    }

                                    public void setItemId(int itemId) {
                                        this.itemId = itemId;
                                    }

                                    public boolean isShielded() {
                                        return shielded;
                                    }

                                    public void setShielded(boolean shielded) {
                                        this.shielded = shielded;
                                    }
                                }
                            }

                            public static class CoverBean {
                                /**
                                 * feed : http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg
                                 * detail : http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageMogr2/quality/60/format/jpg
                                 * blurred : http://img.kaiyanapp.com/a9e85220b78aa2e54cc71a5f4313ecb9.png?imageMogr2/quality/60/format/jpg
                                 * sharing : null
                                 * homepage : http://img.kaiyanapp.com/efbbf8a0b377e72989e50fac5f5378d3.png?imageView2/1/w/720/h/560/format/jpg/q/75|watermark/1/image/aHR0cDovL2ltZy5rYWl5YW5hcHAuY29tL2JsYWNrXzMwLnBuZw==/dissolve/100/gravity/Center/dx/0/dy/0|imageslim
                                 */

                                private String feed;
                                private String detail;
                                private String blurred;
                                private Object sharing;
                                private String homepage;

                                public String getFeed() {
                                    return feed;
                                }

                                public void setFeed(String feed) {
                                    this.feed = feed;
                                }

                                public String getDetail() {
                                    return detail;
                                }

                                public void setDetail(String detail) {
                                    this.detail = detail;
                                }

                                public String getBlurred() {
                                    return blurred;
                                }

                                public void setBlurred(String blurred) {
                                    this.blurred = blurred;
                                }

                                public Object getSharing() {
                                    return sharing;
                                }

                                public void setSharing(Object sharing) {
                                    this.sharing = sharing;
                                }

                                public String getHomepage() {
                                    return homepage;
                                }

                                public void setHomepage(String homepage) {
                                    this.homepage = homepage;
                                }
                            }

                            public static class WebUrlBean {
                                /**
                                 * raw : http://www.eyepetizer.net/detail.html?vid=254033
                                 * forWeibo : https://m.eyepetizer.net/u1/video-detail?video_id=254033&resource_type=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0
                                 */

                                private String raw;
                                private String forWeibo;

                                public String getRaw() {
                                    return raw;
                                }

                                public void setRaw(String raw) {
                                    this.raw = raw;
                                }

                                public String getForWeibo() {
                                    return forWeibo;
                                }

                                public void setForWeibo(String forWeibo) {
                                    this.forWeibo = forWeibo;
                                }
                            }

                            public static class VideoPosterBeanBean {
                                /**
                                 * scale : 0.725
                                 * url : http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/3ee1621d11fcdb12920dd2c053465f0d.mp4
                                 * fileSizeStr : 5.24MB
                                 */

                                private double scale;
                                private String url;
                                private String fileSizeStr;

                                public double getScale() {
                                    return scale;
                                }

                                public void setScale(double scale) {
                                    this.scale = scale;
                                }

                                public String getUrl() {
                                    return url;
                                }

                                public void setUrl(String url) {
                                    this.url = url;
                                }

                                public String getFileSizeStr() {
                                    return fileSizeStr;
                                }

                                public void setFileSizeStr(String fileSizeStr) {
                                    this.fileSizeStr = fileSizeStr;
                                }
                            }

                            public static class TagsBean {
                                /**
                                 * id : 744
                                 * name : 每日创意灵感
                                 * actionUrl : eyepetizer://tag/744/?title=%E6%AF%8F%E6%97%A5%E5%88%9B%E6%84%8F%E7%81%B5%E6%84%9F
                                 * adTrack : null
                                 * desc : 技术与审美结合，探索视觉的无限可能
                                 * bgPicture : http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg
                                 * headerImage : http://img.kaiyanapp.com/bc2479c09cd15cb93b69d82e5f21c3fc.png?imageMogr2/quality/60/format/jpg
                                 * tagRecType : IMPORTANT
                                 * childTagList : null
                                 * childTagIdList : null
                                 * haveReward : false
                                 * ifNewest : false
                                 * newestEndTime : null
                                 * communityIndex : 0
                                 */

                                private int id;
                                private String name;
                                private String actionUrl;
                                private Object adTrack;
                                private String desc;
                                private String bgPicture;
                                private String headerImage;
                                private String tagRecType;
                                private Object childTagList;
                                private Object childTagIdList;
                                private boolean haveReward;
                                private boolean ifNewest;
                                private Object newestEndTime;
                                private int communityIndex;

                                public int getId() {
                                    return id;
                                }

                                public void setId(int id) {
                                    this.id = id;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getActionUrl() {
                                    return actionUrl;
                                }

                                public void setActionUrl(String actionUrl) {
                                    this.actionUrl = actionUrl;
                                }

                                public Object getAdTrack() {
                                    return adTrack;
                                }

                                public void setAdTrack(Object adTrack) {
                                    this.adTrack = adTrack;
                                }

                                public String getDesc() {
                                    return desc;
                                }

                                public void setDesc(String desc) {
                                    this.desc = desc;
                                }

                                public String getBgPicture() {
                                    return bgPicture;
                                }

                                public void setBgPicture(String bgPicture) {
                                    this.bgPicture = bgPicture;
                                }

                                public String getHeaderImage() {
                                    return headerImage;
                                }

                                public void setHeaderImage(String headerImage) {
                                    this.headerImage = headerImage;
                                }

                                public String getTagRecType() {
                                    return tagRecType;
                                }

                                public void setTagRecType(String tagRecType) {
                                    this.tagRecType = tagRecType;
                                }

                                public Object getChildTagList() {
                                    return childTagList;
                                }

                                public void setChildTagList(Object childTagList) {
                                    this.childTagList = childTagList;
                                }

                                public Object getChildTagIdList() {
                                    return childTagIdList;
                                }

                                public void setChildTagIdList(Object childTagIdList) {
                                    this.childTagIdList = childTagIdList;
                                }

                                public boolean isHaveReward() {
                                    return haveReward;
                                }

                                public void setHaveReward(boolean haveReward) {
                                    this.haveReward = haveReward;
                                }

                                public boolean isIfNewest() {
                                    return ifNewest;
                                }

                                public void setIfNewest(boolean ifNewest) {
                                    this.ifNewest = ifNewest;
                                }

                                public Object getNewestEndTime() {
                                    return newestEndTime;
                                }

                                public void setNewestEndTime(Object newestEndTime) {
                                    this.newestEndTime = newestEndTime;
                                }

                                public int getCommunityIndex() {
                                    return communityIndex;
                                }

                                public void setCommunityIndex(int communityIndex) {
                                    this.communityIndex = communityIndex;
                                }
                            }

                            public static class PlayInfoBean {
                                /**
                                 * height : 428
                                 * width : 854
                                 * urlList : [{"name":"aliyun","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=","size":18128955},{"name":"ucloud","url":"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=ucloud&playUrlType=url_oss&udid=","size":18128955}]
                                 * name : 标清
                                 * type : normal
                                 * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=
                                 */

                                private int height;
                                private int width;
                                private String name;
                                private String type;
                                private String url;
                                private List<UrlListBean> urlList;

                                public int getHeight() {
                                    return height;
                                }

                                public void setHeight(int height) {
                                    this.height = height;
                                }

                                public int getWidth() {
                                    return width;
                                }

                                public void setWidth(int width) {
                                    this.width = width;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getType() {
                                    return type;
                                }

                                public void setType(String type) {
                                    this.type = type;
                                }

                                public String getUrl() {
                                    return url;
                                }

                                public void setUrl(String url) {
                                    this.url = url;
                                }

                                public List<UrlListBean> getUrlList() {
                                    return urlList;
                                }

                                public void setUrlList(List<UrlListBean> urlList) {
                                    this.urlList = urlList;
                                }

                                public static class UrlListBean {
                                    /**
                                     * name : aliyun
                                     * url : http://baobab.kaiyanapp.com/api/v1/playUrl?vid=254033&resourceType=video&editionType=normal&source=aliyun&playUrlType=url_oss&udid=
                                     * size : 18128955
                                     */

                                    private String name;
                                    private String url;
                                    private int size;

                                    public String getName() {
                                        return name;
                                    }

                                    public void setName(String name) {
                                        this.name = name;
                                    }

                                    public String getUrl() {
                                        return url;
                                    }

                                    public void setUrl(String url) {
                                        this.url = url;
                                    }

                                    public int getSize() {
                                        return size;
                                    }

                                    public void setSize(int size) {
                                        this.size = size;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
