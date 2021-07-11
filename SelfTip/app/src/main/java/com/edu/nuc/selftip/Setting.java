package com.edu.nuc.selftip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.List;

public class Setting extends AppCompatActivity {
    private ImageView setting_back=null;
    private TableRow tr_setting_changepassword=null;
    private TableRow tr_setting_changemessage=null;
    private TableRow tr_setting_call=null;
    private TableRow tr_setting_mysuggestion=null;
    private TableRow tr_setting_shareapp=null;
    private TableRow tr_setting_helpcenter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting_back = findViewById(R.id.setting_back);
        tr_setting_changepassword = findViewById(R.id.tr_setting_changepassword);
        tr_setting_changemessage = findViewById(R.id.tr_setting_changemessage);
        tr_setting_call = findViewById(R.id.tr_setting_call);
        tr_setting_mysuggestion = findViewById(R.id.tr_setting_mysuggestion);
        tr_setting_shareapp = findViewById(R.id.tr_setting_shareapp);
        tr_setting_helpcenter = findViewById(R.id.tr_setting_helpcenter);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.setting_back:
                        finish();
                        break;
                    case R.id.tr_setting_changepassword:
                        Intent intent=new Intent(Setting.this,ChangePassword.class);
                        startActivity(intent);
                        break;
                    case R.id.tr_setting_changemessage:
                        Intent intent1=new Intent(Setting.this,changemessage.class);
                        startActivity(intent1);
                        break;
                    case R.id.tr_setting_call:
                    case R.id.tr_setting_mysuggestion:
                        if(isQQClientAvailable(Setting.this)){
                            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=2802386554&version=1";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                        }else{
                            Toast.makeText(Setting.this,"请安装QQ客户端", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.tr_setting_shareapp:
                        if(isQQClientAvailable(Setting.this)){
                            final String qqUrl = "mqqwpa://im";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                        }else{
                            Toast.makeText(Setting.this,"请安装QQ客户端", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.tr_setting_helpcenter:
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cn.bing.com"));
                        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                        startActivity(it);
                        break;
                    default:break;
                }
            }
        };

        setting_back.setOnClickListener(onClickListener);
        tr_setting_changepassword.setOnClickListener(onClickListener);
        tr_setting_changemessage.setOnClickListener(onClickListener);
        tr_setting_call.setOnClickListener(onClickListener);
        tr_setting_mysuggestion.setOnClickListener(onClickListener);
        tr_setting_shareapp.setOnClickListener(onClickListener);
        tr_setting_helpcenter.setOnClickListener(onClickListener);
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}