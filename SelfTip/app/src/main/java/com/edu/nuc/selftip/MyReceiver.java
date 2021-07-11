package com.edu.nuc.selftip;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyReceiver extends BroadcastReceiver {

    public static final String BROADCAST_ACTION = "cn.edu.nuc.SelfTip.Weizhi";
    private static final String BROADECAST_UNLOGINED = "cn.edu.nuc.SelfTip.UNLOGINED";
    private static final String BROADECAST_LOGINED = "cn.edu.nuc.SelfTip.Logined";
    private static final String BROADECAST_LOGOUT = "cn.edu.nuc.SelfTip.Logout";
    private static final String BROADECAST_USERORPSDEOR = "cn.edu.nuc.SelfTip.UserOrPsdEor";

    private static String mUserFileName = "UserInfo";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().equals(BROADECAST_UNLOGINED)){
            Intent intent1 = new Intent(context,LoginActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

        if(intent.getAction().equals(BROADECAST_LOGOUT)){
            Intent intent1 = new Intent(context,LoginActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        if(intent.getAction().equals(BROADECAST_USERORPSDEOR)){
            Toast.makeText(context,"用户名或密码输入错误,请重新输入!",Toast.LENGTH_LONG).show();
        }
        if(intent.getAction().equals(BROADECAST_LOGINED)){
            String username = intent.getStringExtra("username");
            boolean isholduserid = intent.getBooleanExtra("isholduserid",false);
            SharedPreferences sharedPreferences = context.getSharedPreferences(mUserFileName, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username",username);
            editor.putBoolean("isholduserid",isholduserid);
            editor.commit();
        }
        if(intent.getAction().equals(BROADCAST_ACTION)) {
            double latitude = intent.getDoubleExtra("Latitude", -1);
            double altitude = intent.getDoubleExtra("Latitude", -1);
            double longitude = intent.getDoubleExtra("Longitude", -1);
            String locality = intent.getStringExtra("locality");
            MainActivity.myApplication.Latitude = latitude;
            MainActivity.myApplication.Altitude = altitude;
            MainActivity.myApplication.Longitude = longitude;
            Log.i("address", "" + latitude);
            Log.i("address", "" + altitude);
            Log.i("address", "" + longitude);
            if (locality != null) {
                MainActivity.myApplication.locality = locality;
            }
        }
    }
}