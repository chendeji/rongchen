package com.chendeji.rongchen.common.map.factory;

import android.content.Context;

import com.chendeji.rongchen.common.map.BaiduMap;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.ifactory.IMapFactory;

/**
 * Created by chendeji on 19/4/15.
 */
public class BaiduMapFactory implements IMapFactory{

    @Override
    public IMap getInstance(Context context) {
        return new BaiduMap(context);
    }

    @Override
    public void release() {
        //百度地图暂时不用
    }
}
