package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;

/**
 * Created by chendeji on 19/6/15.
 */
public class AutoHorizontalLinearLayout extends ViewGroup {

    private static final String TAG = AutoHorizontalLinearLayout.class.getSimpleName();

    private final int SPACE = 10;
    private int mSpace;

    public AutoHorizontalLinearLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AutoHorizontalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AutoHorizontalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoHorizontalLinearLayout);
        mSpace = (int) a.getDimension(R.styleable.AutoHorizontalLinearLayout_space, SPACE);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量所有的子view
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        //一行宽度
        int rowWidth = 0;
        //一行高度
        int rowHeight = 0;

        //wrap_content
        if (widthMode == MeasureSpec.AT_MOST
                || heightMode == MeasureSpec.AT_MOST) {
            //这个时候需要自己计算宽度和高度
            //先计算每个子view的测量宽高
            int childCount = getChildCount();
            View childView;
            for (int i = 0; i < childCount; i++) {
                childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                int childHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                if (rowWidth > widthSize) {
                    //换行,记录当前得到的最宽的宽度
                    width = Math.max(width, rowWidth);
                    //现在是已经换行了，那么当前的行宽就是现在的子View的宽度
                    rowWidth = childWidth + mSpace;
                    //换行了，那么就要加上一行的高度
                    rowHeight = childHeight + mSpace;
                    height += rowHeight;
                } else {
                    rowWidth += childWidth + mSpace;
                    rowHeight = Math.max(rowHeight, childHeight + mSpace);
                    height = Math.max(height, rowHeight);
                }
            }
        }

        Logger.d(TAG, "width : " + width);
        Logger.d(TAG, "height : " + height);
        Logger.d(TAG, "widthSize : " + widthSize);
        Logger.d(TAG, "heightSize : " + heightSize);

        int finalWidth = widthMode == MeasureSpec.AT_MOST ? width : widthSize;
        int finalHeight = heightMode == MeasureSpec.AT_MOST ? height : heightSize;
        Logger.d(TAG, "finalWidth:" + finalWidth);
        Logger.d(TAG, "finalHeight:" + finalHeight);

        setMeasuredDimension(finalWidth, finalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed)
            return;
        int childCount = getChildCount();
        int width = getWidth();

        int rowWidth = 0;
        int rowHeith = 0;
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (rowWidth > width){

            }

        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
    }
}
