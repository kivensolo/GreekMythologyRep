package com.zeke.play.activities

import android.os.Bundle
import com.kingz.module.common.base.BaseActivity
import com.kingz.module.common.bean.MediaParams
import com.zeke.module_player.R
import kotlinx.android.synthetic.main.activity_zplayer.*

/**
 * author：ZekeWang
 * date：2021/6/5
 * description：ZplayerView的测试
 */
class ZPlayerViewTestPage: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zplayer)

        with(player_view){
            val url = "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=257456&resourceType=video&editionType=high&source=ucloud&playUrlType=url_oss&udid=435865baacfc49499632ea13c5a78f944c2f28aa"
            setDataSource(MediaParams().apply {
                videoUrl = url
            })
            open()
        }

        play_player.setOnClickListener{
            player_view.play()
        }
        pause_player.setOnClickListener {
            player_view.stop()
        }
    }
}