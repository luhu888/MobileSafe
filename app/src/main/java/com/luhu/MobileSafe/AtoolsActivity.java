package com.luhu.MobileSafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by LuHu on 2016/10/6.
 */
public class AtoolsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**号码归属地查询的页面
     * @param view
     */
    public void numberQuery(View view) {
        Intent intent=new Intent(this,NumberAddressQueryActivity.class);
        startActivity(intent);

    }
}
