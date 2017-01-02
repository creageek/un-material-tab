package com.creageek.unmaterialtabs.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruslan Kishai aka creageek on 12/31/2016.
 */

public class RoundTabView extends HorizontalScrollView {
    Rect textBounds;
    RectF tab;
    List<RoundTab> tabs = new ArrayList<>();
    int clickedTab;
    boolean isCentering = false;

    public RoundTabView(Context context) {
        super(context);
        initTab();
    }

    public RoundTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTab();
    }

    public RoundTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTab();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTab();
    }

    private void initTab() {
        RoundTab tab = new RoundTab(getContext());
        tab.setTabText("HEARTRATE");
        tab.initTextBounds();
        tabs.add(tab);


        tab = new RoundTab(getContext());
        tab.setTabText("OXYGEN");
        tab.initTextBounds();
        tabs.add(tab);


        tab = new RoundTab(getContext());
        tab.setTabText("EMOTIONAL");
        tab.initTextBounds();
        tabs.add(tab);


        tab = new RoundTab(getContext());
        tab.setTabText("BATTERY");
        tab.initTextBounds();
        tabs.add(tab);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < tabs.size(); i++) {
            RoundTab tab = tabs.get(i);
            if (i == 0) {
                //tab.setTabLeft(dpToPx(16));
                tab.setTabRight(tab.getTabLeft() + tab.getTextBounds().width() + dpToPx(16) * 2);
                tab.setTabTop(getHeight() / 2 - tab.getTextBounds().height() / 2 - dpToPx(12));
                tab.setTabBottom(getHeight() / 2 + tab.getTextBounds().height() / 2 + dpToPx(12));
            } else if (i < tabs.size() - 1) {
                RoundTab prev = tabs.get(i - 1);

                tab.setTabLeft(prev.getTabRight() + dpToPx(16));
                //tab.setTabLeft(0);
                tab.setTabRight(tab.getTabLeft() + tab.getTextBounds().width() + dpToPx(16) * 2);
                tab.setTabTop(getHeight() / 2 - tab.getTextBounds().height() / 2 - dpToPx(12));
                //tab.setTabTop(0);
                tab.setTabBottom(getHeight() / 2 + tab.getTextBounds().height() / 2 + dpToPx(12));
            } else {
                RoundTab prev = tabs.get(i - 1);

                tab.setTabLeft(prev.getTabRight() + dpToPx(16));
                //tab.setTabLeft(0);
                if (!isCentering) {
                    tab.setBeforeCentering(tab.getTabLeft() + tab.getTextBounds().width() + dpToPx(16) * 2);
                    tab.setTabRight(tab.getTabLeft() + tab.getTextBounds().width() + dpToPx(16) * 2);
                } else
                    tab.setTabRight(tab.getTabLeft() + tab.getTextBounds().width() + dpToPx(16) * 2);
                tab.setTabTop(getHeight() / 2 - tab.getTextBounds().height() / 2 - dpToPx(12));
                //tab.setTabTop(0);
                tab.setTabBottom(getHeight() / 2 + tab.getTextBounds().height() / 2 + dpToPx(12));
            }

            tab.setParentWidth(getWidth());
            tab.setParentHeight(getHeight());
            tab.onDraw(canvas);
        }

        setCentered(false);
    }

    private void drawTab(Canvas canvas, String tabText) {
        Paint tabPaint = new Paint();
        Paint paint = new Paint();
        paint.setFakeBoldText(true);
        tabPaint.setAntiAlias(true);

        paint.setTextSize(spToPx(13));
        textBounds = new Rect();
        paint.getTextBounds(tabText, 0, tabText.length(), textBounds);

        tab = new RectF();
        tab.left = getWidth() / 2 - textBounds.width() / 2 - dpToPx(16);
        tab.right = getWidth() / 2 + textBounds.width() / 2 + dpToPx(16);
        tab.top = getHeight() / 2 - textBounds.height() / 2 - dpToPx(12);
        tab.bottom = getHeight() / 2 + textBounds.height() / 2 + dpToPx(12);
        //if (!clickedTab) {
        tabPaint.setStyle(Paint.Style.STROKE);
        tabPaint.setStrokeWidth(dpToPx(1.5f));
        tabPaint.setColor(0xffffffff);
        paint.setColor(0xffffffff);
        //} else {
        tabPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tabPaint.setColor(0xffffffff);
        paint.setColor(0xff37BBAD);
        tab.left -= dpToPx(1.5f / 2.0f);
        tab.right += dpToPx(1.5f / 2.0f);
        tab.top -= dpToPx(1.5f / 2.0f);
        tab.bottom += dpToPx(1.5f / 2.0f);
        //}

        canvas.drawRoundRect(tab, 50, 50, tabPaint);
        canvas.drawText(tabText, getWidth() / 2 - textBounds.width() / 2, getHeight() / 2 + textBounds.height() / 2, paint);
    }

    public void setCentered(boolean isCentered) {
        isCentering = isCentered;
        RoundTab last = tabs.get(tabs.size() - 1);
        if (isCentered && (last.beforeCentering + dpToPx(16)) < getWidth()) {
            float freeSpace = getWidth() - last.beforeCentering - dpToPx(16) * 2;
            tabs.get(0).setTabLeft(dpToPx(16) + freeSpace / 2);
        } else tabs.get(0).setTabLeft(dpToPx(16));
        invalidate();
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

    private void resetTabs(RoundTab tab) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < tabs.size(); i++) {
                    RoundTab tab = tabs.get(i);
                    if (isInBounds(event, tab.tab)) {
                        if (clickedTab != i)
                            tabs.get(clickedTab).clickedTab = false;
                        clickedTab = i;
                        tab.invertClicked();
                        invalidate();
                        return true;
                    }
                }
        }
        return false;
    }

    private boolean isInBounds(MotionEvent event, RectF tab) {
        return tab.contains((int) event.getX(), (int) event.getY());
    }

   /* private boolean isInBounds(MotionEvent event, RoundTab tab) {
        return event.getX() < tab.getTabRight() && event.getX() > tab.getTabLeft() && event.getY() < tab.getTabTop() && event.getY() > tab.getTabBottom();
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) dpToPx(48));
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        final LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        updateTabViewLayoutParams(lp);
        return lp;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
      /*  if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
            lp.width = 0;
            lp.weight = 1;
        } else {*/
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.weight = 0;
        //}
    }

    int spToPx(float sps) {
        return Math.round(getResources().getDisplayMetrics().scaledDensity * sps);
    }

    int dpToPx(float dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    private class TabStrip extends LinearLayout{

        public TabStrip(Context context) {
            super(context);
        }

        public TabStrip(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public TabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public TabStrip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
    }
}
