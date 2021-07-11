package com.edu.nuc.selftip;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.StrictMode;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by ws on 2017/2/25.
 */

public class NowHwWeatherView extends View {
    String ul;


    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;

    private float mWidth;
    private float mHeight;
    private float radius;//半径

    private int startAngle;//圆弧开始角
    private int sweepAngle;//圆弧总角度数
    private int count;//圆弧被分的份数

    private int currentTemp;//当前温度
    private int maxTemp;
    private int minTemp;
    private Bitmap bitmap;
    private int ocAngle;//0度初始角
    private int fgAngle;//总覆盖的角
    private int offset;


    public NowHwWeatherView(Context context) {
        this(context, null);
    }

    public NowHwWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NowHwWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

    }

    private void init(Context context) {
        initPaint();
        JSONArray jsonArray = JsonPost.getweather();

        startAngle = 120;
        sweepAngle = 300;
        count = 60;//刻度份数
        if (jsonArray==null) {
            Log.i("temp","error");
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather1);
            currentTemp=26;
        } else {
            try {
                JSONObject day01 = jsonArray.getJSONObject(0);
                JSONObject day02 = jsonArray.getJSONObject(1);
                String type = day01.getString("type");
                String high = day01.getString("high");
                String low = day01.getString("low");
                String date = day01.getString("date");

                String str2="";
                if(high!= null && !"".equals(high)){
                    for(int i=0;i<high.length();i++){
                        if(high.charAt(i)>=48 && high.charAt(i)<=57){
                            str2+=high.charAt(i);
                        }
                    }

                }

                String str3="";
                if(low!= null && !"".equals(low)){
                    for(int i=0;i<low.length();i++){
                        if(low.charAt(i)>=48 && low.charAt(i)<=57){
                            str3+=low.charAt(i);
                        }
                    }

                }

                int currentTemphigh = Integer.parseInt(str2);
                int currentTemplow = Integer.parseInt(str3);
                maxTemp=currentTemphigh;
                minTemp=currentTemplow;
                currentTemp = (currentTemphigh+currentTemplow)/2;
                Log.i("temp",""+currentTemp);


                switch (type) {
                    case "多云":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather8);
                        setblack();
                        break;
                    case "阴":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather6);
                        break;
                    case "晴":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather9);
                        setblack();
                        break;
                    case "阵雨":
                    case "雷阵雨":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather7);
                        setblack();
                        break;
                    case "雷阵雨伴有冰雹":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather7);
                        break;
                    case "雨夹雪":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather4);
                        setblack();
                        break;
                    case "小雨":
                    case "大暴雨-特大暴雨":
                    case "暴雨-大暴雨":
                    case "大雨-暴雨":
                    case "中雨-大雨":
                    case "小雨-中雨":
                    case "冻雨":
                    case "特大暴雨":
                    case "暴雨":
                    case "大雨":
                    case "中雨":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather5);
                        setblack();
                        break;
                    case "阵雪":
                    case "小雪":
                    case "小雪-中雪":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather2);
                        break;
                    case "中雪":
                    case "中雪-大雪":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather3);
                        break;
                    case "大雪":
                    case "暴雪":
                    case "大雪-暴雪":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather1);
                        break;
                    case "雾":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather11);
                        setblack();
                        break;
                    case "浮尘":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather10);
                        setblack();
                        break;
                    case "扬沙":
                    case "霾":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather13);
                        setblack();
                        break;
                    case "强沙尘暴":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather12);
                        setblack();
                        break;
                    default:
                        Log.e("weather", "error");
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather13);
                        currentTemp=26;
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather13);
                currentTemp=26;
            }
        }
        bitmap = bitmapsmall(bitmap);
            ocAngle = 230;
            fgAngle = 90;
            offset = 22;

        }

    private void setblack() {
        mArcPaint.setColor(Color.BLACK);
        mLinePaint.setColor(Color.BLACK);
        mTextPaint.setColor(Color.BLACK);
        mPointPaint.setColor(Color.BLACK);
    }

    private void initPaint() {
        mArcPaint = new Paint();
        mArcPaint.setColor(Color.WHITE);
        mArcPaint.setStrokeWidth(2);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStrokeWidth(4);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(144);

        mPointPaint = new Paint();
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStrokeWidth(2);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wrap_Len = 600;
        int width = measureDimension(wrap_Len, widthMeasureSpec);
        int height = measureDimension(wrap_Len, heightMeasureSpec);
        int len = Math.min(width, height);
        //保证是一个正方形
        setMeasuredDimension(len, len);

    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        radius = (mWidth - getPaddingLeft() - getPaddingRight()) / 2;//半径
        canvas.translate(mWidth / 2, mHeight / 2);
        //drawArcView(canvas);//画圆环
        drawLine(canvas);//画短线
        drawTextBitmapView(canvas);//画中间的温度和下边的图片
        drawTempLineView(canvas);//画动态温度

    }

    private void drawTempLineView(Canvas canvas) {
        mTextPaint.setTextSize(24);
        //canvas.drawText("0°C",getRealCosX(ocAngle,offset,true),getRealSinY(ocAngle,offset,true),mTextPaint);//固定0度的位置

        int startTempAngle = getStartAngle(minTemp, maxTemp);
       /* if(startTempAngle<=startAngle){//如果开始角小于startAngle，防止过边界
            startTempAngle=startAngle+10;
        }else if((startTempAngle+fgAngle)>=(startAngle+sweepAngle)){//如果结束角大于(startAngle+sweepAngle)
            startTempAngle =startAngle+sweepAngle-20-fgAngle;
        }*/
        canvas.drawText(minTemp + "°", getRealCosX(startTempAngle, offset, true), getRealSinY(startTempAngle, offset, true), mTextPaint);
        canvas.drawText(maxTemp + "°", getRealCosX(startTempAngle + fgAngle, offset, true), getRealSinY(startTempAngle + fgAngle, offset, true), mTextPaint);

        int circleAngle = startTempAngle + (currentTemp - minTemp) * fgAngle / (maxTemp - minTemp);
        mPointPaint.setColor(getRealColor(minTemp, maxTemp));
        canvas.drawCircle(getRealCosX(circleAngle, 50, false), getRealSinY(circleAngle, 50, false), 7, mPointPaint);
    }

    private void drawArcView(Canvas canvas) {
        RectF mRect = new RectF(-radius, -radius, radius, radius);
        //canvas.drawRect(mRect,mArcPaint);
        canvas.drawArc(mRect, startAngle, sweepAngle, false, mArcPaint);
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        float angle = (float) sweepAngle / count;//刻度间隔
        canvas.rotate(-270 + startAngle);//将起始刻度点旋转到正上方
        for (int i = 0; i <= count; i++) {
            if (i == 0 || i == count) {
                mLinePaint.setStrokeWidth(1);
                //mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(0, -radius, 0, -radius + 40, mLinePaint);

            } else if (i >= getStartLineIndex(minTemp, maxTemp) && i <= getEndLineIndex(minTemp, maxTemp)) {
                mLinePaint.setStrokeWidth(3);
                mLinePaint.setColor(getRealColor(minTemp, maxTemp));
                canvas.drawLine(0, -radius, 0, -radius + 30, mLinePaint);

            } else {
                mLinePaint.setStrokeWidth(2);
                //mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(0, -radius, 0, -radius + 30, mLinePaint);
            }

            canvas.rotate(angle);//逆时针旋转
        }
        canvas.restore();
    }

    private void drawTextBitmapView(Canvas canvas) {
        mTextPaint.setTextSize(144);
        canvas.drawText(currentTemp + "°", 0, 0 + getTextPaintOffset(mTextPaint), mTextPaint);
        canvas.drawBitmap(bitmap, 0 - bitmap.getWidth() / 2, radius - bitmap.getHeight() / 2 - 40, null);

    }


    public float getTextPaintOffset(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return -fontMetrics.descent + (fontMetrics.bottom - fontMetrics.top) / 2;
    }

    //根据角获得X坐标  R*cos&+getTextPaintOffset(mTextPaint)-off
    private float getCosX(int Angle) {

        return (float) (radius * Math.cos(Angle * Math.PI / 180)) + getTextPaintOffset(mTextPaint);
    }

    private float getSinY(int Angle) {

        return (float) (radius * Math.sin(Angle * Math.PI / 180)) + getTextPaintOffset(mTextPaint);
    }

    //根据象限加一个偏移量
    private float getRealCosX(int Angle, int off, boolean outoff) {
        if (!outoff) {
            off = -off;
        }
        if (getCosX(Angle) < 0) {
            return getCosX(Angle) - off;
        } else {
            return getCosX(Angle) + off;
        }
    }

    private float getRealSinY(int Angle, int off, boolean outoff) {
        if (!outoff) {
            off = -off;
        }
        if (getSinY(Angle) < 0) {
            return getSinY(Angle) - off;
        } else {
            return getSinY(Angle) + off;
        }
    }

    //根据当天温度范围获得扇形开始角。
    private int getStartAngle(int minTemp, int maxTemp) {
        int startFgAngle = 0;
        if (minTemp >= maxTemp) {
            Log.e("ws", "getStartAngle---?fail");
            return startFgAngle;
        }
        if (minTemp <= 0) {
            startFgAngle = ocAngle - (0 - minTemp) * fgAngle / (maxTemp - minTemp);
        } else {
            startFgAngle = ocAngle + (minTemp - 0) * fgAngle / (maxTemp - minTemp);
        }
        //边界 start
        if (startFgAngle <= startAngle) {//如果开始角小于startAngle，防止过边界
            startFgAngle = startAngle + 10;
        } else if ((startFgAngle + fgAngle) >= (startAngle + sweepAngle)) {//如果结束角大于(startAngle+sweepAngle)
            startFgAngle = startAngle + sweepAngle - 20 - fgAngle;
        }
        //边界 end
        return startFgAngle;
    }

    //根据当天温度范围获取开始短线的索引
    private int getStartLineIndex(int minTemp, int maxTemp) {
        return (getStartAngle(minTemp, maxTemp) - startAngle) / (sweepAngle / count);
    }

    private int getEndLineIndex(int minTemp, int maxTemp) {
        return (getStartAngle(minTemp, maxTemp) - startAngle) / (sweepAngle / count) + fgAngle / (sweepAngle / count);
    }

    //根据温度返回颜色值
    public int getRealColor(int minTemp, int maxTemp) {
        if (maxTemp <= 0) {
            return Color.parseColor("#00008B");//深海蓝
        } else if (minTemp <= 0 && maxTemp > 0) {
            return Color.parseColor("#4169E1");//黄君兰
        } else if (minTemp > 0 && minTemp < 15) {
            return Color.parseColor("#40E0D0");//宝石绿
        } else if (minTemp >= 15 && minTemp < 25) {
            return Color.parseColor("#00FF00");//酸橙绿
        } else if (minTemp >= 25 && minTemp < 30) {
            return Color.parseColor("#FFD700");//金色
        } else if (minTemp >= 30) {
            return Color.parseColor("#CD5C5C");//印度红
        }

        return Color.parseColor("#00FF00");//酸橙绿;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.bitmap = mBitmap;
        invalidate();
    }

    private Bitmap bitmapsmall(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth = 100;
        int newHeight = 100;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
    }
}