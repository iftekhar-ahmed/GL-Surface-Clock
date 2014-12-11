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

    private Paint dialPaint;
    private Point centerPoint;
    private Path dialPathShape;

    private final float SEC_HOUR_MARK_RADIUS = 3;
    private final float DIAL_RADIUS = 200;
    private final float ADVANCE = (2 * (float)Math.PI * DIAL_RADIUS) / 60 - (SEC_HOUR_MARK_RADIUS / 2) + 0.15f;
    private final float PHASE = ADVANCE;

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
        dialPathShape = new Path();
        dialPathShape.addCircle(0, 0, SEC_HOUR_MARK_RADIUS, Path.Direction.CW);
        dialPaint = new Paint();
        dialPaint.setStyle(Paint.Style.STROKE);
        dialPaint.setStrokeWidth(5);
        dialPaint.setColor(Color.BLACK);
        dialPaint.setAntiAlias(true);
        dialPaint.setPathEffect(new PathDashPathEffect(dialPathShape, ADVANCE, 0, PathDashPathEffect.Style.TRANSLATE));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, DIAL_RADIUS, dialPaint);

        super.onDraw(canvas);
    }
}
