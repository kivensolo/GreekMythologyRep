package com.kingz.customviews.chart;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.kingz.utils.ScreenTools;
import com.kingz.utils.ZLog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * author: King.Z <br>
 * date:  2017/8/6 10:40 <br>
 * description: 帧耗时曲线表图 <br>
 *             |
 *   frameT(ms)|
 *             |
 *             |
 *             |
 *             |____________________
 *             Time
 */
public class FramesCurveChartView extends View {

    private static final String TAG = "FramesCurveChartView";
    /**
     * Latency of frames
     */
    private static final int Frame_Latency = 60;
    /**
     * Y轴总显示 128 ms
     */
    private static final int Frame_Time_Limit_MS = 128;
    /**
     * Y轴显示刻度 8 ms
     */
    public static final int Y_LINE_DEC = 8;
    /**
     * 丢帧阈值时间 32 s
     */
    public static final int DROP_FRAME_T = 16 * 2;
    public static final int PADDING_TOP = ScreenTools.Operation(50);
    public static final int PADDING_BOTTOM = ScreenTools.Operation(50);
    public static final int PADDING_LEFT = ScreenTools.Operation(45);
    public static final int PADDING_RIGHT = ScreenTools.Operation(20);
    private int viewWidth, viewHeigth,chartHeight,chartWidth;
    private float averageWidth, averageHeigth;
    Paint curvePaint, coordinateDescPaint, yLinePaint,coordinateLinePaint;
    private String[] xValues = new String[] { "Time-------->", " ", " ", " ", " ", " ", " " };
    private List<Float> pointX, pointY;
    private Path curvePath;    // 曲线路径
    private boolean hasDrawLine = false;
    private static enum Linestyle{
        Line, Curve
    }

    public FramesCurveChartView(Context context) {
        super(context);
        init();
    }

    public FramesCurveChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FramesCurveChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ZLog.d(TAG, "init..");
        curvePaint = new Paint();
        curvePaint.setColor(Color.parseColor("#2E88CE"));
        curvePaint.setAntiAlias(true);
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setStrokeWidth(4);
        curvePaint.setPathEffect(new CornerPathEffect(10));
        coordinateDescPaint = new Paint();
        coordinateDescPaint.setColor(Color.parseColor("#ffffff"));
        coordinateDescPaint.setStrokeWidth(3.0f);
        coordinateDescPaint.setTextSize(ScreenTools.Operation(26));
        yLinePaint = new Paint();
        yLinePaint.setColor(Color.parseColor("#33ffffff"));
        yLinePaint.setStrokeWidth(2.5f);
        yLinePaint.setStyle(Paint.Style.FILL);
        coordinateLinePaint = new Paint();
        coordinateLinePaint.setColor(Color.parseColor("#adff2f"));
        coordinateLinePaint.setStrokeWidth(3f);
        coordinateLinePaint.setStyle(Paint.Style.FILL);
        pointX = new ArrayList<>(Frame_Latency);
        pointY = new LinkedList<>();
        curvePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = getWidth();
        viewHeigth = getHeight();

        if(viewWidth != 0 && pointX.size() == 0){
            chartHeight = viewHeigth - PADDING_TOP - PADDING_BOTTOM;
            chartWidth = viewWidth - PADDING_LEFT - PADDING_RIGHT;
            averageHeigth = chartHeight / Frame_Time_Limit_MS;
            averageWidth =  chartWidth / Frame_Latency;
            for (int i = 0; i <= Frame_Latency; i++) {
                pointX.add(PADDING_LEFT + (float) ((i + 0.5) * averageWidth));
            }
        }
        //ZLog.d(TAG,"onMeasure() viewWidth = " + viewWidth + ";viewHeigth=" + viewHeigth + ";averageWidth = " + averageWidth + ";averageHeigth=" + averageHeigth);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void addYData(float pos_y){
        if(pointY.size() == Frame_Latency){
            pointY.remove(0);
        }
        pointY.add(pos_y);
        this.invalidate();
    }

    public void setYDatas(final List<Float> pointY1) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!hasDrawLine){
            drawYCoordinateValue(canvas);
            drawYCoordinateLine(canvas);
            drawXCoordinateText(canvas);
            drawCoordinate(canvas);
        }
        drawCurveChart(canvas);
        super.onDraw(canvas);
    }

    private void drawCurveChart(Canvas canvas) {
        curvePath.reset();
        if(pointX.size() != 0){
            for (int i = 0; i < pointY.size(); i++) {
                if(i == 0){
                    curvePath.moveTo(pointX.get(i),viewHeigth - PADDING_BOTTOM - pointY.get(i) * averageHeigth);
                }else{
                    curvePath.lineTo(pointX.get(i),viewHeigth - PADDING_BOTTOM - pointY.get(i) * averageHeigth);
                }
            }
        }
        canvas.drawPath(curvePath, curvePaint);
    }

    private void drawYCoordinateLine(Canvas canvas) {
        for (int i = 1; i <= Frame_Time_Limit_MS; i++){
            if(i == DROP_FRAME_T){
                yLinePaint.setColor(Color.parseColor("#5FCDDA"));
            }else{
                yLinePaint.setColor(Color.parseColor("#33F7D5D5"));
            }
            if(i % Y_LINE_DEC == 0){
                canvas.drawLine(PADDING_LEFT,viewHeigth - PADDING_BOTTOM - averageHeigth * i,
                        viewWidth - PADDING_RIGHT,viewHeigth - PADDING_BOTTOM - averageHeigth * i, yLinePaint);
            }
        }
    }

    private void drawXCoordinateText(Canvas canvas) {
        canvas.drawText(xValues[0], PADDING_LEFT + averageWidth, viewHeigth - PADDING_BOTTOM + averageHeigth * 8, coordinateDescPaint);
    }

    private void drawYCoordinateValue(Canvas canvas) {
        Log.d(TAG, "drawYCoordinateValue..");
        for (int i = 0; i <= Frame_Time_Limit_MS; i++){
            if(i % Y_LINE_DEC == 0){
                canvas.drawText("" + i, 0, viewHeigth - PADDING_BOTTOM - averageHeigth * i - averageHeigth / 2, coordinateDescPaint);
            }
        }
    }


    private void drawCoordinate(Canvas canvas) {
        //Y轴
        canvas.drawLine(PADDING_LEFT,PADDING_TOP,
                PADDING_LEFT, getHeight() - PADDING_BOTTOM,
                coordinateLinePaint);
        //X轴
        canvas.drawLine(PADDING_LEFT,viewHeigth - PADDING_BOTTOM,
                viewWidth - PADDING_RIGHT,viewHeigth - PADDING_BOTTOM,
                coordinateLinePaint);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
