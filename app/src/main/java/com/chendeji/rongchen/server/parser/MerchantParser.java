package com.chendeji.rongchen.server.parser;

import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.merchant.Merchant;

import java.util.List;

/**
 * Created by chendeji on 1/6/15.
 */
public class MerchantParser implements Parsable<Merchant>{
    @Override
    public Merchant parse(JSONObject object) {
        List<Merchant> merchants = new MerchantListParser().parse(object);
        return merchants.get(0);
    }
}
