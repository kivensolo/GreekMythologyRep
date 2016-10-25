package com.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.base.BaseActivity;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/10/25 21:51
 * description: PathEffect是用来控制绘制轮廓(线条)的方式
 *       DashPathEffect是PathEffect类的一个子类,
 *     可以使paint画出类似虚线的样子,并且可以任意指定虚实的排列方式
 *     Android包含了多个PathEffect，包括：
        【CornerPathEffect】：可以使用圆角来代替尖锐的角从而对基本图形的形状尖锐的边角进行平滑。
        【DashPathEffect】：可以使用DashPathEffect来创建一个虚线的轮廓(短横线/小圆点)，而不是使用实线。你还可以指定任意的虚/实线段的重复模式。
            DashPathEffect(float[] intervals, float offset) 其中intervals为虚线的ON(实)和OFF数组，该数组的length必须大于等于2，phase为绘制时的偏移量。
        【DiscretePathEffect】：这个类的作用是打散Path的线段，使得在原来路径的基础上发生打散效果。与DashPathEffect相似，
            但是添加了随机性。当绘制它的时候，需要指定每一段的长度和与原始路径的偏离度。一般的，
            通过构造DiscretePathEffect(float segmentLength,float deviation)来构造一个实例，
            其中，segmentLength指定最大的段长，deviation指定偏离量。

        【PathDashPathEffect】：这种效果可以定义一个新的形状(路径)并将其用作原始路径的轮廓标记。
            下面的效果可以在一个Paint中组合使用多个Path Effect。

        【ComposePathEffect】：将两种效果组合起来应用，先使用第一种效果，然后在这种效果的基础上应用第二种效果。
        【SumPathEffect】:叠加效果 但与ComposePathEffect不同的是，在表现时，会分别对两个参数的效果各自独立进行表现，然后将两个效果简单的重叠在一起显示出来。
         对象形状的PathEffect的改变会影响到形状的区域。这就能够保证应用到相同形状的填充效果将会绘制到新的边界中。
 */
public class PathEffects extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }
    private static class SampleView extends View {
        private PathEffect[] mEffects;  //储存路径效果的容器
        private Paint mPaint;
        private Paint mPaintRect;
        private Path mPath;
        private int[] mColors;
        private float mPhase;

        private static PathEffect makeDash(float phase) {
            //float数组,必须是偶数长度,且>=2,指定了多少长度的实线之后再画多少长度的空白
            return new DashPathEffect(new float[] { 15, 5, 8, 5 }, phase);
        }

        /**
         * 添加效果
         * @param e  path的效果
         * @param phase  偏移量
         */
        private static void makeEffects(PathEffect[] e, float phase) {
            // no effect
            e[0] = null;
            //边角平滑效果
            e[1] = new CornerPathEffect(10);
            //画自定义虚线
            e[2] = new DashPathEffect(new float[] {12, 5, 7, 5}, phase);

            //使用Path图形来填充当前的路径shape则是指填充图形，advance指每个图形间的间距，
            // phase为绘制时的偏移量，
            // style为该类自由的枚举值，有三种情况：Style.ROTATE、Style.MORPH和Style.TRANSLATE。
            //      其中ROTATE的情况下，线段连接处的图形转换以旋转到与下一段移动方向相一致的角度进行转转，
            //      MORPH时图形会以发生拉伸或压缩等变形的情况与下一段相连接，
            //      TRANSLATE时，图形会以位置平移的方式与下一段相连接。
            e[3] = new PathDashPathEffect(makeStampPathDash(), 12, phase,PathDashPathEffect.Style.ROTATE);
            e[4] = new ComposePathEffect(e[2], e[1]);
            e[5] = new ComposePathEffect(e[3], e[1]);
        }

        public SampleView(Context context) {
            super(context);
            setFocusable(true);
            setFocusableInTouchMode(true);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(6);
            mPaintRect = new Paint(mPaint);
            mPaintRect.setStrokeWidth(2);

            //获取path路径
            mPath = makeFollowPath();

            mEffects = new PathEffect[6];
            mColors = new int[] { Color.BLACK, Color.RED, Color.BLUE,
                                  Color.GREEN, Color.MAGENTA, Color.BLACK
                                };
        }

        @Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            RectF bounds = new RectF();
            mPath.computeBounds(bounds, false);
            canvas.translate(20 - bounds.left, 20 - bounds.top);

            //初始化每个效果
            makeEffects(mEffects, mPhase);
            mPhase += 1;
            invalidate();

            for (int i = 0; i < mEffects.length; i++) {
                mPaint.setPathEffect(mEffects[i]);
                mPaint.setColor(mColors[i]);

                mPaintRect.setPathEffect(mEffects[i]);
                mPaintRect.setColor(mColors[i]);
                canvas.drawPath(mPath, mPaint);
                canvas.drawRoundRect(bounds,0,0,mPaintRect);
                canvas.translate(0, 80);
            }
        }

        @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    mPath = makeFollowPath();
                    return true;
            }
            return super.onKeyDown(keyCode, event);
        }

        private static Path makeFollowPath() {
            Path p = new Path();
            p.moveTo(0, 0);
            //画15个阶段的路径
            for (int i = 1; i <= 15; i++) {
                p.lineTo(i*40, (float)Math.random() * 45);
            }
            return p;
        }

        /**
         * 绘制填充图形的路径
         * @return 印花的形状
         */
        private static Path makeStampPathDash() {
            Path p = new Path();
//            p.moveTo(4, 0);
//            p.lineTo(0, -4);
//            p.lineTo(8, -4);
//            p.lineTo(12, 0);
//            p.lineTo(8, 4);
//            p.lineTo(0, 4);
            p.lineTo(12, -6);
            p.lineTo(5,0);
            p.lineTo(12,6);
            return p;
        }
    }
}
