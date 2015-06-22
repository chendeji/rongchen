package com.chendeji.rongchen.server.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.server.httpCommon.DPHttpRequestClient;
import com.chendeji.rongchen.server.parser.MerchantListParser;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.Sort;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.parser.ErrorInfoParser;
import com.chendeji.rongchen.server.parser.MerchantParser;

/**
 * @author chendeji
 * @Description: 后台访问商户数据的具体操作实现
 * @date 27/9/14 下午7:35
 * <p/>
 * ${tags}
 */
public final class MerchantRequest{

    public ReturnMes<List<Merchant>> findMerchants(String city, String category, Sort sort,int page, int limit
            , Offset_Type offset_type, Platform platform) throws IOException, HttpException{
        return findMerchants(city,category,sort,page,limit,offset_type,offset_type,platform);
    }

    public ReturnMes<List<Merchant>> findMerchants(String category, Sort sort,int page, int limit
            , Offset_Type offset_type, Platform platform) throws IOException, HttpException{
        return findMerchants(null,category,sort,page,limit,offset_type,offset_type,platform);
    }

    public ReturnMes<List<Merchant>> findMerchants(String city, String category, Sort sort,int page, int limit
            , Offset_Type offset_type, Offset_Type out_offset_type
            , Platform platform) throws IOException, HttpException {

        IMap map = MapManager.getManager().getMap();

        StringBuilder url = UrlConfigerUtil.getDPBaseUrl();
        UrlConfigerUtil.addPath(url, AppConst.MERCHANT_BASE_URL);
        UrlConfigerUtil.addPath(url, AppConst.FIND_MERCHANTS);

        //拼接参数
        HashMap<String,String> parame = new HashMap<String, String>();
        if(!TextUtils.isEmpty(category)){
            parame.put("category", category);
        }
        if(sort != Sort.NONE){
            parame.put("sort", String.valueOf(sort.getValue()));
        }
        parame.put("page", String.valueOf(page));
        parame.put("limit", String.valueOf(limit));


        if(offset_type != Offset_Type.DEFUALT){
            parame.put("offset_type", String.valueOf(offset_type.getValue()));
            if (offset_type == Offset_Type.GAODE){
                double[] location = map.getLocation();
                if (location != null){
                    double latitude = location[0];
                    double longitude = location[1];
                    parame.put("latitude",String.valueOf(latitude));
                    parame.put("longitude",String.valueOf(longitude));
                }
            }
        }
        if(out_offset_type != Offset_Type.DEFUALT){
            parame.put("out_offset_type", String.valueOf(out_offset_type.getValue()));
        }

        parame.put("platform", String.valueOf(platform.getValue()));

        //如果有传入城市也将城市作为参数发送出去
        if(!TextUtils.isEmpty(city)){
            parame.put("city", city);
        }

        //发送请求并获取服务器返回结果
        StringBuilder receive = new StringBuilder();
        int retCode = DPHttpRequestClient.doGet(url.toString(), parame, receive);
        JSONObject object = JSON.parseObject(receive.toString());
        if(retCode == HttpStatus.SC_OK){
            if(UrlConfigerUtil.isStatusOK(object)){
                List<Merchant> merchantList = new MerchantListParser().parse(object);
                ReturnMes returnMes = new ReturnMes(retCode,"");
                returnMes.status = object.getString(AppConst.ReturnMesConst.STATUS);
                returnMes.object = merchantList;
                return returnMes;
            }else{
               ReturnMes returnMes = new ErrorInfoParser().parse(object);
               return returnMes;
            }
        }else{
            //服务端请求失败
            throw new HttpException(retCode, receive.toString());
        }
    }

    public ReturnMes<Merchant> findSingleMerchant(long mMerchantID) throws IOException, HttpException {

        StringBuilder url = UrlConfigerUtil.getDPBaseUrl();
        UrlConfigerUtil.addPath(url, AppConst.MERCHANT_BASE_URL);
        UrlConfigerUtil.addPath(url, AppConst.GET_SINGLE_BUSINESS);

        //拼接参数
        HashMap<String,String> parame = new HashMap<String, String>();
        if (mMerchantID < 0){
            throw new IllegalArgumentException();
        }

        parame.put("business_id", String.valueOf(mMerchantID));
        Offset_Type offset_type = MapManager.getManager().getOffset_type();
        parame.put("out_offset_type", String.valueOf(offset_type.getValue()));
        parame.put("platform", String.valueOf(Platform.HTML5.getValue()));

        //发送请求并获取服务器返回结果
        StringBuilder receive = new StringBuilder();
        int retCode = DPHttpRequestClient.doGet(url.toString(), parame, receive);
        JSONObject object = JSON.parseObject(receive.toString());
        if(retCode == HttpStatus.SC_OK){
            if(UrlConfigerUtil.isStatusOK(object)){
                //解析
                Merchant merchant = new MerchantParser().parse(object);
                ReturnMes returnMes = new ReturnMes(retCode,"");
                returnMes.status = object.getString(AppConst.ReturnMesConst.STATUS);
                returnMes.object = merchant;
                return returnMes;
            }else{
                ReturnMes returnMes = new ErrorInfoParser().parse(object);
                return returnMes;
            }
        } else {
            //服务端请求失败
            throw new HttpException(retCode, receive.toString());
        }
    }
}
