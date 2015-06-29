package com.chendeji.rongchen.server.imp;

import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.Sort;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.IMerchantOperation;
import com.chendeji.rongchen.server.request.MerchantRequest;

import java.io.IOException;
import java.util.List;

/**
 * @author chendeji
 * @Description:
 * @date 27/9/14 下午7:31
 * <p/>
 * ${tags}
 */
public class MerchantOperation implements IMerchantOperation {

    @Override
    public ReturnMes<List<Merchant>> findMerchant(int page, int limit, Offset_Type offset_type, Offset_Type out_offset_type, Platform platform) throws IOException, HttpException {
        MerchantRequest request = new MerchantRequest();
        return request.findMerchants(null, null, null, page, limit, offset_type, out_offset_type, platform);
    }

    @Override
    public ReturnMes<List<Merchant>> findMerchants(String city, String category, Sort sort, int page,
                                                   int limit, Offset_Type offset_type,
                                                   Offset_Type out_offset_type, Platform platform) throws IOException, HttpException {
        MerchantRequest request = new MerchantRequest();
        return request.findMerchants(city, category, sort, page, limit, offset_type, out_offset_type, platform);
    }

    @Override
    public ReturnMes<List<Merchant>> findMerchants(String city, String category, Sort sort, int page, int limit, Offset_Type offset_type, Platform platform) throws IOException, HttpException {
        MerchantRequest request = new MerchantRequest();
        return request.findMerchants(city, category, sort, page, limit, offset_type, platform);
    }

    @Override
    public ReturnMes<List<Merchant>> findMerchants(String category, Sort sort, int page, int limit, Offset_Type offset_type, Platform platform) throws IOException, HttpException {
        MerchantRequest request = new MerchantRequest();
        return request.findMerchants(category, sort, page, limit, offset_type, platform);
    }

    @Override
    public ReturnMes<Merchant> findSingleMerchant(long mMerchantID) throws IOException, HttpException {
        MerchantRequest request = new MerchantRequest();
        return request.findSingleMerchant(mMerchantID);
    }
}
