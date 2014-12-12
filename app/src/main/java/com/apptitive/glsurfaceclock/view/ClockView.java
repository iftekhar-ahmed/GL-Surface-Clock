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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Iftekhar on 08/12/2014.
 */
public class ClockView extends View {

    private Paint paintSecondMarks;
    private Paint paintHourMarks;
    private Paint paintSecondHand;
    private Paint paintHourAndMinuteHand;
    private Point centerPoint;
    private Path dialPathMark;

    private final float SEC_HOUR_MARK_THICKNESS = 5f;
    private final float SECOND_HAND_THICKNESS = 2f;
    private final float SEC_HOUR_MARK_RADIUS = 3f;
    private final float DIAL_RADIUS_SECOND_MARKS = 200f;
    private final float DIAL_RADIUS_HOUR_MARKS = DIAL_RADIUS_SECOND_MARKS - 15f;
    private final float ADVANCE_SECOND_MARKS = (2f * (float)Math.PI * DIAL_RADIUS_SECOND_MARKS) / 60f + 0.05f;
    private final float ADVANCE_HOUR_MARKS = ((2f * (float)Math.PI * DIAL_RADIUS_HOUR_MARKS) / 60f + 0.05f) * 5f;
    private final float PHASE = 0f;
    private final float SECOND_HAND_LENGTH = DIAL_RADIUS_HOUR_MARKS - 10f;
    private final float MINUTE_HAND_LENGTH = DIAL_RADIUS_HOUR_MARKS - 10f;
    private final float HOUR_HAND_LENGTH = DIAL_RADIUS_HOUR_MARKS - 30f;

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
        paintSecondMarks = new Paint();
        paintSecondMarks.setStyle(Paint.Style.STROKE);
        paintSecondMarks.setStrokeWidth(SEC_HOUR_MARK_THICKNESS);
        paintSecondMarks.setColor(Color.BLACK);
        paintSecondMarks.setAntiAlias(true);
        paintSecondMarks.setPathEffect(new PathDashPathEffect(dialPathMark, ADVANCE_SECOND_MARKS, PHASE, PathDashPathEffect.Style.TRANSLATE));
        paintHourMarks = new Paint();
        paintHourMarks.setStyle(Paint.Style.STROKE);
        paintHourMarks.setStrokeWidth(SEC_HOUR_MARK_THICKNESS);
        paintHourMarks.setColor(Color.BLACK);
        paintHourMarks.setAntiAlias(true);
        paintHourMarks.setPathEffect(new PathDashPathEffect(dialPathMark, ADVANCE_HOUR_MARKS, PHASE, PathDashPathEffect.Style.TRANSLATE));
        paintSecondHand = new Paint();
        paintSecondHand.setStyle(Paint.Style.STROKE);
        paintSecondHand.setStrokeWidth(SECOND_HAND_THICKNESS);
        paintSecondHand.setColor(Color.BLACK);
        paintSecondHand.setAntiAlias(true);
        paintHourAndMinuteHand = new Paint();
        paintHourAndMinuteHand.setStrokeWidth(SEC_HOUR_MARK_THICKNESS);
        paintHourAndMinuteHand.setColor(Color.BLACK);
        paintHourAndMinuteHand.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, DIAL_RADIUS_SECOND_MARKS, paintSecondMarks);
        canvas.drawCircle(centerPoint.x, centerPoint.y, DIAL_RADIUS_HOUR_MARKS, paintHourMarks);
        canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x + SECOND_HAND_LENGTH, centerPoint.y, paintSecondHand);
        canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x + 20f, centerPoint.y - MINUTE_HAND_LENGTH, paintHourAndMinuteHand);
        canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x, centerPoint.y - HOUR_HAND_LENGTH, paintHourAndMinuteHand);
        super.onDraw(canvas);
    }
}
