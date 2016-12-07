package com.luhu.MobileSafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/29.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {

        sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean protecting= sp.getBoolean("protecting",false);
        if (protecting){
            //开启防盗保护才执行这个地方，避免乱发短信
            tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //读取之前保存的sim卡信息
            String saveSim=sp.getString("sim","")+"aa";

            //读取当前的sim卡信息
            String realSim=tm.getSimSerialNumber();

            //比较是否一样
            if (saveSim.equals(realSim)){
                Toast.makeText(context, "sim卡未变", Toast.LENGTH_LONG).show();

                //sim卡没变更
            }else{
                //sim卡变更，发短信给短信安全号码
                Toast.makeText(context, "sim卡变更", Toast.LENGTH_LONG).show();

                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber","")
                        ,null,"sim changing...",null,null);
            }

        }
    }
}
