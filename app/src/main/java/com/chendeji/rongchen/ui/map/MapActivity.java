package com.chendeji.rongchen.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.rey.material.app.SimpleDialog;

import java.io.Serializable;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOCATION_KEY = "location";
    private static final String TAG = MapActivity.class.getSimpleName();

    private LinearLayout map_container;
    private IMap map;

    private Button take_bus;
    private Button drive;
    private Button walk;
    private double[] location;
    private SimpleDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initComponent();
        initEvent();
        map_container = (LinearLayout) findViewById(R.id.ll_map);
        map = MapManager.getManager().getMap();
        location = getIntent().getDoubleArrayExtra(LOCATION_KEY);
        map.show(this,map_container,savedInstanceState, location);
    }


//    @Override
//    public void onMarkClick() {
//        //TODO 显示一个Dialog提示是否设置为终点
//        builder = new SimpleDialog.Builder(R.style.SimpleDialog){
//
//            @Override
//            protected void onBuildDone(Dialog dialog) {
//                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            }
//
//            @Override
//            public void onPositiveActionClicked(DialogFragment fragment) {
//                //TODO 点击确定之后生成路线规划列表，从底部弹出。或者跳到另外一个activity界面去选择
//                startRoadPlan();
//                super.onPositiveActionClicked(fragment);
//            }
//
//            @Override
//            public void onNegativeActionClicked(DialogFragment fragment) {
//                super.onNegativeActionClicked(fragment);
//            }
//        };
//
//        builder.message(getString(R.string.begin_local_navigation))
//                .title(getString(R.string.begin_navigation))
//                .positiveAction(getString(R.string.positive))
//                .negativeAction(getString(R.string.negative));
//        DialogFragment.newInstance(builder).show(getSupportFragmentManager(), null);
//    }

    private void startRoute(int route_type) {
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtra(RouteActivity.ROUTE_TYPE, route_type);
        intent.putExtra(RouteActivity.END_POINT_LOCATION_KEY, location);
        startActivityForResult(intent, RouteActivity.REQUEST_KEY);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode == RESULT_OK){
            Serializable routeResult = intent.getSerializableExtra(RouteActivity.Route);
            Logger.i(TAG, "routeResult:"+requestCode);
            map.showRoute(routeResult);
        }
        super.startActivityForResult(intent, requestCode);
    }

    private void initEvent() {
        take_bus.setOnClickListener(this);
        drive.setOnClickListener(this);
        walk.setOnClickListener(this);
    }

    private void initComponent() {
        take_bus = (Button) findViewById(R.id.bt_take_bus);
        drive = (Button) findViewById(R.id.bt_drive);
        walk = (Button) findViewById(R.id.bt_walk);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        if (builder != null){
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
        int mRoute = 0;
        switch (v.getId()){
            case R.id.bt_take_bus:
                //乘坐公交
                mRoute = RouteActivity.BUS_ROUTE;
                break;
            case R.id.bt_drive:
                //开车
                mRoute = RouteActivity.CAR_ROUTE;
                break;
            case R.id.bt_walk:
                //步行
                mRoute = RouteActivity.WALK_ROUTE;
                break;
        }
        startRoute(mRoute);
    }

}
