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

    private int par_padding_left;
    private int par_padding_right;
    private int par_padding_top;
    private int par_padding_bottom;

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

        par_padding_left = getPaddingLeft();
        par_padding_right = getPaddingRight();
        par_padding_top = getPaddingTop();
        par_padding_bottom = getPaddingBottom();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量所有的子view
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = par_padding_top;

        //一行宽度
        int rowWidth = 0;
        //一行高度
        int rowHeight = 0;

        //wrap_content
        if ((widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED)
                || (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED)) {
            //这个时候需要自己计算宽度和高度
            //先计算每个子view的测量宽高
            int childCount = getChildCount();
            View childView;
            for (int i = 0; i < childCount; i++) {
                childView = getChildAt(i);
                int visitable = childView.getVisibility();
                if (visitable == View.GONE)
                    continue;
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                int childHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

                //1，添加宽度
                //2，判断宽度是否超过父类布局的宽度
                //3，超过换行添加行高，没超过继续添加行宽

                rowWidth += childWidth + mSpace;
                rowHeight = childHeight + mSpace;
                if (rowWidth > widthSize) {
                    //换行,记录当前得到的最宽的宽度
                    width = Math.max(width, rowWidth);
                    //现在是已经换行了，那么当前的行宽就是现在的子View的宽度
                    rowWidth = childWidth + mSpace;
                    //换行了，那么就要加上一行的高度
                    height += rowHeight;
                } else {
                    //如果没有换行，那么也要记录行高
                    height = Math.max(height, rowHeight);
                }
            }
            //循环遍历完之后要将最后一行下面的space减掉
            height -= mSpace;
        }

        int finalWidth = (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) ? width : widthSize;
        int finalHeight = (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) ? height : heightSize;

        setMeasuredDimension(finalWidth, finalHeight + par_padding_bottom + par_padding_top);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed)
            return;

        int childCount = getChildCount();
        int row = 0;

        int childRight = par_padding_left;     //子View的相对父控件的左边的距离
        int childBottom = 0;    //子view的相对父控件的顶部的距离

        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            childRight += childWidth + mSpace;
            if (childRight > r) {
                childRight = childWidth + mSpace + par_padding_left;
                row++;
            }

            childBottom = row * (childHeight + mSpace) + childHeight + mSpace + par_padding_top;
            child.layout(childRight - (childWidth + mSpace), childBottom - (childHeight + mSpace), childRight - mSpace, childBottom - mSpace);
        }


    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
    }
}
