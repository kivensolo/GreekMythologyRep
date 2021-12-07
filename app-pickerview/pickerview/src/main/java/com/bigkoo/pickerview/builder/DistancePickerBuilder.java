package com.bigkoo.pickerview.builder;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.view.WheelView;

/**
 * 距离选择控件
 * 支持样式:
 * ----------------
 * |   1  | 0     |
 * |   2  | 1     |
 * |   3 .| 2 公里 |
 * |   4  | 3     |
 * |   5  | 4     |
 * ----------------
 */
public class DistancePickerBuilder {

    //配置类
    private PickerOptions mPickerOptions;


    //Required
    public DistancePickerBuilder(Context context) {
        mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_FLOAT_DISTANCE);
        mPickerOptions.context = context;
    }

    public DistancePickerBuilder(Context context, OnOptionsSelectListener listener) {
        mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_FLOAT_DISTANCE);
        mPickerOptions.context = context;
        mPickerOptions.optionsSelectListener = listener;
    }

    //Option
    public DistancePickerBuilder setSubmitText(String textContentConfirm) {
        mPickerOptions.textContentConfirm = textContentConfirm;
        return this;
    }

    public DistancePickerBuilder setCancelText(String textContentCancel) {
        mPickerOptions.textContentCancel = textContentCancel;
        return this;
    }

    public DistancePickerBuilder setTitleText(String textContentTitle) {
        mPickerOptions.textContentTitle = textContentTitle;
        return this;
    }

    public DistancePickerBuilder isDialog(boolean isDialog) {
        mPickerOptions.isDialog = isDialog;
        return this;
    }

    public DistancePickerBuilder addOnCancelClickListener(View.OnClickListener cancelListener) {
        mPickerOptions.cancelListener = cancelListener;
        return this;
    }


    public DistancePickerBuilder setSubmitColor(int textColorConfirm) {
        mPickerOptions.textColorConfirm = textColorConfirm;
        return this;
    }

    public DistancePickerBuilder setCancelColor(int textColorCancel) {
        mPickerOptions.textColorCancel = textColorCancel;
        return this;
    }


    /**
     * {@link #setOutSideColor} instead.
     *
     * @param backgroundId color resId.
     */
    @Deprecated
    public DistancePickerBuilder setBackgroundId(int backgroundId) {
        mPickerOptions.outSideColor = backgroundId;
        return this;
    }

    /**
     * 显示时的外部背景色颜色,默认是灰色
     *
     * @param outSideColor color resId.
     * @return
     */
    public DistancePickerBuilder setOutSideColor(int outSideColor) {
        mPickerOptions.outSideColor = outSideColor;
        return this;
    }

    /**
     * ViewGroup 类型
     * 设置PickerView的显示容器
     *
     * @param decorView Parent View.
     * @return
     */
    public DistancePickerBuilder setDecorView(ViewGroup decorView) {
        mPickerOptions.decorView = decorView;
        return this;
    }

    public DistancePickerBuilder setLayoutRes(int res, CustomListener listener) {
        mPickerOptions.layoutRes = res;
        mPickerOptions.customListener = listener;
        return this;
    }

    public DistancePickerBuilder setBgColor(int bgColorWheel) {
        mPickerOptions.bgColorWheel = bgColorWheel;
        return this;
    }

    public DistancePickerBuilder setTitleBgColor(int bgColorTitle) {
        mPickerOptions.bgColorTitle = bgColorTitle;
        return this;
    }

    public DistancePickerBuilder setTitleColor(int textColorTitle) {
        mPickerOptions.textColorTitle = textColorTitle;
        return this;
    }

    public DistancePickerBuilder setSubCalSize(int textSizeSubmitCancel) {
        mPickerOptions.textSizeSubmitCancel = textSizeSubmitCancel;
        return this;
    }

    public DistancePickerBuilder setTitleSize(int textSizeTitle) {
        mPickerOptions.textSizeTitle = textSizeTitle;
        return this;
    }

    public DistancePickerBuilder setContentTextSize(int textSizeContent) {
        mPickerOptions.textSizeContent = textSizeContent;
        return this;
    }

    public DistancePickerBuilder setOutSideCancelable(boolean cancelable) {
        mPickerOptions.cancelable = cancelable;
        return this;
    }


     /**
     * 设置后缀提示文本 即单位量
     */
    public DistancePickerBuilder setLabels(String label1, String label2) {
        mPickerOptions.label1 = label1;
        mPickerOptions.label2 = label2;
        return this;
    }

    /**
     * 设置前缀提示文本
     */
    public DistancePickerBuilder setPrefixLabels(String label1, String label2) {
        mPickerOptions.prefix1 = label1;
        mPickerOptions.prefix2 = label2;
        return this;
    }

    protected DistancePickerBuilder setLabelAlignMode(WheelView.LabelAlignMode mode) {
        mPickerOptions.labelAlignMode = mode;
        return this;
    }

    /**
     * 设置Item 的间距倍数，用于控制 Item 高度间隔
     *
     * @param lineSpacingMultiplier 浮点型，1.0-4.0f 之间有效,超过则取极值。
     */
    public DistancePickerBuilder setLineSpacingMultiplier(float lineSpacingMultiplier) {
        mPickerOptions.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    /**
     * Set item divider line type color.
     *
     * @param dividerColor color resId.
     */
    public DistancePickerBuilder setDividerColor(@ColorInt int dividerColor) {
        mPickerOptions.dividerColor = dividerColor;
        return this;
    }

    /**
     * Set item divider line type.
     *
     * @param dividerType enum Type {@link WheelView.DividerType}
     */
    public DistancePickerBuilder setDividerType(WheelView.DividerType dividerType) {
        mPickerOptions.dividerType = dividerType;
        return this;
    }

    /**
     * Set the textColor of selected item.
     *
     * @param textColorCenter color res.
     */
    public DistancePickerBuilder setTextColorCenter(int textColorCenter) {
        mPickerOptions.textColorCenter = textColorCenter;
        return this;
    }

    /**
     * Set the textColor of selected item prefix label.
     *
     * @param textColorCenter color res.
     */
    public DistancePickerBuilder setTextColorCenterPrefix(int textColorCenter) {
        mPickerOptions.textColorCenterPrefix = textColorCenter;
        return this;
    }

    /**
     * Set the textColor of outside item.
     *
     * @param textColorOut color resId.
     */
    public DistancePickerBuilder setTextColorOut(@ColorInt int textColorOut) {
        mPickerOptions.textColorOut = textColorOut;
        return this;
    }

    public DistancePickerBuilder setTypeface(Typeface font) {
        mPickerOptions.font = font;
        return this;
    }

    public DistancePickerBuilder setCyclic(boolean cyclic1, boolean cyclic2) {
        mPickerOptions.cyclic1 = cyclic1;
        mPickerOptions.cyclic2 = cyclic2;
        return this;
    }

    public DistancePickerBuilder setSelectOptions(int option1) {
        mPickerOptions.option1 = option1;
        return this;
    }

    public DistancePickerBuilder setSelectOptions(int option1, int option2) {
        mPickerOptions.option1 = option1;
        mPickerOptions.option2 = option2;
        return this;
    }

    public DistancePickerBuilder setTextXOffset(int xOffsetOne, int xOffsetTwo) {
        mPickerOptions.x_offset_one = xOffsetOne;
        mPickerOptions.x_offset_two = xOffsetTwo;
        return this;
    }

    public DistancePickerBuilder isCenterLabel(boolean isCenterLabel) {
        mPickerOptions.isCenterLabel = isCenterLabel;
        return this;
    }


    /**
     * 设置最大可见数目
     *
     * @param count 建议设置为 3 ~ 9之间。
     */
    public DistancePickerBuilder setItemVisibleCount(int count) {
        mPickerOptions.itemsVisibleCount = count;
        return this;
    }

    /**
     * 透明度是否渐变
     *
     * @param isAlphaGradient true of false
     */
    public DistancePickerBuilder isAlphaGradient(boolean isAlphaGradient) {
        mPickerOptions.isAlphaGradient = isAlphaGradient;
        return this;
    }

    /**
     * 切换选项时，是否还原第一项
     *
     * @param isRestoreItem true：还原； false: 保持上一个选项
     * @return TimePickerBuilder
     */
    public DistancePickerBuilder isRestoreItem(boolean isRestoreItem) {
        mPickerOptions.isRestoreItem = isRestoreItem;
        return this;
    }

    /**
     * @param listener 切换item项滚动停止时，实时回调监听。
     * @return
     */
    public DistancePickerBuilder setOptionsSelectChangeListener(OnOptionsSelectChangeListener listener) {
        mPickerOptions.optionsSelectChangeListener = listener;
        return this;
    }

    public DistancePickerBuilder setOptionsSelectListener(OnOptionsSelectListener listener) {
        mPickerOptions.optionsSelectListener = listener;
        return this;
    }


    public <T> OptionsPickerView<T> build() {
        return new OptionsPickerView<>(mPickerOptions);
    }
}
