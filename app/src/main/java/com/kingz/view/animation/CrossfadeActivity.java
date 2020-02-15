package com.kingz.view.animation;

import com.base.BaseActivity;

/**
 * author: King.Z <br>
 * date:  2017/1/4 20:37 <br>
 * description: Crossfading Two Views <br>
 */

public class CrossfadeActivity extends BaseActivity {
//    @BindView(R.id.content)
//    public View mContentView;
//    @BindView(R.id.lorem_ipsum_view)
//    public TextView mTextView;
//    @BindView(R.id.loading_spinner)
//    public View mLoadingView;
//    private int mShortAnimationDuration;
//
//    private NameViewModel testViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crossfade);
//        // Initially hide the content view.
//        mContentView.setVisibility(View.GONE);
//        // Retrieve and cache the system's default "short" animation time.
//        //mShortAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
//        mShortAnimationDuration = 1200;
//        crossfade();
//
//        // 在大多数情况下，应用组件的 onCreate() 方法是开始观察 LiveData 对象的正确着手点
//        testViewModel = ViewModelProviders.of(this).get(NameViewModel.class);
//        // Create the observer which updates the UI.
//        final Observer<String> nameObserver = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                ZLog.d("onChanged ", "s: "+s);
//                mTextView.setText(R.string.lorem_ipsum_change);
//            }
//        };
//
//        /*
//         * 将 Observer 对象附加到 LiveData 对象，
//         * observe() 方法会采用 LifecycleOwner 对象。
//         * 这样会使 Observer 对象订阅 LiveData 对象，以使其收到有关更改的通知。
//         * 通常情况下，可以在界面控制器（如 Activity 或 Fragment）中附加 Observer 对象。
//         *
//         * 若要注册未关联 LifecycleOwner 对象可以使用 observeForever(Observer) 方法
//         */
//        testViewModel.getLiveDataOfString().observe(this, nameObserver);
//
//        // 更新存储在 LiveData 对象中的值时，它会触发所有已注册的观察者（只要附加的 LifecycleOwner 处于活跃状态)
//        testViewModel.getLiveDataOfString().setValue("hahhah");
//    }
//
//
//    @OnClick(R.id.lorem_ipsum_view)
//    public void onTextClicked(View view){
//        testViewModel.getLiveDataOfString().setValue("数据改变了！！");
//    }
//
//    private void crossfade() {
//
//        // Set the content view to 0% opacity but visible, so that it is visible
//        // (but fully transparent) during the animation.
//        mContentView.setAlpha(0f);
//        mContentView.setVisibility(View.VISIBLE);
//
//        mContentView.animate()
//                .alpha(1f)
//                .setDuration(mShortAnimationDuration)
//                .setListener(null);
//
//        mLoadingView.animate()
//                .alpha(0f)
//                .setDuration(mShortAnimationDuration)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mLoadingView.setVisibility(View.GONE);
//                    }
//                });
//    }

}
