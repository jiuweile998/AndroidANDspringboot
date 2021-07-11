package com.edu.nuc.selftip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.edu.nuc.selftip.User.DataService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    static MyApplication myApplication = null;

    //定义SharedPreferences数据文件名称
    private static String mUserFileName = "UserInfo";

    //定义一个文件访问工具类对象
    private DataFileAccess mDataFileAccess = new DataFileAccess(MainActivity.this);

    private static final String BROADECAST_UNLOGINED = "cn.edu.nuc.SelfTip.UNLOGINED";
    private static final String BROADECAST_LOGINED = "cn.edu.nuc.SelfTip.Logined";
    private static final String BROADECAST_LOGOUT = "cn.edu.nuc.SelfTip.Logout";
    private static final String BROADECAST_USERORPSDEOR = "cn.edu.nuc.SelfTip.UserOrPsdEor";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        myApplication = (MyApplication)getApplication();//获得全局变量对象
        myApplication.g_context = getApplicationContext();
        myApplication.g_user = mDataFileAccess.readUserInfoFromFile("userinfo.txt");
        if(myApplication.g_user==null){
            myApplication.g_user = new MyUser();//如果读取后为空的话，重新创建用户
        }
        if(myApplication.g_user.mIsLogined.equals(false)){
            Intent intent = new Intent(BROADECAST_UNLOGINED);//将用户未登录状态封装到intent中
            intent.setComponent(new ComponentName("com.edu.nuc.selftip","com.edu.nuc.selftip.MyReceiver"));
            sendBroadcast(intent);//将用户未登录状态广播出去
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_Maps, R.id.navigation_data, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
