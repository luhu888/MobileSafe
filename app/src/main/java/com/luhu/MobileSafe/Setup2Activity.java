package com.luhu.MobileSafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.luhu.MobileSafe.ui.SettingItemView;
import com.luhu.MobileSafe.utils.BaseSetupActivity;

/**
 * Created by Administrator on 2016/9/27.
 */
public class Setup2Activity extends BaseSetupActivity {
    private SettingItemView siv_setup2_sim;
    private TelephonyManager tm;  //是系统的一个服务，可以读取SIM卡序列号
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        siv_setup2_sim = (SettingItemView) findViewById(R.id.siv_setup2_sim);
        tm =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String sim=sp.getString("sim",null);
        if (TextUtils.isEmpty(sim)){
            //没有绑定
            siv_setup2_sim.setChecked(false);
        }else{
            //已经绑定
            siv_setup2_sim.setChecked(true);
        }
        siv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sp.edit();
                if (siv_setup2_sim.isChecked()){
                    siv_setup2_sim.setChecked(false);
                    editor.putString("sim",null);
                }else{
                    //保存sim卡的序列号
                    String sim= tm.getSimSerialNumber();
                    siv_setup2_sim.setChecked(true);
                    editor.putString("sim",sim);
                }
                 editor.commit();
        }
        });
    }

    @Override
    public void showNext() {
        //取出是否绑定sim卡，必须绑定才能进入下一步
        String sim=sp.getString("sim",null);
        if (TextUtils.isEmpty(sim)){
            //没有绑定
            Toast.makeText(Setup2Activity.this, "sim卡没有绑定", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity（intent）后面执行
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);

    }
    @Override
    public void showPre() {
        Intent intent=new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity（intent）后面执行
        overridePendingTransition(R.anim.tran_pre,R.anim.tran_pre_out);

    }

}
