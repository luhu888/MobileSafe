package com.luhu.MobileSafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.luhu.MobileSafe.utils.BaseSetupActivity;
/**
 * Created by Administrator on 2016/9/27.
 */
public class Setup3Activity extends BaseSetupActivity {
    private EditText et_setup3_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_setup3_phone= (EditText) findViewById(R.id.et_setup3_phone);
        et_setup3_phone.setText(sp.getString("safenumber",""));
    }

    @Override
    public void showNext() {
        //保存号码前判断之前是否有安全号码
       String phone= et_setup3_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(Setup3Activity.this, "安全号码还未设置", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存安全号码
       SharedPreferences.Editor editor= sp.edit();
        editor.putString("safenumber",phone);
        editor.commit();
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity（intent）后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        //要求在finish()或者startActivity（intent）后面执行
        overridePendingTransition(R.anim.tran_pre, R.anim.tran_pre_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)
            return;
       String phone=data.getStringExtra("phone").replace("-","");
        et_setup3_phone.setText(phone);
    }

    /**选择联系人的点击事件
     * @param view
     */
    public void selectContact(View view){
        Intent intent=new Intent(this,SelectContactActivity.class);
        startActivityForResult(intent,0);

    }
}
