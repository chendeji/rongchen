package com.chendeji.rongchen.server.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.ReturnMes;

import com.chendeji.rongchen.server.AppConst;

import java.util.List;

/**
 * @author chendeji
 * @Description:
 * @date 29/9/14 下午11:10
 * <p/>
 * ${tags}
 */
public class ErrorInfoParser implements Parsable<ReturnMes> {

    @Override
    public ReturnMes parse(JSONObject object) {
        ReturnMes<List<Merchant>> returnMes = new ReturnMes<List<Merchant>>();
        ErrorInfo info = new ErrorInfo();
        String error = object.getString(AppConst.ReturnMesConst.ERROR);
        returnMes.errorInfo = JSON.parseObject(error, ErrorInfo.class);
        return returnMes;
    }
}
