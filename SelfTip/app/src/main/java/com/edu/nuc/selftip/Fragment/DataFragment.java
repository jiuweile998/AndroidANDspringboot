package com.edu.nuc.selftip.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.edu.nuc.selftip.JsonPost;
import com.edu.nuc.selftip.MainActivity;
import com.edu.nuc.selftip.MyApplication;
import com.edu.nuc.selftip.MyData;
import com.edu.nuc.selftip.R;
import com.edu.nuc.selftip.StreamTool;
import com.edu.nuc.selftip.WebSocket;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class DataFragment extends Fragment {
    String ul;

    private TextView tv_week = null;
    private TextView tv_weekmsg = null;
    private ImageView iv_weather1 = null;
    private ImageView iv_weather2 = null;
    private ImageView iv_weather3 = null;
    private ImageView iv_weather4 = null;

    private TextView tv_temp1 =null;
    private TextView tv_temp2 =null;
    private TextView tv_temp3 =null;
    private TextView tv_temp4 =null;

    private TextView tv_thisweek1 =null;
    private TextView tv_thisweek2 =null;
    private TextView tv_thisweek3 =null;
    private TextView tv_thisweek4 =null;

    private VideoView videoView = null;

    private TableRow tr_msg_all=null;

    private  TextView tv_data_pulse;
    private  TextView tv_data_body_temp;
    private  TextView tv_data_blood_oxygen_concentration;

    MyApplication myApplication;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_data, container, false);
        tv_week = view1.findViewById(R.id.tv_week);
        tv_weekmsg = view1.findViewById(R.id.tv_weekmsg);
        iv_weather1 = view1.findViewById(R.id.iv_weather1);
        iv_weather2 = view1.findViewById(R.id.iv_weather2);
        iv_weather3 = view1.findViewById(R.id.iv_weather3);
        iv_weather4 = view1.findViewById(R.id.iv_weather4);
        tv_temp1 = view1.findViewById(R.id.tv_temp1);
        tv_temp2 = view1.findViewById(R.id.tv_temp2);
        tv_temp3 = view1.findViewById(R.id.tv_temp3);
        tv_temp4 = view1.findViewById(R.id.tv_temp4);
        tv_thisweek1 = view1.findViewById(R.id.tv_thisweek1);
        tv_thisweek2 = view1.findViewById(R.id.tv_thisweek2);
        tv_thisweek3 = view1.findViewById(R.id.tv_thisweek3);
        tv_thisweek4 = view1.findViewById(R.id.tv_thisweek4);
        videoView = view1.findViewById(R.id.videoView);

        tr_msg_all=view1.findViewById(R.id.tr_msg_all);

        tv_data_blood_oxygen_concentration=view1.findViewById(R.id.tv_data_blood_oxygen_concentration);
        tv_data_body_temp = view1.findViewById(R.id.tv_data_body_temp);
        tv_data_pulse=view1.findViewById(R.id.tv_data_pulse);

        tr_msg_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebSocket.class);
                startActivity(intent);
            }
        });

        myApplication=(MyApplication) getActivity().getApplication();

        MyData myData=new MyData();
        if(myApplication.getMyData()!=null){
        myData=myApplication.getMyData();
        if(myData.body_temp!=-1){
            tv_data_blood_oxygen_concentration.setText(myData.blood_oxygen_concentration);
            tv_data_body_temp.setText(myData.body_temp);
            tv_data_pulse.setText(myData.pulse);
        }}

        JSONArray jsonArray = JsonPost.getweather();
        if (jsonArray==null) {
            Toast.makeText(view1.getContext(), "error", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject day01 = jsonArray.getJSONObject(0);
                JSONObject day02 = jsonArray.getJSONObject(1);
                JSONObject day03 = jsonArray.getJSONObject(2);
                JSONObject day04 = jsonArray.getJSONObject(3);
                JSONObject day05 = jsonArray.getJSONObject(4);

                String type1 = day01.getString("type");
                String date1 = day01.getString("date");
                String type2 = day02.getString("type");
                String high2 = day02.getString("high");
                String low2 = day02.getString("low");
                String date2 = day02.getString("date");
                String type3 = day03.getString("type");
                String high3 = day03.getString("high");
                String low3 = day03.getString("low");
                String date3 = day03.getString("date");
                String type4 = day04.getString("type");
                String high4 = day04.getString("high");
                String low4 = day04.getString("low");
                String date4 = day04.getString("date");
                String type5 = day05.getString("type");
                String high5 = day05.getString("high");
                String low5 = day05.getString("low");
                String date5 = day05.getString("date");
                setbackground(type1);


                tv_weekmsg.setText(type1 + "");
                tv_week.setText(date1 + "");
                typeswitch(iv_weather1, type2);
                tv_temp1.setText(tempgo(high2, low2));
                tv_thisweek1.setText(date2);
                typeswitch(iv_weather2, type3);
                tv_temp2.setText(tempgo(high3, low3));
                tv_thisweek2.setText(date3);
                typeswitch(iv_weather3, type4);
                tv_temp3.setText(tempgo(high4, low4));
                tv_thisweek3.setText(date4);
                typeswitch(iv_weather4, type5);
                tv_temp4.setText(tempgo(high5, low5));
                tv_thisweek4.setText(date5);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view1;
    }
    private void setbackgroundwithtype(int tomany) {
        videoView.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + tomany));
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.tomany));
                videoView.start();
            }
        });
    }

    private void setblack() {
        tv_weekmsg.setTextColor(Color.BLACK);
        tv_week.setTextColor(Color.BLACK);
        tv_temp1.setTextColor(Color.BLACK);
        tv_thisweek1.setTextColor(Color.BLACK);
        tv_temp2.setTextColor(Color.BLACK);
        tv_thisweek2.setTextColor(Color.BLACK);
        tv_temp3.setTextColor(Color.BLACK);
        tv_thisweek3.setTextColor(Color.BLACK);
        tv_temp4.setTextColor(Color.BLACK);
        tv_thisweek4.setTextColor(Color.BLACK);
    }

    private void setbackground(String type){
        switch (type) {
            case "多云":
                setbackgroundwithtype(R.raw.tomany);
                setblack();
                break;
            case "阴":
                setbackgroundwithtype(R.raw.nosun);
                break;
            case "晴":
                setbackgroundwithtype(R.raw.sun);
                setblack();
                break;
            case "阵雨":
                setbackgroundwithtype(R.raw.shower);
                setblack();
                break;
            case "雷阵雨伴有冰雹":
                setbackgroundwithtype(R.raw.icefromwind);
                break;
            case "雷阵雨":
                setbackgroundwithtype(R.raw.thunderstrom);
                setblack();
                break;
            case "雨夹雪":
                setbackgroundwithtype(R.raw.rain);
                setblack();
                break;
            case "小雨":
            case "大暴雨-特大暴雨":
            case "暴雨-大暴雨":
            case "大雨-暴雨":
                setbackgroundwithtype(R.raw.rain2);
                setblack();
                break;
            case "中雨-大雨":
            case "小雨-中雨":
            case "冻雨":
            case "特大暴雨":
                setbackgroundwithtype(R.raw.rain3);
                setblack();
                break;
            case "暴雨":
            case "大雨":
            case "中雨":
                setbackgroundwithtype(R.raw.rain1);
                setblack();
                break;
            case "阵雪":
            case "小雪":
            case "小雪-中雪":
                setbackgroundwithtype(R.raw.snowing);
                break;
            case "中雪":
            case "中雪-大雪":
            case "大雪":
            case "暴雪":
            case "大雪-暴雪":
                setbackgroundwithtype(R.raw.bigsnow);
                break;
            case "雾":
                setbackgroundwithtype(R.raw.wu);
                setblack();
                break;
            case "浮尘":
            case "强沙尘暴":
            case "扬沙":
            case "霾":
                setbackgroundwithtype(R.raw.wumai);
                setblack();
                break;
            default:
                Log.e("weather", "error");
                setbackgroundwithtype(R.raw.wu);
                setblack();
                break;
        }
    }

    private void typeswitch(ImageView imageView,String type){
        switch (type) {
            case "多云":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather8));
                break;
            case "阴":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather6));
                break;
            case "晴":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather9));
                break;
            case "阵雨":
            case "雷阵雨":
            case "雷阵雨伴有冰雹":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather7));
                break;
            case "雨夹雪":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather4));
                break;
            case "小雨":
            case "大暴雨-特大暴雨":
            case "暴雨-大暴雨":
            case "大雨-暴雨":
            case "中雨-大雨":
            case "小雨-中雨":
            case "冻雨":
            case "特大暴雨":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather5));
                break;
            case "暴雨":
            case "大雨":
            case "中雨":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather5));
                break;
            case "阵雪":
            case "小雪":
            case "小雪-中雪":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather2));
                break;
            case "中雪":
            case "中雪-大雪":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather3));
                break;
            case "大雪":
            case "暴雪":
            case "大雪-暴雪":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather1));
                break;
            case "雾":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather11));
                setblack();
                break;
            case "浮尘":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather10));
                break;
            case "扬沙":
            case "霾":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather13));
                break;
            case "强沙尘暴":
                imageView.setBackground(getResources().getDrawable(R.drawable.weather12));
                break;
            default:
                Log.e("weather", "error");
                imageView.setBackground(getResources().getDrawable(R.drawable.weather12));
                break;
        }
    }

    private String tempgo(String high1,String low1){
        String str2="";
        if(high1!= null && !"".equals(high1)){
            for(int i=0;i<high1.length();i++){
                if(high1.charAt(i)>=48 && high1.charAt(i)<=57){
                    str2+=high1.charAt(i);
                }
            }

        }

        String str3="";
        if(low1!= null && !"".equals(low1)){
            for(int i=0;i<low1.length();i++){
                if(low1.charAt(i)>=48 && low1.charAt(i)<=57){
                    str3+=low1.charAt(i);
                }
            }

        }

        int currentTemphigh = Integer.parseInt(str2);
        int currentTemplow = Integer.parseInt(str3);
        return currentTemphigh+"°/"+currentTemplow +"°";
    }

}