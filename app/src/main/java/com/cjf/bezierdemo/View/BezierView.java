package com.cjf.bezierdemo.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.cjf.bezierdemo.R;

public class BezierView extends View {

    Point staticPoint1 = new Point(-1, -1);
    Point staticPoint2 = new Point(-1, -1);
    Point fingerPoint1 = new Point(-1, -1);
    Point fingerPoint2 = new Point(-1, -1);

    Paint staticPointPaint;
    Paint fingerPointPaint;
    Paint linePaint;
    Paint bezierPaint;
    Path bezierPath;


    boolean isOnePoint = true;

    public BezierView(Context context) {
        super(context);
        initPaint();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        staticPointPaint = new Paint();
        staticPointPaint.setStrokeWidth(15);
        staticPointPaint.setAntiAlias(true);
        fingerPointPaint = new Paint();
        fingerPointPaint.setStrokeWidth(25);
        fingerPointPaint.setAlpha(100);
        fingerPointPaint.setColor(getResources().getColor(R.color.colorPrimary));
        fingerPointPaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setStrokeWidth(5);
        linePaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        linePaint.setAntiAlias(true);
        bezierPaint = new Paint();
        bezierPaint.setStrokeWidth(10);
        bezierPaint.setStyle(Paint.Style.STROKE);
        bezierPaint.setAntiAlias(true);
        bezierPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoint(canvas);
        drawLine(canvas);
    }

    private void drawPoint(Canvas canvas) {
        if (staticPoint1.x != -1) {
            canvas.drawPoint(staticPoint1.x, staticPoint1.y, staticPointPaint);
        }
        if (staticPoint2.x != -1) {
            canvas.drawPoint(staticPoint2.x, staticPoint2.y, staticPointPaint);
        }

        if (fingerPoint1.x != -1) {
            canvas.drawPoint(fingerPoint1.x, fingerPoint1.y, fingerPointPaint);
        }
        if (fingerPoint2.x != -1) {
            canvas.drawPoint(fingerPoint2.x, fingerPoint2.y, fingerPointPaint);
        }
    }

    private void drawLine(Canvas canvas) {
        if (staticPoint1.x != -1 && staticPoint2.x != -1) { //结束点已定位
            bezierPath = new Path();
            bezierPath.moveTo(staticPoint1.x, staticPoint1.y);
            if (isOnePoint()) {   //二阶贝塞尔模式（一个控制点）
                if (fingerPoint1.x != -1) {
                    canvas.drawLine(staticPoint1.x, staticPoint1.y, fingerPoint1.x, fingerPoint1.y, linePaint);
                    canvas.drawLine(staticPoint2.x, staticPoint2.y, fingerPoint1.x, fingerPoint1.y, linePaint);
                    bezierPath.quadTo(fingerPoint1.x, fingerPoint1.y, staticPoint2.x, staticPoint2.y);
                    canvas.drawPath(bezierPath, bezierPaint);
                }
            } else {
                if (fingerPoint1.x != -1 && fingerPoint2.x != -1) {
                    canvas.drawLine(staticPoint1.x, staticPoint1.y, fingerPoint1.x, fingerPoint1.y, linePaint);
                    canvas.drawLine(staticPoint2.x, staticPoint2.y, fingerPoint2.x, fingerPoint2.y, linePaint);
                    canvas.drawLine(fingerPoint1.x, fingerPoint1.y, fingerPoint2.x, fingerPoint2.y, linePaint);
                    bezierPath.cubicTo(fingerPoint1.x, fingerPoint1.y, fingerPoint2.x, fingerPoint2.y, staticPoint2.x, staticPoint2.y);
                    canvas.drawPath(bezierPath, bezierPaint);
                }
            }

        }
    }

    boolean selectPoint1 = false;
    boolean selectPoint2 = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (staticPoint1.x != -1 && staticPoint2.x != -1) {
                    if (isOnePoint()) {
                        selectPoint1 = true;
                    } else {
                        selectPoint(event.getX(), event.getY());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (staticPoint1.x == -1) {
                    staticPoint1.set((int) event.getX(), (int) event.getY());
                } else if (staticPoint2.x == -1) {
                    staticPoint2.set((int) event.getX(), (int) event.getY());
                } else if (fingerPoint1.x == -1) {
                    fingerPoint1.set((int) event.getX(), (int) event.getY());
                } else if (fingerPoint2.x == -1 && !isOnePoint()) {
                    fingerPoint2.set((int) event.getX(), (int) event.getY());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (staticPoint1.x != -1 && staticPoint2.x != -1) {
                    if (selectPoint1) {
                        fingerPoint1.set((int) event.getX(), (int) event.getY());
                    }
                    if (selectPoint2) {
                        fingerPoint2.set((int) event.getX(), (int) event.getY());
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void selectPoint(float x, float y) {

        if (isOnePoint() && fingerPoint1.x == -1) {
            selectPoint1 = false;
            selectPoint2 = false;
            return;
        }

        if (!isOnePoint() && fingerPoint2.x == -1) {
            selectPoint1 = false;
            selectPoint2 = false;
            return;
        }

        if (isOnePoint()) {
            selectPoint1 = true;
            selectPoint2 = false;
        } else {
            double distanceToFigurePoint1 = Math.sqrt(Math.abs(x - fingerPoint1.x) * Math.abs(x - fingerPoint1.x) + Math.abs(y - fingerPoint1.y) * Math.abs(y - fingerPoint1.y));
            double distanceToFigurePoint2 = Math.sqrt(Math.abs(x - fingerPoint2.x) * Math.abs(x - fingerPoint2.x) + Math.abs(y - fingerPoint2.y) * Math.abs(y - fingerPoint2.y));
            if (Math.min(distanceToFigurePoint1, distanceToFigurePoint2) > 100) {
                selectPoint1 = false;
                selectPoint2 = false;
            } else if (distanceToFigurePoint1 < distanceToFigurePoint2) {
                selectPoint1 = true;
                selectPoint2 = false;
            } else {
                selectPoint1 = false;
                selectPoint2 = true;
            }
        }
    }

    public boolean isOnePoint() {
        return isOnePoint;
    }

    public void setOnePoint(boolean onePoint) {
        isOnePoint = onePoint;
        clean();
        invalidate();
    }

    public void clean() {
        staticPoint1.set(-1, -1);
        staticPoint2.set(-1, -1);
        fingerPoint1.set(-1, -1);
        fingerPoint2.set(-1, -1);
        invalidate();
    }
}
