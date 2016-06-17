package com.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/1
 * Discription:  公共工具类
 */
public class ToastTools {
     public static void showMgtvWaringToast(Context context,String info) {
        Toast mToast = new Toast(context);
//        View view = View.inflate(this,R.layout.custom_toast_layout,null);
        //设置View
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_toast_layout,null);
        root.setBackgroundResource(R.drawable.custom_toast_bg);

        ImageView mIcon=(ImageView) root.findViewById(R.id.toast_img);
		LinearLayout.LayoutParams mIconParams=(LinearLayout.LayoutParams) mIcon.getLayoutParams();
		mIconParams.width = 32;
		mIconParams.height = 32;
//		mIconParams.bottomMargin = 5;
//		mIconParams.topMargin = 15;
		mIconParams.leftMargin = 10;
        mIcon.setLayoutParams(mIconParams);

        //设置文字
        TextView txtContent=(TextView) root.findViewById(R.id.toast_info);
		txtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,24);
		txtContent.setMaxEms(15);
		txtContent.setShadowLayer(3, 1, 1, Color.BLACK);
		txtContent.setText(info);
		txtContent.setSingleLine();
        txtContent.setPadding(10,5,5,10);
        LinearLayout.LayoutParams mtxtParams=(LinearLayout.LayoutParams) txtContent.getLayoutParams();
		mtxtParams.rightMargin = 15;

        //设置需要显示的View
        mToast.setView(root);
        mToast.setGravity(Gravity.CENTER_VERTICAL,0,50);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

}
