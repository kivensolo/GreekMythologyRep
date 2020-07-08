package com.kingz.utils.dialog

//import com.afollestad.materialdialogs.GravityEnum
//import com.afollestad.materialdialogs.MaterialDialog

class DialogHelper {
//    companion object {
//        fun getInstance() =
//                Helper.instance
//    }
//
//    private object Helper {
//        val instance = DialogHelper()
//    }
//
//    private var materialDialog: MaterialDialog? = null
//    private var mIsFinish: Boolean = false
//
//    fun showProgress(activity: Activity, msg: String = "请稍候...") {
//        mIsFinish = !activity.isFinishing
//        if (mIsFinish) {
//            if (materialDialog == null) {
//                showProgressDialog(activity, msg)
//            } else {
//                if (!materialDialog!!.isShowing) {
//                    showProgressDialog(activity, msg)
//                }
//            }
//        }
//    }
//
//    private fun showProgressDialog(activity: Activity, msg: String) {
//        materialDialog = MaterialDialog.Builder(activity)
//            .content(msg)
//            .cancelable(false)
//            .canceledOnTouchOutside(false)
//            .contentGravity(GravityEnum.CENTER)
//            .progress(true, 0)
//            .progressIndeterminateStyle(false)
//            .show()
//    }
//
//    fun dismissProgress() {
//        if (mIsFinish) {
//            materialDialog?.dismiss()
//        }
//    }
}