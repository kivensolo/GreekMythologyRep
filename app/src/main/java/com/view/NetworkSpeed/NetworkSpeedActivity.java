package com.view.NetworkSpeed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;
import com.utils.ScreenTools;
import com.utils.ToastTools;
import com.utils.NetTools;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/3/8 16:46
 * description: 网络测速页面
 */
public class NetworkSpeedActivity extends Activity{
    private static final String TAG = NetworkSpeedActivity.class.getSimpleName();

	private LinearLayout speedLayout;   //测速布局
    private TextView tvProgress;        //进度条
	private TextView tvInfo;
	private SeekBar mSeekBar;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"NetworkSpeedActivity onCreate");
        setContentView(R.layout.activity_network);
        mContext = this;
        initView();
        sendMsg2Ui();
    }

    private void initView() {
        speedLayout = new SpeedLayout(mContext);
        ((LinearLayout.LayoutParams)speedLayout.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
        mSeekBar = (SeekBar) speedLayout.findViewById(R.id.service_seekbar);
		View mSeekBarLayout = speedLayout.findViewById(R.id.service_seekbar_bg);
        mSeekBarLayout.getLayoutParams().width = ScreenTools.OperationWidth(799);
		mSeekBarLayout.getLayoutParams().height = ScreenTools.OperationHeight(36);
		mSeekBarLayout.setBackground(getResources().getDrawable(R.drawable.service_layout_seekbar_bg));
		ViewGroup.LayoutParams seek_lp = mSeekBar.getLayoutParams();
		seek_lp.width = ScreenTools.OperationWidth(757);
		seek_lp.height = ScreenTools.OperationHeight(36);
		tvProgress = (TextView) findViewById(R.id.service_tv_progress);
		tvProgress.setTextSize(TypedValue.COMPLEX_UNIT_PX,22);
		tvInfo = (TextView) findViewById(R.id.service_tv_info);
		tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX,22);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.requestFocus();
    }

    private void sendMsg2Ui() {
        if (!NetTools.isNetworkConnected(this)) {
            ToastTools.getInstance().showMgtvWaringToast(NetworkSpeedActivity.this,"网络连接超时，请检查网络后再试");
            Log.e(TAG,"网络连接超时，请检查网络后再试。");
		}
    }
}
