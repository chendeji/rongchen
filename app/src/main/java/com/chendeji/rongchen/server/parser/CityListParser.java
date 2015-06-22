package com.chendeji.rongchen.server.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CityListParser implements Parsable<List<String>> {

    public static final String CITY_KEY = "cities";
    @Override
    public List<String> parse(JSONObject object) {

        if(object == null || object.isEmpty()){
            return null;
        }
        String array = object.getString(CITY_KEY);
        if (!TextUtils.isEmpty(array)){
            List<String> cities = JSON.parseArray(array, String.class);
            return cities;
        }

        return null;
    }
}
