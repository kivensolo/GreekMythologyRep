package com.kingz.module.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import java.util.*

/**
 * 包含多种状态的View,用于满足业务场景中的各类提示UI
 * 比如加载、重试、错误状态的视图
 */
class LoadStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0    // 注意这个attr的默认值,有的控件不一定是0
) : FrameLayout(context, attrs, defStyleAttr) {

    //除开contentView之外的View
    private val mOtherIds: ArrayList<Int> = ArrayList()

    // <editor-fold defaultstate="collapsed" desc="New">
    private val mInflater: LayoutInflater
    private val mEmptyViewResId: Int
    private val mErrorViewResId: Int
    private val mLoadingViewResId: Int
    private val mNoNetworkViewResId: Int
    private val mContentViewResId: Int

    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null

    private var mViewStatus = STATUS_CONTENT
    // </editor-fold>

    companion object {
        private val DEFAULT_LAYOUT_PARAMS = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        const val STATUS_CONTENT = 0x01
        const val STATUS_LOADING = 0x02
        const val STATUS_EMPTY = 0x03
        const val STATUS_ERROR = 0x04
        const val STATUS_NO_NETWORK = 0x05
        private const val NULL_CUSTOM_RESOURCE_ID = -1
    }

    init {
        val typeAttar = context.obtainStyledAttributes(attrs, R.styleable.LoadStatusView, defStyleAttr, 0)
        mLoadingViewResId = typeAttar.getResourceId(R.styleable.LoadStatusView_loadingView, R.layout.status_default_loading_view)
        mEmptyViewResId = typeAttar.getResourceId(R.styleable.LoadStatusView_emptyView, R.layout.status_default_empty_view)
        mErrorViewResId = typeAttar.getResourceId(R.styleable.LoadStatusView_errorView, R.layout.status_default_error_view)
        mNoNetworkViewResId = typeAttar.getResourceId( R.styleable.LoadStatusView_noNetworkView, R.layout.status_default_no_network_view)
        mContentViewResId = typeAttar.getResourceId(R.styleable.LoadStatusView_contentView, NULL_CUSTOM_RESOURCE_ID)
        typeAttar.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView)
        mOtherIds.clear()
        if (null != mOnRetryClickListener) {
            mOnRetryClickListener = null
        }
    }

    // <editor-fold defaultstate="collapsed" desc="显示空视图">
    @JvmOverloads
    fun showEmpty(
        layoutId: Int = mEmptyViewResId,
        layoutParams: ViewGroup.LayoutParams? = DEFAULT_LAYOUT_PARAMS
    ) {
        showEmpty(if (null == mEmptyView) inflateView(layoutId) else mEmptyView, layoutParams)
    }

    fun showEmpty(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        checkNull(view, "Empty view is null.")
        checkNull(layoutParams, "Layout params is null.")
        mViewStatus = STATUS_EMPTY
        if (null == mEmptyView) {
            mEmptyView = view
            val emptyRetryView = mEmptyView!!.findViewById<View>(R.id.empty_retry_view)
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mEmptyView!!.id)
            addView(mEmptyView, 0, layoutParams)
        }
        showViewById(mEmptyView!!.id)
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="显示错误视图">

    @JvmOverloads
    fun showError(
        layoutId: Int = mErrorViewResId,
        layoutParams: ViewGroup.LayoutParams? = DEFAULT_LAYOUT_PARAMS
    ) {
        showError(if (null == mErrorView) inflateView(layoutId) else mErrorView, layoutParams)
    }

    fun showError(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        checkNull(view, "Error view is null.")
        checkNull(layoutParams, "Layout params is null.")
        mViewStatus = STATUS_ERROR
        if (null == mErrorView) {
            mErrorView = view
            val errorRetryView = mErrorView!!.findViewById<View>(R.id.error_retry_view)
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mErrorView!!.id)
            addView(mErrorView, 0, layoutParams)
        }
        showViewById(mErrorView!!.id)
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="显示加载中视图">
    @JvmOverloads
    fun showLoading(
        layoutId: Int = mLoadingViewResId,
        layoutParams: ViewGroup.LayoutParams? = DEFAULT_LAYOUT_PARAMS
    ) {
        val v = if (null == mLoadingView) inflateView(layoutId) else mLoadingView
        showLoading(v, layoutParams)
    }

    fun showLoading(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        checkNull(view, "Loading view is null.")
        checkNull(layoutParams, "Layout params is null.")
        mViewStatus = STATUS_LOADING
        if (null == mLoadingView) {
            mLoadingView = view
            mOtherIds.add(mLoadingView!!.id)
            addView(mLoadingView, 0, layoutParams)
        }
        showViewById(mLoadingView!!.id)
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="显示无网络视图">
    @JvmOverloads
    fun showNoNetwork(
        layoutId: Int = mNoNetworkViewResId,
        layoutParams: ViewGroup.LayoutParams? = DEFAULT_LAYOUT_PARAMS
    ) {
        val v = if (null == mNoNetworkView) {
            inflateView(layoutId)
        } else {
            mNoNetworkView
        }
        showNoNetwork(v, layoutParams)
    }

    fun showNoNetwork(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        checkNull(view, "No network view is null.")
        checkNull(layoutParams, "Layout params is null.")
        mViewStatus = STATUS_NO_NETWORK
        if (null == mNoNetworkView) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView!!.findViewById<View>(R.id.no_network_retry_view)
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mNoNetworkView!!.id)
            addView(mNoNetworkView, 0, layoutParams)
        }
        showViewById(mNoNetworkView!!.id)
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="显示内容视图">
    fun showContent() {
        mViewStatus = STATUS_CONTENT
        if (null == mContentView && mContentViewResId != NULL_CUSTOM_RESOURCE_ID) {
            mContentView = mInflater.inflate(mContentViewResId, null)
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    /**
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showContent(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showContent(inflateView(layoutId), layoutParams)
    }

    /**
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showContent(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        checkNull(view, "Content view is null.")
        checkNull(layoutParams, "Layout params is null.")
        mViewStatus = STATUS_CONTENT
        clear(mContentView)
        mContentView = view
        addView(mContentView, 0, layoutParams)
        showViewById(mContentView!!.id)
    }

    // </editor-fold>
    private fun inflateView(layoutId: Int): View {
        return mInflater.inflate(layoutId, null)
    }

    // <editor-fold defaultstate="collapsed" desc="内部UI控制API">
    /**
     * 根据id显示指定视图，其他视图一概隐藏
     */
    private fun showViewById(viewId: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (view.id == viewId) VISIBLE else GONE
        }
    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) GONE else VISIBLE
        }
    }

    private fun clear(vararg views: View?) {
        views.forEach { view ->
            view?.let { removeView(it) }
        }
    }
    // </editor-fold>

    private fun checkNull(`object`: Any?, hint: String) {
        if (null == `object`) {
            throw NullPointerException(hint)
        }
    }

    // <editor-fold defaultstate="collapsed" desc="设置重试点击事件">
    private var mOnRetryClickListener: OnClickListener? = null
    fun setOnRetryClickListener(onRetryClickListener: OnClickListener?) {
        mOnRetryClickListener = onRetryClickListener
    }
    // </editor-fold>

    // ------------------------------------------------------------

    /* fun showError(){
         showError(null)
     }

     fun showError(listener: OnClickListener?) {
         visibility = View.VISIBLE
         val errorString = resources.getString(R.string.network_error)
         val spannableString = SpannableString(errorString).apply {
             val errorSpan = ForegroundColorSpan(resources.getColor(R.color.dialog_text_gray))
             val error2Span = ForegroundColorSpan(resources.getColor(R.color.dialog_text))
             setSpan(errorSpan, 0, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
             setSpan(error2Span, 8, errorString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
         }
         statusText.apply {
             text = spannableString
             setOnClickListener(listener)
             visibility = View.VISIBLE
         }
         progress?.apply {
             visibility = View.GONE
             stop()
         }
     }*/

/* 遗留Spannable复用
    fun show(
        @StringRes string: Int,
        onClickListener: OnClickListener?
    ) {
        this.visibility = View.VISIBLE
        val str = resources.getString(string)
        if (str.contains("\n")) {
            val spannableString = SpannableString(str)
            //实例化ForegroundColorSpan对象并设置前景色
            val errorSpan = ForegroundColorSpan(resources.getColor(R.color.dialog_text))
            spannableString.setSpan(
                errorSpan, 0,
                str.indexOf("\n"),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )//设置字体颜色
            //实例化ForegroundColorSpan对象并设置前景色
            val error2Span = ForegroundColorSpan(Color.parseColor("#989898"))
            spannableString.setSpan(
                error2Span, str.indexOf("\n"),
                str.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )//设置字体颜色
            statusText.text = spannableString
        } else {
            statusText.text = str
        }
        statusText.setOnClickListener(onClickListener)
        statusText.visibility = View.VISIBLE
        progress?.apply {
            stop()
            visibility = View.GONE
        }
    }*/

    fun dismiss() {
        mContentView?.visibility = View.GONE
    }

}
