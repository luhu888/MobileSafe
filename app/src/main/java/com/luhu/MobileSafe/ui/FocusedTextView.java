package com.luhu.MobileSafe.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
/**自定义的TextView,一开始就有焦点
 * Created by Administrator on 2016/9/25.
 */
public class FocusedTextView extends TextView{

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * @return当前并没有焦点，只是欺骗了Android系统，当做有焦点处理
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
