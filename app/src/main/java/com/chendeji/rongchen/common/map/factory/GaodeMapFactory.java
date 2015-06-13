package com.chendeji.rongchen.common.map.factory;

import android.content.Context;

import com.chendeji.rongchen.common.map.GaodeMap;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.ifactory.IMapFactory;

/**
 * Created by chendeji on 19/4/15.
 */
public class GaodeMapFactory implements IMapFactory {

    private static IMap map;
    private static IMapFactory factory;
    private GaodeMapFactory(){}
    public static IMapFactory getFactory(){
        if (factory == null)
            factory = new GaodeMapFactory();
        return factory;
    }

    @Override
    public IMap getInstance(Context context) {
        if (map == null)
            map = new GaodeMap(context);
        return map;
    }

    @Override
    public void release() {
        if (map != null){
            map.release();
            map = null;
        }
        if (factory != null){
            factory = null;
        }
    }
}
