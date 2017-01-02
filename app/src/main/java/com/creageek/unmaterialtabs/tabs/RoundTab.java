package com.creageek.unmaterialtabs.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Ruslan Kishai aka creageek on 1/1/2017.
 */

public class RoundTab extends View {

    Rect textBounds = new Rect();
    float parentWidth = 0, parentHeight = 0;
    boolean clickedTab = false;
    RectF tab = new RectF();
    String tabText = "";
    float tabLeft, tabRight, tabTop, tabBottom, beforeCentering;
    float textLeft, textRight, textTop, textBottom;
    Paint paint;

    public float getBeforeCentering() {
        return beforeCentering;
    }

    public void setBeforeCentering(float beforeCentering) {
        this.beforeCentering = beforeCentering;
    }

    public RoundTab(Context context) {
        super(context);
        paint = new Paint();

    }

    public RoundTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

    }

    public RoundTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint = new Paint();

    }

    public void invertClicked() {
        clickedTab = !clickedTab;
    }

    public Rect getTextBounds() {
        return textBounds;
    }

    public void setTextBounds(Rect textBounds) {
        this.textBounds = textBounds;
    }

    public float getParentWidth() {
        return parentWidth;
    }

    public void setParentWidth(float parentWidth) {
        this.parentWidth = parentWidth;
    }

    public float getParentHeight() {
        return parentHeight;
    }

    public void setParentHeight(float parentHeight) {
        this.parentHeight = parentHeight;
    }

    public String getTabText() {
        return tabText;
    }

    public void setTabText(String tabText) {
        this.tabText = tabText;
    }

    public float getTabLeft() {
        return tabLeft;
    }

    public void setTabLeft(float tabLeft) {
        this.tabLeft = tabLeft;
    }

    public float getTabRight() {
        return tabRight;
    }

    public void setTabRight(float tabRight) {
        this.tabRight = tabRight;
    }

    public float getTabTop() {
        return tabTop;
    }

    public void setTabTop(float tabTop) {
        this.tabTop = tabTop;
    }

    public float getTabBottom() {
        return tabBottom;
    }

    public void setTabBottom(float tabBottom) {
        this.tabBottom = tabBottom;
    }

    public float getTextLeft() {
        return textLeft;
    }

    public void setTextLeft(float textLeft) {
        this.textLeft = textLeft;
    }

    public float getTextRight() {
        return textRight;
    }

    public void setTextRight(float textRight) {
        this.textRight = textRight;
    }

    public float getTextTop() {
        return textTop;
    }

    public void setTextTop(float textTop) {
        this.textTop = textTop;
    }

    public float getTextBottom() {
        return textBottom;
    }

    public void setTextBottom(float textBottom) {
        this.textBottom = textBottom;
    }

    public void initTextBounds() {
        paint.setFakeBoldText(true);
        paint.setTextSize(spToPx(13));

        textBounds = new Rect();
        paint.getTextBounds(tabText, 0, tabText.length(), textBounds);
    }

    private void drawTab(Canvas canvas, String tabText) {
        Paint tabPaint = new Paint();

        tabPaint.setAntiAlias(true);

        textLeft = textBounds.left;
        textRight = textBounds.right;
        textTop = textBounds.top;
        textBottom = textBounds.bottom;
        //tab.left = getWidth() / 2 - textBounds.width() / 2 - dpToPx(16);
        tab.left = tabLeft;
        tab.right = tabRight;
        //tab.top = getHeight() / 2 - textBounds.height() / 2 - dpToPx(12);
        tab.top = tabTop;
        tab.bottom = tabBottom;
        if (!clickedTab) {
            tabPaint.setStyle(Paint.Style.STROKE);
            tabPaint.setStrokeWidth(dpToPx(1.5f));
            tabPaint.setColor(0xffffffff);
            paint.setColor(0xffffffff);
        } else {
            tabPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            tabPaint.setColor(0xffffffff);
            paint.setColor(0xff37BBAD);
            tab.left -= dpToPx(1.5f / 2.0f);
            tab.right += dpToPx(1.5f / 2.0f);
            tab.top -= dpToPx(1.5f / 2.0f);
            tab.bottom += dpToPx(1.5f / 2.0f);
        }

        canvas.drawRoundRect(tab, 50, 50, tabPaint);
        canvas.drawText(tabText, tabLeft + dpToPx(16), parentHeight / 2 + textBounds.height() / 2, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTab(canvas, tabText);
    }

    private Rect drawTabText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xffffffff);
        paint.setTextSize(spToPx(13));
        Rect bounds = new Rect();
        String tab = "HEARTRATE";
        paint.getTextBounds(tab, 0, tab.length(), bounds);
        canvas.drawText(tab, getWidth() / 2 - bounds.width() / 2, getHeight() / 2 + bounds.height() / 2, paint);
        return bounds;
    }

    private float spToPx(float spValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm);
    }

    private float dpToPx(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm);
    }
}
