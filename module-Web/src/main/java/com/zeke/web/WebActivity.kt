package com.zeke.web

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.router.RPath
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.repository.WanAndroidRepository
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModel
import kotlinx.android.synthetic.main.web_activity.*

@Route(path = RPath.PAGE_WEB)
class WebActivity : BaseVMActivity<WanAndroidRepository, WanAndroidViewModel>(),
    PopupMenu.OnMenuItemClickListener {

    override val viewModel: WanAndroidViewModel by viewModels {
        ViewModelFactory.build { WanAndroidViewModel() }
    }

    var agentWeb: AgentWeb? = null
    private var mId = 0
    private var mTitle: String? = null
    /**
     * 初次打开时的url
     */
    private var mUrl: String? = null
    private var mAuthor: String? = null
    /**
     * 当前url
     */
    private var mCurUrl: String? = null
    /**
     * 当前的收藏状态
     */
    private var isCollect = false
    private var isArticle = false
    /**
     * 用来记住最初的收藏状态
     */
    private var isStatus = false
    var popupMenu: PopupMenu? = null
    private var toolbarrightImg: ImageView?= null
    private var titleTextView: TextView?= null

    override fun getContentLayout(): Int {
        return R.layout.web_activity
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        toolbarrightImg = findViewById(R.id.iv_toolbar_right)
        toolbarrightImg?.setImageResource(R.drawable.btn_more_selector)
        titleTextView = findViewById(R.id.tvTitle)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val intent = intent
        mTitle = intent.getStringExtra(WADConstants.KEY_TITLE)
        mUrl = intent.getStringExtra(WADConstants.KEY_URL)
        mAuthor = intent.getStringExtra(WADConstants.KEY_AUTHOR)
        mId = intent.getIntExtra(WADConstants.KEY_ID, -1)
        isCollect = intent.getBooleanExtra(WADConstants.KEY_IS_COLLECT, false)
        isStatus = isCollect
        isArticle = mId > 0
        setTitleText(mTitle)
        loadUrl(mUrl)
    }

    override fun onResume() {
        super.onResume()
        agentWeb!!.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb!!.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb!!.webLifeCycle.onDestroy()
    }

    override fun onBackPressed() {
        if (!agentWeb!!.back()) { //通知首页改变收藏状态
            if (isStatus != isCollect) {
                val intent = Intent()
                intent.putExtra(WADConstants.KEY_IS_COLLECT, isCollect)
                setResult(Activity.RESULT_OK, intent)
            }
            super.onBackPressed()
        }
    }

    fun loadUrl(url: String?) {
        if (agentWeb == null) {
            agentWeb = AgentWeb.with(this)
                .setAgentWebParent(
                    web_root_layout!!, LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                .useDefaultIndicator(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimaryDark
                    ), 2
                )
                .setWebChromeClient(object : WebChromeClient() {
                    override fun onReceivedTitle(
                        view: WebView,
                        title: String
                    ) {
                        super.onReceivedTitle(view, title)
                        if (!TextUtils.isEmpty(title)) {
                            setTitleText(title)
                        }
                    }
                })
                .setWebViewClient(object : WebViewClient() {
                    override fun onPageStarted(
                        view: WebView,
                        url: String,
                        favicon: Bitmap
                    ) {
                        super.onPageStarted(view, url, favicon)
                        mCurUrl = url
                    }
                })
                .createAgentWeb()
                .go(url)
        } else {
            agentWeb!!.webCreator.webView.loadUrl(url)
        }
    }

    private fun setTitleText(text: String?) {
        titleTextView?.text = text
    }

    private fun setIconEnable(menu: Menu, enable: Boolean) {
        try {
            val clazz =
                Class.forName("com.android.internal.view.menu.MenuBuilder")
            val m = clazz.getDeclaredMethod(
                "setOptionalIconsVisible",
                Boolean::class.javaPrimitiveType
            )
            m.isAccessible = true
            //传入参数
            m.invoke(menu, enable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickMenu() {
        if (popupMenu == null) {
            popupMenu = PopupMenu(this, toolbarrightImg)
            popupMenu!!.menu.clear()
            setIconEnable(popupMenu!!.menu, true)
            popupMenu!!.menuInflater
                .inflate(R.menu.toolbar_menu, popupMenu!!.menu)
            popupMenu!!.setOnMenuItemClickListener(this)
            //            try {
//                Field field = popupMenu.getClass().getDeclaredField("mPopup");
//                field.setAccessible(true);
//                MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
//                helper.setForceShowIcon(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        val item =
            popupMenu!!.menu.findItem(R.id.menuCollect)
        item.isVisible = isArticle
        item.setTitle(if (isCollect) R.string.toolbar_menu_un_collect else R.string.toolbar_menu_collect)
        popupMenu!!.show()
    }

    fun OnClick(v: View) {
        if (v.id == R.id.iv_toolbar_right) {
            clickMenu()
        }
    }

    private fun clickShare() {
        val intent =
            Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mCurUrl)
        startActivity(
            Intent.createChooser(
                intent,
                getString(R.string.toolbar_menu_share)
            )
        )
    }

    /**
     * 收藏，通过id收藏站内文章。
     */
    private fun clickCollect() { //        if(checkLogin()){
//            if(isCollect){
//                mViewModel.unCollect(mId).observe(this, resource ->{
//                    resource.handle(new OnCallback<CollectBean>() {
//                        @Override
//                        public void onSuccess(CollectBean data) {
//                            isCollect = false;
//                            showToast(R.string.success_un_collect);
//                        }
//                    });
//                });
//            }else{
//                //收藏
//                mViewModel.collect(mId).observe(this, resource ->{
//                    resource.handle(new OnCallback<CollectBean>() {
//                        @Override
//                        public void onSuccess(CollectBean data) {
//                            isCollect = true;
//                            showToast(R.string.success_collect);
//                        }
//                    });
//                });
//            }
//
//        }
    }

    private fun clickExplorer() {
        val uri = Uri.parse(mCurUrl)
        val intent =
            Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuShare -> {
                clickShare()
            }
            R.id.menuCollect -> {
                clickCollect()
            }
            R.id.menuRefresh -> {
                loadUrl(mCurUrl)
            }
            R.id.menuExplorer -> {
                clickExplorer()
            }
        }
        return true
    }

}