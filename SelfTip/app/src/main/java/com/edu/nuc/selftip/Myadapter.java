package com.edu.nuc.selftip;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.edu.nuc.selftip.MyData;
import com.edu.nuc.selftip.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Myadapter {

    private static final String DB_NAME="selftip.db"; //数据库名字
    private static final String DB_TABLE="selftipinfo"; //数据表名字
    private static final String DB_TABLE2="scheduleinfo"; //数据表名字
    private static final int DB_VERSION=1; //数据库版本号
    private static final String KEY_ID="_id"; //id字段名称
    private static final String PULSE="pulse"; //脉搏
    private static final String ALTITUDE="altitude"; //
    private static final String ALONGITUDE="alongitude"; //
    private static final String BLOOD_OXYGEN_CONCENTRATION="blood_oxygen_concentration"; //血痒浓度
    private static final String WEAREORNOT="wearornot"; //时间
    private static final String BODY_TEMP="body_temp"; //时间
    private static final String DATE="date"; //时间
    private static final String TODONAME="todoname"; //时间
    private static final String DETAIL="detail"; //时间
    private static final String LATITUDE="latitude"; //时间
    private static final String LONGITUDE="longitude"; //时间
    private static final String ISDONE="isdone"; //时间

    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelper dbOpenHelper;

    public Myadapter(Context context){
        this.context=context;
    }

    //    关闭数据库
    public void close(){
        if (db!=null){
            db.close();
        }
    }

    //    创建并打开数据库
    public void open() throws SQLiteException {
        dbOpenHelper=new DBOpenHelper(context,DB_NAME,null,DB_VERSION);
        try {
            db=dbOpenHelper.getWritableDatabase();
        }catch (SQLException ex){
            db=dbOpenHelper.getReadableDatabase();
        }
    }

    //    删除数据库
    public void delete() throws SQLiteException{
        db.deleteDatabase(new File(DB_NAME));
    }



    //    创建数据表
    public void create_table(String createTableSql) throws SQLiteException{
        db.execSQL(createTableSql);
    }

    //    删除数据表
    public void delete_table(String tableName) throws SQLiteException{
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
    }

    //    插入菜品记录
    public long insert(MyData data){
        ContentValues newValues=new ContentValues();//以键值对形式封装数据到ContentValues对象
        //newValues.put(KEY_ID,data.id);
        newValues.put(PULSE,data.pulse);
        newValues.put(BLOOD_OXYGEN_CONCENTRATION,data.blood_oxygen_concentration);
        newValues.put(ALTITUDE,data.altitude);
        newValues.put(WEAREORNOT,data.wearornot);
        newValues.put(ALONGITUDE,data.alongitude);
        newValues.put(BODY_TEMP,data.body_temp);

        return  db.insert(DB_TABLE,null,newValues);
    }

    //    插入菜品记录
    public long insert(Schedule schedule){
        ContentValues newValues=new ContentValues();//以键值对形式封装数据到ContentValues对象
        newValues.put(KEY_ID,schedule.id);
        newValues.put(DATE,schedule.date.getTime());
        newValues.put(TODONAME,schedule.todoname);
        newValues.put(DETAIL,schedule.detail);
        newValues.put(LATITUDE,schedule.latitude);
        newValues.put(LONGITUDE,schedule.longitude);
        newValues.put(ISDONE,schedule.isdone);

        return  db.insert(DB_TABLE2,null,newValues);
    }

    //    删除所有记录
    public long deleteAllData(){
        return db.delete(DB_TABLE,null,null);
    }
    public long deleteAllschedule(){
        return db.delete(DB_TABLE2,null,null);
    }

    //    根据ID删除单条记录
    public long delteOneData(long id){
        return db.delete(DB_TABLE,KEY_ID+" = "+id,null);
    }
    public long delteOneschedule(long id){
        return db.delete(DB_TABLE2,KEY_ID+" = "+id,null);
    }

    //    根据ID修改一条记录
    public long updateOneData(long id,MyData data){
        ContentValues updateValues=new ContentValues();
        updateValues.put(KEY_ID,data.id);
        updateValues.put(PULSE,data.pulse);
        updateValues.put(BLOOD_OXYGEN_CONCENTRATION,data.blood_oxygen_concentration);
        updateValues.put(ALTITUDE,data.altitude);
        updateValues.put(WEAREORNOT,data.wearornot);
        updateValues.put(ALONGITUDE,data.alongitude);
        updateValues.put(BODY_TEMP,data.body_temp);

        return db.update(DB_TABLE,updateValues,KEY_ID+" = "+id,null);

    }

    //    根据ID修改一条记录
    public long updateOneData(long id,Schedule schedule){
        ContentValues updateValues=new ContentValues();
        updateValues.put(KEY_ID,schedule.id);
        updateValues.put(DATE,schedule.date.getTime());
        updateValues.put(TODONAME,schedule.todoname);
        updateValues.put(DETAIL,schedule.detail);
        updateValues.put(LATITUDE,schedule.latitude);
        updateValues.put(LONGITUDE,schedule.longitude);
        updateValues.put(ISDONE,schedule.isdone);;

        return db.update(DB_TABLE2,updateValues,KEY_ID+" = "+id,null);

    }
    //    查询所有记录
    public ArrayList<MyData> queryAllData(){
        Cursor results=db.query(DB_TABLE,
                new String[]{KEY_ID,PULSE,BLOOD_OXYGEN_CONCENTRATION,ALTITUDE,WEAREORNOT,ALONGITUDE,BODY_TEMP}, //要查询的字段
                null,//查询条件
                null,//该数组用于替换查询条件中的通配符“？”
                null, //查询结果按指定的字段分组
                null,// 限定分组条件
                null//查询结果的排序条件
        );

        //将查询你结果集转换成MyData
        return convertToData(results);

    }
    //根据ID查询一条记录
    public  ArrayList<MyData> queryOneData(long id){
        Cursor results=db.query(DB_TABLE,
                new String[]{KEY_ID,PULSE,BLOOD_OXYGEN_CONCENTRATION,ALTITUDE,WEAREORNOT,ALONGITUDE,BODY_TEMP}, //要查询的字段
                KEY_ID+" = "+id,//查询条件
                null,//该数组用于替换查询条件中的通配符“？”
                null,//查询结果按指定的字段分组
                null, // 限定分组条件
                null,//查询结果的排序条件
                null
        );
        return convertToData(results);
    }

    public ArrayList<MyData> convertToData(Cursor cursor){
        //        将查询的结果集转换成People
        int resultCount=cursor.getCount(); //求出结果集中的记录条数
        if (resultCount==0||!cursor.moveToFirst()){
            return null;
        }else {
            ArrayList<MyData> dataArrayList = new ArrayList<MyData>();
            for (int i = 0; i < resultCount; i++) {
                MyData data = new MyData();
                data.id = cursor.getInt(0);
                data.pulse = cursor.getInt(cursor.getColumnIndex(PULSE));
                data.blood_oxygen_concentration = cursor.getInt(cursor.getColumnIndex(BLOOD_OXYGEN_CONCENTRATION));
                data.altitude= cursor.getDouble(cursor.getColumnIndex(ALTITUDE));
                data.wearornot= cursor.getInt(cursor.getColumnIndex(WEAREORNOT));
                data.alongitude= cursor.getDouble(cursor.getColumnIndex(ALONGITUDE));
                data.body_temp = cursor.getInt(cursor.getColumnIndex(BODY_TEMP));
                dataArrayList.add(data);//将dish对象存放到dishArrayList中
                cursor.moveToNext();//游标指向下一条记录
            }
            return dataArrayList;
        }
    }

    public ArrayList<Schedule> queryAllScheduleData() {
        Cursor results = db.query(DB_TABLE2,
                new String[]{KEY_ID,DATE,TODONAME,DETAIL,LATITUDE,LONGITUDE,ISDONE}, //要查询的字段
                null,//查询条件
                null,//该数组用于替换查询条件中的通配符“？”
                null, //查询结果按指定的字段分组
                null,// 限定分组条件
                null//查询结果的排序条件
        );

        //将查询你结果集转换成MyData
        return convertitToData(results);
    }

    public  ArrayList<Schedule> queryOneScheduleData(long id){
        Cursor results=db.query(DB_TABLE2,
                new String[]{KEY_ID,DATE,TODONAME,DETAIL,LATITUDE,LONGITUDE,ISDONE}, //要查询的字段
                KEY_ID+" = "+id,//查询条件
                null,//该数组用于替换查询条件中的通配符“？”
                null,//查询结果按指定的字段分组
                null, // 限定分组条件
                null,//查询结果的排序条件
                null
        );
        return convertitToData(results);
    }

    public ArrayList<Schedule> convertitToData(Cursor cursor){
        //        将查询的结果集转换成People
        int resultCount=cursor.getCount(); //求出结果集中的记录条数
        if (resultCount==0||!cursor.moveToFirst()){
            return null;
        }else {
            ArrayList<Schedule> scheduleArrayList = new ArrayList<Schedule>();
            for (int i = 0; i < resultCount; i++) {
                Schedule schedule = new Schedule();
                schedule.id = cursor.getInt(0);
                schedule.date=new Date(cursor.getInt(cursor.getColumnIndex(DATE))) ;
                schedule.todoname = cursor.getString(cursor.getColumnIndex(TODONAME));
                schedule.detail= cursor.getString(cursor.getColumnIndex(DETAIL));
                schedule.latitude= cursor.getInt(cursor.getColumnIndex(LATITUDE));
                schedule.longitude= cursor.getDouble(cursor.getColumnIndex(LONGITUDE));
                schedule.isdone = cursor.getExtras().getBoolean(ISDONE);
                scheduleArrayList.add(schedule);//将dish对象存放到dishArrayList中
                cursor.moveToNext();//游标指向下一条记录
            }
            return scheduleArrayList;
        }
    }



    public static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE="create table "+DB_TABLE
                +"("+KEY_ID+" integer primary key autoincrement,"
                +PULSE+" integer not null,"
                +BLOOD_OXYGEN_CONCENTRATION+" integer, " +
                ALTITUDE+" real, "+
                WEAREORNOT+" integer, "+
                ALONGITUDE+" real, " +
                BODY_TEMP + " integer" +
                ");";
        private static final String DB_CREATE2="create table "+DB_TABLE2
                +"("+KEY_ID+" integer primary key ,"
                +DATE+ " integer not null,"
                +TODONAME+" char, " +
                DETAIL+" char, "+
                LATITUDE+" float, "+
                LONGITUDE+" float, " +
                ISDONE + " numeric" +
                ");";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            db.execSQL(DB_CREATE2);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE2);
            onCreate(db);
        }
    }

    //将菜品数据保存到数据库中
    public boolean fillDataTable(ArrayList<MyData> dataes){
        for (int i=0;i<dataes.size();i++){
            MyData data=dataes.get(i);
            if (insert(data)==-1){
                return false;
            }
        }
        return true;
    }

    public boolean fillScheduleTable(ArrayList<Schedule> schedules){
        for (int i=0;i<schedules.size();i++){
            Schedule schedule=schedules.get(i);
            if (insert(schedule)==-1){
                return false;
            }
        }
        return true;
    }

    //从数据库中取出菜品数据，存放到一个ArrayList<Map<String ,Object>>，作为显示菜品信息的列表的数据源
    public ArrayList<Map<String,Object>> getData(){
        //先从数据库中查询出所有的菜
        ArrayList<MyData> dishes=queryAllData();
        ArrayList<Map<String,Object>> Datas=new ArrayList<Map<String, Object>>();
        for (int i=0;i<dishes.size();i++){
            MyData theData =dishes.get(i);
            Map<String,Object> map=new HashMap<String, Object>();
            map.put(KEY_ID,theData.id);
            map.put(PULSE,theData.pulse);
            map.put(BLOOD_OXYGEN_CONCENTRATION,theData.blood_oxygen_concentration);
            map.put(ALTITUDE,theData.altitude);
            map.put(WEAREORNOT,theData.wearornot);
            map.put(ALONGITUDE,theData.alongitude);
            map.put(BODY_TEMP,theData.body_temp);
            Datas.add(map);
        }
        return Datas;
    }

    //从数据库中取出菜品数据，存放到一个ArrayList<Map<String ,Object>>，作为显示菜品信息的列表的数据源
    public ArrayList<Map<String,Object>> getSchedule(){
        //先从数据库中查询出所有的菜
        ArrayList<Schedule> schedules=queryAllScheduleData();
        ArrayList<Map<String,Object>> Schedules=new ArrayList<Map<String, Object>>();
        for (int i=0;i<schedules.size();i++){
            Schedule theData =schedules.get(i);
            Map<String,Object> map=new HashMap<String, Object>();
            map.put(KEY_ID,theData.id);
            map.put(DATE,theData.date);
            map.put(TODONAME,theData.todoname);
            map.put(DETAIL,theData.detail);
            map.put(LATITUDE,theData.latitude);
            map.put(LONGITUDE,theData.longitude);
            map.put(ISDONE,theData.isdone);
            Schedules.add(map);
        }
        return Schedules;
    }
}
