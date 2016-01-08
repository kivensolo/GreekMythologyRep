package com.kingz.uiusingActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.kingz.uiusingListViews.R;

import java.security.PrivateKey;

/**
 * Created by KingZ on 2015/11/3.
 * Discription:
 */
public class BitmapActivity extends Activity implements View.OnClickListener{

    private Button btn_NetPic, btn_LocalPic;
    private ImageView imgNetPic, imgResPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitmap_demo_layout);

        initviews();
    }

    private void initviews() {
        btn_NetPic = (Button) findViewById(R.id.loadpic_btn);
        btn_LocalPic = (Button) findViewById(R.id.localpic_btn);
        imgNetPic = (ImageView) findViewById(R.id.image_net);
        imgResPic = (ImageView) findViewById(R.id.image_local);
    }

    @Override
    public void onClick(View v) {

    }
}
