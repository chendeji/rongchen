package com.chendeji.rongchen.ui.map.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chendeji on 17/6/15.
 */
public class RoutePagerAdapter extends PagerAdapter{

    private final Context mContext;
    private final List<View> mViews;

    public RoutePagerAdapter(Context context, List<View> views){
        this.mContext = context;
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
