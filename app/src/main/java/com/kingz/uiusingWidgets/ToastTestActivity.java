package com.kingz.uiusingWidgets;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/31
 * Discription: Toast的集合
 */
public class ToastTestActivity extends Activity implements View.OnClickListener{
    private Button btn_img;
    private Button btn_mgtv;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widgets_toastlayout);
        initViews();
    }

    private void initViews() {
        mToast = new Toast(this);
        btn_img = (Button) findViewById(R.id.showImgToast);
        btn_mgtv = (Button) findViewById(R.id.showMgtvToast);
        btn_img.setOnClickListener(this);
        btn_mgtv.setOnClickListener(this);
    }

    public void showImgToast(){
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.thumb);
        mToast.setView(img);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showMgtvWaringToast(String info) {
//        View view = View.inflate(this,R.layout.custom_toast_layout,null);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_toast_layout,null);
        root.setBackgroundResource(R.drawable.custom_toast_bg);
        ImageView mIcon=(ImageView) root.findViewById(R.id.toast_img);
        //设置图片
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

//        if(mToast!=null){
//			mToast.setText(info);//需要由Toast.makeText()产生的Toast才能setText
//		}else{
//			mToast = new Toast(this.getApplicationContext());
//		}

        mToast.setView(root);
        mToast.setGravity(Gravity.CENTER_VERTICAL,0,50);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showMgtvToast:
                showMgtvWaringToast("芒果Tv 4.0样式");
                break;
            case R.id.showImgToast:
                showImgToast();
                break;
        }
    }

}
