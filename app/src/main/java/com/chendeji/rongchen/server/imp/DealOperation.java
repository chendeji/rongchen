package com.chendeji.rongchen.server.imp;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.IDealOperation;
import com.chendeji.rongchen.server.request.DealRequest;

import java.io.IOException;

/**
 * Created by chendeji on 27/5/15.
 */
public class DealOperation implements IDealOperation {
    @Override
    public ReturnMes<Deal> getDeal(String deal_id) throws IOException, HttpException {
        DealRequest request = new DealRequest();
        return request.getDeal(deal_id);
    }
}
