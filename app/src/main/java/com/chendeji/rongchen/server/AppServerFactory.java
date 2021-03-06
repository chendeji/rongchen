package com.chendeji.rongchen.server;

import com.chendeji.rongchen.server.iinterface.ICategoryOperation;
import com.chendeji.rongchen.server.iinterface.ICityOperation;
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
    private ICityOperation mCityOperation;
    private ICategoryOperation mCategoryOperation;

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

    public void setCityOperation(ICityOperation operation){
        this.mCityOperation = operation;
    }

    public void setDealOperation(IDealOperation operation){
        this.dealOperation = operation;
    }
    public void setCategoryOperation(ICategoryOperation operation){
        this.mCategoryOperation = operation;
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
    public ICityOperation getCityOperation(){
        return this.mCityOperation;
    }
    public ICategoryOperation getCategoryOperation(){
        return this.mCategoryOperation;
    }

}
