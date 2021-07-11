package com.edu.nuc.selftip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.media.tv.TvView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AnalogClock;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewSchedule extends AppCompatActivity {
    private TextView tv_mydialog_time=null;
    private PopupWindow popupWindow = null;
    private Handler mHandler = new Handler();
    private CalendarView cv_user = null;
    private AnalogClock ac_mew_schedule = null;
    private ImageView new_schedule_back;
    private TableRow tv_mydialog_cancel;
    private TableRow tr_mydialog_submit;
    private EditText et_mydialog_todoname;
    private EditText et_mydialog_detail;
    private EditText et_mydialog_map;
    private TableRow tr_newschedule_search;
    private Marker marker;
    private com.baidu.mapapi.map.TextureMapView mMapView = null;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    int i=0;
    public double latitude=-1;
    public double lontitude=-1;
    Date date;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        SDKInitializer.initialize(getApplicationContext());

        tv_mydialog_time =findViewById(R.id.tv_mydialog_time);
        ac_mew_schedule=findViewById(R.id.ac_mew_schedule);
        new_schedule_back=findViewById(R.id.new_schedule_back);
        tv_mydialog_cancel = findViewById(R.id.tv_mydialog_cancel);
        et_mydialog_todoname = findViewById(R.id.et_mydialog_todoname);
        et_mydialog_detail = findViewById(R.id.et_mydialog_detail);
        et_mydialog_map = findViewById(R.id.et_mydialog_map);
        tr_mydialog_submit = findViewById(R.id.tr_mydialog_submit);
        tr_newschedule_search = findViewById(R.id.tr_newschedule_search);

        MyApplication myApplication=(MyApplication)getApplication();



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss获取当前时间
        date = new Date(System.currentTimeMillis());
        tv_mydialog_time.setText(""+simpleDateFormat.format(date));

        
        View.OnClickListener onClickListener=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ac_mew_schedule:
                        View layout = getLayoutInflater().inflate(R.layout.window_popu, null);
                        final LinearLayout linearLayout = layout.findViewById(R.id.ll_time);
                        cv_user = layout.findViewById(R.id.cv_user);
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //添加退场动画
                                closeAnimation(linearLayout);
                            }
                        });
                        cv_user.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                month=month+1;
                                tv_mydialog_time.setText(year+"年"+month+"月"+dayOfMonth+"日");
                                String str=year+"-"+month+"-"+dayOfMonth;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
                                try {
                                    date = sdf.parse(str);
                                } catch (ParseException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        });
                        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        popupWindow.setFocusable(true);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        //添加进场动画
                        showAnimation(linearLayout);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setFocusable(false);
                        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        break;
                    case R.id.tv_mydialog_cancel:
                    case R.id.new_schedule_back:
                        finish();
                    break;
                    case R.id.tr_newschedule_search:
                        View layout1 = getLayoutInflater().inflate(R.layout.map_popu, null);
                        final LinearLayout linearLayout1 = layout1.findViewById(R.id.ll_map_popu);
                        mMapView = layout1.findViewById(R.id.map_popu);
                        mBaiduMap = mMapView.getMap();

                        LatLng cenpt = new LatLng(myApplication.Latitude,myApplication.Longitude);
                        //定义地图状态
                        MapStatus mMapStatus = new MapStatus.Builder()
                                .target(cenpt)
                                .zoom(18)
                                .build();
                        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                        //改变地图状态
                        mBaiduMap.setMapStatus(mMapStatusUpdate);
                        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                if(i!=0){
                                    marker.remove();
                                }else {
                                    i=i+1;
                                }
                                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.point);
                                //准备 marker option 添加 marker 使用
                                MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(latLng);
                                //获取添加的 marker 这样便于后续的操作
                                marker = (Marker) mBaiduMap.addOverlay(markerOptions);

                                GeoCoder mSearch = GeoCoder.newInstance();
                                mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                                    @Override
                                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                                    }

                                    @Override
                                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                                        Log.i("test","打印转换后的地址" + reverseGeoCodeResult.getAddress());
                                        et_mydialog_map.setText(reverseGeoCodeResult.getAddress());
                                    }
                                });
                                //下面是传入对应的经纬度
                                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
                                latitude= latLng.latitude;
                                lontitude = latLng.longitude;
                            }

                            @Override
                            public void onMapPoiClick(MapPoi mapPoi) {

                            }
                        });
                        linearLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //添加退场动画
                                closeAnimation(linearLayout1);
                                mMapView.onPause();
                            }
                        });
                        popupWindow = new PopupWindow(layout1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        popupWindow.setFocusable(true);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        //添加进场动画
                        showAnimation(linearLayout1);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setFocusable(false);
                        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        break;
                    case R.id.tr_mydialog_submit:
                        if (latitude==-1){
                            GeoPoint geoPoint = getGeoPointBystr(et_mydialog_map.getText().toString().trim());
                            latitude=geoPoint.getLatitude();
                            lontitude=geoPoint.getLongitude();
                        }
                        Schedule schedule = new Schedule();
                        schedule.todoname = et_mydialog_todoname.getText().toString().trim();
                        schedule.isdone=false;
                        schedule.latitude=latitude;
                        schedule.longitude=lontitude;
                        schedule.date=date;
                        schedule.detail=et_mydialog_detail.getText().toString().trim();
                        if(myApplication.g_dbAdapter.queryAllScheduleData()!=null) {
                        List<Schedule> list=myApplication.g_dbAdapter.queryAllScheduleData();
                        Schedule schedule1=list.get(list.size()-1);
                        schedule.id=schedule1.id+1;
                        }
                        else {
                            schedule.id=1;
                        }
                        myApplication.g_dbAdapter.insert(schedule);
                        Toast.makeText(NewSchedule.this,"创建成功",Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };

        tv_mydialog_time.setOnClickListener(onClickListener);
        ac_mew_schedule.setOnClickListener(onClickListener);
        new_schedule_back.setOnClickListener(onClickListener);
        tv_mydialog_cancel.setOnClickListener(onClickListener);
        tr_mydialog_submit.setOnClickListener(onClickListener);
        tr_newschedule_search.setOnClickListener(onClickListener);
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 800, 200);
                    fadeAnim.setDuration(800);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(800);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }
    }

    private void closeAnimation(ViewGroup layout) {
        ArrayList<View> viewArrayList = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            viewArrayList.add(layout.getChildAt(i));
        }
        Collections.reverse(viewArrayList);
        for (int i = 0; i < viewArrayList.size(); i++) {
            final View child = viewArrayList.get(i);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 200, -900);
                    fadeAnim.setDuration(800);
                    fadeAnim.start();
                    fadeAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                            popupWindow.dismiss();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            }, i * 50);
        }
    }

    public GeoPoint getGeoPointBystr(String str) {//GeoPoint来自谷歌
        GeoPoint gpGeoPoint = null;
        if (str!=null) {
            Geocoder gc = new Geocoder(this, Locale.CHINA);
            List<Address> addressList = null;
            try {
                addressList = gc.getFromLocationName(str, 1);
                Log.i("tag","addressList=="+addressList.size());
                if (!addressList.isEmpty()) {
                    Address address_temp = addressList.get(0);
                    //计算经纬度
                    double Latitude=address_temp.getLatitude()*1E6;
                    double Longitude=address_temp.getLongitude()*1E6;
                    //生产GeoPoint
                    gpGeoPoint = new GeoPoint((int)Longitude, (int)Latitude);
                }
                else{
                    Toast.makeText(this,"找不到地址位置",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"输入地址为空" ,Toast.LENGTH_LONG).show();
        }
        return gpGeoPoint;
    }
}