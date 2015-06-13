package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.Sort;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.server.AppServerConfig;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICommentOperation;
import com.chendeji.rongchen.server.iinterface.IMerchantOperation;

import junit.framework.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 25/5/15.
 */
public class CommentOperationTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        AppServerConfig.getInstance().initServerFactory();
        //初始化地图
        MapManager.getManager().init(this.getContext());
        super.setUp();
    }

    public void testGetTenComment1() throws IOException, HttpException {
        long id = 9903562;
        ICommentOperation commentOperation = AppServerFactory.getFactory().getCommentOperation();
        ReturnMes<List<Comment>> commentReturnMes = commentOperation.findTenRecordComment(id);
        Assert.assertNotNull(commentOperation);
        Assert.assertNotNull(commentReturnMes);

        List<Comment> commentList = commentReturnMes.object;
        Assert.assertNotNull(commentList);
        for (Comment comment : commentList){
            long review_id = comment.review_id;
            String created_time = comment.created_time;
            String user_nickname = comment.user_nickname;
            String text_excerpt = comment.text_excerpt;
            String review_url = comment.review_url;
            Assert.assertTrue(review_id > 0);
            Assert.assertFalse(TextUtils.isEmpty(created_time));
            Assert.assertFalse(TextUtils.isEmpty(user_nickname));
            Assert.assertFalse(TextUtils.isEmpty(text_excerpt));
            Assert.assertFalse(TextUtils.isEmpty(review_url));
        }
        Assert.assertNotNull(commentReturnMes.moreInfo);
    }

    public void testGetTenComment() throws IOException, HttpException {
        IMerchantOperation merchantOperation = AppServerFactory.getFactory().getMerchantOperation();
        ReturnMes<List<Merchant>> merchantReturnMes = merchantOperation.findMerchants("福州","美食", Sort.DEFAULT,1,20, Offset_Type.DEFUALT,Offset_Type.DEFUALT, Platform.HTML5);

        List<Merchant> merchants = merchantReturnMes.object;
        Assert.assertNotNull(merchants);
        List<Comment> comments = null;
        ReturnMes<List<Comment>> commentReturnMes = null;
        for (Merchant merchant : merchants){
            long business_id = merchant.business_id;

            ICommentOperation commentOperation = AppServerFactory.getFactory().getCommentOperation();
            commentReturnMes = commentOperation.findTenRecordComment(business_id);
            List<Comment> commentList = commentReturnMes.object;
            if (commentList == null)
                continue;
            if (commentList.size() > 0){
                break;
            }
        }

        Assert.assertNotNull(comments);
        for(Comment comment : comments){
            long review_id = comment.review_id;
            String created_time = comment.created_time;
            String user_nickname = comment.user_nickname;
            String text_excerpt = comment.text_excerpt;
            String review_url = comment.review_url;
            Assert.assertTrue(review_id > 0);
            Assert.assertFalse(TextUtils.isEmpty(created_time));
            Assert.assertFalse(TextUtils.isEmpty(user_nickname));
            Assert.assertFalse(TextUtils.isEmpty(text_excerpt));
            Assert.assertFalse(TextUtils.isEmpty(review_url));
        }
        Assert.assertNotNull(commentReturnMes.moreInfo);
    }

}
