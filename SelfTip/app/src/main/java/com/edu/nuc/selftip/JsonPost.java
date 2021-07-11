package com.edu.nuc.selftip;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

public class JsonPost {

    private static String app_id = "vctjuritjhzmknfi";
    private static String app_secret = "bTRsbVdCMFFEOHpwYnI5eGR6Z0tZdz09";

    public static String runJsonPost(String changemessage,String changemessagename,String changemessageid,String thepath){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("changemessage", changemessage);
            jsonObject.put("changemessagename", changemessagename);
            jsonObject.put("changemessageid", changemessageid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JsonPost(jsonObject,thepath);
    }

    public static String runJsonPost(String newpassword,String changemessageid,String thepath){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("newpassword",newpassword);
            jsonObject.put("changepsdid",MainActivity.myApplication.g_user.mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JsonPost(jsonObject,thepath);
    }

    public static String runJsonPost(String barcode,String thepath){
        thepath=thepath+"?barcode="+barcode+"&app_id="+app_id+"&app_secret="+app_secret;
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("barcode",barcode);
            jsonObject.put("app_id",app_id);
            jsonObject.put("app_secret",app_secret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JsonPost(jsonObject,thepath);
    }

    public static String JsonPost(JSONObject jsonObject,String thePath){
        BufferedReader reader = null;
        String res = null;
        try {
            URL url = new URL(thePath);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            //设置发送数据长度（用于发送大量数据使用）
            connection.setRequestProperty("accept", "application/json");
            //一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            Log.i("scan",jsonObject.toString());
            out.append(jsonObject.toString());
            out.flush();
            out.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 读取响应
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    res = line;
                }
                Log.i("res", res + "");
                reader.close();
            } else {
                res = "请求错误," + connection.getResponseCode();
                Log.i("res", res + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
            res = "请求异常,请检查网络";
            Log.e("POST IOException", e + "");
        }
        return res;
    }

    public static JSONArray getweather() {
        MyApplication myApplication = (MyApplication)MainActivity.myApplication;
        Log.i("address",myApplication.locality);
        if (TextUtils.isEmpty(myApplication.locality)) {
            return null;
        }
        try {
            String ul = myApplication.g_weather_ip + URLEncoder.encode(myApplication.locality, "UTF-8");

            URL url = new URL(ul);

            //设置必要的参数信息
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            //判断响应码
            int code = conn.getResponseCode();

            if (code == 200) {
                //连接网络成功
                InputStream in = conn.getInputStream();
                String data = StreamTool.decodeStream(in);


                //解析json格式的数据
                JSONObject jsonObj = new JSONObject(data);
                //获得desc的值
                String result = jsonObj.getString("desc");
                if ("OK".equals(result)) {
                    //城市有效，返回了需要的数据
                    JSONObject dataObj = jsonObj.getJSONObject("data");

                    JSONArray jsonArray = dataObj.getJSONArray("forecast");
                    return jsonArray;
                } else {
                    //城市无效
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
