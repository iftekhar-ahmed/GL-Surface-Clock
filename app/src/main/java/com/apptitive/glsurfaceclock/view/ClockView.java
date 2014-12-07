package com.apptitive.glsurfaceclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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

    private Paint circlePaint;
    private Point centerPoint;

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
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setPathEffect(new DashPathEffect(new float[] {10, 11}, 0));
        circlePaint.setColor(Color.BLACK);
        centerPoint = new Point();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(centerPoint);
            centerPoint.set(centerPoint.x / 3, centerPoint.y / 3);
        }
        else {
            centerPoint.set(display.getWidth() / 3, display.getHeight() / 3);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, 150, circlePaint);
        super.onDraw(canvas);
    }
}
