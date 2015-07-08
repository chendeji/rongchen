package com.chendeji.rongchen.common.map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.amap.api.location.core.AMapLocException;
import com.amap.api.services.route.RouteResult;
import com.chendeji.rongchen.common.interfase.StaticClassReleace;
import com.chendeji.rongchen.ui.map.RouteActivity;

import java.io.Serializable;

/**
 * Created by chendeji on 19/4/15.
 */
public interface IMap extends StaticClassReleace{

    public static final int CAR_ROUTE = 0;
    public static final int BUS_ROUTE = 1;
    public static final int WALK_ROUTE = 2;

    public static final int MAP_STATE_LOCATION = 3;
    public static final int MAP_STATE_ROUTE = 4;
    public static final int MAP_STATE_POISEARCH = 5;


    void setContext(Context context);

    void setCity(String city);

    /**
     * 用于在地图生成POI搜索的Marker的方法
     * @param tip 搜索关键字
     */
    void startPoiSearch(String tip);

    interface OnSearchRouteListener{
        void onBeginSearch();
        void onSearcheDone(int routeType, RouteResult result);
    }

    interface OnMarkClickListener{
        /**
         * 当地图上的图标被点击的时候调用
         * @param title 点击地点的地址内容
         * @param latitude 点击图标的坐标
         * @param longitude 点击图标的坐标
         */
        void onMarkClick(String title, double latitude, double longitude);
    }

    public void unregisteListener();

    void show(Context context, ViewGroup map_container, Bundle savedInstanceState, double[] location);

    void onPause();

    String getLocalCity();

    void onResume();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void showRoute(Object path, int routeType) throws AMapLocException;

    void startRoute(Context context, double[] start_point_location, double[] end_point_location, int route_type);

    void setOnSearchRouteListener(OnSearchRouteListener listener);

    void setOnMarkClickListener(OnMarkClickListener listener);

//    void setOnMarkeClickListener(OnMapMarkeClickListener listener);

    interface IMapLocationListener{
        void onLocationSuccece(String city, double latitude, double longitude);
        void onLocationFail();
    }
    //能够进行定位
    public void startLocation(IMapLocationListener listener);
    //获取到坐标
    public double[] getLocation();
}
