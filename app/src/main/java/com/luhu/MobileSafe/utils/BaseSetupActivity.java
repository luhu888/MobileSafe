package com.luhu.MobileSafe.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


/**
 * Created by Administrator on 2016/9/29.
 */
public abstract class BaseSetupActivity extends AppCompatActivity{
     //1.定义一个手势识别器
    protected SharedPreferences sp;
    private GestureDetector detector;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        //2.实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //屏蔽斜划
                if (Math.abs((e2.getRawY()-e1.getRawY()))>100){   //e2-e1在Y轴上距离的绝对值
                    Toast.makeText(BaseSetupActivity.this, "不能这样划", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //屏蔽在x轴滑动太慢的情况
                if (Math.abs(velocityX)<400){
                    Toast.makeText(BaseSetupActivity.this, "划动得太慢了", Toast.LENGTH_SHORT).show();
                    return true;
                }




                if ((e2.getRawX()-e1.getRawX())>200){
                    //显示上一个页面，从左往右滑
                    System.out.println("显示上一个界面");
                    showPre();
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

                 public abstract void showNext();  //抽象方法由继承的子类实现
                 public abstract void showPre();
                 /**下一步的点击事件
                  * @param view
                  */
                 public void next(View view){
                     showNext();
                 }
                 /**上一步的点击事件
                  * @param view
                  */
                 public void pre(View view){
                     showPre();
                 }

                 //3.使用手势识别器
                 public boolean onTouchEvent(MotionEvent event){
                     detector.onTouchEvent(event);
                     return super.onTouchEvent(event);
                 }





}
