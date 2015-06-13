package com.chendeji.rongchen.server.parser;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chendeji
 * @Description:
 * @date 29/9/14 下午10:47
 * <p/>
 * ${tags}
 */
public interface Parsable<T> {

    T parse(JSONObject object);
}
