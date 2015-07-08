package com.chendeji.rongchen.ui.map.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.core.AMapLocException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.common.util.DistanceUtil;
import com.chendeji.rongchen.common.util.ToastUtil;

/**
 * Created by chendeji on 18/6/15.
 */
public class CommonRouteListView extends RelativeLayout implements AdapterView.OnItemClickListener {

    private RouteResult mRouteResult;
    private final int mRouteType;
    private RouteAdapter adapter;
    private View loadingView;
    private ListView routeList;

    public static final String PATH = "path";
    public static final String TYPE = "type";

    public CommonRouteListView(Context context, RouteResult routeResult, int route_type) {
        super(context);
        this.mRouteResult = routeResult;
        this.mRouteType = route_type;
        init();
        initEvent();
    }

    private void initEvent() {
        routeList.setOnItemClickListener(this);
    }

    public int getRouteCount() {
        return adapter.getCount();
    }

    public void setRouteResult(RouteResult routeResult) {
        this.mRouteResult = routeResult;
        adapter.notifyDataSetChanged();
        hideLoading();
    }

    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.route_list_layout, this, true);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        loadingView = view.findViewById(R.id.rl_loading_layout);
        routeList = (ListView) findViewById(R.id.lv_route_list);
        if (adapter == null) {
            adapter = new RouteAdapter();
        }
        routeList.setAdapter(adapter);
        showLoading();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RouteResult result = (RouteResult) parent.getItemAtPosition(position);
        LatLonPoint startPos = result.getStartPos();
        LatLonPoint targetPos = result.getTargetPos();
        double[] start = new double[]{startPos.getLatitude(), startPos.getLongitude()};
        double[] end = new double[]{targetPos.getLatitude(), targetPos.getLongitude()};

        IMap map = MapManager.getManager().getMap();
        try {
            switch (mRouteType) {
                case IMap.BUS_ROUTE:
                    BusRouteResult busRouteResult = (BusRouteResult) result;
                    map.showRoute(busRouteResult.getPaths().get(position), IMap.BUS_ROUTE);
                    break;
                case IMap.CAR_ROUTE:
                    DriveRouteResult driveRouteResult = (DriveRouteResult) result;
                    map.showRoute(driveRouteResult.getPaths().get(position), IMap.CAR_ROUTE);
                    break;
                case IMap.WALK_ROUTE:
                    WalkRouteResult walkRouteResult = (WalkRouteResult) result;
                    map.showRoute(walkRouteResult.getPaths().get(position), IMap.WALK_ROUTE);
                    break;
            }
        } catch (AMapLocException e) {
            String errorMsg = e.getErrorMessage();
            ToastUtil.showLongToast(getContext(), errorMsg);
        }
        ((Activity) getContext()).finish();
    }

    class RouteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (mRouteResult != null) {
                switch (mRouteType) {
                    case IMap.BUS_ROUTE:
                        BusRouteResult busRouteResult = (BusRouteResult) mRouteResult;
                        count = busRouteResult.getPaths().size();
                        break;
                    case IMap.CAR_ROUTE:
                        DriveRouteResult driveRouteResult = (DriveRouteResult) mRouteResult;
                        count = driveRouteResult.getPaths().size();
                        break;
                    case IMap.WALK_ROUTE:
                        WalkRouteResult walkRouteResult = (WalkRouteResult) mRouteResult;
                        count = walkRouteResult.getPaths().size();
                        break;
                }
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            return mRouteResult;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.route_item_layout, null);
                holder.lineName = (TextView) convertView.findViewById(R.id.tv_route_line_name);
                holder.lineDetail = (TextView) convertView.findViewById(R.id.tv_route_line_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            switch (mRouteType) {
                case IMap.BUS_ROUTE:
                    BusRouteResult busRouteResult = (BusRouteResult) mRouteResult;
                    BusPath busPath = busRouteResult.getPaths().get(position);
                    String busLineName = busPath.getSteps().get(0).getBusLine().getBusLineName();
                    int bus_duration = (int) (busPath.getDuration() / 60);
                    String bus_distance = DistanceUtil.getDistance(busPath.getDistance() / 1000);
                    int cost = (int) busPath.getCost();
                    holder.lineName.setText(busLineName);
                    holder.lineDetail.setText(bus_duration + "分钟 | " + bus_distance + "公里 | " + cost + "元");
                    break;
                case IMap.CAR_ROUTE:
                    DriveRouteResult driveRouteResult = (DriveRouteResult) mRouteResult;
                    DrivePath drivePath = driveRouteResult.getPaths().get(position);
                    String roadName = drivePath.getSteps().get(0).getInstruction();
                    String drive_distance = DistanceUtil.getDistance(drivePath.getDistance() / 1000);
                    int drive_duration = (int) (drivePath.getDuration() / 60);
                    float toll = drivePath.getSteps().get(0).getTolls();
                    holder.lineName.setText(roadName);
                    holder.lineDetail.setText(drive_duration + "分钟 | " + drive_distance + "公里 | " + toll + "元");
                    break;
                case IMap.WALK_ROUTE:
                    WalkRouteResult walkRouteResult = (WalkRouteResult) mRouteResult;
                    WalkPath walkPath = walkRouteResult.getPaths().get(position);
                    String walk_road_name = walkRouteResult.getPaths().get(0).getSteps().get(0).getInstruction();
                    int walk_duration = (int) (walkPath.getDuration() / 60);
                    holder.lineName.setText(walk_road_name);
                    holder.lineDetail.setText(walk_duration + "分钟");
                    break;
            }


            return convertView;
        }

        class ViewHolder {
            TextView lineName;
            TextView lineDetail;
        }
    }


}
