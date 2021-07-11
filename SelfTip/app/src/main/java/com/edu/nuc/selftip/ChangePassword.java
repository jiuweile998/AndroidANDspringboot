package com.edu.nuc.selftip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePassword extends AppCompatActivity {

    private EditText et_login_oldpassword=null;
    private EditText et_login_newpassword=null;
    private EditText et_login_newAffirmpassword=null;
    private TextView change_warning=null;
    private Button btn_change = null;

    String res;
    String newpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        MyApplication myApplication=(MyApplication)getApplication();

        et_login_oldpassword=findViewById(R.id.et_login_oldpassword);
        et_login_newpassword=findViewById(R.id.et_login_newpassword);
        et_login_newAffirmpassword=findViewById(R.id.et_login_newAffirmpassword);
        change_warning=findViewById(R.id.change_warning);
        btn_change = findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_login_oldpassword.getText().toString().trim().equals(myApplication.g_user.mPassword)){
                    change_warning.setText("原密码输入错误请重新输入！");
                    et_login_oldpassword.setText("");
                    et_login_oldpassword.setFocusable(true);//让密码框获得焦点
                    et_login_oldpassword.setFocusableInTouchMode(true);//百度一下
                    et_login_oldpassword.requestFocus();
                }else {
                    if (!et_login_newpassword.getText().toString().trim().equals(et_login_newAffirmpassword.getText().toString().trim())){
                        change_warning.setText("两次密码输入不一致请重新输入！");
                        et_login_newpassword.setFocusable(true);//让密码框获得焦点
                        et_login_newpassword.setFocusableInTouchMode(true);//百度一下
                        et_login_newpassword.requestFocus();
                    }else {
                        newpassword=et_login_newpassword.getText().toString().trim();
                        try {
                            if (jsoncall()){
                                Toast.makeText(ChangePassword.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private Boolean jsoncall() throws InterruptedException {
        Thread thread = new Thread(new MyThread());
        thread.start();
        thread.join();
        if(res.equals("success")){
            return true;
        }else{
            return false;
        }
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
           res = JsonPost.runJsonPost(newpassword,
                   MainActivity.myApplication.g_user.mUserId,
                   "http://"+MainActivity.myApplication.g_ip+"/changepsd");
        }
    }
}