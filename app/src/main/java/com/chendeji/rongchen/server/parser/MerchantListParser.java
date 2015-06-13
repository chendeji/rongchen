package com.chendeji.rongchen.server.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.model.merchant.Merchant;

import java.util.List;

/**
 * @author chendeji
 * @Description:
 * @date 29/9/14 下午10:49
 * <p/>
 * ${tags}
 */
public class MerchantListParser implements Parsable<List<Merchant>> {
    private static final String MERCHANT_KEY = "businesses";

    @Override
    public List<Merchant> parse(JSONObject object) {
        // 解析商户信息
        List<Merchant> merchants = null;
        if(object == null || object.isEmpty()){
            return null;
        }
        String array = object.getString(MERCHANT_KEY);
        Logger.i("chendeji",array);
        if (array != null){
            merchants = JSON.parseArray(array, Merchant.class);
            return merchants;
        }
        return null;
    }
}
