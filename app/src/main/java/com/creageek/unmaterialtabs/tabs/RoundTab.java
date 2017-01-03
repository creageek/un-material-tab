package com.creageek.unmaterialtabs.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.creageek.unmaterialtabs.DimensUtils;

/**
 * Created by Ruslan Kishai aka creageek on 1/1/2017.
 */

public class RoundTab extends View {

    private String tabText;

    private Paint tabPaint, textPaint;
    private Rect textBounds;
    private RectF tab;

    private int parentHeight;
    private boolean isClicked = false;
    private boolean isFirst = false;
    private boolean isLast = false;

    public RoundTab(Context context) {
        super(context);
        initView();
    }

    public RoundTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RectF getTab() {
        return tab;
    }

    private void initView() {
        textBounds = new Rect();
        textPaint = new Paint();
        tabPaint = new Paint();
    }

    public RoundTab initTab(String tabText) {
        this.tabText = tabText;

        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DimensUtils.spToPx(getContext(), 13));
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(0xffffffff);
        textPaint.getTextBounds(tabText, 0, tabText.length(), textBounds);

        tabPaint.setAntiAlias(true);
        tabPaint.setStyle(Paint.Style.STROKE);
        tabPaint.setStrokeWidth(DimensUtils.dpToPx(getContext(), 1.5f));
        tabPaint.setColor(0xffffffff);

        return this;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (isClicked) {
           /* tab = new RectF(
                    DimensUtils.dpToPx(getContext(), 12),
                    parentHeight / 2 - textBounds.height() / 2 - DimensUtils.dpToPx(getContext(), 12),
                    textBounds.right + DimensUtils.dpToPx(getContext(), 12) + DimensUtils.dpToPx(getContext(), 16) * 2,
                    parentHeight / 2 + textBounds.height() / 2 + DimensUtils.dpToPx(getContext(), 12));*/
            tab = new RectF(
                    DimensUtils.dpToPx(getContext(), 6),
                    parentHeight / 2 - textBounds.height() / 2 - DimensUtils.dpToPx(getContext(), 12),
                    textBounds.right + DimensUtils.dpToPx(getContext(), 6) + DimensUtils.dpToPx(getContext(), 16) * 2,
                    parentHeight / 2 + textBounds.height() / 2 + DimensUtils.dpToPx(getContext(), 12));

            if (isFirst) {
                tab.left = DimensUtils.dpToPx(getContext(), 16);
                tab.right = textBounds.right + DimensUtils.dpToPx(getContext(), 16) + DimensUtils.dpToPx(getContext(), 16) * 2;
                textBounds.left = DimensUtils.dpToPx(getContext(), 16);
            }

            tabPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            tabPaint.setColor(0xffffffff);
            textPaint.setColor(0xff37BBAD);
            tab.left -= DimensUtils.dpToPx(getContext(), 1.5f / 2.0f);
            tab.right += DimensUtils.dpToPx(getContext(), 1.5f / 2.0f);
            tab.top -= DimensUtils.dpToPx(getContext(), 1.5f / 2.0f);
            tab.bottom += DimensUtils.dpToPx(getContext(), 1.5f / 2.0f);
        } else {
            tab = new RectF(
                    DimensUtils.dpToPx(getContext(), 6),
                    parentHeight / 2 - textBounds.height() / 2 - DimensUtils.dpToPx(getContext(), 12),
                    textBounds.right + DimensUtils.dpToPx(getContext(), 6) + DimensUtils.dpToPx(getContext(), 16) * 2,
                    parentHeight / 2 + textBounds.height() / 2 + DimensUtils.dpToPx(getContext(), 12));

            if (isFirst) {
                tab.left = DimensUtils.dpToPx(getContext(), 16);
                tab.right = textBounds.right + DimensUtils.dpToPx(getContext(), 16) + DimensUtils.dpToPx(getContext(), 16) * 2;
                textBounds.left = DimensUtils.dpToPx(getContext(), 16);
            }

            tabPaint.setStyle(Paint.Style.STROKE);
            tabPaint.setStrokeWidth(DimensUtils.dpToPx(getContext(), 1.5f));
            tabPaint.setColor(0xffffffff);
            textPaint.setColor(0xffffffff);
        }


        canvas.drawRoundRect(tab, 100, 100, tabPaint);
        canvas.drawText(tabText, tab.left + DimensUtils.dpToPx(getContext(), 16), parentHeight / 2 + textBounds.height() / 2, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst)
            setMeasuredDimension(textBounds.right + DimensUtils.dpToPx(getContext(), 16) * 3 +
                    DimensUtils.dpToPx(getContext(), 6), heightMeasureSpec);
        else if (isLast)
            setMeasuredDimension(textBounds.right + DimensUtils.dpToPx(getContext(), 16) * 3 +
                    DimensUtils.dpToPx(getContext(), 6), heightMeasureSpec);
        else
            setMeasuredDimension(textBounds.right + DimensUtils.dpToPx(getContext(), 16) * 2 +
                    DimensUtils.dpToPx(getContext(), 12), heightMeasureSpec);

    }

    public int getParentHeight() {
        return parentHeight;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClicked = !isClicked;
                invalidate();
                return true;
        }
        return false;
    }*/
}
