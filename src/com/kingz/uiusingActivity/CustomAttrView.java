package com.kingz.uiusingActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by KingZ on 2015/11/19.
 * Discription:自定义View
 */
public class CustomAttrView extends View{

    private ViewGroup.LayoutParams layoutParams;
    /**
     * 至少需要提供一个构造器，包含一个contenx和AttributeSet对象作为参数。
     *  这个constructor允许layout editor创建并编辑我自定义的view的实例。
     * @param context
     * @param attrs
     */
    public CustomAttrView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAttrView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /*为了添加一个内置的View到你的UI上，你需要通过XML属性来指定它的样式与行为。
     良好的自定义views可以通过XML添加和改变样式，为了让你的自定义的view也有如此的行为
        1:为你的view在资源标签下定义自设的属性
        2:在你的XML layout中指定属性值
        3:在运行时获取属性值
        4:把获取到的属性值应用在你的view上

     */
//    layoutParams = new LayoutInflater(layoutParams.MATCH_PARENT, layoutParams.WRAP_CONTENT);

}
