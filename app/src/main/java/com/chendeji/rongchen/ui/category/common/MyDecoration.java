package com.chendeji.rongchen.ui.category.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chendeji on 25/6/15.
 */
public class MyDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public MyDecoration(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}
