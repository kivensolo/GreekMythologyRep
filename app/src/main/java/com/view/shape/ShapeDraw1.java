package com.view.shape;

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
import android.widget.LinearLayout;
import com.BaseActivity;

public class ShapeDraw1 extends BaseActivity {

    private static final float BALL_SIZE = 300f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(lps);
        setContentView(root);
        addContentView(new MyShapeView(this),lps);

    }

    public class MyShapeView extends View {
        ShapeHolder gitem = null;

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
    }
}
