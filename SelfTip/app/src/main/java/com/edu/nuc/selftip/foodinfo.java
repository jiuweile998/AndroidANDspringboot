package com.edu.nuc.selftip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class foodinfo extends AppCompatActivity {
    private static int REQUEST_CODE = 0x0000bacc;
    private Button foodinfo_btn = null;
    private TextView tv_foodinfo = null;
    private String thepath ="https://www.mxnzp.com/api/barcode/goods/details";
    private String result;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_foodinfo);
        foodinfo_btn = findViewById(R.id.foodinfo_btn);
        foodinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodinfo.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        tv_foodinfo = findViewById(R.id.tv_foodinfo);
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        if (resultCode == RESULT_OK){
            if(data==null){
                Log.i("foodinfo","error");
                return;
            }else {
                Bundle bundle = data.getExtras();
                String ss="";
                for (String key: bundle.keySet())
                {
                    Object s=bundle.get(key);
                    Class c=s.getClass();
                    Log.i("Bundle Content", "Key=" + key + ", content=" +s.toString());
                }
                switch (bundle.get("SCAN_RESULT_FORMAT").toString()){
                    case "UPC_A":
                    case "UPC_E":
                    case "EAN_13":
                    case "EAN_8":
                    case "RSS_14":
                    case "RSS_EXPANDED":
                    case "CODE_39":
                    case "CODE_128":
                    case "ITF":
                    case "CODABAR":
                    case "AZTEC":
                    case "MAXICODE":
                        ss=ss+"扫到的码为一维码\n";
                        result=bundle.getString("SCAN_RESULT");
                        Thread thread = new Thread(new MyThread());
                        thread.start();
                        try {
                            thread.join();
                            JSONObject jsonObject=new JSONObject(res);
                            JSONObject dataobj= jsonObject.getJSONObject("data");
                            ss=ss +jsonObject.getString("msg")+"\n商品名称："+
                            dataobj.getString("goodsName")+"\n商品序列码："+
                                    dataobj.getString("barcode")+"\n预估价格："+
                                    dataobj.getString("price")+"\n品牌："+
                                    dataobj.getString("brand")+"\n厂商："+
                                    dataobj.getString("supplier")+"\n规格："+
                                    dataobj.getString("standard")
                            ;
                        } catch (JSONException |InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "DATA_MATRIX":
                    case "PDF_417":
                    case "QR_CODE":
                        ss=ss+"扫到的码为二维码\n"+"SCAN_RESULT_BYTES:"+bundle.getByte("SCAN_RESULT_BYTES")+"\n"+
                                "SCAN_RESULT_BYTE_SEGMENTS_0"+bundle.getByte("SCAN_RESULT_BYTE_SEGMENTS_0")+"\n"+
                                "SCAN_RESULT:"+bundle.getString("SCAN_RESULT");
                        ;
                        break;
                    default:break;
                }
                tv_foodinfo.setText(ss+"\n");
            }
        }
    }


    public class MyThread implements Runnable {
        @Override
        public void run() {
            res = JsonPost.runJsonPost(result, thepath);
        }
    }

}