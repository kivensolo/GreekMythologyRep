package com.zeke.eyepetizer.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.ext.startActivity
import com.kingz.module.common.utils.image.GlideLoader
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer
import com.zeke.eyepetizer.bean.Item
import com.zeke.eyepetizer.bean.ItemTypeBanner
import com.zeke.eyepetizer.bean.cards.item.*
import com.zeke.eyepetizer.constant.ViewTypeEnum
import com.zeke.eyepetizer.videodetail.VideoDetailPlayActivity
import com.zeke.kangaroo.utils.ZLog
import com.zeke.moudle_eyepetizer.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

// <editor-fold defaultstate="collapsed" desc="别名属性  Alias property">
//开眼视频别名设置
internal typealias BannerItemData = ItemTypeBanner
// </editor-fold>

/**
 * author: King.Z <br>
 * date:  2021/5/24 13:05 <br>
 * description: 开眼视频分类内容数据Adapter <br>
 *     主要进行数据绑定逻辑
 *
 *  @param data: 外部初始化时，可传入初始数据
 *
 *  BaseQuickAdapter的layout参数，是默认子View的布局样式
 */
open class EyepetizerPagerAdapter(
    data: MutableList<Item>? = null
) : BaseQuickAdapter<Item, BaseViewHolder>(R.layout.item_article, data) {
    private var mContext: Context? = null

// <editor-fold defaultstate="collapsed" desc="业务属性">
    //banner模板缓存数据: key为模板id,value是数据集
    private var bannerDataListCache:HashMap<Int,ArrayList<BannerItemData>>?= null

// </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="基础Adapter回调函数">
    // <editor-fold defaultstate="collapsed" desc="确定子视图类型 Get view type">
    /**
     * 创建每一个子View的Holder前，
     * 重写getDefItemViewType确定每个子View的Type
     */
    override fun getDefItemViewType(position: Int): Int {
        val type = data[position].type
        return ViewTypeEnum.getViewTypeEnum(type).value
    }
    // </editor-fold>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        return super.onCreateViewHolder(parent, viewType)
    }

    // <editor-fold defaultstate="collapsed" desc="根据类型创建模板视图实例 Create View holder">
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        fun getHolder(@LayoutRes resource: Int): BaseViewHolder {
            return BaseViewHolder(inflater.inflate(resource, parent, false))
        }

        val viewHolder =
            when (viewType) {
                ViewTypeEnum.TheEnd.value -> getHolder(R.layout.template_the_end)
                ViewTypeEnum.TextCard.value -> getHolder(R.layout.template_text_card)
                ViewTypeEnum.BriefCard.value -> getHolder(R.layout.template_brief_card)
                ViewTypeEnum.FollowCard.value -> getHolder(R.layout.template_follow_card)
                ViewTypeEnum.VideoSmallCard.value -> getHolder(R.layout.template_video_small_card)
                ViewTypeEnum.DynamicInfoCard.value -> getHolder(R.layout.template_dynamic_info_card)
                ViewTypeEnum.AutoPlayFollowCard.value -> getHolder(R.layout.template_auto_play_follow_card)
                ViewTypeEnum.SquareCardCollection.value -> getHolder(R.layout.template_square_card_collection)
                else -> {
                    ZLog.d("Invalid view type: ${ViewTypeEnum.getNameFromValue(viewType)}  Use default layout.")
                    getHolder(R.layout.template_not_impletation)
//                还有 HorizontalScrollCard和VideoCollectionWithBrief 等没实现
                }
            }
        return BaseViewHolder(viewHolder.itemView)
    }
    // </editor-fold>

    override fun convert(holder: BaseViewHolder, item: Item) {
        when (ViewTypeEnum.getViewTypeEnum(item.type).value) {
            ViewTypeEnum.TheEnd.value -> initTheEndView(holder, item)
            ViewTypeEnum.TextCard.value -> initTextCardView(holder, item)
            ViewTypeEnum.BriefCard.value -> initBriefCardView(holder, item)
            ViewTypeEnum.FollowCard.value -> initFollowCardView(holder, item)
            ViewTypeEnum.VideoSmallCard.value -> initVideoSmallCardView(holder, item)
            ViewTypeEnum.DynamicInfoCard.value -> initDynamicInfoCardView(holder, item)
            ViewTypeEnum.AutoPlayFollowCard.value -> initAutoPlayFollowCardView(holder, item)
            ViewTypeEnum.SquareCardCollection.value -> initSquareCardCollectionView(holder, item)
        }
    }

    public override fun getDefItemCount(): Int {
        return super.getDefItemCount()
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="各种子模板初始化  Init item views">

    // <editor-fold defaultstate="collapsed" desc="结尾模板  Template End">
    /**
     * 没有更多数据，到底部的提示ItemView
     */
    private fun initTheEndView(holder: BaseViewHolder, item: Item) {
        holder.getView<TextView>(R.id.tvEnd).typeface = Typeface.createFromAsset(
            mContext!!.assets, "fonts/Lobster-1.4.otf"
        )
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="标题模板  Template Title">
    private fun initTextCardView(holder: BaseViewHolder, item: Item) {
        val jsonObject = item.data
        val textCard = Gson().fromJson(jsonObject, TextCard::class.java)
        val textType = textCard.type
        val header5View = holder.getView<TextView>(R.id.header5Text)
        val footer2View = holder.getView<TextView>(R.id.footer2Text)
        when (textType) {
            //左侧大Titlle
            "header5" -> {
                header5View.apply {
                    visibility = View.VISIBLE
                    text = textCard.text
                }
                footer2View.visibility = View.GONE
            }
            //右侧小Titlle
            "footer2" -> {
                footer2View.apply {
                    visibility = View.VISIBLE
                    text = textCard.text
                }
                header5View.visibility = View.GONE
            }
            else -> {
                Toast.makeText(mContext, "unknown type$textType", Toast.LENGTH_LONG).show()
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="摘要模板  Template Brief">
    private fun initBriefCardView(holder: BaseViewHolder, item: Item) {

        val jsonObject = item.data
        val briefCard = Gson().fromJson(jsonObject, BriefCard::class.java)

        val title = briefCard.title
        val iconUrl = briefCard.icon
        val iconType = briefCard.iconType
        val description = briefCard.description

        val tvTitle = holder.getView<TextView>(R.id.tvTitle)
        val tvDescription = holder.getView<TextView>(R.id.tvDescription)
        val ivFeed = holder.getView<ImageView>(R.id.ivFeed)

        tvTitle.text = title
        tvDescription.text = description

        if (iconType == "square") {
            GlideLoader.loadNetImageWithCorner(mContext!!, ivFeed, iconUrl)
        } else {
            GlideLoader.loadNetCircleImage(mContext!!, ivFeed, iconUrl)
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="左播放器+右Title 模板  Template video small">
    private fun initVideoSmallCardView(holder: BaseViewHolder, item: Item) {
        val jsonObject = item.data
        val videoSmallCard = Gson().fromJson(jsonObject, VideoSmallCard::class.java)

        val videoTitle =
            videoSmallCard.title                                                       //视频标题
        val videoPlayUrl =
            videoSmallCard.playUrl                                                   //视频播放地址
        val videoId =
            videoSmallCard.id.toString()                                                  //视频ID
        val videoFeedUrl =
            videoSmallCard.cover.detail                                              //视频封面Url
        val videoCategory =
            "#" + videoSmallCard.category                                           //视频类别
        val videoDuration = getFormatHMS(videoSmallCard.duration * 1000.toLong())       //视频时长

        // init View
        val tvTitle = holder.getView<TextView>(R.id.tvTitle)
        val tvCatogory = holder.getView<TextView>(R.id.tvCatogory)
        val tvVideoDuration = holder.getView<TextView>(R.id.tvVideoDuration)
        val ivFeed = holder.getView<ImageView>(R.id.ivFeed)

        tvTitle.text = videoTitle
        tvCatogory.text = videoCategory
        tvVideoDuration.text = videoDuration

        GlideLoader.loadNetImageWithCorner(mContext!!, ivFeed, videoFeedUrl)

        // init Event
        holder.itemView.setOnClickListener {
            startVideoActivity(
                videoId, videoTitle, videoFeedUrl,
                videoPlayUrl, videoSmallCard.cover.blurred
            )
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="评论模板  Template dynamic info">
    private fun initDynamicInfoCardView(holder: BaseViewHolder, item: Item) {
        val jsonObject = item.data
        val dynamicInfoCard = Gson().fromJson(jsonObject, DynamicInfoCard::class.java)

        val text = dynamicInfoCard.text                               //。。。
        val avatarUrl = dynamicInfoCard.user.avatar                   //评论者头像Url
        val authorName = dynamicInfoCard.user.nickname                //评论者昵称
        val replyMessage = dynamicInfoCard.reply.message              //评论内容
        val ifHotReply = dynamicInfoCard.reply.ifHotReply             //是否是热评
        val videoTitle = dynamicInfoCard.simpleVideo.title            //视频标题
        val videoPlayUrl = dynamicInfoCard.simpleVideo.playUrl        //视频播放地址
        val videoId = dynamicInfoCard.simpleVideo.id.toString()      //视频Id
        val videoType = "#${dynamicInfoCard.simpleVideo.category}"    //视频类型
        val likeCount = dynamicInfoCard.reply.likeCount.toString()    //评论点赞数
        val videoFeedUrl = dynamicInfoCard.simpleVideo.cover.detail   //视频封面图片
        //评论时间
        val timeStamp = timeStamp2Date(dynamicInfoCard.createDate, "yyyy/MM/dd")
        val videoDuration =
            getFormatHMS(dynamicInfoCard.simpleVideo.duration * 1000.toLong())       //视频时长

        //init view
        val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
        GlideLoader.loadNetCircleImage(mContext!!, ivAvatar, avatarUrl)
        holder.getView<TextView>(R.id.tvText).text = text
        holder.getView<TextView>(R.id.tvAuthor).text = authorName
        holder.getView<TextView>(R.id.tvReplyMessage).text = replyMessage

        val ivFeed = holder.getView<ImageView>(R.id.ivFeed)
        GlideLoader.loadNetCircleImage(mContext!!, ivFeed, videoFeedUrl)

        holder.getView<TextView>(R.id.tvDate).text = timeStamp
        holder.getView<TextView>(R.id.tvVieoType).text = videoType
        holder.getView<TextView>(R.id.tvLikeCount).text = likeCount
        holder.getView<TextView>(R.id.tvVideoName).text = videoTitle
        holder.getView<TextView>(R.id.tvVideoDuration).text = videoDuration

        holder.getView<TextView>(R.id.labelHotComment).visibility =
            if (ifHotReply) View.VISIBLE else View.GONE
        holder.getView<ImageView>(R.id.ivGoHotComment).visibility =
            if (ifHotReply) View.VISIBLE else View.GONE

        //init Event
        holder.getView<View>(R.id.bg).setOnClickListener {
            startVideoActivity(
                videoId,
                videoTitle,
                videoFeedUrl,
                videoPlayUrl,
                dynamicInfoCard.simpleVideo.cover.blurred
            )
        }
    }
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="AutoPlayFollow">
    private fun initAutoPlayFollowCardView(holder: BaseViewHolder, item: Item) {

        val jsonObject = item.data
        val autoPlayFollowCard = Gson().fromJson(jsonObject, AutoPlayFollowCard::class.java)
        val iconUrl = autoPlayFollowCard.header.icon                                        //头像Url
        val tags = autoPlayFollowCard.content.data.tags                                  //标签列表
        val title = autoPlayFollowCard.content.data.title                                   //标题
        val issueName = autoPlayFollowCard.header.issuerName                                //头像代表名称
        val playUrl = autoPlayFollowCard.content.data.playUrl                               //视频播放地址
        val videoId = autoPlayFollowCard.content.data.id.toString()                         //视频ID
        val videoDuration =
            autoPlayFollowCard.content.data.duration                           //视频时长
        val description = autoPlayFollowCard.content.data.description                       //内容
        val videoCoverUrl =
            autoPlayFollowCard.content.data.cover.detail                    //视频封面Url
        val replyCount = autoPlayFollowCard.content.data.consumption.replyCount                //评论数
        val collectionCount = autoPlayFollowCard.content.data.consumption.collectionCount

        //init view
        holder.getView<TextView>(R.id.tvTitle).text = title
        holder.getView<TextView>(R.id.tvIssueName).text = issueName
        holder.getView<TextView>(R.id.tvDescription).text = description
        holder.getView<TextView>(R.id.tvReply).text = replyCount.toString()
        holder.getView<TextView>(R.id.tvCollectionCount).text = collectionCount.toString()
        holder.getView<TextView>(R.id.tvDuration).text = getFormatHMS(videoDuration * 1000.toLong())

        val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
        val ivVideoCover = holder.getView<ImageView>(R.id.ivVideoCover)
        GlideLoader.loadNetCircleImage(mContext!!, ivAvatar, iconUrl)
        GlideLoader.loadNetCircleImage(mContext!!, ivVideoCover, videoCoverUrl)

        val flexLayout = holder.getView<FlexboxLayout>(R.id.flexLayout)
        flexLayout.removeAllViews()
        val layoutParams = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        tags?.forEach { itemTag ->
            val itemView = LayoutInflater.from(mContext).inflate(
                R.layout.textview_tag,
                flexLayout, false
            )
            itemView.findViewById<TextView>(R.id.tvTag).text = itemTag.name
            flexLayout.addView(itemView, layoutParams)
        }
        // init Event
       ivVideoCover.setOnClickListener {
           startVideoActivity(
               videoId,
               title,
               videoCoverUrl,
               playUrl,
               autoPlayFollowCard.content.data.cover.blurred
           )
       }
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="轮播图模板  Template Banner">
    private fun initSquareCardCollectionView(holder: BaseViewHolder, item: Item) {
        //init data
        val jsonObject = item.data
        val squareCardCollection = Gson().fromJson(jsonObject,
            SquareCardCollection::class.java)
        var headerList = ArrayList<ItemTypeBanner>()
        val id = squareCardCollection.header.id
        val title = squareCardCollection.header.title
        ZLog.d("轮播图模板+1  Title=$title")
        if(bannerDataListCache == null){
            bannerDataListCache = HashMap()
        }
        val headerListData = bannerDataListCache?.get(id)
        if(headerListData == null){
            ZLog.d("轮播图模板 No cached list.")
            squareCardCollection.itemList.forEach {
                val banner = Gson().fromJson(it.data, ItemTypeBanner::class.java)
//                ZLog.d("轮播图模板 add banner2:$banner")
                headerList.add(banner)
            }
            bannerDataListCache?.put(id, headerList)
        }else{
            ZLog.d("轮播图模板 Find cached list.")
            return
        }

        holder.getView<TextView>(R.id.tvTitle).text = title
        val bannerLayout = holder.getView<Banner<ItemTypeBanner,
                TemplateBannerAdapter<ItemTypeBanner>>>(R.id.banner)
        if (bannerLayout.adapter == null) {
            ZLog.d("轮播图模板 banner初始化.")
            bannerLayout.apply {
                adapter = TemplateBannerAdapter(null)
                setPageTransformer(ScaleInTransformer())
                indicator = CircleIndicator(context)
                // init Event
                setOnBannerListener { data, _ ->
                    // todo
                }
                bannerLayout.setDatas(headerList)
            }
        }

    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="上播放器+下Title模板  Template FollowCard">
    private fun initFollowCardView(holder: BaseViewHolder, item: Item) {
        val jsonObject = item.data
        val followCard = Gson().fromJson(jsonObject, FollowCard::class.java)
//        val videoJson = Gson().toJson(jsonObject)

        val avatarUrl =
            followCard.header.icon                                                           //发布者头像
        val title =
            followCard.content.data.title                                                        //标题
        val playUrl =
            followCard.content.data.playUrl                                                    //视频播放地址
        val feedUrl =
            followCard.content.data.cover.detail                                               //发布内容对应封面
        val videoId =
            followCard.content.data.id.toString()                                              //视频Id
        val description =
            "${followCard.header.title}  /  #${followCard.content.data.category}"          //描述
        val duration = getFormatHMS(followCard.content.data.duration * 1000.toLong())        //视频时长

        holder.getView<TextView>(R.id.tvTitle).text = title
        holder.getView<TextView>(R.id.tvSlogan).text = description
        holder.getView<TextView>(R.id.tvVideoDuration).text = duration


        val options = RequestOptions().placeholder(R.mipmap.avatar_default)
            .transform(RoundedCorners(20))
        val bkgImage = holder.getView<ImageView>(R.id.ivBg)
        val avatarImage = holder.getView<ImageView>(R.id.ivAvatar)

        if (!TextUtils.isEmpty(feedUrl)) {
            Glide.with(bkgImage)
                .load(feedUrl)
                .apply(options)
                .into(bkgImage)
        }
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(avatarImage)
                .load(avatarUrl)
                .apply(options)
                .into(avatarImage)
        }

        bkgImage.setOnClickListener {
            startVideoActivity(videoId,title,feedUrl,playUrl,followCard.content.data.cover.blurred)
        }

    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="临时工具函数">
    //TODO 搞到工具类中去
    open fun getFormatHMS(t: Long): String? {
        val h = (t / 3600000).toInt()
        val m = (t % 3600000 / 60000).toInt()
        val s = (t % 60000 / 1000).toInt()
        return if (h > 0) String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            h,
            m,
            s
        ) else String.format(Locale.getDefault(), "%02d:%02d", m, s)
    }

    open fun timeStamp2Date(timeStamp: Long, dateFormat: String?): String? {
        val sdf =
            SimpleDateFormat(dateFormat, Locale.getDefault())
        val date = Date(timeStamp)
        return sdf.format(date)
    }
    // </editor-fold>

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        bannerDataListCache?.clear()
        bannerDataListCache = null
    }

    //启动视频播放页面
    private fun startVideoActivity(videoId: String, videoTitle: String,
                                   videoFeedUrl: String, videoPlayUrl: String,
                                   videoBgUrl: String) {
        mContext?.startActivity<VideoDetailPlayActivity> {
            it.apply {
                putExtra(MediaParams.PARAMS_KEY, MediaParams().apply {
                    this.videoId = videoId
                    this.videoUrl = videoPlayUrl
                    this.videoBkg = videoBgUrl
                    this.videoName = videoTitle
                    this.videoType = "vod"
                })
//                putExtra("VIDEO_FEED_URL", videoFeedUrl)
            }
        }
    }
}


