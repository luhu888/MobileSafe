package com.luhu.MobileSafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/6.
 */
public class GPSService extends Service {
    private LocationManager lm;
    private MyLocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyLocationListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = lm.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates("gps", 0, 0, listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    class MyLocationListener implements LocationListener{

        /**当位置发生改变的时候回调的方法
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            String longitude="经度："+ location.getLongitude();
            String latitude="纬度："+ location.getLatitude();
            String accuracy="精确度："+location.getAccuracy();
           //发短信给安全号码
            //把标准的GPS坐标转换成火星坐标
            InputStream is = null;
            try {
               is= getAssets().open("axisoffset.dat");
                ModifyOffset offset= ModifyOffset.getInstance(is);
                PointDouble double1=offset.s2c(new PointDouble(location.getLongitude(),location.getLatitude()));
                longitude="j:"+offset.X+"\n";
                latitude="w:"+offset.Y+"\n";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("lastlocation",longitude+latitude+accuracy);
            editor.commit();

        }

        /**当状态发生改变的时候回调，当开启到关闭，当关闭到开启
         * @param provider
         * @param status
         * @param extras
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**某一个位置提供者可使用时回调的方法
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**某一个位置提供者不可使用时回调的方法
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
