package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;

import java.io.IOException;
import java.util.List;

import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.Sort;
import com.chendeji.rongchen.server.AppServerConfig;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.IMerchantOperation;

import junit.framework.Assert;

/**
 * @author chendeji
 * @Description:
 * @date 27/9/14 下午10:34
 * <p/>
 * ${tags}
 */
public class MerchantOperationTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {

        AppServerConfig.getInstance().initServerFactory();
        MapManager manager = MapManager.getManager();
        manager.init(getContext());
        manager.setOffset_type(Offset_Type.GAODE);
        super.setUp();
    }

    public void testFindMerchants() throws IOException, HttpException {
        IMerchantOperation operation = AppServerFactory.getFactory().getMerchantOperation();
        ReturnMes<List<Merchant>> returnMes = operation.findMerchants("福州","美食",Sort.DEFAULT,1,20,Offset_Type.DEFUALT,Offset_Type.DEFUALT,Platform.HTML5);
    }

    public void testGetSingleMerchants() throws IOException, HttpException {
        long merchantid = 6162303;
        IMerchantOperation operation = AppServerFactory.getFactory().getMerchantOperation();
        ReturnMes<Merchant> merchantReturnMes = operation.findSingleMerchant(merchantid);
        Assert.assertNotNull(merchantReturnMes);
        Assert.assertNotNull(merchantReturnMes.object);
    }

}
