package com.kingz.widgets.android_src

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kingz.customdemo.R
import com.kingz.customdemo.databinding.BasicControlsBinding
import com.kingz.utils.ToastTools
import com.zeke.kangaroo.utils.UIUtils
import com.zeke.kangaroo.view.popwindow.CommonPopupWindow
import com.zeke.ktx.App

/**
 * Created by KingZ.
 * Data: 2016 2016/2/1
 * Discription: 常用控件集合
 *
 *
 * CompoundButton：需要实现OnCheckedChangeListener接口
 * RatingBar： 需要实现OnRatingBarChangeListener接口
 * Gallery: 需要实现OnItemSelectedListener接口
 */
class BasicControlsActivity : AppCompatActivity(),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener,
    RatingBar.OnRatingBarChangeListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var basicControlsBinding: BasicControlsBinding
    private lateinit var mToast: ToastTools
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        basicControlsBinding = BasicControlsBinding.inflate(LayoutInflater.from(this))
        context = App.instance!!.applicationContext
        setContentView(basicControlsBinding.root)
        initViews()
    }

    private fun initViews() {
        mToast = ToastTools.getInstance()

        basicControlsBinding.apply {
            initTextViews()

            val galleryAdaper = MyAdapter(this@BasicControlsActivity)
            val adapter = ArrayAdapter(
                this@BasicControlsActivity,
                android.R.layout.simple_spinner_item, stringArray
            )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
            galleryArea.adapter = galleryAdaper
            radio1.setOnCheckedChangeListener(this@BasicControlsActivity)
            radio1.setOnCheckedChangeListener(this@BasicControlsActivity)
            ratingBar.onRatingBarChangeListener = this@BasicControlsActivity
            btnGridView.setOnClickListener(this@BasicControlsActivity)
            btnDialogFramentId.setOnClickListener(this@BasicControlsActivity)
        }
    }

    /**
     * 文本控件初始化
     */
    private fun BasicControlsBinding.initTextViews() {
        val arduinoDrawable = context.resources.getDrawable(R.drawable.arduino)
        arduinoDrawable.setBounds(
            0, 0, UIUtils.dip2px(context, 20f),
            UIUtils.dip2px(context, 10f)
        )
        withStartDrawableLTRTextView.setCompoundDrawables(
            arduinoDrawable,
            null,
            null,
            null
        )
        withStartDrawableRTLTextView.setCompoundDrawablesRelative(
            arduinoDrawable,
            null,
            null,
            null
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_gridView -> {
                val intent = Intent(this, AppsGridView::class.java)
                startActivity(intent)
            }
            R.id.btn_dialog_frament_id -> {
                showAlertDialogFragment()
            }
        }
    }

    /**
     * 显示FragmentDilaog
     */
    private fun showAlertDialogFragment() {
        val dialog = AlertDialogFragment.newInstance("测试标题", "测试文本内容", true)
        dialog.show(fragmentManager, "dialog")
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.radio1 -> if (isChecked) {
                mToast.showToast(context, "选择了左边的RaidoButton")
            }
            R.id.radio2 -> if (isChecked) {
                mToast.showToast(context, "选择了右边的RaidoButton")
            }
            else -> {}
        }
    }

    /**
     * RatingBar的监听回调函数
     */
    override fun onRatingChanged(ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
        if (fromUser) {
            //android:isIndicator="true" 的属性 指示器的作用  只看
            mToast.showToast(this, "设置的星级为：$rating;并且设置指示器属性")
            ratingBar.setIsIndicator(true)
        }
    }

    /**
     * Item被选中的回调函数
     *
     * @param parent   父容器
     * @param view     所选择的视图
     * @param position 所点击的位置
     * @param id       所点击的控件id
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    internal inner class MyAdapter(private val context: Context) : BaseAdapter() {
        private val imagesArray = intArrayOf(
            R.mipmap.sample_1,
            R.mipmap.sample_2,
            R.mipmap.sample_3,
            R.mipmap.sample_4,
            R.mipmap.sample_5,
            R.mipmap.sample_6,
            R.mipmap.sample_7,
            R.mipmap.sample_thumb_0,
            R.mipmap.sample_thumb_1,
            R.mipmap.sample_thumb_2,
            R.mipmap.sample_thumb_3,
            R.mipmap.sample_thumb_4
        )

        override fun getCount(): Int {
            return imagesArray.size
            //            return Integer.MAX_VALUE;
        }

        override fun getItem(position: Int): Any {
            return imagesArray[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val i: ImageView
            if (convertView != null) {
                i = convertView as ImageView //获取缓存视图
            } else {
                i = ImageView(this@BasicControlsActivity)
                i.scaleType = ImageView.ScaleType.FIT_XY
                i.layoutParams = Gallery.LayoutParams(400, 300)
            }
//            i.setBackgroundResource(imagesArray[position]);
//            i.setImageResource(imagesArray[position%imagesArray.length]);
            i.setImageResource(imagesArray[position])
            return i
        }
    }

    /**
     * 显示PopUpWindow
     *
     * @param view
     */
    fun showPopupWindow(view: View?) {
        //常规设置方法
//        View contentView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.simple_listviewitem, null);
//        TextView textView = (TextView) contentView.findViewById(R.id.commonadapter_item_text);
//        textView.setText("新疆电视台");
//
//        PopupWindow popupWindow = new PopupWindow(contentView,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("mengdd", "onTouch : ");
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
        //如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_loading));
//        popupWindow.showAsDropDown(view);
        val commonPopupWindow = CommonPopupWindow.Builder(this)
            .setView(R.id.commonadapter_item_text)
            .setFocusable(true)
            .setListener { popView, layoutResId ->
                val textView =
                    popView.findViewById<View>(R.id.commonadapter_item_text) as TextView
                textView.text = "测试数据"
            }
            .create()
        commonPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.drawable_loading))
        //popupWindow从view下方弹起
        commonPopupWindow.showAsDropDown(view)
    }

    companion object {
        const val TAG = "BasicControlsActivity"
        private val stringArray = arrayOf(
            "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"
        )
    }
}