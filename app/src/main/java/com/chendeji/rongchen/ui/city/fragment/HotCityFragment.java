package com.chendeji.rongchen.ui.city.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.view.AutoHorizontalLinearLayout;
import com.chendeji.rongchen.common.view.CommonLoadingProgressView;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.ui.city.task.GetRecentSearchCityTask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHotCityClicked} interface
 * to handle interaction events.
 * Use the {@link HotCityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotCityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnHotCityClicked mListener;
    private AutoHorizontalLinearLayout recent_city;
    private AutoHorizontalLinearLayout hot_city;
    private CommonLoadingProgressView progressView;
    private TextView recentlySearchCity;
    private Button currentCity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotCityFragment.
     */
    public static HotCityFragment newInstance(String param1, String param2) {
        HotCityFragment fragment = new HotCityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HotCityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_city, container, false);
        recent_city = (AutoHorizontalLinearLayout) view.findViewById(R.id.hll_recent_search_city);
        hot_city = (AutoHorizontalLinearLayout) view.findViewById(R.id.hll_hot_city);
        progressView = (CommonLoadingProgressView) view.findViewById(R.id.common_loading_layout);
        recentlySearchCity = (TextView) view.findViewById(R.id.tv_recently_search_city);
        currentCity = (Button) view.findViewById(R.id.bt_current_city);
        initData();

        SettingFactory.getInstance().registSharePreferencesListener(this);
        return view;
    }

    private void showProgress() {
        if (progressView != null) {
            progressView.show();
        }
    }

    private void hideProgress() {
        if (progressView != null) {
            progressView.hide();
        }
    }

    public void initData() {
        String currentCityStr = SettingFactory.getInstance().getCurrentCity();
        if (TextUtils.isEmpty(currentCityStr)){
            currentCity.setVisibility(View.INVISIBLE);
        } else {
            currentCity.setText(currentCityStr);
        }

        final OnCityButtonClickedListener listener = new OnCityButtonClickedListener();
        //开启子线程去完成数据库提取最近搜索城市的任务
        new GetRecentSearchCityTask(getActivity(), new UITaskCallBack<ReturnMes<List<String>>>() {
            @Override
            public void onPreExecute() {
                showProgress();
            }

            @Override
            public void onPostExecute(ReturnMes<List<String>> returnMes) {
                hideProgress();
                List<String> stringList = returnMes.object;
                Button cityView;
                // 最近搜索城市数据填充
                if (stringList != null && !stringList.isEmpty()) {
                    //TODO 这里有一个BUG 当再次回到这个页面进行选择城市的时候，不能重新刷新最近搜索城市
                    for (String city : stringList) {
                        cityView = new Button(getActivity());
                        cityView.setTextColor(getResources().getColor(R.color.common_button_textcolor));
                        cityView.setText(city);
                        cityView.setBackgroundResource(R.drawable.radius_white_bg);
                        cityView.setOnClickListener(listener);
                        recent_city.addView(cityView);
                    }
                } else {
                    hideRecentSearchCityLayout();
                }
            }

            @Override
            public void onExecuteError(String errorMsg) {
                hideRecentSearchCityLayout();
                hideProgress();
                ToastUtil.showLongToast(getActivity(),errorMsg);
            }
        }).excuteProxy((Void[]) null);


        //热门城市数据填充
        Button city;
        String[] hot_cities = getActivity().getResources().getStringArray(R.array.hot_cities);
        int hot_cities_count = hot_cities.length;
        for (int i = 0; i < hot_cities_count; i++) {
            city = new Button(getActivity());
            city.setTextColor(getResources().getColor(R.color.common_button_textcolor));
            city.setBackgroundResource(R.drawable.radius_white_bg);
            city.setText(hot_cities[i]);
            city.setOnClickListener(listener);
            hot_city.addView(city);
        }
    }

    private void hideRecentSearchCityLayout(){
        if (recent_city != null) {
            recent_city.setVisibility(View.GONE);
        }
        if (recentlySearchCity != null) {
            recentlySearchCity.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(SettingFactory.CITY_SETTING)){
            String city = SettingFactory.getInstance().getCurrentCity();
            currentCity.setText(city);
        }
    }

    private class OnCityButtonClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            onButtonPressed(v);
        }
    }

    public void onButtonPressed(View view) {
        Button city = (Button) view;
        if (mListener != null) {
            mListener.onCityClicked(city.getText().toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnHotCityClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHotCityClicked {
        public void onCityClicked(String city);
    }

}
