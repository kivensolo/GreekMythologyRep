package com.zeke.ktx.modules.home

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kingz.customdemo.R
import com.zeke.ktx.modules.home.fragments.ISwitcher
import com.zeke.ktx.modules.home.fragments.ISwitcher.ButtomType
import java.util.*

/**
 * description：首页-底部控制
 *
 */
class MainBottomController(private val rootView: View) : View.OnClickListener {
    private var listener: ISwitcher? = null
    private var vodLayout: View? = null
    private var vodImg: ImageView? = null
    private var vodTxt: TextView? = null
    private var liveLayout: View? = null
    private var liveImg: ImageView? = null
    private var liveTxt: TextView? = null
    private var vipLayout: View? = null
    private var vipImg: ImageView? = null
    private var vipTxt: TextView? = null
    private var mineLayout: View? = null
    private var mineImg: ImageView? = null
    private var mineTxt: TextView? = null

    private var currentPosition: Int = 0
    private val imageList = ArrayList<String>()

    fun setListener(listener: ISwitcher) {
        this.listener = listener
    }

    init {
        // 同java构造函数
        initView()
    }

    private fun initView() {
        vodLayout = rootView.findViewById(R.id.bottom_vod_layout)
        vodLayout!!.setOnClickListener(this)
        vodImg = rootView.findViewById(R.id.vod_img)
        vodTxt = rootView.findViewById(R.id.vod_text)
        liveLayout = rootView.findViewById(R.id.bottom_live_layout)
        liveLayout!!.setOnClickListener(this)
        liveImg = rootView.findViewById(R.id.live_img)
        liveTxt = rootView.findViewById(R.id.live_text)
        vipLayout = rootView.findViewById(R.id.bottom_vip_layout)
        vipLayout!!.setOnClickListener(this)
        vipImg = rootView.findViewById(R.id.vip_img)
        vipTxt = rootView.findViewById(R.id.vip_text)
        mineLayout = rootView.findViewById(R.id.bottom_mine_layout)
        mineLayout!!.setOnClickListener(this)
        mineImg = rootView.findViewById(R.id.mine_img)
        mineTxt = rootView.findViewById(R.id.mine_text)
        switchCheckState(ISwitcher.TYPE_VOD)
    }

    /**
     * 更换每个按钮的图片
     */
    private fun switchCheckState(@ButtomType position: Int) {
        vodTxt!!.isEnabled = position == ISwitcher.TYPE_VOD
        liveTxt!!.isEnabled = position == ISwitcher.TYPE_LIVE
        vipTxt!!.isEnabled = position == ISwitcher.TYPE_VIP
        mineTxt!!.isEnabled = position == ISwitcher.TYPE_MINE
        //后台下发的图 第一张表示选中的 第二张表示未选中的
        if (imageList.size == 8) {
            if (position == 0) {
                vodImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[0]))
                liveImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[3]))
                vipImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[5]))
                mineImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[7]))
            } else if (position == 1) {
                vodImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[1]))
                liveImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[2]))
                vipImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[5]))
                mineImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[7]))
            } else if (position == 2) {
                vodImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[1]))
                liveImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[3]))
                vipImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[4]))
                mineImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[7]))
            } else if (position == 3) {
                vodImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[1]))
                liveImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[3]))
                vipImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[5]))
                mineImg!!.setImageBitmap(BitmapFactory.decodeFile(imageList[6]))
            }
        } else {
            //图片没缓存成功的 使用默认图
            if (position == ISwitcher.TYPE_VOD) {
                siwtchButtomUi(R.drawable.ic_vod_sel, R.drawable.ic_live_nor,
                        R.drawable.ic_vip_nor, R.drawable.ic_mine_nor)
            } else if (position == ISwitcher.TYPE_LIVE) {
                siwtchButtomUi(R.drawable.ic_vod_nor, R.drawable.ic_live_sel,
                        R.drawable.ic_vip_nor, R.drawable.ic_mine_nor)
            } else if (position == ISwitcher.TYPE_VIP) {
                siwtchButtomUi(R.drawable.ic_vod_nor, R.drawable.ic_live_nor,
                        R.drawable.ic_vip_sel, R.drawable.ic_mine_nor)
            } else if (position == ISwitcher.TYPE_MINE) {
                siwtchButtomUi(R.drawable.ic_vod_nor, R.drawable.ic_live_nor,
                        R.drawable.ic_vip_nor, R.drawable.ic_mine_sel)
            }
        }
    }

    private fun siwtchButtomUi(vod: Int, live: Int, vip: Int, mine: Int) {
        vodImg!!.setImageResource(vod)
        liveImg!!.setImageResource(live)
        vipImg!!.setImageResource(vip)
        mineImg!!.setImageResource(mine)
    }

    override fun onClick(v: View) {
        // 同java的switch
        when (v.id) {
            R.id.bottom_vod_layout -> {
                switchCheckState(ISwitcher.TYPE_VOD)
                if (currentPosition != ISwitcher.TYPE_VOD && listener != null) {
                    listener!!.switchFragment(ISwitcher.TYPE_VOD)
                }
                currentPosition = ISwitcher.TYPE_VOD
            }
            R.id.bottom_live_layout -> {
                switchCheckState(ISwitcher.TYPE_LIVE)
                if (currentPosition != ISwitcher.TYPE_LIVE && listener != null) {
                    listener!!.switchFragment(ISwitcher.TYPE_LIVE)
                }
                currentPosition = ISwitcher.TYPE_LIVE
            }
            R.id.bottom_vip_layout -> {
                switchCheckState(ISwitcher.TYPE_VIP)
                if (currentPosition != ISwitcher.TYPE_VIP && listener != null) {
                    listener!!.switchFragment(ISwitcher.TYPE_VIP)
                }
                currentPosition = ISwitcher.TYPE_VIP
            }
            R.id.bottom_mine_layout -> {
                switchCheckState(ISwitcher.TYPE_MINE)
                if (currentPosition != ISwitcher.TYPE_MINE && listener != null) {
                    listener!!.switchFragment(ISwitcher.TYPE_MINE)
                }
                currentPosition = ISwitcher.TYPE_MINE
            }
        }
    }
}
