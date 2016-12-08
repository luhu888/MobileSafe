package com.luhu.MobileSafe;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luhu.MobileSafe.utils.MD5Utils;

/**
 * Created by LuHu on 2016/9/23.
 */
public class HomeActivity extends AppCompatActivity{
    private GridView list_home;
    private  MyAdapter adapter;
    private SharedPreferences sp;
    private static String[]names={
            "手机防盗","通讯卫士","软件管理",
            "进程管理","流量统计","手机杀毒",
            "缓存清理","高级工具","设置中心"
    };
    private static int[] ids={
            R.drawable.safe,        R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager,  R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools,      R.drawable.settings,
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       list_home= (GridView) findViewById(R.id.list_home);
        adapter=new MyAdapter();
        list_home.setAdapter(adapter);
        sp=getSharedPreferences("config",MODE_PRIVATE);

        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 8://进入设置中心
                        Intent intent=new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break; 
                    case 0:  //进入手机防盗页面
                        showLostFindDialog();
                        break;
                    case 7://进入高级工具
                        Intent intents=new Intent(HomeActivity.this,AtoolsActivity.class);
                        startActivity(intents);
                        break;
                    default:
                    break;
                   
                }

                }

        });

    }

    private void showLostFindDialog() {
        //判断是否设置过密码
        if (isSetupPwd()){
            //已经设置过密码，弹出的是输入对话框
            showEnterDialog();
        }else{
            //没有设置密码，弹出设置密码对话框
            shoeSetupDialog();
        }

    }
    private EditText et_setup_pwd;
    private EditText et_setup_confirm;
    private Button ok;
    private Button cancel;
    private AlertDialog dialog;
    /**
     * 设置密码对话框
     */
    private void shoeSetupDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
        //定义一个布局文件
        View view=View.inflate(HomeActivity.this,R.layout.dialog_setup_password,null);
      et_setup_pwd= (EditText) view.findViewById(R.id.et_setup_pwd);
      et_setup_confirm= (EditText) view.findViewById(R.id.et_setup_confirm);
       ok= (Button) view.findViewById(R.id.ok);
       cancel= (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //把这个对话框取消
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出密码
                String password=et_setup_pwd.getText().toString().trim();//trim为去除字符串两端的空格
                String password_confirm=et_setup_confirm.getText().toString().trim();
                //判断密码框和确定密码框，如果有一个为空就返回


                if(TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm)){
                    Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断是否一致才保存，把对话框消掉，进入手机防盗页面
                if (password.equals(password_confirm)){
                    //一致的话就保存密码
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("password", MD5Utils.md5Password(password));
                    editor.commit();//edit每次编辑完都需提交
                    dialog.dismiss();
                    Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        builder.setView(view);
        dialog =builder.show();
    }

    private void showEnterDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
        //定义一个布局文件
        View view=View.inflate(HomeActivity.this,R.layout.dialog_enter_password,null);
        et_setup_pwd= (EditText) view.findViewById(R.id.et_setup_pwd);
        ok= (Button) view.findViewById(R.id.ok);
        cancel= (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //把这个对话框取消
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出密码
                String password=et_setup_pwd.getText().toString().trim();
                String savePassword=sp.getString("password","");//取出加密后的
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(HomeActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (MD5Utils.md5Password(password).equals(savePassword)) {
                    //输入的密码是之前设置的密码
                    //对话框消掉，进入主页面
                    dialog.dismiss();
                    Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
                    startActivity(intent);


                }else{
                    Toast.makeText(HomeActivity.this, "密码错误 ", Toast.LENGTH_LONG).show();
                    et_setup_pwd.setText("");
                    return;
                }
                }

        });
        builder.setView(view);
        dialog =builder.show();
    }

    /**判断是否设置密码
     * @return
     */
    private boolean isSetupPwd(){
        String password=sp.getString("password",null);
//        if(TextUtils.isEmpty(password)){855
//            return false;    //返回假时，表示已设置过密码
//        }else{
//            return true; //返回真时，表示未设置密码
//        }   //简化代码
        return !TextUtils.isEmpty(password);   //所以此处取反，颠倒真假值
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return names.length;//length是属性，不是方法，所以不用加（）
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
         View view=  View.inflate(HomeActivity.this,R.layout.list_item_home,null);
            ImageView iv_item= (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item= (TextView) view.findViewById(R.id.tv_item);
            tv_item.setText(names[position]);
            iv_item.setImageResource(ids[position]);
            return view;
        }
    }
}
