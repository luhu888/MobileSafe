package com.luhu.MobileSafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private TextView tv_safenumber;
    private ImageView iv_protecting;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp=getSharedPreferences("config",MODE_PRIVATE);
        //判断是否设置向导，如果没有就跳到设置向导界面
       Boolean configed=sp.getBoolean("config",false);
        if (configed){
            //设置过向导，就在手机防盗页面
            setContentView(R.layout.activity_lost_find);
            tv_safenumber= (TextView) findViewById(R.id.tv_safenumber);
            iv_protecting= (ImageView) findViewById(R.id.iv_protecting);
            //得到我们设置的安全号码
            String safenumber=sp.getString("safenumber","");
            tv_safenumber.setText(safenumber);
            //设置防盗保护的状态
            boolean protecting=sp.getBoolean("protecting",false);
            if (protecting){
                //已经开启防盗保护
                iv_protecting.setImageResource(R.drawable.lock);
            }else{
                //还没开启防盗保护
                iv_protecting.setImageResource(R.drawable.unlock);
            }
        }else{
            //还没有设置向导
            Intent intent=new Intent(this,Setup1Activity.class);
            startActivity(intent);
            //关闭当前页面
            finish();
        }
    }
    public void reEnterSetup(View view){
        Intent intent=new Intent(this,Setup1Activity.class);;
        startActivity(intent);
        finish();
    }
}
