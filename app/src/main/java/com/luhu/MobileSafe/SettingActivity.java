package com.luhu.MobileSafe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.luhu.MobileSafe.ui.SettingItemView;

/**
 * Created by LuHu on 2016/9/25.
 */
public class SettingActivity extends AppCompatActivity {
    private SettingItemView siv_update;
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);//保存应用数据
        sp=getSharedPreferences("config",MODE_PRIVATE);  //创建一个用于保存应用信息的表
        siv_update= (SettingItemView) findViewById(R.id.siv_update);
       boolean update= sp.getBoolean("update",false); //获取应用信息
        if (update){
            //自动升级已经开启
            siv_update.setChecked(true);
        }else{
            //自定升级已经关闭
            siv_update.setChecked(false);
        }
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sp.edit();  //创建sp的编辑器
                //判断是否选中，当已经打开自动升级了
                if (siv_update.isChecked()){
                    siv_update.setChecked(false);
                    editor.putBoolean("update",false);
                }
                else{
                    //没有打开自动升级
                    siv_update.setChecked(true);
                    editor.putBoolean("update",true);
                }
                editor.commit();
            }
        });
    }
}
