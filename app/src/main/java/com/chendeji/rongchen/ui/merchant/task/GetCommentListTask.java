package com.chendeji.rongchen.ui.merchant.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 25/5/15.
 */
public class GetCommentListTask extends BaseUITask<Void, Void, ReturnMes<List<Comment>>> {

    long business_id;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetCommentListTask(Context context,long business_id, UITaskCallBack taskCallBack) {
        super(context, taskCallBack);
        this.business_id = business_id;
    }

    @Override
    protected void onPostExecute(ReturnMes<List<Comment>> listReturnMes) {
        super.onPostExecute(listReturnMes);
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
            ErrorInfo errorInfo = listReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected ReturnMes<List<Comment>> doInBackground(Void... params) {
        AppServerFactory factory = AppServerFactory.getFactory();
        try {
            ReturnMes<List<Comment>> commentReturnMes = factory.getCommentOperation().findTenRecordComment(business_id);
            return commentReturnMes;
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }
        return null;
    }
}
