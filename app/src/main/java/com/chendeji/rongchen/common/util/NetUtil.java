package com.chendeji.rongchen.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author chendeji
 * @Description: 访问网络工具类
 * @date 30/10/14 下午11:33
 * <p/>
 * ${tags}
 */
public final class NetUtil {

    /**
     * 检查是否有网络
     * @param context 上下文句柄
     * @return 是否有网络
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con == null) {
            return false;
        }
        NetworkInfo workinfo = con.getActiveNetworkInfo();
        if (workinfo == null || !workinfo.isAvailable()) {
            return false;
        }
        return true;
    }

}
