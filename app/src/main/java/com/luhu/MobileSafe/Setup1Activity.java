package com.luhu.MobileSafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.luhu.MobileSafe.utils.BaseSetupActivity;

/**
 * Created by LuHu on 2016/9/27.
 */
public class Setup1Activity extends BaseSetupActivity{
        //1.定义一个手势识别器
    private GestureDetector detector;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        //2.实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if ((e2.getRawX()-e1.getRawX())>200){
                    //显示上一个页面，从左往右滑
                    System.out.println("显示上一个界面");
                    return true;
                }
                if((e1.getRawX()-e2.getRawX())>200){
                    //显示下一个界面，从右向左滑
                    System.out.println("显示下一个界面");
                    showNext();
                    return true;
                }
                return super.onFling(e1,e2,velocityX,velocityY);
            }
        });
    }


    public void showNext() {
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity（intent）后面执行
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }
    @Override
    public void showPre() {
    }
}
