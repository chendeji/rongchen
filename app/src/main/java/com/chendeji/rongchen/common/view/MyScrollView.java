package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.chendeji.rongchen.common.util.Logger;

/**
 * Created by chendeji on 23/6/15.
 */
public class MyScrollView extends ScrollView {

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                starRowY = ev.getRawY();
//                startY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                deltY = ev.getRawY() - starRowY;
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        Logger.i("chendeji", "oldt:"+oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
