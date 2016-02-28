package com.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.Toast;

public class UITools {
	private final static String TAG = "UITools";

	public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;
	public static final int DESIGN_WIDTH = 1280;
	public static final int DESIGN_HEIGHT = 720;
	public static float mainScale = 1.0f;

	public static void init(int screenWidth, int srceenHeight )
	{
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = srceenHeight;
		mainScale = Math.min( SCREEN_HEIGHT/DESIGN_HEIGHT, SCREEN_WIDTH/DESIGN_WIDTH );
	}
	public static int XH( int h )
	{
		return (int) (SCREEN_HEIGHT * (h * 1.0f / DESIGN_HEIGHT) + 0.5f);
	}
	public static int XW( int w )
	{
		return XH(w);
	}
	/**
	 * 获取容器控件中的第一个可以得到焦点的view
	 * @param parent 容器控件
	 * @return 第一个view,没有则返回空
	 */
	public static final View getFirstFocusableView(ViewParent parent) {
		try {
			ViewGroup group=(ViewGroup) parent;
			int count=group.getChildCount();
			for(int i=0;i<count;i++){
				if(group.getChildAt(i).getVisibility()==View.VISIBLE &&
						group.getChildAt(i).isEnabled() &&
						group.getChildAt(i).isFocusable()){
					return group.getChildAt(i);
				}
			}
		} catch (Exception e) {
			Log.e(TAG,"getFirstFocusableView error:"+e.getLocalizedMessage());
		}
		return null;
	}
	/**
	 * 最近一次浮出框的时间
	 */
	private static long lastTosatTime = 0;

	/**
	 *
	 * @Description: 设置一个View的宽高
	 * @param width width
	 * @param height height
	 */
	public static void setViewSize(View view,int width,int height)
	{
		if(view==null) {
			Log.e( TAG, "setViewSize view==null");

			return;
		}
        if (view.getLayoutParams() == null) {
            Log.e( TAG, "setViewSize view.getLayoutParams()==null");
            return;
        }
        view.getLayoutParams().width=ScreenSize.Operation(width);
		view.getLayoutParams().height=ScreenSize.Operation(height);
	}

	/**
	 *
	 */
	public static void setViewMargin(View view,int leftMargin,int topMargin,int rightMargin,int bottomMargin)
	{
        if (view == null) {
            Log.e( TAG, "setViewMargin view==null");
            return;
        }
        if (view.getLayoutParams() == null) {
            Log.e( TAG, "setViewMargin view.getLayoutParams()==null");
            return;
        }
		MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
		params.leftMargin = leftMargin;
		params.topMargin = topMargin;
		params.rightMargin = rightMargin;
		params.bottomMargin = bottomMargin;
	}



	public final static void showToast(Context context, String str)
	{
		if (System.currentTimeMillis() - lastTosatTime > 1000 || System.currentTimeMillis() < lastTosatTime )
		{
			Toast.makeText(context, str, Toast.LENGTH_SHORT);
			lastTosatTime = System.currentTimeMillis();
		}
	}
	private static Toast toast;
//	public final static void ShowCustomToast(Context context,String str){
//		LinearLayout root=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
//		root.setBackgroundResource(R.drawable.custom_toast_bgv4);
//		ImageView mIcon=(ImageView) root.findViewById(R.id.iv_warning);
//		LayoutParams mIconParams=(LayoutParams) mIcon.getLayoutParams();
//		mIconParams.width = App.Operation(32);
//		mIconParams.height = App.Operation(32);
//		mIconParams.bottomMargin =App.Operation(5);
//		mIconParams.topMargin =App.Operation(5);
//		mIconParams.leftMargin =App.Operation(15);
//
//		mIcon.setLayoutParams(mIconParams);
//
//		TextView txtContent=(TextView) root.findViewById(android.R.id.message);
//		txtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,App.Operation(24));
//		txtContent.setMaxEms(15);
//		txtContent.setShadowLayer(3, 1, 1, Color.BLACK);
//		txtContent.setText(str);
//		txtContent.setSingleLine();
//		LayoutParams mtxtParams=(LayoutParams) txtContent.getLayoutParams();
//		mtxtParams.rightMargin =App.Operation(15);
//
//		if(toast!=null){
//			toast.setText(str);
//		}else{
//			toast = new Toast(context.getApplicationContext());
//		}
//        //设置Toast的位置
//        toast.setGravity(Gravity.CENTER_VERTICAL, App.Operation(0), App.Operation(50));
//        toast.setDuration(Toast.LENGTH_SHORT);
//        //让Toast显示为我们自定义的样子
//        toast.setView(root);
//        toast.show();
//	}

//	public final static void ShowLongToast(Context context,String str){
//		LinearLayout root=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
//		root.setPadding(App.Operation(30), App.Operation(10), App.Operation(30),App.Operation(10));
//		root.setBackgroundColor(0xCC000000);
//		WindowManager.LayoutParams rootParams=new WindowManager.LayoutParams();
//		rootParams.width=App.Operation(552);
//		rootParams.height=App.Operation(50);
//		root.setLayoutParams(rootParams);
//		ImageView mIcon=(ImageView) root.findViewById(R.id.iv_warning);
//		LayoutParams mIconParams=(LayoutParams) mIcon.getLayoutParams();
//		mIconParams.width = App.Operation(32);
//		mIconParams.height = App.Operation(32);
//		mIconParams.bottomMargin =App.Operation(5);
//		mIconParams.topMargin =App.Operation(5);
//		mIconParams.leftMargin =App.Operation(15);
//		mIcon.setLayoutParams(mIconParams);
//
//		TextView txtContent=(TextView) root.findViewById(android.R.id.message);
//		txtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,App.Operation(28));
//
//		//params.width=App.Operation(360);
//		txtContent.setMaxEms(15);
//		txtContent.setGravity(Gravity.CENTER);
//		txtContent.setShadowLayer(3, 1, 1, Color.BLACK);
//		txtContent.setText(str);
//		txtContent.setSingleLine();
//		LayoutParams mtxtParams=(LayoutParams) txtContent.getLayoutParams();
//		mtxtParams.rightMargin =App.Operation(15);
//		if(toast!=null){
//			toast.setText(str);
//		}else{
//			toast = new Toast(context.getApplicationContext());
//		}
//        //设置Toast的位置
//        toast.setGravity(Gravity.CENTER_VERTICAL, 10, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        //让Toast显示为我们自定义的样子
//        toast.setView(root);
//        toast.show();
//	}


}
