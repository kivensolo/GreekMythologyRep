package com.kingz.base.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.kingz.base.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface OnDialogButtonClickListener {
    fun onClick(dialog: Dialog, button: Int, data: String = "")
}

object ZDialogHelper {
    const val BUTTON_LEFT = -1
    const val BUTTON_MIDDLE = 0
    const val BUTTON_RIGHT = 1

    private var dialog: Dialog? = null

    fun isDialogShowing() = if (dialog == null) false else dialog!!.isShowing

    fun showNoteDialog(
        context: Context,
        title: String = context.getString(R.string.alert_dialog_title),
        message: String,
        buttonText: String = context.getString(R.string.alert_dialog_confirm),
        listener: OnDialogButtonClickListener? = null
    ) = GlobalScope.launch(Dispatchers.Main) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_note_base, null).apply {
            findViewById<TextView>(R.id.dialogTitle).text = title
            findViewById<TextView>(R.id.dialogMessage).text = message
            findViewById<Button>(R.id.dialogButton).apply {
                text = buttonText
                setOnClickListener {
                    dialog?.dismiss()
                    listener?.onClick(dialog!!, BUTTON_MIDDLE)
                }
            }
        }
        if (!isDialogShowing()) {
            dialog = Dialog(context, R.style.base_Theme_Dialog).apply {
                setContentView(view)
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }

            dialog?.window?.apply {
                setGravity(Gravity.CENTER)
                attributes?.width = 500
                attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        } else {
            view.findViewById<TextView>(R.id.dialogMessage).text = message
        }
    }

    /**
     * 显示常规的文本确认对话框
     */
    fun showConfirmDialog(
        context: Context,
        title: String = context.getString(R.string.alert_dialog_title),
        message: String,
        buttonTextLeft: String = context.getString(R.string.alert_dialog_cancle),
        buttonTextRight: String = context.getString(R.string.alert_dialog_confirm),
        listener: OnDialogButtonClickListener? = null
    ) = GlobalScope.launch(Dispatchers.Main) {
        dialog?.dismiss()
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_base, null).apply {
            findViewById<TextView>(R.id.dialogTitle).text = title
            findViewById<TextView>(R.id.dialogMessage).text = message
            findViewById<Button>(R.id.dialogButtonLeft).apply {
                text = buttonTextLeft
                setOnClickListener {
                    dialog?.dismiss()
                    listener?.onClick(dialog!!, BUTTON_LEFT)
                }
                setOnFocusChangeListener { v, hasFocus ->
                    typeface = if (hasFocus) {
                        Typeface.defaultFromStyle(Typeface.BOLD)
                    } else {
                        Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }
            }
            findViewById<Button>(R.id.dialogButtonRight).apply {
                text = buttonTextRight
                setOnClickListener {
                    dialog?.dismiss()
                    listener?.onClick(dialog!!, BUTTON_RIGHT)
                }
                setOnFocusChangeListener { v, hasFocus ->
                    typeface = if (hasFocus) {
                        Typeface.defaultFromStyle(Typeface.BOLD)
                    } else {
                        Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }
            }
        }
        dialog = Dialog(context, R.style.base_Theme_Dialog).apply {
            setContentView(view)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }

        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            attributes?.width = 500
            attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    /**
     * 显示带EditText的输入对话框
     */
    fun showEditDialog(
        context: Context,
        title: String = context.getString(R.string.alert_dialog_title),
        editHintText: String = "",
        defaultText: String? = "",
        buttonTextLeft: String = context.getString(R.string.alert_dialog_confirm),
        buttonTextRight: String = context.getString(R.string.alert_dialog_cancle),
        listener: OnDialogButtonClickListener? = null,
        editorActionListener: TextView.OnEditorActionListener? = null
    ) = GlobalScope.launch(Dispatchers.Main) {
        dialog?.dismiss()
        var editText : AppCompatEditText? = null
        val view: View = View.inflate(context, R.layout.dialog_edit_base, null).apply {
            findViewById<TextView>(R.id.dialogTitle).text = title
            editText = findViewById<AppCompatEditText>(R.id.dialogEdit).apply {
                hint = editHintText
                if (!defaultText.isNullOrEmpty()) {
                    setText(defaultText)
                }
                setOnEditorActionListener(editorActionListener)
            }
            findViewById<Button>(R.id.dialogButtonLeft).apply {
                text = buttonTextLeft
                setOnClickListener {
                    listener?.onClick(dialog!!, BUTTON_LEFT, editText?.text.toString())
                    hideSoftInputFromWindow(context, editText)
                    dialog?.dismiss()
                }
                setOnFocusChangeListener { v, hasFocus ->
                    typeface = if (hasFocus) {
                        Typeface.defaultFromStyle(Typeface.BOLD)
                    } else {
                        Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }
            }
            findViewById<Button>(R.id.dialogButtonRight).apply {
                text = buttonTextRight
                setOnClickListener {
                    listener?.onClick(dialog!!, BUTTON_RIGHT, editText?.text.toString())
                    hideSoftInputFromWindow(context, editText)
                    dialog?.dismiss()
                }
                setOnFocusChangeListener { v, hasFocus ->
                    typeface = if (hasFocus) {
                        Typeface.defaultFromStyle(Typeface.BOLD)
                    } else {
                        Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }
            }
        }
        dialog = Dialog(context, R.style.base_Theme_Dialog).apply {
            setContentView(view)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
            showSoftInputFromWindow(context as Activity, editText)
        }

        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            attributes?.width = 500
            attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    private fun showSoftInputFromWindow(activity: Activity, editText: AppCompatEditText?) {
        editText?.let {
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun hideSoftInputFromWindow(context: Context, editText: AppCompatEditText?) {
        editText?.let {
            (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}