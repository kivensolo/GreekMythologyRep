package com.zeke.play.activities

import android.os.Bundle
import android.widget.Button
import com.kingz.module.common.base.BaseActivity
import com.kingz.module.common.bean.MediaParams
import com.zeke.module_player.R
import com.zeke.play.wigets.SimpleZPlayerView

/**
 * author：ZekeWang
 * date：2021/6/5
 * description：ZplayerView的测试
 */
class ZPlayerViewTestPage: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zplayer)

        var playerView = findViewById<SimpleZPlayerView>(R.id.player_view)
        var play_player = findViewById<Button>(R.id.play_player)
        var pause_player = findViewById<Button>(R.id.pause_player)
        with(playerView){
            val url = "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=257456&resourceType=video&editionType=high&source=ucloud&playUrlType=url_oss&udid=435865baacfc49499632ea13c5a78f944c2f28aa"
            setDataSource(MediaParams().apply {
                videoUrl = url
            })
            open()
        }

        play_player.setOnClickListener{
            playerView.play()
        }
        pause_player.setOnClickListener {
            playerView.stop()
        }
    }
}