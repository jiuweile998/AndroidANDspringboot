package com.edu.nuc.selftip;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.List;


public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "cn.edu.nuc.SelfTip.Weizhi";
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明

    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        intent.setComponent(new ComponentName("com.edu.nuc.selftip","com.edu.nuc.selftip.MyReceiver"));
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                //可选，设置定位模式，默认高精度
                //LocationMode.Hight_Accuracy：高精度；
                //LocationMode. Battery_Saving：低功耗；
                //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
                //可选，设置返回经纬度坐标类型，默认GCJ02
                //GCJ02：国测局坐标；
                //BD09ll：百度经纬度坐标；
                //BD09：百度墨卡托坐标；
                //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(10000);
                //可选，设置发起定位请求的间隔，int类型，单位ms
                //如果设置为0，则代表单次定位，即仅定位一次，默认为0
                //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
                //可选，设置是否使用gps，默认false
                //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
                //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
                //可选，定位SDK内部是一个service，并放到了独立进程。
                //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
                //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
                //可选，V7.2版本新增能力
                //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
                //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
                //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);
                //mLocationClient为第二步初始化过的LocationClient对象
                //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
                //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            intent.putExtra("Latitude", latitude);
            intent.putExtra("Longitude", longitude);
            intent.putExtra("Altitude", location.getAltitude());
            intent.putExtra("locality",location.getCity());
            sendBroadcast(intent);

        }
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}