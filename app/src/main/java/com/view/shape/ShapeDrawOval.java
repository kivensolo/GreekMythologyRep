package com.view.shape;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.BaseActivity;
import com.utils.ZLog;

public class ShapeDrawOval extends BaseActivity {

    public static final String TAG = "ShapeDrawOval";

    private static final float BALL_SIZE = 300f;
    public static final int DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(lps);
        setContentView(root);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        Button startBtn = new Button(this);
        final MyShapeView shapeView = new MyShapeView(this);
        addContentView(shapeView,lp);
        startBtn.setX(50);
        startBtn.setY(20);
        startBtn.setWidth(200);
        startBtn.setHeight(150);
        addContentView(startBtn,lp);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeView.startAnimation();
            }
        });
    }

    public class MyShapeView extends View {

        ShapeHolder gitem = null;
        ValueAnimator bounceAnim = null;


        public MyShapeView(Context context) {
            super(context);
            gitem = initShape(500f,200f);
        }

        public ShapeHolder initShape(float x,float y){
            OvalShape arcShape = new OvalShape();
            arcShape.resize(BALL_SIZE,BALL_SIZE);
            ShapeDrawable drawable = new ShapeDrawable(arcShape);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x);
            shapeHolder.setY(y);
            int red = (int)(50 + Math.random() * 155);
            int green = (int)(50 + Math.random() * 155);
            int blue = (int)(50 + Math.random() * 155);
            int lightColor = 0xff000000 | red << 16 | green << 8 | blue;
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
            //半径中心坐标x/y,渐变开始颜色，结束颜色，材质拼接模式
            Paint paint = drawable.getPaint();
            RadialGradient gradient = new RadialGradient(40f,20f,120f,lightColor,darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);    //设置画笔着色器
            shapeHolder.setPaint(paint);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.translate(gitem.getX(),gitem.getY()); //前乘矩阵
            gitem.getShape().draw(canvas);
            super.onDraw(canvas);
        }

        private void createAnimation(){
            if(null == bounceAnim){
                bounceAnim = ObjectAnimator.ofFloat(gitem,"y",gitem.getY(),getHeight()-BALL_SIZE).setDuration(DURATION);
                bounceAnim.setInterpolator(new BounceInterpolator());
                bounceAnim.addUpdateListener(new LocalAnimatorUpdateListener());
                bounceAnim.addListener(new LocalAnimatorListener());
            }
        }

        private void startAnimation(){
            createAnimation();
            bounceAnim.start();
        }

        class LocalAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener{

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        }


        class LocalAnimatorListener implements Animator.AnimatorListener{

            @Override
            public void onAnimationStart(Animator animation) {
                ZLog.i(TAG,"onAnimationStart()...");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ZLog.i(TAG,"onAnimationEnd()...");

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                ZLog.i(TAG,"onAnimationCancel()...");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                ZLog.i(TAG,"onAnimationRepeat()...");

            }
        }
    }
}
