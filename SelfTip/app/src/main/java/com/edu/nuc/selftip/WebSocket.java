package com.edu.nuc.selftip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class WebSocket extends AppCompatActivity {
    private EditText et_web_subessage;
    private TableRow tr_web_button;
    private TextView tv_web_message;
    private JWebSocketClient client;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);

        et_web_subessage=findViewById(R.id.et_web_subessage);
        tr_web_button=findViewById(R.id.tr_web_button);
        tv_web_message=findViewById(R.id.tv_web_message);

        myApplication = (MyApplication) getApplication();
        URI uri = URI.create(myApplication.g_webs_ip+""+myApplication.g_user.mUserId);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                tv_web_message.setText(message);
                JSONObject jsonObject;
                try {
                    jsonObject=new JSONObject(message);
                    jsonObject.getJSONObject("params");
                    myApplication.myData.blood_oxygen_concentration=jsonObject.getInt("blood_oxygen_concentration");
                    myApplication.myData.body_temp=jsonObject.getInt("body_temp");
                    myApplication.myData.pulse=jsonObject.getInt("pulse");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tr_web_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (client != null && client.isOpen()) {
                    client.send(et_web_subessage.getText().toString().trim());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        URI uri = URI.create(myApplication.g_webs_ip+""+myApplication.g_user.mUserId);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                tv_web_message.setText(message);
            }
        };
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        closeConnect();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        closeConnect();
        super.onDestroy();

    }
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }
}