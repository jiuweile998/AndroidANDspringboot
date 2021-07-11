package com.edu.nuc.selftip;
import android.app.Application;
import android.content.Context;


/*
 * 在APP运行期间能够使用用户的类，共享数据，数据缓存，数据传递的作用
 */
public class MyApplication extends Application {//用于保存全局变量
    MyUser g_user;//用户
    public String g_ip = "192.168.43.184:8080";//用TCP通信时店面服务器地址
    public String g_http_ip = "";//用HTTP通信时店面的IP地址
    public  int g_communiMode = 1;//通信模式，1表示TCP通信，2表示Http通信
    public int g_objPort = 35885;
    public String locality="北京";
    public double Latitude=40.0;
    public double Altitude=40.0;
    public double Longitude=100.0;
    public String g_weather_ip = "http://wthrcdn.etouch.cn/weather_mini?city=";
    public String g_webs_ip = "ws://"+g_ip+"/websocket/";
    public Schedules schedules;
    public MyData myData;

    Context g_context;

    public Myadapter g_dbAdapter=null;//操作数据库的工具类对象

    public String getusername() {
        return g_user.mUserName;
    }
    public int getuserweight() {
        return g_user.mUserWeight;
    }
    public int getuserheight() {
        return g_user.mUserHeight;
    }
    public int getuserbmi() {
        return g_user.mUserBmi;
    }
    public String getuserblood() {
        return g_user.mUserBlood;
    }

    public MyData getMyData() {
        return myData;
    }
}
