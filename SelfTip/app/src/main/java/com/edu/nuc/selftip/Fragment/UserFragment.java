package com.edu.nuc.selftip.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.edu.nuc.selftip.KickBackAnimator;
import com.edu.nuc.selftip.MainActivity;
import com.edu.nuc.selftip.MyApplication;
import com.edu.nuc.selftip.R;
import com.edu.nuc.selftip.Schedule;
import com.edu.nuc.selftip.Schedules;
import com.edu.nuc.selftip.Setting;
import com.edu.nuc.selftip.StreamTool;
import com.edu.nuc.selftip.changemessage;
import com.edu.nuc.selftip.foodinfo;
import com.edu.nuc.selftip.toDoList;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class UserFragment extends Fragment {

    private ImageView iv_setting;
    private AnalogClock ac_user =null;
    private PopupWindow popupWindow = null;
    private Handler mHandler = new Handler();
    private LinearLayout ll_userll = null;
    private CalendarView cv_user = null;
    private TextView tv_waittodo = null;
    private TextView tv_name = null;
    private TextView tv_weight = null;
    private TextView tv_height = null;
    private TextView tv_bmi = null;
    private TextView tv_blood = null;
    private LinearLayout ll_height = null;
    private TextView msg_waittodo = null;
    private TableRow tr_getfoodmessage=null;
    private TableRow message_user=null;




    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View square = inflater.inflate(R.layout.fragment_user, container, false);

        iv_setting=square.findViewById(R.id.iv_setting);
        ac_user = square.findViewById(R.id.ac_user);
        ll_userll = square.findViewById(R.id.ll_userll);
        tv_waittodo = square.findViewById(R.id.tv_waittodo);
        ll_height = square.findViewById(R.id.ll_height);
        msg_waittodo = square.findViewById(R.id.msg_waittodo);
        tv_name= square.findViewById(R.id.tv_name);
        tv_weight= square.findViewById(R.id.tv_weight);
        tv_height= square.findViewById(R.id.tv_height);
        tv_bmi= square.findViewById(R.id.tv_bmi);
        tv_blood= square.findViewById(R.id.tv_blood);
        tr_getfoodmessage=square.findViewById(R.id.tr_getfoodmessage);
        message_user=square.findViewById(R.id.message_user);

        MyApplication myApplication=(MyApplication)getActivity().getApplication();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ac_user:
                        View layout = getLayoutInflater().inflate(R.layout.window_popu, null);
                        final LinearLayout linearLayout = layout.findViewById(R.id.ll_time);
                        cv_user = layout.findViewById(R.id.cv_user);
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //添加退场动画
                                closeAnimation(linearLayout);
                            }
                        });
                        cv_user.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                tv_waittodo.setText(year+"年"+month+"月"+dayOfMonth+"日待办事项");
                                Date date = new Date(year,month,dayOfMonth);
                                if(myApplication.schedules.getScheduleByDate(date)!=null){
                                    msg_waittodo.setText(myApplication.schedules.getScheduleByDate(date).toString());
                                }
                            }
                        });
                        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        popupWindow.setFocusable(true);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        //添加进场动画
                        showAnimation(linearLayout);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setFocusable(false);
                        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        break;
                    case R.id.iv_setting:
                        Intent intent = new Intent(getActivity(), Setting.class);
                        startActivity(intent);
                        break;
                    case R.id.tr_getfoodmessage:
                        Intent intent1=new Intent(getActivity(),foodinfo.class);
                        startActivity(intent1);
                        break;
                    case R.id.message_user:
                        Intent intent2 = new Intent(getActivity(), changemessage.class);
                        startActivity(intent2);
                        break;
                    case R.id.tv_waittodo:
                        Intent intent3 = new Intent(getActivity(),toDoList.class);
                        startActivity(intent3);
                    case R.id.msg_waittodo:
                        Intent intent4 = new Intent(getActivity(),toDoList.class);
                        startActivity(intent4);
                    default:break;
                }
            }
        };

        ac_user.setOnClickListener(onClickListener);
        iv_setting.setOnClickListener(onClickListener);
        ll_height.setOnClickListener(onClickListener);
        tr_getfoodmessage.setOnClickListener(onClickListener);
        message_user.setOnClickListener(onClickListener);
        tv_waittodo.setOnClickListener(onClickListener);
        msg_waittodo.setOnClickListener(onClickListener);
        return square;

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication myApplication=(MyApplication)getActivity().getApplication();
        tv_name.setText(myApplication.getusername()+"");
        tv_weight.setText(myApplication.getuserweight()+"");
        tv_height.setText(myApplication.getuserheight()+"");
        tv_bmi.setText(myApplication.getuserbmi()+"");
        tv_blood.setText(myApplication.getuserblood()+"");
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 800, 750);
                    fadeAnim.setDuration(800);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(800);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }
    }

    private void closeAnimation(ViewGroup layout) {
        ArrayList<View> viewArrayList = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            viewArrayList.add(layout.getChildAt(i));
        }
        Collections.reverse(viewArrayList);
        for (int i = 0; i < viewArrayList.size(); i++) {
            final View child = viewArrayList.get(i);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 750, -900);
                    fadeAnim.setDuration(800);
                    fadeAnim.start();
                    fadeAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                            //判断如果是最后一个退场的View,退出PopupWindow窗口
                            if (child.getId() == R.id.cv_user) {
                                popupWindow.dismiss();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            }, i * 50);
        }
    }


}