package com.zeke.web

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.kingz.base.BaseHeaderActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.web.databinding.WebPageBinding
import kotlinx.android.synthetic.main.web_page.*

@Route(path = RouterConfig.PAGE_WEB)
class WebActivity : BaseHeaderActivity(),
    PopupMenu.OnMenuItemClickListener {

    override val viewModel: WanAndroidViewModelV2 by viewModels {
        ViewModelFactory.build { WanAndroidViewModelV2() }
    }

    var agentWeb: AgentWeb? = null

    @JvmField
    @Autowired(name = RouterConfig.PARAM_WEB_ARTICAL_INFO)
    var articalInfo: Article? = null

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

    private lateinit var bindingobj:WebPageBinding

    override fun getContentLayout(): Int  = com.kingz.base.R.layout.layout_invalid

    override fun getContentView(): View? {
        if(!::bindingobj.isInitialized){
            bindingobj = WebPageBinding.inflate(LayoutInflater.from(this))
        }
        return bindingobj.root
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()
        setRightDrawableRes(R.drawable.btn_more_selector)
//        setHeaderBackgroundColor(Color.TRANSPARENT)
        setHeaderMarginTopInImmersion(web_root_layout)
    }

    override fun initData(savedInstanceState: Bundle?) {
        isCollect = articalInfo?.collect ?: false
        isStatus = isCollect
        isArticle = (articalInfo?.id ?: -1) > 0
        setTitle(articalInfo?.title)
        loadUrl(articalInfo?.link)
    }

    override fun onResume() {
        super.onResume()
        agentWeb?.webLifeCycle?.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb?.webLifeCycle?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb?.webLifeCycle?.onDestroy()
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

    private fun loadUrl(url: String?) {
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
                            setTitle(title)
                        }
                    }
                })
                .setWebViewClient(object : WebViewClient() {
                    //Crash
                    //    java.lang.IllegalArgumentException:
                    //    Parameter specified as non-null is null: method kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull,
                    //    parameter favicon
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
            popupMenu = PopupMenu(this, findViewById(R.id.ivRight))
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