package com.chendeji.rongchen.common.map;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.chendeji.rongchen.common.interfase.StaticClassReleace;
import com.chendeji.rongchen.ui.map.RouteActivity;

import java.io.Serializable;

/**
 * Created by chendeji on 19/4/15.
 */
public interface IMap extends StaticClassReleace{

    public void unregisteListener();

    void show(Context context, ViewGroup map_container, Bundle savedInstanceState, double[] location);

    void onPause();

    String getLocalCity();

    void onResume();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void showRoute(Serializable serializableExtra);

    void startRoute(Context context, double[] start_point_location, double[] end_point_location, int route_type);

//    void setOnMarkeClickListener(OnMapMarkeClickListener listener);

    interface IMapLocationListener{
        void onLocationSuccece();
        void onLocationFail();
    }
    //能够进行定位
    public void startLocation(IMapLocationListener listener);
    //获取到坐标
    public double[] getLocation();
}
