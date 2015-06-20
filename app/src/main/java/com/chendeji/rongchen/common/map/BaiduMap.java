package com.chendeji.rongchen.common.map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by chendeji on 19/4/15.
 */
public class BaiduMap implements IMap {

    private Context mContext;
    private double[] location ;

    private IMapLocationListener listener;

    public BaiduMap(Context context){
        this.mContext = context;
    }

    @Override
    public void release() {

    }

    @Override
    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void unregisteListener() {
        if (listener != null){
            listener = null;
        }
    }

    @Override
    public void show(Context context, ViewGroup map_container, Bundle savedInstanceState, double[] location) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public String getLocalCity() {
        return null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void showRoute(Object path, int routeType) {

    }

    @Override
    public void startRoute(Context context, double[] start_point_location, double[] end_point_location, int route_type) {

    }

    @Override
    public void setOnSearchRouteListener(OnSearchRouteListener listener) {

    }

//    @Override
//    public void setOnMarkeClickListener(OnMapMarkeClickListener listener) {
//
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//    }

    @Override
    public void startLocation(IMapLocationListener listener) {

    }

    @Override
    public double[] getLocation() {
        if (location == null)
            throw new NullPointerException("定位失敗");
        return location;
    }
}
