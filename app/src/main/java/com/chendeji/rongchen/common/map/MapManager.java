package com.chendeji.rongchen.common.map;

import android.content.Context;

import com.chendeji.rongchen.common.interfase.StaticClassReleace;
import com.chendeji.rongchen.common.map.factory.GaodeMapFactory;
import com.chendeji.rongchen.common.map.ifactory.IMapFactory;
import com.chendeji.rongchen.model.Offset_Type;

/**
 * 管理地图类，地图单例
 * Created by chendeji on 30/4/15.
 */
public class MapManager implements StaticClassReleace{

    private static MapManager manager;
    private MapManager(){};
    public static MapManager getManager(){
        if (manager == null){
            manager = new MapManager();
        }
        return manager;
    }

    private IMapFactory factory;
    private Context mContext;

    private Offset_Type offset_type;

    public void setOffset_type(Offset_Type offset_type){
        this.offset_type = offset_type;
    }
    public Offset_Type getOffset_type(){
        return this.offset_type;
    }

    /**
     * 初始化地图管理类，并初始化地图工厂
     * @函数名称  :init
     * @brief
     * @see
     * @author  : chendeji
     * @date  : Thu Apr 30 11:08:00 CST 2015
     */
    public void init(Context context){
        this.mContext = context;
        if (factory == null) {
            factory = GaodeMapFactory.getFactory();
//            factory = new BaiduMapFactory();
        }
    }

//    public void startLocation(IMap.IMapLocationListener listener){
//        if (factory == null)
//            throw new NullPointerException("地图工厂实例化失败");
//        if (map == null)
//            map = factory.getInstance(mContext);
//        map.startLocation(listener);
//    }

    /**
     * 获取地图实例
     * @函数名称  :getMap
     * @brief
     * @see
     * @return {@link com.chendeji.rongchen.common.map.IMap}
     * @author  : chendeji
     * @date  : Thu Apr 30 11:06:54 CST 2015
     */
    public IMap getMap(){
        return factory.getInstance(mContext);
    }


    @Override
    public void release() {
        if (factory != null)
            factory.release();
        if (mContext != null){
            mContext = null;
        }
    }
}
