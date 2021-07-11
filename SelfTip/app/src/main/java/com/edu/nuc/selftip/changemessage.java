package com.edu.nuc.selftip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.textclassifier.ConversationActions;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class changemessage extends AppCompatActivity {
    private ImageView changemessage_back = null;
    private TextView tv_changemessage_weight=null;
    private TextView tv_changemessage_height=null;
    private TextView tv_changemessage_bmi=null;
    private TextView tv_changemessage_blood=null;
    private EditText et_changemessage_weight=null;
    private EditText et_changemessage_height=null;
    private EditText et_changemessage_bmi=null;
    private EditText et_changemessage_blood=null;
    private TableRow tr_changemessage_weight_btn=null;
    private TableRow tr_changemessage_height_btn=null;
    private TableRow tr_changemessage_bmi_btn=null;
    private TableRow tr_changemessage_blood_btn=null;

    public static final int HAND_REQUEST_SUCCESS = 300;
    public static final int HAND_REQUEST_FAILURE = 400;

    String res;
    String changemessagename;
    String changemessage;

    String ul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changemessage);

        changemessage_back = findViewById(R.id.changemessage_back);
        tv_changemessage_weight = findViewById(R.id.tv_changemessage_weight);
        tv_changemessage_height = findViewById(R.id.tv_changemessage_height);
        tv_changemessage_bmi = findViewById(R.id.tv_changemessage_bmi);
        tv_changemessage_blood = findViewById(R.id.tv_changemessage_blood);
        et_changemessage_weight = findViewById(R.id.et_changemessage_weight);
        et_changemessage_height = findViewById(R.id.et_changemessage_height);
        et_changemessage_bmi = findViewById(R.id.et_changemessage_bmi);
        et_changemessage_blood = findViewById(R.id.et_changemessage_blood);
        tr_changemessage_weight_btn = findViewById(R.id.tr_changemessage_weight_btn);
        tr_changemessage_height_btn = findViewById(R.id.tr_changemessage_height_btn);
        tr_changemessage_bmi_btn = findViewById(R.id.tr_changemessage_bmi_btn);
        tr_changemessage_blood_btn = findViewById(R.id.tr_changemessage_blood_btn);

        MyApplication myApplication = (MyApplication)getApplication();
        tv_changemessage_weight.setText(myApplication.g_user.mUserWeight+"");
        tv_changemessage_height.setText(myApplication.g_user.mUserHeight+"");
        tv_changemessage_bmi.setText(myApplication.g_user.mUserBmi+"");
        tv_changemessage_blood.setText(myApplication.g_user.mUserBlood+"");
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                switch (v.getId()){
                    case R.id.changemessage_back:
                        finish();
                        break;
                    case  R.id.tr_changemessage_weight_btn:
                        changemessagename="mweight";
                        changemessage=et_changemessage_weight.getText().toString().trim();
                        if(jsoncall()){
                            Toast.makeText(changemessage.this,"修改成功",Toast.LENGTH_SHORT).show();
                            tv_changemessage_weight.setText(et_changemessage_weight.getText().toString().trim()+"");
                            myApplication.g_user.mUserWeight=Integer.parseInt(et_changemessage_weight.getText().toString().trim());
                        }
                        break;
                    case  R.id.tr_changemessage_height_btn:
                        changemessagename="mheight";
                        changemessage=et_changemessage_height.getText().toString().trim();
                        if(jsoncall()){
                        Toast.makeText(changemessage.this,"修改成功",Toast.LENGTH_SHORT).show();
                            tv_changemessage_height.setText(et_changemessage_height.getText().toString().trim()+"");
                            myApplication.g_user.mUserHeight=Integer.parseInt(et_changemessage_height.getText().toString().trim());
                    }
                        break;
                    case  R.id.tr_changemessage_bmi_btn:
                        changemessagename="mbmi";
                        changemessage=et_changemessage_bmi.getText().toString().trim();
                        if(jsoncall()){
                            Toast.makeText(changemessage.this,"修改成功",Toast.LENGTH_SHORT).show();
                            tv_changemessage_bmi.setText(et_changemessage_bmi.getText().toString().trim()+"");
                            myApplication.g_user.mUserBmi=Integer.parseInt(et_changemessage_bmi.getText().toString().trim());
                        }
                        break;
                    case  R.id.tr_changemessage_blood_btn:
                        changemessagename="mblood";
                        changemessage=et_changemessage_blood.getText().toString().trim();
                        if(jsoncall()){
                            Toast.makeText(changemessage.this,"修改成功",Toast.LENGTH_SHORT).show();
                            tv_changemessage_blood.setText(et_changemessage_blood.getText().toString().trim()+"");
                            myApplication.g_user.mUserBlood=et_changemessage_blood.getText().toString().trim();
                        }
                        break;
                }
            }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        changemessage_back.setOnClickListener(onClickListener);
        tr_changemessage_weight_btn.setOnClickListener(onClickListener);
        tr_changemessage_height_btn.setOnClickListener(onClickListener);
        tr_changemessage_bmi_btn.setOnClickListener(onClickListener);
        tr_changemessage_blood_btn.setOnClickListener(onClickListener);
    }

    private Boolean jsoncall() throws InterruptedException {
        Thread thread = new Thread(new MyThread());
        thread.start();
        thread.join();
        Log.i("res", res+"");
        if(res.equals("success")){
            return true;
        }else{
            return false;
        }
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            res = JsonPost.runJsonPost(changemessage,
                    changemessagename,
                    MainActivity.myApplication.g_user.mUserId,
                    "http://"+MainActivity.myApplication.g_ip+"/changemsg");
        }
    }
}