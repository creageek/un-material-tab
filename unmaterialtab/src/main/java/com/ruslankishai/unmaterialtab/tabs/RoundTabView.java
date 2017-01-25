package com.ruslankishai.unmaterialtab.tabs;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ruslankishai.unmaterialtab.DimensUtils;
import com.ruslankishai.unmaterialtab.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruslan Kishai aka creageek on 12/31/2016.
 */

public class RoundTabView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    public static final int ANIMATION_FADE_IN = 1;
    public static final int ANIMATION_FADE_OUT = 0;
    List<RoundTab> tabs = new ArrayList<>();
    int clickedPosition = 0;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout tabStrip;
    ViewPager viewPager;

    int tabBackColor, tabStrokeColor;

    public RoundTabView(Context context) {
        super(context);
        addViews(context);
        setHorizontalScrollBarEnabled(false);
    }

    public RoundTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundTabView);
        tabStrokeColor = typedArray.getColor(R.styleable.RoundTabView_accent, 0xff464646);
        typedArray.recycle();

        try {
            ColorDrawable tabViewColor = (ColorDrawable) getBackground();
            tabBackColor = tabViewColor.getColor();
        } catch (ClassCastException exception) {
            Log.d("TAB", "Class Cast Exception");
            tabBackColor = 0x00ffffff;
        }

        addViews(context);
        setHorizontalScrollBarEnabled(false);
    }

    private void addViews(Context context) {
        tabStrip = new LinearLayout(context);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        tabStrip.setLayoutParams(layoutParams);
        tabStrip.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(tabStrip, layoutParams);
    }

    public void setupWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        PagerAdapter adapter = viewPager.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            String tabText = (String) adapter.getPageTitle(i);
            RoundTab tab = new RoundTab(getContext()).initTab(tabText.toUpperCase());
            if (i == viewPager.getCurrentItem()) {
                tab.setTabBackgroundColor(tabStrokeColor);
                tab.setTabTextColor(tabBackColor);
            } else {
                tab.setTabBackgroundColor(tabBackColor);
                tab.setTabTextColor(tabStrokeColor);
            }
            tab.setTabStrokeColor(tabStrokeColor);
            tabs.add(tab);
        }
        for (RoundTab tab : tabs)
            tabStrip.addView(tab, layoutParams);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (int i = 0; i < tabs.size(); i++) {
            final RoundTab tab = tabs.get(i);
            tab.setParentHeight(getHeight());
            final int finalI = i;
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI != clickedPosition && clickedPosition != -1) {
                        animateFade(tabs.get(clickedPosition), ANIMATION_FADE_OUT);
                        tabs.get(clickedPosition).invalidate();
                        clickedPosition = finalI;
                        animateFade(tab, ANIMATION_FADE_IN);
                        tab.invalidate();
                        viewPager.setCurrentItem(finalI);
                    }
                }
            });
            if (i == 0) {
                tab.setFirst(true);
                tab.requestLayout();
            } else if (i == tabs.size() - 1) {
                tab.setLast(true);
                tab.requestLayout();
            }
            tab.invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (DimensUtils.dpToPx(getContext(), 48)));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        scrollTabView(position);
        if (position != clickedPosition && clickedPosition != -1) {
            animateFade(tabs.get(clickedPosition), ANIMATION_FADE_OUT);
            tabs.get(clickedPosition).invalidate();
            clickedPosition = position;
            animateFade(tabs.get(position), ANIMATION_FADE_IN);
            tabs.get(position).invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void animateFade(final RoundTab tab, int animType) {
        int colorFrom, colorTo, textColorFrom, textColorTo;

        switch (animType) {
            case ANIMATION_FADE_IN:
            default:
                colorFrom = 0x00ffffff;
                colorTo = tabStrokeColor;
                textColorFrom = tabStrokeColor;
                textColorTo = tabBackColor;
                break;
            case ANIMATION_FADE_OUT:
                textColorFrom = tabBackColor;
                textColorTo = tabStrokeColor;
                colorFrom = tabStrokeColor;
                colorTo = 0x00ffffff;
                break;
        }

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimator.setDuration(250);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tab.setTabBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimator.start();

        ValueAnimator textAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), textColorFrom, textColorTo);
        textAnimator.setDuration(250);
        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tab.setTabTextColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        textAnimator.start();
    }

    private void scrollTabView(int position) {
        int previousPosition = 0;
        int currentPosition = 0;
        int nextPosition = 0;

        if (position == tabs.size() - 1)
            smoothScrollTo(getRight(), 0);

        if (position - 1 >= 0)
            for (int i = 0; i < position - 1; i++)
                previousPosition += tabs.get(i).getWidth();

        if (position + 1 < tabs.size())
            for (int i = 0; i < position + 1; i++)
                nextPosition += tabs.get(i).getWidth();

        for (int i = 0; i <= position; i++)
            currentPosition += tabs.get(i).getWidth();

        if (previousPosition < getScrollX())
            smoothScrollTo(previousPosition, 0);
        else if (currentPosition > getRight())
            smoothScrollTo(previousPosition, 0);
        else if (nextPosition > getRight())
            smoothScrollTo(currentPosition, 0);
    }
}