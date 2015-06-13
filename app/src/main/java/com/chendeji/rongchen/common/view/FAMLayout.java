package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;

/**
 * Created by chendeji on 9/6/15.
 */
public class FAMLayout extends ViewGroup {

    public static final int ORDER = 0;
    public static final int ALL = 1;

    private int mAnimationType;
    private int mRadius;

    public FAMLayout(Context context) {
        this(context,null,0);
    }

    public FAMLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FAMLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, defStyleAttr, 0);
        //可能是要适配int值和dp值之间的转换
        mRadius = (int) a.getDimension(R.styleable.FloatingActionMenu_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        mAnimationType = a.getInt(R.styleable.FloatingActionMenu_animation, ORDER);
        a.recycle();

        Logger.e("chendeji", "半径长度："+mRadius+"   动画类型："+mAnimationType);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        for (int i = 0; i < count ; i++){
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed){

        }

    }
}
