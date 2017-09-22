package com.kingz.view.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import com.kingz.customdemo.R;

public class ViewFlipperAnimation extends Activity {

    private String[] mStrings = {"Push up", "Push left", "Cross fade", "Hyperspace"}; //往上/往左/淡入淡出/超空间
    private ViewFlipper mFlipper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_viewfliper);

        mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
        mFlipper.startFlipping();

        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new FirstSpinnerOnItemSelectedListener());
    }

    public class FirstSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startAnimation(R.anim.push_up_in, R.anim.push_up_out);
                    break;
                case 1:
                    startAnimation(R.anim.push_left_in, R.anim.push_left_out);
                    break;
                case 2:
                    startAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
                default:
                    startAnimation(R.anim.hyperspace_in, R.anim.hyperspace_out);
                    break;
            }
        }

        private void startAnimation(int startId, int outId) {
            mFlipper.setInAnimation(AnimationUtils.loadAnimation(ViewFlipperAnimation.this, startId));
            mFlipper.setOutAnimation(AnimationUtils.loadAnimation(ViewFlipperAnimation.this, outId));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
