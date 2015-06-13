package com.chendeji.rongchen.common.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;

import java.io.Serializable;

/**
 * Created by chendeji on 19/4/15.
 */
public class GaodeMap implements IMap, LocationSource, AMapLocationListener {
    private static final String TAG = GaodeMap.class.getSimpleName();

    private Context mContext;
    private LocationManagerProxy proxy;

    private double[] location;
    private boolean isStartedLocation;
    private IMapLocationListener mListener;

    private MapView mapView;
    private OnLocationChangedListener onLocationChangeListener;
    private AMap map;


    private Marker locationMarker;
    private Marker merchantMarker;
    private LatLng mMerchantLocation;   //商户的地址
    private boolean isShowMerchantPosition;

    public GaodeMap(Context context) {
        this.mContext = context;
    }

    @Override
    public void release() {
        if (mContext != null)
            mContext = null;
        isStartedLocation = false;
        if (mListener != null)
            mListener = null;
        if (proxy != null) {
            proxy = null;
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangeListener = onLocationChangedListener;
        if (proxy == null){
            proxy = LocationManagerProxy.getInstance(mContext);
            proxy.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        if (proxy != null) {
            proxy.removeUpdates(this);
            proxy.destroy();
        }
        proxy = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            mListener.onLocationFail();
            return;
        }
        double latitude = aMapLocation.getLatitude();
        double longitude = aMapLocation.getLongitude();
        Logger.i(TAG, "latitude:" + latitude + "longitude:" + longitude);

        //TODO 设置标识，是否在路线规划
        if (onLocationChangeListener != null && !isShowMerchantPosition) {
            //如果开启路线规划，那么就要进行跳跃到手机当前坐标
            onLocationChangeListener.onLocationChanged(aMapLocation);
        }

        if (isShowMerchantPosition){
            CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(mMerchantLocation)
                    .zoom(18)
                    .build());
            isShowMerchantPosition = false;
        }

        if (latitude != 0 && longitude != 0) {
            location = new double[]{latitude, longitude};

            if (locationMarker != null){
                locationMarker.setPosition(new LatLng(latitude, longitude));
                //TODO 设置旋转角度
                //locationMarker.setRotateAngle();
            }

            mListener.onLocationSuccece();
        }
        if (location == null) {
            mListener.onLocationFail();
        }
        isStartedLocation = false;

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void unregisteListener() {
        if (mListener != null) {
            mListener = null;
        }
        if (mContext != null){
            mContext = null;
        }
        deactivate();
    }

    @Override
    public void show(Context context, ViewGroup map_container, Bundle savedInstanceState, double[] location) {
        this.mContext = context;
        this.isShowMerchantPosition = true;
        this.mMerchantLocation = new LatLng(location[0], location[1]);
        if (mapView == null) {
            mapView = new MapView(context);
            mapView.onCreate(savedInstanceState);
            map_container.addView(mapView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (map == null){
                map = mapView.getMap();

                MyLocationStyle style = new MyLocationStyle();
                style.anchor(0.5f,1.0f);
                style.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_48dp));
                style.radiusFillColor(context.getResources().getColor(android.R.color.transparent));
                style.strokeWidth(0);
                map.setMyLocationStyle(style);

                map.setLocationSource(this);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setMyLocationEnabled(true);
                map.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

                merchantMarker = map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp))
                        .draggable(false)
                        .position(mMerchantLocation));
                merchantMarker.setTitle(context.getString(R.string.click_me));


//                locationMarker = map.addMarker(new MarkerOptions()
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_48dp))
//                        .draggable(false));

//                initMarkerEvent();

            }
        } else {
            mapView.onResume();
        }
    }

//    private void initMarkerEvent() {
//        map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Logger.d(TAG, "mark点击");
//                String markerID = marker.getId();
//                if (markerID.equals(merchantMarker.getId())){
//                    //要做什么让界面去决定
//                    mOnMarkClickListener.onMarkClick();
//                }
//                // 定位marker不知道是否需要进行点击交互操作
//                return true;
//            }
//        });
//    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
            deactivate();
        }
    }

    @Override
    public void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (locationMarker != null){
            locationMarker.remove();
            locationMarker = null;
        }
        if (merchantMarker != null){
            merchantMarker.remove();
            merchantMarker = null;
        }

        if (map != null){
            map.clear();
            map = null;
        }
        if (mapView != null) {
            mapView.onDestroy();
            deactivate();
            mapView = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void showRoute(Serializable serializableExtra) {
        if (serializableExtra == null)
            return;

    }

//    @Override
//    public void setOnMarkeClickListener(OnMapMarkeClickListener listener) {
//        this.mOnMarkClickListener = listener;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        if (mapView != null){
//            mapView.onCreate(savedInstanceState);
//        }
//    }

    @Override
    public void startLocation(IMapLocationListener listener) {
        if (isStartedLocation)
            return;
        isStartedLocation = true;
        this.mListener = listener;
        proxy = LocationManagerProxy.getInstance(mContext);
        proxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, this);
    }

    @Override
    public double[] getLocation() {
        if (location == null)
            throw new NullPointerException("定位失敗");
        return location;
    }
}
