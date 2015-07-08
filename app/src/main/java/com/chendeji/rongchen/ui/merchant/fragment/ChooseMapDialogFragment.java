package com.chendeji.rongchen.ui.merchant.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.SystemUtil;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.map.MapActivity;

/**
 * Created by chendeji on 7/7/15.
 */
public class ChooseMapDialogFragment extends DialogFragment {

    private Merchant mMerchant;

    public ChooseMapDialogFragment(){}
    public ChooseMapDialogFragment(Merchant merchant) {
        super();
        this.mMerchant = merchant;
    }

    private boolean checkGaodeMapAPPExist(String packageName) {
        boolean isInstall = SystemUtil.isAppInstalled(getActivity(), packageName);
        return isInstall;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialog = inflater.inflate(R.layout.dialog_choose_map_layout,container,false);
        boolean isGaodeExist = checkGaodeMapAPPExist("com.autonavi.minimap");

        Button local = (Button) dialog.findViewById(R.id.bt_local_map);
        Button gaode = (Button) dialog.findViewById(R.id.bt_gaode_map);
        gaode.setEnabled(isGaodeExist);
        gaode.setClickable(isGaodeExist);
        gaode.setFocusable(isGaodeExist);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                Logger.i("chendeji", "latitude:" + mMerchant.latitude + "longitude:" + mMerchant.longitude);
                intent.putExtra(MapActivity.LOCATION_KEY, mMerchant);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setPackage("com.autonavi.minimap");
                intent.setData(Uri.parse("androidamap://viewMap?sourceApplication=" + getActivity().getPackageName()
                        + "&poiname=" + mMerchant.name + "&lat=" + mMerchant.latitude + "&lon=" + mMerchant.longitude + "&dev=0"));
                startActivity(intent);
                dismiss();
            }
        });
        return dialog;
    }
}
