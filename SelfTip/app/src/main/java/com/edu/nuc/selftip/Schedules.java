package com.edu.nuc.selftip;

import java.util.ArrayList;
import java.util.Date;

public class Schedules {
    public ArrayList<Schedule> mSchedules;

    public int getScheduleQuantity(){return mSchedules.size();}

    public Schedule getScheduleByIndex(int index){return mSchedules.get(index);}

    public ArrayList<Schedule> getScheduleByDate(Date date){
        ArrayList<Schedule> Schedules = new ArrayList<Schedule>();
        for (int i = 0 ; i < mSchedules.size();i++) {//遍历整个菜品列表
            Schedule theDish = mSchedules.get(i);
            if(date.equals(theDish.date)) {//用要查找的菜名根每一个菜品的名字进行比对
                Schedules.add(theDish);
            }
        }
        return Schedules;
    }
}
