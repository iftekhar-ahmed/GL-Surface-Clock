package com.apptitive.glsurfaceclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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
    private Paint dialPaint;
    private Paint paint;
    private Point centerPoint;
    private Path dialPath;

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
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setAntiAlias(true);
        centerPoint = new Point();
        dialPath = new Path();
        dialPath.addCircle(250, 350, 20, Path.Direction.CW);
        dialPath.addCircle(260, 360, 20, Path.Direction.CW);
        dialPath.addRect(260, 370, 360, 470, Path.Direction.CW);
        paint = new Paint();
        paint.setColor(Color.BLACK);                    // set the color
        paint.setStrokeWidth(10);               // set the size
        paint.setDither(true);                    // set the dither to true
        paint.setStyle(Paint.Style.STROKE);       // set to STOKE
        paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        paint.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.
        paint.setAntiAlias(true);
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(centerPoint);
            centerPoint.set(centerPoint.x / 3, centerPoint.y / 3);
        } else {
            centerPoint.set(display.getWidth() / 3, display.getHeight() / 3);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, 10, circlePaint);
        canvas.drawPath(dialPath, paint);
        super.onDraw(canvas);
    }
}
