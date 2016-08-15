package com.view.NetworkSpeed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kingz.customdemo.R;
import com.kingz.interfaces.ServiceSelectBtnCallBack;
import com.view.HighLightButton;

/**
 *
 * @author minglu.jiang
 *
 * 2013-6-21
 *
 * 网络优化
 */
public class SpeedLayout extends LinearLayout implements ServiceSelectBtnCallBack
{

	public SpeedLayout(Context context)
	{
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public SpeedLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public void init(Context context)
	{
		inflate(context, R.layout.speed_item, this);
		this.setOrientation(LinearLayout.VERTICAL);

		SeekBar seekBar = (SeekBar) findViewById(R.id.service_seekbar);
		seekBar.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
	}

	@Override
	public void setNextLeftButton(HighLightButton button)
	{
		// TODO Auto-generated method stub

	}

}
