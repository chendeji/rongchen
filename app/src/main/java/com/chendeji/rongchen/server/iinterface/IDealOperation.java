package com.chendeji.rongchen.server.iinterface;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.server.HttpException;

import java.io.IOException;

/**
 * Created by chendeji on 27/5/15.
 */
public interface IDealOperation {


    /**
     * @函数名称  :getDeal
     * @brief
     * @see
     * @param deal_id
     * @return return mes
     * @author  : chendeji
     * @date  : Wed May 27 17:09:17 CST 2015
     */
    ReturnMes<Deal> getDeal(String deal_id) throws IOException, HttpException;

}
