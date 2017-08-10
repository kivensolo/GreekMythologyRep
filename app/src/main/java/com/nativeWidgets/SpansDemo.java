package com.nativeWidgets;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/8 10:12
 * description:
 *         SpannableStringBuilder:
 *       设置Span
 *       SpannableStringBuilder.setSpan(Object what, int start, int end, int flags)
 *       这里的Flag表示：start和end是开区间还是闭区间。
         Flag:
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE —— (a,b)
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE —— (a,b]
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE —— [a,b)
            Spanned.SPAN_INCLUSIVE_INCLUSIVE —— [a,b]
 */
public class SpansDemo extends Activity{

    public static final String GO_TO_BAIDU = "Take you fly,Take you zhuangB";
    public static final String KEY_WORD = "Take";
    public static final int start = GO_TO_BAIDU.indexOf(KEY_WORD);

    private SpannableStringBuilder ssb;
    private TextView spanTitle_A;
    private TextView urlSpanText;
    private TextView spanTitle_B;
    private TextView spanTitle_C;
    private TextView spanTitle_D;
    private TextView spanTitle_E;
    private TextView underLineSpanText;
    private TextView typeFaceSpanText1;
    private TextView typeFaceSpanText2;
    private TextView typeFaceSpanText3;
    private TextView apperanceSpanText;

//    private TextView spanTitle_A;
//    private TextView spanTitle_A;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spans_page);
        initViews();
    }

    private void initViews() {
        ssb = new SpannableStringBuilder(GO_TO_BAIDU);
        //标题
        spanTitle_A = (TextView) findViewById(R.id.span_title_1);
        spanTitle_B = (TextView) findViewById(R.id.underLineSpan_title_2);
        spanTitle_C = (TextView) findViewById(R.id.underLineSpan_title_3);
        spanTitle_D = (TextView) findViewById(R.id.appearanceSpan_text_id);

        //文字
        urlSpanText = (TextView) findViewById(R.id.urlSpan_text_id);
        underLineSpanText = (TextView) findViewById(R.id.underLineSpan_text_id);
        typeFaceSpanText1 = (TextView) findViewById(R.id.typeFaceSpan1_text_id);
        typeFaceSpanText2 = (TextView) findViewById(R.id.typeFaceSpan2_text_id);
        typeFaceSpanText3 = (TextView) findViewById(R.id.typeFaceSpan3_text_id);
        apperanceSpanText = (TextView) findViewById(R.id.appearanceSpan_text_id);
        urlSpanClick();
        underlinSpanShow();
        TypefaceSpan();
        setTextAppearanceSpan();
    }

    private void setTextAppearanceSpan() {
        spanTitle_D.setText("TextAppearanceSpan");
        //设置文字字体、文字样式（粗体、斜体、等等）、文字颜色状态、文字下划线颜色状态等等。
        //TextAppearanceSpan的三个构造方法
        //TextAppearanceSpan(Context context, int appearance)
        //TextAppearanceSpan(Context context, int appearance, int colorList)
        //TextAppearanceSpan(String family, int style, int size,ColorStateList color, ColorStateList linkColor)
            //family:monospace/serif/sans-serif
            //style: Typeface.NORMAL  Typeface.BOLD  Typeface.ITALIC  Typeface.BOLD_ITALIC
            //size字体大小（单位px）
        SpannableStringBuilder ssb = new SpannableStringBuilder(GO_TO_BAIDU);
        ColorStateList colorStateList = null;
        Resources resource = getBaseContext().getResources();
        //系统版本大于4.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            //得到color的xml
            colorStateList = resource.getColorStateList(R.color.span_text);
        }
//        try {
//            colorStateList = ColorStateList.createFromXml(getResources(), Resources.getSystem().getXml(R.color.span_text));
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ssb.setSpan(new TextAppearanceSpan("serif", Typeface.BOLD_ITALIC, 40, colorStateList, colorStateList),
                    start, start + KEY_WORD.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        apperanceSpanText.setText(ssb);
        apperanceSpanText.setEnabled(true);
//        apperanceSpanText.setTextColor(colorStateList);
    }

    private void TypefaceSpan() {
        spanTitle_C.setText("TypefaceSpan: monospace + serif + sans-serif");
        ssb = new SpannableStringBuilder("fontFaceType can Fly-----monospace");
        ssb.setSpan(new TypefaceSpan("monospace"), start, start + "fontFaceType".length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        typeFaceSpanText1.setText(ssb);

        ssb = new SpannableStringBuilder("fontFaceType can Fly-----serif");
        ssb.setSpan(new TypefaceSpan("serif"), start, start + "fontFaceType".length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        typeFaceSpanText2.setText(ssb);

        ssb = new SpannableStringBuilder("fontFaceType can Fly-----sans-serif");
        ssb.setSpan(new TypefaceSpan("sans-serif"), start, start + "fontFaceType".length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        typeFaceSpanText3.setText(ssb);
    }

     /**
     * 超链接的Span
     */
    private void urlSpanClick() {
        spanTitle_A.setText("URL_Span_click：");
        ssb.setSpan(new URLSpan("https://www.baidu.com"),start,start + KEY_WORD.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        // 在单击链接时凡是有要执行的动作，都必须setMovementMethod
        urlSpanText.setText(ssb);
        urlSpanText.setMovementMethod(LinkMovementMethod.getInstance());
        // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        urlSpanText.setHighlightColor(getResources().getColor(R.color.lime));
    }

    /**
     * 下划线Span
     */
    private void underlinSpanShow() {
        ssb = new SpannableStringBuilder("UnderLine Text Info");
        spanTitle_B.setText("UnderLine_Span:");
        ssb.setSpan(new UnderlineSpan(), 0, "UnderLine".length() , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        underLineSpanText.setText(ssb);
    }
}
