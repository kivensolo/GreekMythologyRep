package com.view.pages;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.view.views.WaveLoadingView;
import com.kingz.uiusingListViews.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/5/29 14:46
 * description:
 */

public class WaveLoadingActivity extends Activity {
    private WaveLoadingView mWaveLoadingView;
    private SeekBar mSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waveloading_layout);
        mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoading);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mWaveLoadingView.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}

