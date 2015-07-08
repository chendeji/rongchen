package com.chendeji.rongchen.ui.map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.WalkRouteResult;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.view.CursorView;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.map.adapter.RoutePagerAdapter;
import com.chendeji.rongchen.ui.map.view.CommonRouteListView;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, IMap.OnSearchRouteListener {

    private static final String TAG = RouteActivity.class.getSimpleName();

    public static final int REQUEST_KEY = 100;
    private int mRouteType;

    public static final String END_POINT_LOCATION_KEY = "end_point_location";
    public static final String ROUTE_TYPE = "route_type";
    public static final String Route = "route_result";
    private Toolbar routeToolBar;
    private TextView endAddress;
    private ViewPager routeViewPager;
    private RadioGroup route_group;

    private CommonRouteListView busRoutes;     //公交车路线
    private CommonRouteListView carRoutes;     //汽车路线
    private CommonRouteListView walkRoutes;    //步行路线
    private int route_type;

    private double[] end_point_location;
    private double[] start_point_location;
    private Merchant merchant;
    private IMap iMap;
    private AutoCompleteTextView startPointInput;
    private ArrayAdapter aAdapter;
    private CursorView direction;
    private int prePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.translucentStatusBar(this);
        setContentView(R.layout.activity_road_line_planning);

        iMap = MapManager.getManager().getMap();
        start_point_location = iMap.getLocation();
        route_type = getIntent().getIntExtra(RouteActivity.ROUTE_TYPE, IMap.BUS_ROUTE);
        merchant = (Merchant) getIntent().getSerializableExtra(RouteActivity.END_POINT_LOCATION_KEY);
        end_point_location = new double[]{merchant.latitude, merchant.longitude};

        routeToolBar = (Toolbar) findViewById(R.id.tb_route);
        routeToolBar.setTitle(getString(R.string.check_route));
        setSupportActionBar(routeToolBar);

        initComponent();
        initEvent();

        setData();
        startRoute();
    }

    private void startRoute() {
        if (start_point_location != null) {
            iMap.startRoute(this, start_point_location, end_point_location, IMap.BUS_ROUTE);
            iMap.startRoute(this, start_point_location, end_point_location, IMap.CAR_ROUTE);
            iMap.startRoute(this, start_point_location, end_point_location, IMap.WALK_ROUTE);

            busRoutes.showLoading();
            carRoutes.showLoading();
            walkRoutes.showLoading();
        } else {
            ToastUtil.showLongToast(this, "定位环境差，请用户手动输入位置信息");
        }
    }

    private void setData() {
        endAddress.setText(merchant.address);
    }

    private void initEvent() {
        direction.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                clearButtonColor();
                int cursorWidth = direction.getMeasuredWidth();
                switch (route_type) {
                    case IMap.BUS_ROUTE:
                        routeViewPager.setCurrentItem(0, false);
                        direction.setTranslateX(cursorWidth * 0);
                        ((RadioButton) route_group.getChildAt(0)).setTextColor(getResources().getColor(R.color.common_button_textcolor));
                        break;
                    case IMap.CAR_ROUTE:
                        routeViewPager.setCurrentItem(1, false);
                        direction.setTranslateX(cursorWidth * 1);
                        ((RadioButton) route_group.getChildAt(1)).setTextColor(getResources().getColor(R.color.common_button_textcolor));
                        break;
                    case IMap.WALK_ROUTE:
                        routeViewPager.setCurrentItem(2, false);
                        direction.setTranslateX(cursorWidth * 2);
                        ((RadioButton) route_group.getChildAt(2)).setTextColor(getResources().getColor(R.color.common_button_textcolor));
                        break;
                }
                direction.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        startPointInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tip = (String) parent.getItemAtPosition(position);
                iMap.startPoiSearch(tip);
                //在开始POI搜索之后，关闭掉当前的页面，然后去地图中选择一个marker作为起始点
                finish();
            }
        });
        startPointInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                // 输入信息的回调方法
                Inputtips inputTips = new Inputtips(RouteActivity.this,
                        new Inputtips.InputtipsListener() {

                            @Override
                            public void onGetInputtips(List<Tip> list, int rCode) {
                                List listString = new ArrayList();
                                for (int i = 0; i < list.size(); i++) {
                                    listString.add(list.get(i).getName());
                                }
                                aAdapter = new ArrayAdapter(
                                        getApplicationContext(),
                                        R.layout.route_inputs, listString);
                                startPointInput.setAdapter(aAdapter);
                                aAdapter.notifyDataSetChanged();

                            }
                        });
                try {
                    // 发送输入提示请求
                    // 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
                    inputTips.requestInputtips(newText, SettingFactory.getInstance().getCurrentCity());
                } catch (AMapException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        route_group.setOnCheckedChangeListener(this);
        routeViewPager.addOnPageChangeListener(this);
        iMap.setOnSearchRouteListener(this);
    }

    private void initComponent() {

//        myLocation = (TextView) findViewById(R.id.tv_my_location);
        startPointInput = (AutoCompleteTextView) findViewById(R.id.et_start_point_input);
        //商户地址
        endAddress = (TextView) findViewById(R.id.tv_end_address);
        //三种类型的路线规划
        routeViewPager = (ViewPager) findViewById(R.id.vp_route);
        route_group = (RadioGroup) findViewById(R.id.rg_route_type_group);
        direction = (CursorView) findViewById(R.id.cv_pager_cursor);

        if (start_point_location != null) {
            startPointInput.setText(getString(R.string.my_location));
        }

        List<View> views = new ArrayList<>();
        if (busRoutes == null) {
            busRoutes = new CommonRouteListView(this, null, IMap.BUS_ROUTE);
            views.add(busRoutes);
        }
        if (carRoutes == null) {
            carRoutes = new CommonRouteListView(this, null, IMap.CAR_ROUTE);
            views.add(carRoutes);
        }
        if (walkRoutes == null) {
            walkRoutes = new CommonRouteListView(this, null, IMap.WALK_ROUTE);
            views.add(walkRoutes);
        }

        routeViewPager.setAdapter(new RoutePagerAdapter(this, views));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_road_line_planning, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        clearButtonColor();
        // 切换viewpager
        ((RadioButton) group.findViewById(checkedId)).setTextColor(getResources().getColor(R.color.common_button_textcolor));
        switch (checkedId) {
            case R.id.rt_bus_button:
                routeViewPager.setCurrentItem(0, true);
                if (busRoutes.getRouteCount() == 0) {
                    if (start_point_location == null)
                        return;
                    busRoutes.showLoading();
                    iMap.startRoute(this, start_point_location, end_point_location, IMap.BUS_ROUTE);
                }
                break;
            case R.id.rt_car_button:
                routeViewPager.setCurrentItem(1, true);
                if (carRoutes.getRouteCount() == 0) {
                    if (start_point_location == null)
                        return;
                    carRoutes.showLoading();
                    iMap.startRoute(this, start_point_location, end_point_location, IMap.CAR_ROUTE);
                }
                break;
            case R.id.rt_walk_button:
                routeViewPager.setCurrentItem(2, true);
                if (walkRoutes.getRouteCount() == 0) {
                    if (start_point_location == null)
                        return;
                    walkRoutes.showLoading();
                    iMap.startRoute(this, start_point_location, end_point_location, IMap.WALK_ROUTE);
                }
                break;
        }
        // 加载路径规划

    }

    private void clearButtonColor() {
        int color = getResources().getColor(R.color.black);
        RadioButton bus = (RadioButton) findViewById(R.id.rt_bus_button);
        RadioButton drive = (RadioButton) findViewById(R.id.rt_car_button);
        RadioButton walk = (RadioButton) findViewById(R.id.rt_walk_button);
        bus.setTextColor(color);
        drive.setTextColor(color);
        walk.setTextColor(color);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffsetPixels != 0) {
            int currentCursorPosition = position * direction.getWidth();
            int delt = positionOffsetPixels / 3;
            if (prePosition > position) {
                //向右滑动 position - 1
                direction.setTranslateX(currentCursorPosition + (direction.getWidth() - delt));
            } else {
                //向左滑动 position + 1
                direction.setTranslateX(currentCursorPosition + delt);
            }
            if (positionOffset == 0) {
                prePosition = position;
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        clearButtonColor();
        RadioButton button = (RadioButton) route_group.getChildAt(position);
        button.setTextColor(getResources().getColor(R.color.common_button_textcolor));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onBeginSearch() {
        //TODO 在加载路径规划开始的时候做的事情
        //显示加载进度条对话框
        Logger.d(TAG, "onBeginSearch");
    }

    @Override
    public void onSearcheDone(int routeType, RouteResult result) {
        Logger.d(TAG, "onSearchDone");
        //关闭进度条对话框
        if (result == null) {
            // 提示数据错误
        }
        // 显示数据
        switch (routeType) {
            case IMap.BUS_ROUTE:
                BusRouteResult busRouteResult = (BusRouteResult) result;
                setBusRroute(busRouteResult);
                break;
            case IMap.CAR_ROUTE:
                DriveRouteResult driveRouteResult = (DriveRouteResult) result;
                setDriveRoute(driveRouteResult);
                break;
            case IMap.WALK_ROUTE:
                WalkRouteResult walkRouteResult = (WalkRouteResult) result;
                setWalkRoute(walkRouteResult);
                break;
        }
    }

    private void setWalkRoute(WalkRouteResult walkRouteResult) {
        walkRoutes.setRouteResult(walkRouteResult);
    }

    private void setDriveRoute(DriveRouteResult driveRouteResult) {
        carRoutes.setRouteResult(driveRouteResult);
    }

    private void setBusRroute(BusRouteResult busRouteResult) {
        busRoutes.setRouteResult(busRouteResult);
    }


}
