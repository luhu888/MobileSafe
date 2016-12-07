package com.luhu.MobileSafe.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luhu.MobileSafe.R;

/**自定义的组合控件，里面有两个textView，还有一个checkbox和一个view
 * Created by Administrator on 2016/9/25.
 */
public class SettingItemView extends RelativeLayout {
    private CheckBox cb_status;
    private TextView tv_desc;
    private TextView tv_title;

    private String title;
    private String desc_on;
    private String desc_off;
    /**初始化布局文件
     * @param context
     */
    private void iniView(Context context){
        //把一个布局文件view做成一个SettingItemView
        View.inflate(context, R.layout.setting_item_view,this);
        cb_status= (CheckBox)this.findViewById(R.id.cb_status);
        tv_desc= (TextView) this.findViewById(R.id.tv_desc);
        tv_title= (TextView) this.findViewById(R.id.tv_title);
    }
    public SettingItemView(Context context) {
        super(context);
        iniView(context);
    }

    /**带有两个参数的构造方法，布局文件使用的时候调用
     * @param context
     * @param attrs
     */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);
       title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.luhu.MobileSafe","title");
       desc_on=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.luhu.MobileSafe","desc_on");
       desc_off=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.luhu.MobileSafe","desc_off");
        tv_title.setText(title);
        setDesc(desc_off);//设置默认checkBox
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView(context);
    }



    /** 校验控件是否选中
     * @return
     */
    public boolean isChecked()
    {
        return cb_status.isChecked();
    }

    /**设置组合控件的状态
     * @param checked
     */
    public void setChecked(boolean checked){
        if (checked){
            setDesc(desc_on);
        }else{
            setDesc(desc_off);
        }
        cb_status.setChecked(checked);
    }

    /**设置组合控件的描述信息
     * @param text
     */
    public void setDesc(String text){
        tv_desc.setText(text);
    }
}
