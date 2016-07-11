package com.kingz.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.kingz.uiusingListViews.R;
import com.utils.ToastTools;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/1
 * Discription: 常用控件集合
 *
 *  CompoundButton：需要实现OnCheckedChangeListener接口
 *  RatingBar： 需要实现OnRatingBarChangeListener接口
 *  Gallery: 需要实现OnItemSelectedListener接口
 */
public class BasicControlsActivity extends Activity implements View.OnClickListener
                                        ,CompoundButton.OnCheckedChangeListener
                                        ,RatingBar.OnRatingBarChangeListener
                                        ,AdapterView.OnItemSelectedListener{
    public static final String TAG = "BasicControlsActivity";
    private ToastTools mToast;
    private RadioButton rbLeft,rbRight;
    private Button disabledButton,customDialogFrament;
    private Button gridViewButton;
    private RatingBar mRratingBar;
    private Spinner mSpinner;
    private Gallery mGallery;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"BasicControlsActivity onCreate");
        setContentView(R.layout.basic_controls);

        initViews();
        initListeners();
    }

    private static final String[] stringArray = {
        "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"
    };

    private void initViews() {
        mToast = new ToastTools();
        disabledButton = (Button) findViewById(R.id.button_disabled);
        disabledButton.setEnabled(false);
        gridViewButton  = (Button) findViewById(R.id.btn_gridView);
        rbLeft = (RadioButton) findViewById(R.id.radio1);
        rbRight = (RadioButton) findViewById(R.id.radio2);
        mRratingBar = (RatingBar) findViewById(R.id.ratingBar);
        mGallery = (Gallery) findViewById(R.id.gallery_area);
       customDialogFrament = (Button) findViewById(R.id.btn_dialog_frament_id);

        mSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, stringArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        MyAdapter galleryAdaper = new MyAdapter(this);
        mGallery.setAdapter(galleryAdaper);

    }

    private void initListeners() {
        rbLeft.setOnCheckedChangeListener(this);
        rbRight.setOnCheckedChangeListener(this);
        mRratingBar.setOnRatingBarChangeListener(this);
        gridViewButton.setOnClickListener(this);
        customDialogFrament.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_gridView:
                Intent intent = new Intent(this,MyGridView.class);
                startActivity(intent);
                break;
            case R.id.btn_dialog_frament_id:
                AlertDialogFragment dialog = AlertDialogFragment.newInstance("测试标题",
                        "这是个内容",
                        true);
                dialog.show(getFragmentManager(),"dialog");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.radio1:
                if(isChecked){
                    mToast.showMgtvWaringToast(context,"选择了左边的RaidoButton");
                }
                break;
            case R.id.radio2:
                if(isChecked){
                    mToast.showMgtvWaringToast(context,"选择了右边的RaidoButton");
                }
                break;
            default:
                break;
        }

    }

    /**
     * RatingBar的监听回调函数
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(fromUser){
            //android:isIndicator="true" 的属性 指示器的作用  只看
             mToast.showMgtvWaringToast(this,"设置的星级为：" + rating + ";并且设置指示器属性");
            ratingBar.setIsIndicator(true);
        }
    }

    /**
     * Item被选中的回调函数
     * @param parent  父容器
     * @param view  所选择的视图
     * @param position 所点击的位置
     * @param id 所点击的控件id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class MyAdapter extends BaseAdapter {
        private Context context;

        private int[] imagesArray = {
                R.mipmap.sample_1,R.mipmap.sample_2,R.mipmap.sample_3,
                R.mipmap.sample_4,R.mipmap.sample_5,R.mipmap.sample_6,
                R.mipmap.sample_7,R.mipmap.sample_thumb_0,R.mipmap.sample_thumb_1,
                R.mipmap.sample_thumb_2,R.mipmap.sample_thumb_3,R.mipmap.sample_thumb_4,
        };


        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return imagesArray.length;
//            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return imagesArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;
            if(convertView != null){
                 i = (ImageView)convertView;//获取缓存视图
            }else{
                i  = new ImageView(BasicControlsActivity.this);
                i.setScaleType(ImageView.ScaleType.FIT_XY);
                i.setLayoutParams(new Gallery.LayoutParams(400,300));
            }
//            i.setBackgroundResource(imagesArray[position]);
//            i.setImageResource(imagesArray[position%imagesArray.length]);
            i.setImageResource(imagesArray[position]);
            return i;
        }
    }
}
