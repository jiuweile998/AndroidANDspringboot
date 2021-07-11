package com.edu.nuc.selftip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.edu.nuc.selftip.User.DataService;
import com.edu.nuc.selftip.User.WebServicePost;


public class LoginActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private EditText et_login_username = null;
    private EditText et_login_password = null;
    private EditText et_login_email = null;
    private EditText et_login_phone = null;
    private EditText et_login_Affirmpassword = null;
    private TextView tv_register = null;
    private TextView tv_login = null;
    private TextView tv_lossmyheart = null;
    private Button btn_login = null;
    private TableRow tr_email = null;
    private TableRow tr_phone = null;
    private TableRow tr_Affirmpassword =null;
    private TextView tv_top_login =null;
    private TableRow tr_register = null;
    private Button btn_register = null;
    private CheckBox cb_isHoldId = null;
    private ProgressDialog dialog;
    private static Handler handler = new Handler();
    private static String mUserFileName = "UserInfo";
    private VideoView videoView=null;

    private String info;

    @Override
    public void onBackPressed() {
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        if(grantResults.length == 0){
            return;
        }
        Log.i("AndroidLocation.REQUEST_CODE_LOCATION",321+"==="+requestCode);
        switch (requestCode) {
            case 321:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "授权位置信息 Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions((Activity)this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 321);
            }
        }

        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        et_login_email = findViewById(R.id.et_login_email);
        et_login_phone = findViewById(R.id.et_login_phone);
        et_login_Affirmpassword = findViewById(R.id.et_login_Affirmpassword);
        tv_register = findViewById(R.id.tv_register);
        tv_login = findViewById(R.id.tv_login);
        tv_lossmyheart = findViewById(R.id.tv_lossmyheart);
        btn_login = findViewById(R.id.btn_login);
        tr_email = findViewById(R.id.tr_email);
        tr_phone = findViewById(R.id.tr_phone);
        tr_Affirmpassword = findViewById(R.id.tr_Affirmpassword);
        tv_top_login = findViewById(R.id.tv_top_login);
        tr_register = findViewById(R.id.tr_register);
        btn_register = findViewById(R.id.btn_register);
        cb_isHoldId = findViewById(R.id.cb_isHoldId);
        videoView=findViewById(R.id.videoView);


        tr_email.setVisibility(tr_email.GONE);
        tr_phone.setVisibility(tr_phone.GONE);
        tr_register.setVisibility(tr_register.GONE);
        tr_Affirmpassword.setVisibility(tr_Affirmpassword.GONE);
        tv_login.setVisibility(tv_login.GONE);

        tv_register.setOnClickListener(buttonListener);
        tv_login.setOnClickListener(buttonListener);
        tv_lossmyheart.setOnClickListener(buttonListener);
        btn_login.setOnClickListener(buttonListener);
        btn_register.setOnClickListener(buttonListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login));
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
                videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login));
                videoView.start();
            }
        });
    }

        Button.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_register:
                        tr_email.setVisibility(tr_email.VISIBLE);
                        tr_phone.setVisibility(tr_phone.VISIBLE);
                        btn_login.setVisibility(btn_login.GONE);
                        tv_top_login.setText("您好，请注册");
                        tr_register.setVisibility(tr_register.VISIBLE);
                        tr_Affirmpassword.setVisibility(tr_Affirmpassword.VISIBLE);
                        tv_register.setVisibility(tv_register.GONE);
                        tv_login.setVisibility(tv_login.VISIBLE);
                        break;
                    case R.id.tv_lossmyheart:
                        break;
                    case R.id.tv_login:
                        tr_email.setVisibility(tr_email.GONE);
                        tr_phone.setVisibility(tr_phone.GONE);
                        btn_login.setVisibility(btn_login.VISIBLE);
                        tv_top_login.setText("您好，请登录");
                        tr_register.setVisibility(tr_register.GONE);
                        tr_Affirmpassword.setVisibility(tr_Affirmpassword.GONE);
                        tv_register.setVisibility(tv_register.VISIBLE);
                        tv_login.setVisibility(tv_login.GONE);
                        break;
                    case R.id.btn_login:
                        MyApplication myApplication = (MyApplication) getApplication();
                        // 检测网络，无法检测wifi
                        if (!checkNetwork()) {
                            Toast toast = Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        // 提示框
                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("正在登陆，请稍后...");
                        dialog.setCancelable(false);
                        dialog.show();
                        new Thread(new MyThread()).start();
                        break;
                    case R.id.btn_register:
                        String strPsword = et_login_password.getText().toString().trim();
                        String strAffirmPsword = et_login_Affirmpassword.getText().toString().trim();
                        if(strPsword.equals(strAffirmPsword)){
                            if (!checkNetwork()) {
                                Toast toast = Toast.makeText(LoginActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show(); }
                            dialog = new ProgressDialog(LoginActivity.this);
                            dialog.setTitle("提示");
                            dialog.setMessage("正在注册，请稍后...");
                            dialog.setCancelable(false);
                            dialog.show();
                            // 创建子线程，分别进行Get和Post传输
                            new Thread(new MyThread1()).start();
                        }else{
                            Toast.makeText(LoginActivity.this,"两次输入密码不一致!请重新输入",Toast.LENGTH_SHORT).show();
                            et_login_password.setText("");
                            et_login_Affirmpassword.setText("");
                            et_login_password.setFocusable(true);//让密码框获得焦点
                            et_login_password.setFocusableInTouchMode(true);//百度一下
                            et_login_password.requestFocus();
                        }
                        break;
                    default:
                        break;
                }
            }
        };


    public class MyThread implements Runnable {
        @Override
        public void run() {
            String info = WebServicePost.Login(et_login_username.getText().toString(), et_login_password.getText().toString(),MainActivity.myApplication.g_ip);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (info != null) {
                        String[] infomer = info.split("next");
                        if (infomer[0].equals("failed")) {
                            Toast.makeText(LoginActivity.this, "登录失败" + info, Toast.LENGTH_LONG).show();
                        } else if (infomer[0].equals("success")) {
                            if (cb_isHoldId.isChecked()) {
                                //保存用户名
                                SharedPreferences sharedPreferences1 = getSharedPreferences(mUserFileName, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putString("username", et_login_username.getText().toString().trim());
                                editor.commit();
                            } else {
                                //清除保存的用户名
                                SharedPreferences sharedPreferences2 = getSharedPreferences(mUserFileName, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putString("username", "");
                                editor.commit();
                            }
                            MyApplication myApplication = (MyApplication) getApplication();
                            myApplication.g_user.mIsLogined = true;
                            myApplication.g_user.mUserId = infomer[1];
                            myApplication.g_user.mUserName = et_login_username.getText().toString().trim();
                            myApplication.g_user.mPassword = et_login_password.getText().toString().trim();
                            myApplication.g_user.mUserEmail = infomer[4];
                            myApplication.g_user.mUserPhone = infomer[5];
                            if(!infomer[6].equals("null")){
                                myApplication.g_user.mUserWeight = Integer.parseInt(infomer[6]);
                            }
                            if(!infomer[7].equals("null")){
                                myApplication.g_user.mUserHeight = Integer.parseInt(infomer[7]);
                            }
                            if(!infomer[8].equals("null")){
                                myApplication.g_user.mUserBmi = Integer.parseInt(infomer[8]);
                            }
                            if (!infomer[9].equals("null")){
                                myApplication.g_user.mUserBlood = infomer[9];
                            }
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                            Intent serviceIntent = new Intent(LoginActivity.this, DataService.class);
                            startService(serviceIntent);

                            Intent serviceIntent1 = new Intent(LoginActivity.this, LocationService.class);
                            startService(serviceIntent1);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }

        private boolean checkNetwork() {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null) {
                return connManager.getActiveNetworkInfo().isAvailable();
            }
            return false;
        }

    public class MyThread1 implements Runnable {
        @Override
        public void run() {

            info = WebServicePost.Register(et_login_username.getText().toString(),
                    et_login_password.getText().toString(),
                    et_login_phone.getText().toString(),
                    et_login_email.getText().toString(),
                    MainActivity.myApplication.g_ip
            );
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(info.equals("failed")){
                        Toast.makeText(LoginActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else if(info.equals("success")){
                        Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });
        }

    }
}

