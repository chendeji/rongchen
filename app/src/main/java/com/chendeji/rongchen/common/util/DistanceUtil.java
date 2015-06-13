package com.chendeji.rongchen.common.util;

import java.text.DecimalFormat;

/**
 * @author chendeji
 * @Description:
 * @date 11/11/14 下午11:25
 * <p/>
 * ${tags}
 */
public class DistanceUtil {

    /**
     * 500米距离
     */
    public static final int _500M = 500;
    /**
     * 1000米距离
     */
    public static final int _1KM = 1000;

    private static final String _500MSTR = "<0.5km";
    private static final String _1000MSTR = "<1km";

    private static final DecimalFormat mFormat = new DecimalFormat("####.#");

    /**
     * 精确度到小数点后一位，0.1km 不足0.5km的“<0.5km”,大于0.5km，小于1km的“<1km”。等于1km的直接是1km，大于1km的精确到小数点后一位。
     *
     * @param distance 商户距离当前用户坐标的距离
     * @return
     */
    public static String getDistance(int distance) {

        if (distance <= _500M ){
            return _500MSTR;
        }

        if (distance > _500M && distance < _1KM) {
            return _1000MSTR;
        }

        if (distance >= _1KM ) {
            return mFormat.format((float)distance / _1KM);
        }

        return null;
    }
}
