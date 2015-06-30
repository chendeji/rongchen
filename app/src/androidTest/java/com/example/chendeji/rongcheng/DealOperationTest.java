package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.model.groupbuy.Restriction;
import com.chendeji.rongchen.model.groupbuy.SimpleMerchantInfo;
import com.chendeji.rongchen.server.AppServerConfig;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;

import junit.framework.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 27/5/15.
 */
public class DealOperationTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        AppServerConfig.getInstance().initServerFactory();
        super.setUp();
    }

    public void testGetSingleDealInfo() throws IOException, HttpException {
        String deal_id = "14-2254162";
        ReturnMes<Deal> dealReturnMes = AppServerFactory.getFactory().getDealOperation().getDeal(deal_id);
        Assert.assertNotNull(dealReturnMes);
        Deal deal = dealReturnMes.object;
        Assert.assertNotNull(deal);
        String id =  deal.deal_id;
        int current_price = deal.current_price;
        String description = deal.description;
        String details = deal.details;
        String image_url = deal.image_url;
        int list_price = deal.list_price;
        List<String> more_image_urls = deal.getMore_image_urls();
        List<SimpleMerchantInfo> merchantInfos = deal.getBusinesses();
        String publish_date = deal.publish_date;
        int purchase_count = deal.purchase_count;
        String purchase_deadline = deal.purchase_deadline;
        String title = deal.title;
        Restriction special_tips = deal.getRestrictions();

        Assert.assertNotNull(special_tips);
        int is_refundable = special_tips.is_refundable;
        int is_reservation_required = special_tips.is_reservation_required;
        String str_special_tips = special_tips.special_tips;

        Assert.assertTrue(is_refundable == Restriction.REFUNDABLE);
        Assert.assertTrue(is_reservation_required == Restriction.RESERVATION_NOT_REQUIRED);
        Assert.assertFalse(TextUtils.isEmpty(str_special_tips));

        Assert.assertNotNull(merchantInfos);
        Assert.assertTrue(merchantInfos.size() > 0);
        for(SimpleMerchantInfo info : merchantInfos){
            String name = info.name;
            String address = info.address;
            double latitude = info.latitude;
            double longitude = info.longitude;
            Assert.assertFalse(TextUtils.isEmpty(name));
            Assert.assertFalse(TextUtils.isEmpty(address));
        }
    }
}
