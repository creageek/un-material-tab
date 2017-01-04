package com.creageek.unmaterialtabs.tabs;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.creageek.unmaterialtabs.DimensUtils;
import com.creageek.unmaterialtabs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruslan Kishai aka creageek on 12/31/2016.
 */

public class RoundTabView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    List<RoundTab> tabs = new ArrayList<>();
    int clickedPosition = 0;
    LinearLayout ll;
    ViewPager viewPager;


    public RoundTabView(Context context) {
        super(context);
        addViews(context);
        setHorizontalScrollBarEnabled(false);
    }


    public RoundTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addViews(context);
        setHorizontalScrollBarEnabled(false);

    }

    public RoundTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addViews(context);
        setHorizontalScrollBarEnabled(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        addViews(context);
        setHorizontalScrollBarEnabled(false);

    }

    private void addViews(Context context) {
        ll = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
/*
        tabs.add(new RoundTab(context).initTab("RUSLAN IS AWESOME"));
        tabs.get(0).setClicked(true);

        tabs.add(new RoundTab(context).initTab("HEARTRATE"));
        tabs.add(new RoundTab(context).initTab("OXYGEN"));
        tabs.add(new RoundTab(context).initTab("EMOTIONAL"));
        tabs.add(new RoundTab(context).initTab("BATTERY"));
        for (RoundTab tab : tabs)
            ll.addView(tab, lp);*/

        this.addView(ll, lp);
    }

    public void setupWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        PagerAdapter adapter = viewPager.getAdapter();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;

        for (int i = 0; i < adapter.getCount(); i++) {
            String tabText = (String) adapter.getPageTitle(i);
            RoundTab tab = new RoundTab(getContext()).initTab(tabText.toUpperCase());
            if (i == 0) {
                tab.setClicked(true);
                tab.setTabBackgroundColor(0xffffffff);
            }
            tabs.add(tab);
        }

        for (RoundTab tab : tabs)
            ll.addView(tab, lp);
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
                        tabs.get(clickedPosition).setClicked(false);
                        animateFadeOut(tabs.get(clickedPosition));
                        tabs.get(clickedPosition).invalidate();
                        clickedPosition = finalI;
                        tab.setClicked(true);
                        animateFadeIn(tab);
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
        //scrollRight(position);
        if (position != clickedPosition && clickedPosition != -1) {
            tabs.get(clickedPosition).setClicked(false);
            animateFadeOut(tabs.get(clickedPosition));
            tabs.get(clickedPosition).invalidate();
            clickedPosition = position;
            tabs.get(position).setClicked(true);
            animateFadeIn(tabs.get(position));
            tabs.get(position).invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void animateFadeIn(final RoundTab tab) {
        int fromColor = getContext().getResources().getColor(R.color.colorPrimary);
        int toColor = getContext().getResources().getColor(R.color.colorWhite);

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimator.setDuration(250);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tab.setTabBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimator.start();
    }

    private void animateFadeOut(final RoundTab tab) {
        int fromColor = getContext().getResources().getColor(R.color.colorWhite);
        int toColor = getContext().getResources().getColor(R.color.colorPrimary);

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimator.setDuration(250);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tab.setTabBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimator.start();
    }

    private void scrollTabView(int position) {
        int previousPosition = 0;
        int currentPosition = 0;
        int nextPosition = 0;

        if (position == tabs.size() - 1)
            smoothScrollTo(getRight(), 0);

        if (position - 1 >= 0) {
            for (int i = 0; i < position - 1; i++) {
                previousPosition += tabs.get(i).getWidth();
            }
        }

        if (position + 1 < tabs.size()) {
            for (int i = 0; i < position + 1; i++) {
                nextPosition += tabs.get(i).getWidth();
            }
        }

        for (int i = 0; i <= position; i++)
            currentPosition += tabs.get(i).getWidth();

        if (previousPosition < getScrollX())
            smoothScrollTo(previousPosition, 0);
        else if (currentPosition > getRight())
            smoothScrollTo(previousPosition, 0);
        else if (nextPosition > getRight())
            smoothScrollTo(currentPosition, 0);
    }

    private void scrollRight(int position) {
        int previousPosition = 0;
        int currentPosition = 0;
        int nextPosition = 0;

        if (position == 0)
            smoothScrollTo(0, 0);

        if (position - 1 >= 0) {
            for (int i = tabs.size() - 1; i >= position - 1; i--) {
                previousPosition += tabs.get(i).getWidth();
            }
        }

        if (position + 1 < tabs.size()) {
            for (int i = tabs.size() - 1; i >= position + 1; i--) {
                nextPosition += tabs.get(i).getWidth();
            }
        }

        for (int i = tabs.size() - 1; i >= position; i--)
            currentPosition += tabs.get(i).getWidth();

        if (getWidth() - currentPosition < getLeft()) {
            smoothScrollTo(getWidth() - currentPosition, 0);
        } else if (getWidth() - nextPosition < getLeft()) {
            smoothScrollTo(getWidth() - nextPosition, 0);
        }
    }

    private void scrollTabs(int position) {
        int actualPosition = 0;
        int actualPosition2 = 0;

        if (position - 2 >= 0)
            for (int i = 0; i <= position - 2; i++)
                actualPosition2 += tabs.get(i).getWidth();
        else
            for (int i = 0; i <= position - 1; i++)
                actualPosition2 += tabs.get(i).getWidth();

        if (position + 1 < tabs.size())
            for (int i = 0; i <= position + 1; i++)
                actualPosition += tabs.get(i).getWidth();
        else
            for (int i = 0; i <= position; i++)
                actualPosition += tabs.get(i).getWidth();
        if (actualPosition > getWidth()) {
            final int finalActualPosition = actualPosition;
            this.post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(finalActualPosition, 0);
                }
            });
        } else if (actualPosition2 < getScrollX()) {
            final int finalActualPosition = actualPosition2;
            this.post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(finalActualPosition, 0);
                }
            });
        }
    }

    private void scrollTabsVariant(int position) {
        int actualPosition = 0;
        int actualPosition2 = 0;

        if (position - 2 >= 0)
            for (int i = 0; i <= position - 2; i++)
                actualPosition2 += tabs.get(i).getWidth();
        else
            for (int i = 0; i <= position - 1; i++)
                actualPosition2 += tabs.get(i).getWidth();

        if (position + 1 < tabs.size())
            for (int i = 0; i <= position + 1; i++)
                actualPosition += tabs.get(i).getWidth();
        else
            for (int i = 0; i <= position; i++)
                actualPosition += tabs.get(i).getWidth();
        if (actualPosition > getWidth()) {
            final int finalActualPosition = actualPosition - tabs.get(position).getWidth() - tabs.get(position - 1).getWidth();
            this.post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(finalActualPosition, 0);
                }
            });
        } else if (actualPosition2 < getScrollX()) {
            final int finalActualPosition = actualPosition2;
            this.post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(finalActualPosition, 0);
                }
            });
        }
    }
}

