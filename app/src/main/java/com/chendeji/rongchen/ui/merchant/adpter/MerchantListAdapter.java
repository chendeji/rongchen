package com.chendeji.rongchen.ui.merchant.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.merchant.view.MerchantListItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chendeji
 * @Description: 商户列表的适配器
 * @date 29/10/14 下午11:23
 * <p/>
 * ${tags}
 */
public class MerchantListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Merchant> merchants;

    public MerchantListAdapter(Context context, List<Merchant> merchantList) {
        this.mContext = context;
        this.merchants = merchantList;
    }

    /**
     * 设置适配器的数据
     * @param list  从后台返回的服务端的数据
     * @param isAdd 加载还是刷新
     */
    public void setData(List<Merchant> list, boolean isAdd) {

        if (merchants == null) {
            merchants = new ArrayList<Merchant>();
            merchants.addAll(list);
        } else {
            if (!isAdd){
                if (!merchants.isEmpty()) {
                    merchants.clear();
                }
            }
            merchants.addAll(list);
        }

        // update list
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        if (merchants != null)
            return merchants.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (merchants != null)
            return merchants.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MerchantListItemView itemView = (MerchantListItemView) convertView;
        if (itemView == null) {
            itemView = new MerchantListItemView(mContext);
        }
        Merchant merchant = merchants.get(position);
        itemView.setData(merchant);

        return itemView;
    }
}
