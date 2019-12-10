package com.takt;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.customviews.chart.FramesCurveChartView;

import java.text.DecimalFormat;

/**
 * author: King.Z <br>
 * date:  2017/11/25 16:51 <br>
 * description: 系统FPS显示工具 <br>
 *  学习自wasabeef的第三方库  远程仓库地址为：jp.wasabeef:takt:1.0.1
 */
public class FpsTools {

    private static final String TAG = "FpsTools";
    //此工具会在app整个生命周期存在  忽略在静态类中使用context造成的内存泄漏
    @SuppressLint("StaticFieldLeak")
    private static final Program program = new Program();

    private FpsTools() {}

    //stock
    public static Program init(Application application) {
        return program.prepare(application);
    }

    public static void finish() {
        program.stop();
    }

    public static class Program {
        private MetronomeCallback metronome;
        private boolean show = true;

        private WindowManager wm;
        private View stageView;
        private TextView fpsText;
        private FramesCurveChartView fpsChart;
        private WindowManager.LayoutParams params;
        private final DecimalFormat decimal = new DecimalFormat("#.0\' fps\'");

        private Program prepare(Application application) {
            this.metronome = new MetronomeCallback();
            this.params = new WindowManager.LayoutParams();
            this.params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            this.params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            this.params.type = WindowManager.LayoutParams.TYPE_TOAST;
            this.params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//168
            this.params.format = PixelFormat.TRANSLUCENT;
            this.params.gravity = Gravity.BOTTOM | Gravity.END;
            this.params.x = 10;
            this.wm = WindowManager.class.cast(application.getSystemService(Context.WINDOW_SERVICE));
            LayoutInflater inflater = LayoutInflater.from(application);
            this.stageView = inflater.inflate(R.layout.stage,null);
            this.fpsChart = (FramesCurveChartView)stageView.findViewById(R.id.takt_chart);
            this.fpsText = (TextView)stageView.findViewById(R.id.takt_fps);
            this.listener(new IAudience() {
                public void heartbeat(double fps) {
                    if(fpsText != null) {
                        fpsText.setText(decimal.format(fps));
                    }
                }

                @Override
                public void heartstop(long times,long delayDiff) {
                    //ZLog.e(TAG,"Droped_Frame!! times:" + times + "  delayDiff:"+delayDiff);
                    fpsChart.addYData(delayDiff);
                }
            });
            return this;
        }

        public void play() {
            this.metronome.start();
            if(this.show) {
                this.wm.addView(this.stageView, this.params);
            }
        }

        public void stop() {
            this.metronome.stop();
            if(this.show && this.stageView != null) {
                this.wm.removeView(this.stageView);
            }

        }

        public Program color(int color) {
            this.fpsText.setTextColor(color);
            return this;
        }

        public Program size(float size) {
            this.fpsText.setTextSize(size);
            return this;
        }

        /*
        * alpha from = 0.0, to = 1.0
        */
        public Program alpha(float alpha) {
            this.fpsText.setAlpha(alpha);
            return this;
        }

        public Program interval(int ms) {
            this.metronome.setInterval(ms);
            return this;
        }

        public Program listener(IAudience audience) {
            this.metronome.addListener(audience);
            return this;
        }

        public Program hide() {
            this.show = false;
            return this;
        }

        public Program seat(Seats seat) {
            switch (seat) {
                case TOP_RIGHT:
                    params.gravity = Gravity.TOP | Gravity.END;
                    break;
                case TOP_LEFT:
                    params.gravity = Gravity.TOP | Gravity.START;
                    break;
                case TOP_CENTER:
                    params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    break;
                case CENTER:
                    params.gravity = Gravity.CENTER;
                    break;
                case RIGHT_CENTER:
                    params.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
                    break;
                case LEFT_CENTER:
                    params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
                    break;
                case BOTTOM_RIGHT:
                    params.gravity = Gravity.BOTTOM | Gravity.END;
                    break;
                case BOTTOM_LEFT:
                    params.gravity = Gravity.BOTTOM | Gravity.START;
                    break;
                case BOTTOM_CENTER:
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    break;
            }
            return this;
        }
    }

}
