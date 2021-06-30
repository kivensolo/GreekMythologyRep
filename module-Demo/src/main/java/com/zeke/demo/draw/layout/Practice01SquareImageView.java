package com.zeke.demo.draw.layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自定义Layout_1
 * 把这个Imageview写成正方形的 ImageView
 */
public class Practice01SquareImageView extends AppCompatImageView {
    public Practice01SquareImageView(Context context) {
        super(context);
    }

    public Practice01SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice01SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写 onMeasure()，调整尺寸，让 ImageView 总是正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 先用 getMeasuredWidth() 和 getMeasuredHeight() 取到 super.onMeasure() 的计算结果
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        // 然后通过计算，让宽度和高度一致
        int minWidth = Math.min(measuredHeight, measuredWidth);

        // 再用 setMeasuredDimension(width, height) 来保存最终的宽度和高度
        setMeasuredDimension(minWidth,minWidth);
    }
}
