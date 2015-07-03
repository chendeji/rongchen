package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.internal.view.ViewPropertyAnimatorCompatSet;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.AnimationUtil;
import com.chendeji.rongchen.common.util.Logger;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 需要注意的地方就是主要点击按钮必须要第一个添加进来
 * Created by chendeji on 9/6/15.
 */
public class FAMLayout extends ViewGroup {

    public static final String TAG = FAMLayout.class.getSimpleName();
    public static final int ORDER = 0;
    public static final int ALL = 1;
    private double mAngle;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;

    private int mAnimationType;
    private int mRadius;

//    private OnClickListener mMainChildClickListener;
//    public void setMainViewOnClickListener(View.OnClickListener listener){
//        this.mMainChildClickListener = listener;
//    }

    private OnClickListener mChildClickListener;
    private boolean isChildMenuOpen;

    public void setChildViewOnClickListener(View.OnClickListener listener) {
        this.mChildClickListener = listener;
    }

    public FAMLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FAMLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FAMLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FAMLayout, defStyleAttr, 0);
        //可能是要适配int值和dp值之间的转换
        mRadius = (int) a.getDimension(R.styleable.FAMLayout_fam_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        mAnimationType = a.getInt(R.styleable.FAMLayout_fam_animation, ORDER);
        a.recycle();

        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
        Logger.e("chendeji", "半径长度：" + mRadius + "   动画类型：" + mAnimationType);
    }

    class MainViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //
            if (isChildMenuOpen) {
                isChildMenuOpen = false;
                hideChildView(200);
            } else {
                isChildMenuOpen = true;
                showChildView(200);
            }
        }
    }

    private void hideChildView(long duration) {
        int childCount = getChildCount() - 1;
        AnimatorSet set = null;
        double angle = Math.PI / 2 / (getChildCount() - 2);
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i + 1);
            int childLeft = (int) (mRadius * Math.abs(Math.cos(angle * i)));
            int childTop = (int) (mRadius * Math.abs(Math.sin(angle * i)));

            AnimationSet animationSet = new AnimationSet(false);

            Animation tranAnima = AnimationUtil.getTransAnima(0, childLeft, 0, childTop, duration, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    child.setVisibility(View.INVISIBLE);
                    child.setClickable(false);
                    child.setFocusable(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation alphaAnima = AnimationUtil.getAlphaAnima(duration, 1, 0);

            Animation rotateAnima = AnimationUtil.getRotaAnima(duration, 360, 0, Animation.RELATIVE_TO_SELF
                    , 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            animationSet.addAnimation(rotateAnima);
            animationSet.addAnimation(tranAnima);
            animationSet.addAnimation(alphaAnima);
            animationSet.setStartOffset(i * 30);
            child.startAnimation(animationSet);
        }
    }

    private void showChildView(long duration) {
        int childCount = getChildCount() - 1;
        AnimatorSet set = null;
        double angle = Math.PI / 2 / (getChildCount() - 2);
        for (int i = 0; i < childCount; i++) {
            final int temi = i;
            final View child = getChildAt(i + 1);
            int childLeft = (int) (mRadius * Math.abs(Math.cos(angle * i)));
            int childTop = (int) (mRadius * Math.abs(Math.sin(angle * i)));

            AnimationSet animationSet = new AnimationSet(false);
            Animation tranAnima = AnimationUtil.getTransAnima(childLeft, 0, childTop, 0, duration, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    child.setVisibility(View.VISIBLE);
                    child.setClickable(true);
                    child.setFocusable(true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation alphaAnima = AnimationUtil.getAlphaAnima(duration, 0, 1);

            Animation rotateAnima = AnimationUtil.getRotaAnima(duration, 0, 360, Animation.RELATIVE_TO_SELF
                    , 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            animationSet.addAnimation(rotateAnima);
            animationSet.addAnimation(tranAnima);
            animationSet.addAnimation(alphaAnima);
            animationSet.setStartOffset(i * 30);
            child.startAnimation(animationSet);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int angles = childCount - 2;
        double strangeAngle = Math.PI / 2;
        mAngle = strangeAngle / angles;     //每个子按钮之间的角度

        int parentHeigh = 0;
        int parentWidth = 0;

        int mainButtonHeigth = 0;
        int mainButtonWidth = 0;

        if ((widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED)
                || (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED)) {
            //自己计算父容器的宽高
            View child;
            for (int i = 0; i < childCount; i++) {
                child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                int childHeight = child.getMeasuredHeight();
                int childWidth = child.getMeasuredWidth();
                if (i == 0) {
                    //按钮
                    mainButtonHeigth = childHeight;
                    mainButtonWidth = childWidth;
                    parentHeigh += mainButtonHeigth;
                    parentWidth += mainButtonWidth;
                    child.setOnClickListener(new MainViewClickListener());
                    continue;
                }

                double cos = Math.abs(Math.cos((i - 1) * mAngle));
                double sin = Math.abs(Math.sin((i - 1) * mAngle));

                int width = (int) (mRadius * cos + childWidth / 2 + mainButtonWidth / 2) + childWidth;
                parentWidth = Math.max(parentWidth, width);

                int height = (int) (mRadius * sin + childHeight / 2 + mainButtonHeigth / 2) + childHeight;
                parentHeigh = Math.max(height, parentHeigh);

                child.setOnClickListener(mChildClickListener);
            }
        }

        int finalWidth = (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) ? parentWidth : widthSize;
        int finalHeigth = (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) ? parentHeigh : heightSize;

        setMeasuredDimension(finalWidth + getPaddingLeft() + getPaddingRight(), finalHeigth + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Logger.i(TAG, "onLayout:--------" + "left:" + l + "top:" + t + "right:" + r + "bottom:" + b);
        if (changed) {
            int childcount = getChildCount();
            int mainButtonWidth = 0;
            int mainButtonHeight = 0;
            for (int i = 0; i < childcount; i++) {
                int childLeft = 0;
                int childTop = 0;
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                int childHeigth = child.getMeasuredHeight();
                if (i == 0) {
                    //主按钮
                    mainButtonHeight = childHeigth;
                    mainButtonWidth = childWidth;
                    childLeft = getMeasuredWidth() - childWidth;
                    childTop = getMeasuredHeight() - childHeigth;
                    child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeigth);
                    continue;
                } else {
                    child.setVisibility(View.INVISIBLE);
                    //开始计算其他在圆弧上的子VIEW的位置
                    double sin = Math.abs(Math.sin((i - 1) * mAngle));
                    double cos = Math.abs(Math.cos((i - 1) * mAngle));

                    childLeft = (int) (getMeasuredWidth() - mainButtonWidth / 2 - mRadius * cos - childWidth / 2);
                    childTop = (int) (getMeasuredHeight() - mainButtonHeight / 2 - mRadius * sin - childHeigth / 2);
                }
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeigth);
            }
        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
