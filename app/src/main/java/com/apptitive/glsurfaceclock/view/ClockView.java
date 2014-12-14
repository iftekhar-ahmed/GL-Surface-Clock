package com.apptitive.glsurfaceclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Iftekhar on 08/12/2014.
 */
public class ClockView extends View implements Runnable {

    private boolean isRunning;
    private float secHandX;
    private float secHandY;
    private float minHandX;
    private float minHandY;
    private float hourHandX;
    private float hourHandY;
    private Thread timeThread;
    private Calendar calendar;
    private Paint paintSecMarks;
    private Paint paintHourMarks;
    private Paint paintSecHand;
    private Paint paintHourAndMinuteHand;
    private Point centerPoint;
    private Path dialPathMark;

    private final float SEC_HOUR_MARK_THICKNESS = 5f;
    private final float SEC_HAND_THICKNESS = 2f;
    private final float SEC_HOUR_MARK_RADIUS = 3f;
    private final float DIAL_RADIUS_SEC_MARKS = 200f;
    private final float DIAL_RADIUS_HOUR_MARKS = DIAL_RADIUS_SEC_MARKS - 15f;
    private final float ADVANCE_SEC_MARKS = (2f * (float) Math.PI * DIAL_RADIUS_SEC_MARKS) / 60f + 0.05f;
    private final float ADVANCE_HOUR_MARKS = ((2f * (float) Math.PI * DIAL_RADIUS_HOUR_MARKS) / 60f + 0.05f) * 5f;
    private final float PHASE = 0f;
    private final float SEC_HAND_LENGTH = DIAL_RADIUS_HOUR_MARKS - 10f;
    private final float MIN_HAND_LENGTH = DIAL_RADIUS_HOUR_MARKS - 10f;
    private final float HOUR_HAND_LENGTH = DIAL_RADIUS_HOUR_MARKS - 30f;
    private final float CONST_SEC_HAND_THETA = 6f;
    private final float CONST_MIN_HAND_THETA = 1f / 10f;
    private final float CONST_HOUR_HAND_THETA = 1f / 12f;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void halt() {
        isRunning = false;
        while (true) {
            try {
                timeThread.join();
                return;
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void start() {
        isRunning = true;
        timeThread = new Thread(this);
        timeThread.start();
    }

    private Paint getStrokePaint(float thickness) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        return paint;
    }

    private void init() {
        setBackgroundColor(Color.RED);
        centerPoint = new Point();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(centerPoint);
            centerPoint.set(centerPoint.x / 2, centerPoint.y / 2);
        } else {
            centerPoint.set(display.getWidth() / 2, display.getHeight() / 2);
        }
        dialPathMark = new Path();
        dialPathMark.addCircle(0f, 0f, SEC_HOUR_MARK_RADIUS, Path.Direction.CW);
        paintSecMarks = getStrokePaint(SEC_HOUR_MARK_THICKNESS);
        paintSecMarks.setPathEffect(new PathDashPathEffect(dialPathMark, ADVANCE_SEC_MARKS, PHASE, PathDashPathEffect.Style.TRANSLATE));
        paintHourMarks = getStrokePaint(SEC_HOUR_MARK_THICKNESS);
        paintHourMarks.setPathEffect(new PathDashPathEffect(dialPathMark, ADVANCE_HOUR_MARKS, PHASE, PathDashPathEffect.Style.TRANSLATE));
        paintSecHand = getStrokePaint(SEC_HAND_THICKNESS);
        paintHourAndMinuteHand = getStrokePaint(SEC_HOUR_MARK_THICKNESS);
        start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, DIAL_RADIUS_SEC_MARKS, paintSecMarks);
        canvas.drawCircle(centerPoint.x, centerPoint.y, DIAL_RADIUS_HOUR_MARKS, paintHourMarks);
        canvas.drawLine(centerPoint.x, centerPoint.y, secHandX, secHandY, paintSecHand);
        canvas.drawLine(centerPoint.x, centerPoint.y, minHandX, minHandY, paintHourAndMinuteHand);
        canvas.drawLine(centerPoint.x, centerPoint.y, hourHandX, hourHandY, paintHourAndMinuteHand);
        super.onDraw(canvas);
    }

    @Override
    public void run() {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        float secHandAngle = sec * CONST_SEC_HAND_THETA;
        float minHandAngle = min * CONST_SEC_HAND_THETA + sec * CONST_MIN_HAND_THETA;
        float hourHandAngle = hour * CONST_SEC_HAND_THETA + min * 0.5f + sec * CONST_HOUR_HAND_THETA;
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (secHandAngle >= 360f) {
                secHandAngle = 0f;
            }
            if (minHandAngle >= 360f) {
                minHandAngle = 0f;
            }
            if (hourHandAngle >= 360f) {
                hourHandAngle = 0f;
            }
            secHandAngle += CONST_SEC_HAND_THETA;
            minHandAngle += CONST_MIN_HAND_THETA;
            hourHandAngle += CONST_HOUR_HAND_THETA;
            Log.i("sec min hour", "" + secHandAngle + " " + minHandAngle + " " + hourHandAngle);
            secHandX = centerPoint.x + (float) (SEC_HAND_LENGTH * Math.cos(secHandAngle * (float) Math.PI / 180f));
            secHandY = centerPoint.y + (float) (SEC_HAND_LENGTH * Math.sin(secHandAngle * (float) Math.PI / 180f));
            minHandX = centerPoint.x + (float) (MIN_HAND_LENGTH * Math.cos(minHandAngle * (float) Math.PI / 180f));
            minHandY = centerPoint.y + (float) (MIN_HAND_LENGTH * Math.sin(minHandAngle * (float) Math.PI / 180f));
            hourHandX = centerPoint.x + (float) (HOUR_HAND_LENGTH * Math.cos(hourHandAngle * Math.PI / 180f));
            hourHandY = centerPoint.y + (float) (HOUR_HAND_LENGTH * Math.sin(hourHandAngle * Math.PI / 180f));
            postInvalidate();
        }
    }
}
