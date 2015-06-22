package com.chendeji.rongchen.ui.city.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.view.CommonProgressDialog;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.ui.city.task.CitySearchOnDateBaseTask;
import com.chendeji.rongchen.ui.city.task.CitySearchTask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCityListItemClickedListener} interface
 * to handle interaction events.
 * Use the {@link CitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitiesFragment extends Fragment implements AdapterView.OnItemClickListener, UITaskCallBack<ReturnMes<List<String>>> {

    private static final String TAG = CitiesFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCityListItemClickedListener mListener;
    private CommonProgressDialog progressDialog;
    private AsyncTask<Void, Void, ReturnMes<List<String>>> searchDateBaseTask;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitiesFragment newInstance(String param1, String param2) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CitiesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        ListView city_list = (ListView) view.findViewById(R.id.lv_city_list);
        city_list.setOnItemClickListener(this);
        return view;
    }

    public void searchKeyChanged(String keyWord){
        // 搜索关键字变更了，要更新listview中的数据
        //1,查找到数据库对应的数据
        //2,更新列表
        starSearchCity(keyWord);
    }

    private void starSearchCity(String keyWord) {
        searchDateBaseTask = new CitySearchOnDateBaseTask(getActivity(),this).excuteProxy((Void[])null);
    }

    @Override
    public void onResume() {
        Logger.i(TAG, "onResume");
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String city) {
        if (mListener != null) {
            mListener.onFragmentInteraction(city);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCityListItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        cancelTask();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        cancelTask();
    }

    private void cancelTask(){
        if (searchDateBaseTask != null){
            if (!searchDateBaseTask.isCancelled()){
                searchDateBaseTask.cancel(true);
                searchDateBaseTask = null;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String city = (String) parent.getItemAtPosition(position);
        onButtonPressed(city);
    }

    @Override
    public void onPreExecute() {
        //TODO 显示加载进度条Dialog
        showLoadingDialog();
    }

    private void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new CommonProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void hideLoadingDialog(){
        if (progressDialog != null)
            progressDialog.hide();
    }

    @Override
    public void onPostExecute(ReturnMes<List<String>> returnMes) {
        hideLoadingDialog();
    }

    @Override
    public void onNetWorkError() {

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
    public interface OnCityListItemClickedListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String city);
    }

}
