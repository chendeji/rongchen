package com.chendeji.rongchen.common.map.ifactory;

import android.content.Context;

import com.chendeji.rongchen.common.interfase.StaticClassReleace;
import com.chendeji.rongchen.common.map.IMap;

/**
 * Created by chendeji on 19/4/15.
 */
public interface IMapFactory extends StaticClassReleace{
    //能够获取地图实例

    public IMap getInstance(Context context);
}
