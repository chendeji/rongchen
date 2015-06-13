package com.chendeji.rongchen.server.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.groupbuy.Deal;

import java.util.List;

/**
 * Created by chendeji on 27/5/15.
 */
public class DealParser implements Parsable<Deal> {
    public static final String DEAL_KEY = "deals";

    @Override
    public Deal parse(JSONObject object) {
        Deal deal = null;
        if(object == null || object.isEmpty()){
            return null;
        }
        String array = object.getString(DEAL_KEY);
        if (!TextUtils.isEmpty(array)){
            List<Deal> deals = JSON.parseArray(array, Deal.class);
            deal = deals.get(0);
            return deal;
        }

        return null;
    }
}
