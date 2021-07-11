package com.edu.nuc.selftip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class toDoList extends AppCompatActivity {
    private ListView listView = null;//用来显示四个菜品信息的列表
    static SimpleAdapter mlistItemAdapter = null;//该适配器用于将数据源填充到列表视图中
    private ImageView todo_list_new=null;
    ImageView clear;
    ArrayList<Map<String, Object>> mfoodinfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        todo_list_new=findViewById(R.id.todo_list_new);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.todo_list_new:
                       Intent intent = new Intent(toDoList.this,NewSchedule.class);
                       startActivity(intent);
                }
            }
        };

        todo_list_new.setOnClickListener(onClickListener);

        listView=findViewById(R.id.todo_list);
        //mfoodinfo=getFoodData();//得到菜品列表数据源
        MyApplication myApplication=(MyApplication) getApplication();
        if(myApplication.g_dbAdapter.queryAllScheduleData()!=null){
        mfoodinfo= myApplication.g_dbAdapter.getSchedule();

        //构造适配器，将他和自定义的布局文件listitem以及数据源mfoodinfo关联起来
        mlistItemAdapter=new MyAdapter(this,mfoodinfo,
                R.layout.listitem,new String[]{"todoname","date","isdone","detail"},
                new int[]{R.id.Schedule_todoname,R.id.Schedule_date,R.id.Schedule_isdone,R.id.Schedule_detail});
        mlistItemAdapter.notifyDataSetChanged();//菜品数据发生改变通知适配器刷新数据
        listView.setAdapter(mlistItemAdapter);//显示菜品的列表采用mlistItemAdapter适配器
        //设置ListView选项的单击监听器
            }
    }

    private ArrayList<Map<String,Object>> getScheduleData(){
        ArrayList<Map<String,Object>> foodDatas = new ArrayList<Map<String, Object>>();
        //将菜品信息填充进foodinfo里
        //先来一个菜单对象
        MyApplication myApplication = (MyApplication) getApplication();
        Schedules dishs = myApplication.schedules;
        int s = dishs.mSchedules.size();
        for (int i = 0;i<s;i++){
            Schedule theDish = dishs.mSchedules.get(i);
            Map<String,Object> map = new HashMap<String, Object>();//用来存放每一道菜品四个信息键值对的
            //将菜品的四个信息以键值对的形式存放到map中
            map.put("todoname",theDish.todoname);
            map.put("date",theDish.date);
            map.put("isdone",theDish.isdone);
            map.put("detail",theDish.detail);
            //将存有菜品信息的map添加到ArrayList中
            foodDatas.add(map);
        }
        return foodDatas;
    }
    public class MyAdapter extends SimpleAdapter{
        MyApplication myApplication = MainActivity.myApplication;

        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            myApplication = MainActivity.myApplication;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            clear = view.findViewById(R.id.Schedule_clear);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myApplication.g_dbAdapter.delteOneschedule(position+1);
                    mfoodinfo.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(toDoList.this,"删除成功",Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }


    }

    @Override
    protected void onResume() {
        MyApplication myApplication=(MyApplication) getApplication();
        if(myApplication.g_dbAdapter.queryAllScheduleData()!=null){
            mfoodinfo= myApplication.g_dbAdapter.getSchedule();

            //构造适配器，将他和自定义的布局文件listitem以及数据源mfoodinfo关联起来
            mlistItemAdapter=new MyAdapter(this,mfoodinfo,
                    R.layout.listitem,new String[]{"todoname","date","isdone","detail"},
                    new int[]{R.id.Schedule_todoname,R.id.Schedule_date,R.id.Schedule_isdone,R.id.Schedule_detail});
            mlistItemAdapter.notifyDataSetChanged();//菜品数据发生改变通知适配器刷新数据
            listView.setAdapter(mlistItemAdapter);//显示菜品的列表采用mlistItemAdapter适配器
             }
        super.onResume();
    }
}