package com.davie.chart;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.davie.tally.R;

/**
 * User: davie
 * Date: 15-4-8
 */
public class ArcChartView extends View {

    public ArcChartView(Context context) {
        this(context, null);
    }

    public ArcChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context,attrs,defStyle);
    }



    private Paint arcPaint;

    /**
     * 弧线的尺寸
     */
    private RectF arcRect;


    private Bitmap bitmap;

    private int [][] data ;
    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public void init(Context context,AttributeSet attrs,int defStyle){
        arcPaint = new Paint();
        arcPaint.setColor(Color.BLACK);

        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcPaint.setAntiAlias(true);

        arcRect = new RectF(160,160,500,500);

    }


    public  void setData(int [][] data){
        this.data = data;
        if(data!=null){
            //所有的控件都有的方法,进行刷新操作
            invalidate();
        }
    }

    private int [] colorArray = {Color.RED,Color.GREEN,Color.BLUE,Color.GRAY,Color.DKGRAY,Color.MAGENTA,Color.YELLOW,Color.CYAN};
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //清除内容
        canvas.drawColor(Color.WHITE);

        if(data!=null){
            int [] start = data[1];
        }

        //画绿色的半圆
        arcPaint.setColor(colorArray[0]);
        canvas.drawArc(arcRect,0,80,true,arcPaint);
        //画蓝色的半圆
        arcPaint.setColor(colorArray[1]);
        canvas.drawArc(arcRect,80,60,true,arcPaint);

        arcPaint.setColor(colorArray[2]);
        canvas.drawArc(arcRect,140,35,true,arcPaint);

        arcPaint.setColor(colorArray[3]);
        canvas.drawArc(arcRect,175,55,true,arcPaint);

        arcPaint.setColor(colorArray[4]);
        canvas.drawArc(arcRect,230,130,true,arcPaint);

//        //画中心的圆形
//        arcPaint.setColor(Color.WHITE);
//        canvas.drawCircle(330,330,50,arcPaint);
    }
}