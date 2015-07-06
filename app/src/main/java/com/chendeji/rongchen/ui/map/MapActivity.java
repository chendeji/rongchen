package com.chendeji.rongchen.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.route.Path;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.AnimationUtil;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.map.view.CommonRouteListView;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.FloatingActionButton;

import java.io.Serializable;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, IMap.OnMarkClickListener {

    public static final String LOCATION_KEY = "location";
    private static final String TAG = MapActivity.class.getSimpleName();

    private LinearLayout map_container;
    private IMap map;

    private FloatingActionButton take_bus;
    private FloatingActionButton drive;
    private FloatingActionButton walk;
    private SimpleDialog.Builder builder;
    private Merchant merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initComponent();
        initEvent();
        map_container = (LinearLayout) findViewById(R.id.ll_map);
        map = MapManager.getManager().getMap();
        merchant = (Merchant) getIntent().getSerializableExtra(LOCATION_KEY);
        map.show(this, map_container, savedInstanceState, new double[]{merchant.latitude, merchant.longitude});
        map.setOnMarkClickListener(this);
        showRouteButton();
    }

    private void startRoute(int route_type) {
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtra(RouteActivity.ROUTE_TYPE, route_type);
        intent.putExtra(RouteActivity.END_POINT_LOCATION_KEY, merchant);
        startActivity(intent);
    }

    private void initEvent() {
        take_bus.setOnClickListener(this);
        drive.setOnClickListener(this);
        walk.setOnClickListener(this);
    }

    private void initComponent() {
        take_bus = (FloatingActionButton) findViewById(R.id.bt_take_bus);
        drive = (FloatingActionButton) findViewById(R.id.bt_drive);
        walk = (FloatingActionButton) findViewById(R.id.bt_walk);


        take_bus.setClickable(false);
        take_bus.setFocusable(false);
        take_bus.setVisibility(View.INVISIBLE);

        drive.setClickable(false);
        drive.setFocusable(false);
        drive.setVisibility(View.INVISIBLE);

        walk.setClickable(false);
        walk.setFocusable(false);
        walk.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * 显示底部的三个按钮动画
     */
    private void showRouteButton() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) take_bus.getLayoutParams();
                int bottomPadding = lp.bottomMargin;
                int height = take_bus.getHeight();
                Animation bus_anim = generateAnima(take_bus, bottomPadding + height, 0, 50);
                take_bus.startAnimation(bus_anim);

                Animation drive_anim = generateAnima(drive, bottomPadding + height, 0, 2 * 50);
                drive.startAnimation(drive_anim);

                Animation walk_anim = generateAnima(walk, bottomPadding + height, 0, 3 * 50);
                walk.startAnimation(walk_anim);
            }
        }, 1500);
    }

    private Animation generateAnima(View view, int from, int to, int starOffset) {
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        view.setFocusable(true);
        Animation tranAnima = AnimationUtil.getTransAnima(0, 0, from, to, 200, null);
        tranAnima.setInterpolator(new OvershootInterpolator());
        tranAnima.setStartOffset(starOffset);
        return tranAnima;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        if (builder != null) {
            builder = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_map, menu);
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
    public void onClick(View v) {
        //1，点击进入到路径规划界面，驾车和步行也一样。
        int mRoute = 0;
        switch (v.getId()) {
            case R.id.bt_take_bus:
                //乘坐公交
                mRoute = IMap.BUS_ROUTE;
                break;
            case R.id.bt_drive:
                mRoute = IMap.CAR_ROUTE;
                //开车
                //直接显示导航路径
//                map.startRoute(this, start, end, IMap.CAR_ROUTE);
//                NaviPara para = new NaviPara();
//                para.setTargetPoint(new LatLng(merchant.latitude, merchant.longitude));
//                try {
//                    AMapUtils.openAMapNavi(para, this);
//                } catch (AMapException e) {
//                    ToastUtil.showLongToast(this, e.getErrorMessage());
//                }
                break;
            case R.id.bt_walk:
                mRoute = IMap.WALK_ROUTE;
                //步行
                //直接显示导航路径
//                map.startRoute(this, start, end, IMap.WALK_ROUTE);
                break;
        }
        startRoute(mRoute);
    }

    @Override
    public void onMarkClick(String title, final double latitude, final double longitude) {
        //显示一个对话框，是否要设置这个该点为起始位置
        if (TextUtils.isEmpty(title))
            return;
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                SettingFactory.getInstance().setCurrentLocation(latitude,longitude);
                super.onPositiveActionClicked(fragment);
                //进入路径规划界面，并显示相应的路径
                Intent intent = new Intent(MapActivity.this, RouteActivity.class);
                intent.putExtra(RouteActivity.END_POINT_LOCATION_KEY, merchant);
                startActivity(intent);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        builder.title(getString(R.string.set_location))
                .positiveAction(getString(R.string.positive))
                .negativeAction(getString(R.string.negative));
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }
}
