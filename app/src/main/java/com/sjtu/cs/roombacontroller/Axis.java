package com.sjtu.cs.roombacontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.sjtu.cs.roombacontroller.R;

/**
 * Created by StellEdge on 2018/5/10.
 */

public class Axis extends ImageView {
    public float InitX = 500;
    public float InitY = 1080/2;
    private float currentX = InitX;
    private float currentY = InitY;
    private Bitmap bmp;

    public Axis(Context context) {
        super(context);
        init();
    }

    public Axis(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Axis(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.axis);
        }
    }

    private boolean isClickView = false;//标识是否是人为点击，是则为true

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isClickView == true && bmp != null) {
            //创建画笔
            Paint p = new Paint();
            canvas.drawBitmap(bmp, currentX - (bmp.getWidth() / 2), currentY - (bmp.getHeight() / 2), p);
            isClickView = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前组件的currentX、currentY两个属性
        this.currentX = event.getX();
        this.currentY = event.getY();
        isClickView = true;

        if (event.getAction() == MotionEvent.ACTION_UP && bmp != null) {
            this.currentX = InitX-bmp.getWidth()/2;
            this.currentY = InitY-bmp.getHeight()/2;
            isClickView = false;
        }
        //通知改组件重绘
        //this.invalidate();
        //返回true表明处理方法已经处理该事件
        return true;
    }
}