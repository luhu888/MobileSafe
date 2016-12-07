package com.luhu.MobileSafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.luhu.MobileSafe.R;
import com.luhu.MobileSafe.service.GPSService;

/**
 * Created by Administrator on 2016/10/1.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG ="SMSReceiver" ;
    @Override
    public void onReceive(Context context, Intent intent) {
     //写接收短信的代码
      Object[] objs= (Object[]) intent.getExtras().get("pdus");
        for (Object b:objs){
//            具体的某一条短信
           SmsMessage sms= SmsMessage.createFromPdu((byte[]) b);
            //发送者
           String sender= sms.getOriginatingAddress();
            String body=sms.getMessageBody();
            if ("#*location*#".equals(body)){
                //得到手机的GPS
                Log.i(TAG, "onReceive:得到手机的GPS ");
                //启动服务
                Intent i=new Intent(context, GPSService.class);
                context.startService(i);
                SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
                String lastlocation=sp.getString("lastLocation",null);
                if (TextUtils.isEmpty(lastlocation)){
                    //位置没有得到
                    SmsManager.getDefault().sendTextMessage(sender,null,"geting location...",null,null);
                }else{
                    SmsManager.getDefault().sendTextMessage(sender,null,lastlocation,null,null);
                }

                //把这个广播终止掉
                abortBroadcast();
            }else if("#*alarm*#".equals(body)){
                Log.i(TAG, "播放报警音乐 ");
                MediaPlayer player=MediaPlayer.create(context, R.raw.ylzs);
                player.setLooping(false);//设置是否循环播放
                player.setVolume(1.0f,1.0f);
                player.start();
                abortBroadcast();
            }else if("#*wipedata*#".equals(body)){
                Log.i(TAG, "远程清除数据 ");
                abortBroadcast();
            }else if("#*lockscreeen*#".equals(body)){
                Log.i(TAG, "远程锁屏 ");
                abortBroadcast();
            }
        }
    }
}
