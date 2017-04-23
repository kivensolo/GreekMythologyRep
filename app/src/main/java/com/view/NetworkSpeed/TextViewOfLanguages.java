package com.view.NetworkSpeed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.kingz.widgets.text.LogTextBox;
import com.utils.NetTools;
import com.utils.ToastTools;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/3/8 16:46
 * description: TextView显示文字
 */
public class TextViewOfLanguages extends Activity {
    private static final String TAG = TextViewOfLanguages.class.getSimpleName();

    private TextView mTextView_Ch;
    private TextView mTextView_En;
    private TextView mTextView_Uyg;
    private TextView mTextView_ChEn;
    private TextView mTextView_ChEn2;
    private TextView mTextView_EnUyg;
    private TextView mTextView_ChUyg;
    private LogTextBox log_textbox;

    private Context mContext;
    private String[] contens = {"小天狼星为詹姆在阿兹卡班住了十二年，贝拉特里克斯也为黑魔王在阿兹卡班住了十四年。",
            "Chinese President Xi Jinping inspected the Southern Theater Command of the People's Liberation Army (PLA) on Friday and stressed building a strong arm",
            "بۇ ھەقلىق قانال، ok كۇنۇپكىسىنى بېسىپ ئاچقۇزۇپ كۆرۈڭ",
            "今天的天气非常的nice啊",
            "激活码： KH2J9-PC326-T44D4-39H6V-TVPBY-8XXCC-MF4YK",
            "今天的天气非常的ش كونۇپ啊",
            "Chinese President Xi Jinping پ چىقىڭ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mContext = this;
        initView();
        sendMsg2Ui();
    }

    private void initView() {
        mTextView_Ch = (TextView) findViewById(R.id.textView_zh);
        mTextView_En = (TextView) findViewById(R.id.textView_en);
        mTextView_Uyg = (TextView) findViewById(R.id.textView_uyg);
        mTextView_ChEn = (TextView) findViewById(R.id.textView_ch_en);
        mTextView_ChEn2 = (TextView) findViewById(R.id.textView_ch_en_2);
        mTextView_ChUyg = (TextView) findViewById(R.id.textView_ch_uyg);
        mTextView_EnUyg = (TextView) findViewById(R.id.textView_en_uyg);
        log_textbox = (LogTextBox) findViewById(R.id.log_textbox);
        log_textbox.setBorder(true);
        mTextView_Ch.setText(contens[0]);
        mTextView_En.setText(contens[1]);
        mTextView_Uyg.setText(contens[2]);
        mTextView_ChEn.setText(contens[3]);
        mTextView_ChEn2.setText(contens[4]);
        mTextView_ChUyg.setText(contens[5]);
        mTextView_EnUyg.setText(contens[6]);

        log_textbox.append("汉字内容首个字符宽度:" + getSingleCharWidth(contens[0].toCharArray()[0]) + "\n");
        log_textbox.append("英文内容首个字母字符宽度:" + getSingleCharWidth(contens[1].toCharArray()[0]) + "\n");
        log_textbox.append("维语内容首个字符宽度:" + getSingleCharWidth(contens[2].toCharArray()[0]) + "\n");
        log_textbox.append("维语字符宽度依次为:" + getSingleCharWidthLoop(contens[2]) + "\n");
    }

    private void sendMsg2Ui() {
        if (!NetTools.isNetworkConnected(this)) {
            ToastTools.getInstance().showMgtvWaringToast(TextViewOfLanguages.this, "网络连接超时，请检查网络后再试");
            Log.e(TAG, "网络连接超时，请检查网络后再试。");
        }
    }

    public float getSingleCharWidth(char textChar) {
        float[] width = new float[1];
        new Paint().getTextWidths(new char[]{textChar}, 0, 1, width);
        return width[0];
    }

    public String getSingleCharWidthLoop(String content) {
        float[] width = new float[content.length()];
        StringBuffer sbf = new StringBuffer();
        char[] chars = content.toCharArray();
        Paint paint = new Paint();
        for(int i = 0;i < content.length();i++){
            paint.getTextWidths(new char[]{chars[i]}, 0, 1, width);
            sbf.append(width[0]).append(" ");
        }
        return sbf.toString();
    }

}
