package com.chendeji.rongchen.ui.category.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.view.CommonLoadingProgressView;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.ui.category.common.MyAdapter;
import com.chendeji.rongchen.ui.category.common.MyDecoration;
import com.chendeji.rongchen.ui.category.task.GetCategoryFromDBTask;
import com.chendeji.rongchen.ui.category.task.GetCategoryTask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryListFragment.OnCategoryItemClickedListener} interface
 * to handle interaction events.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment implements MyAdapter.OnItemViewClickedListener {

    public static final int CATEGORY = 0;
    public static final int SUBCATEGORY = 1;
    public static final int SUBCATEGORY1 = 2;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORY_NAME = "category_name";
    private static final String CATEGORY_LEVEL = "category_level";

    private String category_name;
    private int category_level;

    private OnCategoryItemClickedListener mListener;
    private CommonLoadingProgressView progressView;
    private MyAdapter adapter;
    private boolean isLastBranch;
    private AsyncTask<Void, Void, ReturnMes<List<String>>> searchFromDB;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category_name 分类名称
     * @param category_level 分类级别
     * @return A new instance of fragment CategoryListFragment.
     */
    public static CategoryListFragment newInstance(String category_name, int category_level) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME, category_name);
        args.putInt(CATEGORY_LEVEL, category_level);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_name = getArguments().getString(CATEGORY_NAME);
            category_level = getArguments().getInt(CATEGORY_LEVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        //使用RecycleView 显示分类的信息，然后还要带有动画
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_categroy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new MyDecoration(getResources().getDimensionPixelSize(R.dimen.horizontal_10dp)));
        adapter = new MyAdapter(getActivity(), new ArrayList<String>(), this);
        recyclerView.setAdapter(adapter);

        progressView = (CommonLoadingProgressView) view.findViewById(R.id.common_loading_layout);
        if (TextUtils.isEmpty(category_name)){
            initData();
        }else {
            searchDB();
        }
        return view;
    }

    private void searchDB() {
        //TODO 根据传递进来的分类的名称查找数据库
        searchFromDB = new GetCategoryFromDBTask(getActivity(), category_name, category_level, new UITaskCallBack<ReturnMes<List<String>>>() {
            @Override
            public void onPreExecute() {
                showProgress();
            }

            @Override
            public void onPostExecute(ReturnMes<List<String>> returnMes) {
                hideProgress();
                if (returnMes == null) {
                    isLastBranch = true;
                    return;
                }
                List<String> categories = returnMes.object;
                adapter.setData(categories);
            }

            @Override
            public void onNetWorkError() {

            }
        }).excuteProxy((Void[])null);

    }

    private void showProgress(){
        progressView.show();
    }
    private void hideProgress(){
        progressView.hide();
    }


    public void onButtonPressed(String category_name, int category_level) {

        if (searchFromDB.getStatus() == AsyncTask.Status.RUNNING)
            return;
        searchFromDB = new GetCategoryFromDBTask(getActivity(), category_name, category_level,
                new UITaskCallBack<ReturnMes<List<String>>>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(ReturnMes<List<String>> returnMes) {

            }

            @Override
            public void onNetWorkError() {

            }
        }).excuteProxy((Void[])null);


        if (mListener != null) {
            mListener.onFragmentInteraction(category_name, category_level, isLastBranch);
        }
    }

    private void initData() {

        new GetCategoryTask(getActivity(), SettingFactory.getInstance().getCurrentCity(), new UITaskCallBack<ReturnMes<List<Category>>>() {
            @Override
            public void onPreExecute() {
                showProgress();
            }

            @Override
            public void onPostExecute(ReturnMes<List<Category>> returnMes) {
                hideProgress();
                List<String> stringList = new ArrayList<String>();
                List<Category> categories = returnMes.object;
                for (Category category : categories){
                    stringList.add(category.category_name);
                }
                adapter.setData(stringList);
            }

            @Override
            public void onNetWorkError() {

            }
        }).excuteProxy((Void[])null);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCategoryItemClickedListener) activity;
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

    @Override
    public void onClick(String category) {
        onButtonPressed(category, category_level);
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
    public interface OnCategoryItemClickedListener {
        public void onFragmentInteraction(String category, int category_level, boolean isLastBranch);
    }

}
