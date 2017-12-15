package com.takt;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kingz.customdemo.R;

import java.text.DecimalFormat;

/**
 * author: King.Z <br>
 * date:  2017/11/25 16:51 <br>
 * description: 系统FPS显示工具 <br>
 * //TODO 完善具体常量
 */
public class FpsTools {

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

    @SuppressWarnings("WeakerAccess")
    public static class Program {
        private GrapherFramesCallback metronome;
        private boolean show = true;
        private WindowManager wm;
        private View stageView;
        private TextView fpsText;
        private WindowManager.LayoutParams params;
        private final DecimalFormat decimal = new DecimalFormat("#.0\' fps\'");

        private Program prepare(Application application) {
            this.metronome = new GrapherFramesCallback();
            this.params = new WindowManager.LayoutParams();
            this.params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            this.params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            this.params.type = WindowManager.LayoutParams.TYPE_TOAST;
            this.params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//168
            this.params.format = PixelFormat.TRANSLUCENT;
            this.params.gravity = 8388693;
            this.params.x = 10;
            this.wm = WindowManager.class.cast(application.getSystemService(Context.WINDOW_SERVICE));
            LayoutInflater inflater = LayoutInflater.from(application);
            this.stageView = inflater.inflate(R.layout.stage, new RelativeLayout(application));
            this.fpsText = (TextView)this.stageView.findViewById(R.id.takt_fps);
            this.listener(new IAudience() {
                public void heartbeat(double fps) {
                    if(Program.this.fpsText != null) {
                        Program.this.fpsText.setText(Program.this.decimal.format(fps));
                    }
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
            switch(seat.ordinal()) {
            case 1:
                this.params.gravity = 8388661;
                break;
            case 2:
                this.params.gravity = 8388659;
                break;
            case 3:
                this.params.gravity = Gravity.TOP|Gravity.CENTER;
                break;
            case 4:
                this.params.gravity = 17;
                break;
            case 5:
                this.params.gravity = 8388629;
                break;
            case 6:
                this.params.gravity = 8388627;
                break;
            case 7:
                this.params.gravity = 8388693;
                break;
            case 8:
                this.params.gravity = 8388691;
                break;
            case 9:
                this.params.gravity = Gravity.BOTTOM|Gravity.CENTER;
            }
            return this;
        }
    }

}
