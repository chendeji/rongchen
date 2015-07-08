package com.chendeji.rongchen.common.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.city.City;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeji on 19/4/15.
 */
public class GaodeMap implements IMap, LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener, PoiSearch.OnPoiSearchListener {
    private static final String TAG = GaodeMap.class.getSimpleName();

    private Context mContext;
    private LocationManagerProxy proxy;

    private double[] location;
    private IMapLocationListener mListener;

    private MapView mapView;
    private OnLocationChangedListener onLocationChangeListener;
    private AMap map;
    private boolean isStartNavigation;
    private int mMap_State = IMap.MAP_STATE_LOCATION;


    private Marker locationMarker;
    private Marker merchantMarker;
    private LatLng mMerchantLocation;   //商户的地址
    private RouteSearch routeSearch;
    private OnSearchRouteListener mOnSearchRouteListener;
    private DrivingRouteOverlay drivingRouteOverlay;
    private WalkRouteOverlay walkRouteOverlay;
    private BusRouteOverlay busRouteOverlay;
    private OnMarkClickListener mOnMarkClickListener;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private AMapLocation mAMapLocation;
    private PoiOverlay poiOverlay;

    public GaodeMap(Context context) {
        this.mContext = context;
    }

    public String getLocalCity() {
        return SettingFactory.getInstance().getCurrentCity();
    }

    @Override
    public void release() {
        if (mContext != null)
            mContext = null;
        if (mListener != null)
            mListener = null;
        if (proxy != null) {
            proxy = null;
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangeListener = onLocationChangedListener;
        if (proxy == null) {
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
        if (aMapLocation == null || aMapLocation.getAMapException().getErrorCode() != 0) {
            mListener.onLocationFail();
            return;
        }
        this.mAMapLocation = aMapLocation;

        String city = aMapLocation.getCity();
        double latitude = aMapLocation.getLatitude();
        double longitude = aMapLocation.getLongitude();

        Logger.i(TAG, "latitude:" + latitude + "longitude:" + longitude);

        //TODO 设置标识，是否在路线规划
        if (onLocationChangeListener != null && isStartNavigation) {
            //如果开启路线规划，那么就要进行跳跃到手机当前坐标
            onLocationChangeListener.onLocationChanged(aMapLocation);
        }

//        locationMarker.setPosition(new LatLng(latitude,longitude));

        if (latitude != 0 && longitude != 0) {
            location = new double[]{latitude, longitude};

            if (locationMarker != null) {
                Logger.i("chendeji", "更新定位marker位置");
                locationMarker.setPosition(new LatLng(latitude, longitude));
                //TODO 设置旋转角度
                //locationMarker.setRotateAngle();
            }

            mListener.onLocationSuccece(city, aMapLocation.getLatitude(), aMapLocation.getLongitude());
        }
        if (location == null) {
            mListener.onLocationFail();
        }

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
    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void startPoiSearch(String tip) {
        mMap_State = IMap.MAP_STATE_POISEARCH;
        //开启POI搜索
        query = new PoiSearch.Query(tip, "", SettingFactory.getInstance().getCurrentCity());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页

        poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void unregisteListener() {
        if (mListener != null) {
            mListener = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        deactivate();
    }

    @Override
    public void show(Context context, ViewGroup map_container, Bundle savedInstanceState, double[] location) {
        this.mContext = context;
        this.mMerchantLocation = new LatLng(location[0], location[1]);
        if (mapView == null) {
            mapView = new MapView(context);
            mapView.onCreate(savedInstanceState);
            map_container.addView(mapView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (map == null) {
                map = mapView.getMap();

                MyLocationStyle style = new MyLocationStyle();
                style.anchor(0.5f, 1.0f);
                style.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_48dp));
                style.radiusFillColor(context.getResources().getColor(android.R.color.transparent));
                style.strokeWidth(0);
                map.setMyLocationStyle(style);
                map.setOnMarkerClickListener(this);

                map.setLocationSource(this);
//                map.getUiSettings().setMyLocationButtonEnabled(true);
//                map.setMyLocationEnabled(true);
                map.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

                addMerchantMarke();
                addLocationMarke();

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mMerchantLocation, 18));
            }
        } else {
            mapView.onResume();
        }
    }

    private void addMerchantMarke() {
        merchantMarker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp))
                .draggable(false)
                .position(mMerchantLocation));
    }

    private void addLocationMarke() {
        locationMarker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                .draggable(false));
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
        if (locationMarker != null) {
            locationMarker.remove();
            locationMarker = null;
        }
        if (merchantMarker != null) {
            merchantMarker.remove();
            merchantMarker = null;
        }

        if (map != null) {
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
    public void showRoute(Object path, int routeType) {
        if (path == null)
            return;
        isStartNavigation = true;
        removeRouteOverlay();
        removePOIOverlay();
        if (location == null) {
            location = SettingFactory.getInstance().getCurrentLocation();
        }
        switch (routeType) {
            case BUS_ROUTE:
                BusPath busPath = (BusPath) path;
                busRouteOverlay = new BusRouteOverlay(mContext, map,
                        busPath, new LatLonPoint(location[0], location[1]),
                        new LatLonPoint(mMerchantLocation.latitude, mMerchantLocation.longitude));
                busRouteOverlay.addToMap();
                busRouteOverlay.zoomToSpan();
                break;
            case CAR_ROUTE:
                DrivePath drivePath = (DrivePath) path;
                drivingRouteOverlay = new DrivingRouteOverlay(mContext, map,
                        drivePath, new LatLonPoint(location[0], location[1]),
                        new LatLonPoint(mMerchantLocation.latitude, mMerchantLocation.longitude));
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
                break;
            case WALK_ROUTE:
                WalkPath walkPath = (WalkPath) path;
                walkRouteOverlay = new WalkRouteOverlay(mContext, map,
                        walkPath, new LatLonPoint(location[0], location[1]),
                        new LatLonPoint(mMerchantLocation.latitude, mMerchantLocation.longitude));
                walkRouteOverlay.addToMap();
                walkRouteOverlay.zoomToSpan();
                break;
        }

        addLocationMarke();
    }

    private void removePOIOverlay() {
        if (poiOverlay != null){
            poiOverlay.removeFromMap();
        }
    }

    @Override
    public void startRoute(Context context, double[] start_point_location, double[] end_point_location, int route_type) {
        if (routeSearch == null) {
            routeSearch = new RouteSearch(context);
            routeSearch.setRouteSearchListener(this);
        }
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                new LatLonPoint(start_point_location[0], start_point_location[1]),
                new LatLonPoint(end_point_location[0], end_point_location[1]));

        if (mOnSearchRouteListener != null)
            mOnSearchRouteListener.onBeginSearch();

        mMap_State = IMap.MAP_STATE_ROUTE;

        switch (route_type) {
            case BUS_ROUTE:
                RouteSearch.BusRouteQuery busRouteQuery = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, getLocalCity(), 0);
                routeSearch.calculateBusRouteAsyn(busRouteQuery);
                break;
            case CAR_ROUTE:
                RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, getLocalCity());
                routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
                break;
            case WALK_ROUTE:
                RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
                routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
                break;
        }

    }

    @Override
    public void setOnSearchRouteListener(OnSearchRouteListener listener) {
        this.mOnSearchRouteListener = listener;
    }

    @Override
    public void setOnMarkClickListener(OnMarkClickListener listener) {
        this.mOnMarkClickListener = listener;
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
        this.mListener = listener;
        proxy = LocationManagerProxy.getInstance(mContext);
        proxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, this);
    }

    @Override
    public double[] getLocation() {
        if (mAMapLocation != null){
            return new double[]{mAMapLocation.getLatitude(),mAMapLocation.getLongitude()};
        }
        return null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        if (mOnSearchRouteListener != null) {
            mOnSearchRouteListener.onSearcheDone(IMap.BUS_ROUTE, busRouteResult);
        }
        removeRouteOverlay();
//        if (busRouteResult != null && !busRouteResult.getPaths().isEmpty()) {
//            busRouteOverlay = new BusRouteOverlay(mContext, map, busRouteResult.getPaths().get(0),
//                    busRouteResult.getStartPos(), busRouteResult.getTargetPos());
//            busRouteOverlay.addToMap();
//            busRouteOverlay.zoomToSpan();
//        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (mOnSearchRouteListener != null) {
            mOnSearchRouteListener.onSearcheDone(IMap.CAR_ROUTE, driveRouteResult);
        }
        isStartNavigation = true;
        removeRouteOverlay();
        //加载路径到地图上去
//        drivingRouteOverlay = new DrivingRouteOverlay(mContext, map,
//                driveRouteResult.getPaths().get(0), driveRouteResult.getStartPos(),
//                driveRouteResult.getTargetPos());
//        drivingRouteOverlay.addToMap();
//        drivingRouteOverlay.zoomToSpan();
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (mOnSearchRouteListener != null) {
            mOnSearchRouteListener.onSearcheDone(IMap.WALK_ROUTE, walkRouteResult);
        }
        removeRouteOverlay();
//        walkRouteOverlay = new WalkRouteOverlay(mContext, map,
//                walkRouteResult.getPaths().get(0), walkRouteResult.getStartPos(),
//                walkRouteResult.getTargetPos());
//        walkRouteOverlay.addToMap();
//        walkRouteOverlay.zoomToSpan();
    }

    private void removeRouteOverlay() {
        if (busRouteOverlay != null)
            busRouteOverlay.removeFromMap();
        if (drivingRouteOverlay != null)
            drivingRouteOverlay.removeFromMap();
        if (walkRouteOverlay != null)
            walkRouteOverlay.removeFromMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //地图Marker点击事件
        marker.showInfoWindow();
        String title = marker.getTitle();
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;
        if (mMap_State == IMap.MAP_STATE_LOCATION
                || mMap_State == IMap.MAP_STATE_POISEARCH) {
            if (mOnMarkClickListener != null) {
                mOnMarkClickListener.onMarkClick(title, latitude, longitude);
            }
        }
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult != null && poiResult.getQuery() != null) {
            if (poiResult.getQuery().equals(query)) {
                List<PoiItem> poiItems = poiResult.getPois();
                if (poiItems != null && poiItems.size() > 0) {
                    map.clear();// 清理之前的图标
                    poiOverlay = new PoiOverlay(map, poiItems);
                    poiOverlay.removeFromMap();
                    poiOverlay.addToMap();
                    poiOverlay.zoomToSpan();
                } else {
                    ToastUtil.showLongToast(mContext, mContext.getString(R.string.no_result));
                }
            }
        } else {
            ToastUtil.showLongToast(mContext, mContext.getString(R.string.no_result));
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

    }
}
