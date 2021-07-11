package com.edu.nuc.selftip.User;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.edu.nuc.selftip.MyApplication;
import com.edu.nuc.selftip.Myadapter;
import com.edu.nuc.selftip.Schedule;
import com.edu.nuc.selftip.Schedules;

import java.util.ArrayList;
import java.util.Date;

public class DataService extends Service {
    public DataService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();

        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.g_dbAdapter = new Myadapter(this);
        myApplication.g_dbAdapter.open();//创建了存放菜品数据的数据库，并且表也创建出来
        myApplication.schedules=new Schedules();
        myApplication.g_dbAdapter.deleteAllschedule();

        myApplication.g_dbAdapter.fillScheduleTable(fillDisherList());
        myApplication.schedules.mSchedules =myApplication.g_dbAdapter.queryAllScheduleData();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class SericeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control",-1);
            MyApplication myApplication =(MyApplication) getApplication();
            switch (control){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:break;
            }
        }
    }

    private ArrayList<Schedule> fillDisherList() {

        ArrayList<Schedule> theDishesList = new ArrayList<Schedule>();

        //向菜品列表中添加第一道菜品
        Schedule theDish = new Schedule();
        theDish.id = 1;
        theDish.todoname= "创建第一个待办事项吧！";
        theDish.detail = "点击右上角创建第一个待办事项！";
        theDish.date = new Date();
        theDish.longitude = -1;
        theDish.latitude = -1;
        theDish.isdone=false;
        theDishesList.add(theDish);//把这道菜添加到菜品列表中

        return theDishesList;
    }

}