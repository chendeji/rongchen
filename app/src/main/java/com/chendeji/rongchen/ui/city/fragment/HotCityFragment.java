package com.chendeji.rongchen.ui.city.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.view.AutoHorizontalLinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHotCityClicked} interface
 * to handle interaction events.
 * Use the {@link HotCityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotCityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnHotCityClicked mListener;
    private AutoHorizontalLinearLayout recent_city;
    private AutoHorizontalLinearLayout hot_city;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotCityFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        initData();
        return view;
    }

    public void initData(){
        OnCityButtonClickedListener listener = new OnCityButtonClickedListener();
        Button city;
        //最近搜索城市数据填充
        city = new Button(getActivity());
        city.setTextColor(Color.WHITE);
        city.setText("福州");
        city.setBackgroundResource(R.drawable.city_button_bg);
        city.setOnClickListener(listener);
        recent_city.addView(city);

        //热门城市数据填充
        String[] hot_cities = getActivity().getResources().getStringArray(R.array.hot_cities);
        int hot_cities_count = hot_cities.length;
        for (int i = 0; i < hot_cities_count; i++) {
            city = new Button(getActivity());
            city.setTextColor(Color.WHITE);
            city.setBackgroundResource(R.drawable.city_button_bg);
            city.setText(hot_cities[i]);
            city.setOnClickListener(listener);
            hot_city.addView(city);
        }
    }

    private class OnCityButtonClickedListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            onButtonPressed(v);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        public void onCityClicked(String city);
    }

}
