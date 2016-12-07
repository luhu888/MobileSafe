package com.luhu.MobileSafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.luhu.MobileSafe.utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";
    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATA_DIALOG = 1;
    private static final int URL_ERROR = 2;
    private static final int NETWORK_ERROR = 3;
    private static final int JSON_ERROR = 4;
    private TextView tv_version;
    private String description;
    /**
     * 新版本的下载地址
     */
    private String apkurl;
    private String version;
    private TextView tv_update_info;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本号" + getVersionName());
        tv_update_info= (TextView) findViewById(R.id.tv_update_info);
        boolean update=sp.getBoolean("update",false);
        if (update){
            checkUpdate();//检查升级
        }else{
            //自己已经关闭自动升级
            handler.postDelayed(new Runnable() {    //延时两秒进入主页面
                @Override
                public void run() {
                    //进入主页面
                    enterHome();
                }
            },2000);
        }


        AlphaAnimation aa=new AlphaAnimation(0.2f,1.0f);//启动动画，透明度从0.2到1
        aa.setDuration(1000);  //设置动画的播放时间500ms
        findViewById(R.id.rl_root_start).startAnimation(aa);



    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATA_DIALOG://显示升级的对话框
                    Log.i(TAG, "显示升级的对话框");
                    showUpdateDialog();
                    break;
                case URL_ERROR://URL错误
                    enterHome();
                    Toast.makeText(getApplicationContext(), "URL错误", Toast.LENGTH_SHORT).show();
                    break;
                case ENTER_HOME://进入主页面
                    enterHome();
                    break;
                case JSON_ERROR://Json解析错误
                    enterHome();
                    Toast.makeText(MainActivity.this, "Json解析错误", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR://网络异常
                    enterHome();
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }


    };



    /**
     * 检查是否有新版本，如果有，就升级
     */
    private void checkUpdate() {
        new Thread() {
            public void run() {
                //URL:http://192.168.1.105:8080/updata.html
                Message mes = Message.obtain();
                long startTime=System.currentTimeMillis();
                try {
                    URL url = new URL("http://192.168.1.105:8080/update.html");
                    //联网
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //响应码为200即联网成功
                        InputStream is = conn.getInputStream();
                        //把流转换成字符串
                        String result = StreamTools.readFromStream(is);

                        Log.i(TAG, "联网成功" + result);
                        //json解析
                        JSONObject obj = new JSONObject(result);
                        //得到服务器的版本信息
                        version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");
                        //校验是否有新版本
                        if (getVersionName().equals(version)) {

                            //版本一致，进入主页面
                            mes.what = ENTER_HOME;
                        } else {
                            //版本不一致，弹出升级对话框
                            mes.what = SHOW_UPDATA_DIALOG;
                        }

                    }

                } catch (MalformedURLException e) {
                    mes.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    mes.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    mes.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime=System.currentTimeMillis();
                    long dTime=endTime-startTime;
                    if(dTime<2000){
                        try {
                            Thread.sleep(2000-dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(mes);
                }
            }
        }.start();
    }
    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示升级");
       // builder.setCancelable(false);//设置返回键以及触摸其他地方不可取消对话框
        //可作为强制升级的方法，只设置一个按钮，只有点升级才能用
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override   //创建监听器，触摸其他部分时即即取消升级，进入主页面
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setMessage(description);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk，并且替换安装
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //SD卡存在
                    FinalHttp finalhttp=new FinalHttp();
                    finalhttp.download(apkurl, Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/mobilesafe2.0.apk", new AjaxCallBack<File>() {
                        @Override
                        public void onSuccess(File t) {
                            super.onSuccess(t);
                            installAPK(t);
                        }

                        /**安装APK
                         * @param t
                         */
                        private void installAPK(File t) {
                            Intent intent=new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.fromFile(t),"application/vnd.android.package-archive");
                            startActivity(intent);
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                            tv_update_info.setVisibility(View.VISIBLE);
                            int progress= (int) (current*100/count);   //当前下载百分比
                            tv_update_info.setText("下载进度："+progress+"%");
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            t.printStackTrace();
                            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                            super.onFailure(t, errorNo, strMsg);
                        }
                    });
            }else{
                Toast.makeText(MainActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//对话框消失，进入主页面
                enterHome();
            }
        });
        builder.show();

    }
    protected void enterHome() {
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();//关闭当前页面
    }


    /**
     * 得到应用的版本名称
     *
     * @return
     */
    private String getVersionName() {

        //packageManager用来管理手机的apk
        PackageManager pm = getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }
}

