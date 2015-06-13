package com.chendeji.rongchen.server;

import com.chendeji.rongchen.server.iinterface.ICommentOperation;
import com.chendeji.rongchen.server.iinterface.IDealOperation;
import com.chendeji.rongchen.server.iinterface.IMerchantOperation;

/**
 * 接口工厂类，用于管理接口模块
 * Created by chendeji on 27/9/14.
 */
public class AppServerFactory {

    private IMerchantOperation merchantOperation ;
    private ICommentOperation commentOperation;
    private IDealOperation dealOperation;

    private AppServerFactory(){};
    private static AppServerFactory factory;
    public synchronized static AppServerFactory getFactory(){
        if (factory == null)
            factory = new AppServerFactory();
        return factory;
    }

    public void setMerchantOperation(IMerchantOperation operation){
        this.merchantOperation = operation;
    }

    public void setCommentOperation(ICommentOperation operation){
        this.commentOperation = operation;
    }

    public void setDealOperation(IDealOperation operation){
        this.dealOperation = operation;
    }

    public IDealOperation getDealOperation(){
        return this.dealOperation;
    }
    public IMerchantOperation getMerchantOperation(){
        return this.merchantOperation;
    }
    public ICommentOperation getCommentOperation(){
        return this.commentOperation;
    }

}
