package com.chendeji.rongchen.server.iinterface;

import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;

import java.io.IOException;
import java.util.List;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.Sort;
import com.chendeji.rongchen.server.HttpException;

/**
 * @author chendeji
 * @Description: 商户接口的操作类
 * @date 27/9/14 下午2:48
 * <p/>
 * ${tags}
 */
public interface IMerchantOperation {

    /**
     * 查询指定条件下得商户数据
     *
     * @param limit           返回数量
     * @param page            起始页数
     * @param offset_type     坐标偏移类型
     * @param out_offset_type 传出坐标偏移类型
     * @param platform        调用平台
     * @return 商户列表集合
     */
    ReturnMes<List<Merchant>> findMerchant(int page, int limit,
                                           Offset_Type offset_type, Offset_Type out_offset_type,
                                           Platform platform) throws IOException, HttpException;

    /**
     * 查询指定条件下得商户数据
     *
     * @param city            城市
     * @param category        商户类型
     * @param sort            排序类型
     * @param limit           返回数量
     * @param page            起始页数
     * @param offset_type     坐标偏移类型
     * @param out_offset_type 传出坐标偏移类型
     * @param platform        调用平台
     * @return 商户列表集合
     */
    ReturnMes<List<Merchant>> findMerchants(String city, String category, Sort sort, int page, int limit,
                                            Offset_Type offset_type, Offset_Type out_offset_type,
                                            Platform platform) throws IOException, HttpException;

    /**
     * @param city        城市
     * @param category    商户类型
     * @param sort        排序类型
     * @param page        起始页数
     * @param limit       返回数量
     * @param offset_type 坐标偏移类型
     * @param platform    调用平台
     * @return return mes
     * @throws IOException
     * @throws HttpException
     * @函数名称 :findMerchants
     * @brief
     * @author : chendeji
     * @date : Thu Apr 30 15:32:33 CST 2015
     * @see
     */
    public ReturnMes<List<Merchant>> findMerchants(String city, String category, Sort sort, int page, int limit
            , Offset_Type offset_type, Platform platform) throws IOException, HttpException;

    /**
     * @param category    商户类型
     * @param sort        排序类型
     * @param page        起始页数
     * @param limit       返回数量
     * @param offset_type 坐标偏移类型
     * @param platform    调用平台
     * @return return mes
     * @throws IOException
     * @throws HttpException
     * @函数名称 :findMerchants
     * @brief
     * @author : chendeji
     * @date : Thu Apr 30 15:32:35 CST 2015
     * @see
     */
    ReturnMes<List<Merchant>> findMerchants(String category, Sort sort, int page, int limit
            , Offset_Type offset_type, Platform platform) throws IOException, HttpException;


    /**
     * @param mMerchantID
     * @return return mes
     * @函数名称 :findSingleMerchant
     * @brief
     * @author : chendeji
     * @date : Mon Jun 01 09:23:47 CST 2015
     * @see
     */
    ReturnMes<Merchant> findSingleMerchant(long mMerchantID) throws IOException, HttpException;
}
