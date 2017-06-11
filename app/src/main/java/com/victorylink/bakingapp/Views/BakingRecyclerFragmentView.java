package com.victorylink.bakingapp.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.victorylink.bakingapp.Adapters.BakingAdapter;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Interfaces.BakingPresenter;
import com.victorylink.bakingapp.Interfaces.BakingView;
import com.victorylink.bakingapp.Interfaces.Component.BakingRecyclerFragmentViewComponent;
import com.victorylink.bakingapp.Interfaces.Component.DaggerBakingRecyclerFragmentViewComponent;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;
import com.victorylink.bakingapp.Presenters.ActivityPresenter.BakingPresenterImpl;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.Views.BaseViews.BaseFragment;
import com.victorylink.bakingapp.modules.BakingRecyclerFragmentViewModule;
import com.victorylink.bakingapp.utilities.BakingApp;
import com.victorylink.bakingapp.utilities.BakingConstants;
import com.yalantis.pulltomakesoup.PullToRefreshView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aayman on 5/23/2017.
 */

public class BakingRecyclerFragmentView extends BaseFragment implements BakingView {

    BakingPresenter mBakingPresenter;
    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    BakingAdapter mBakingAdapter;
    ArrayList<BackingResponse> mBakingArrayResponse;
    int mTotalScrolled = 0;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefreshView;
    @BindView(R.id.recycler_view_backing)
    RecyclerView BakingRecyclerView;
    @BindString(R.string.baseURL)
    String baseURL;
    @BindString(R.string.functionBaking)
    String functionName;
    @BindBool(R.bool.isTablet)
    boolean isTab;

    public static BakingRecyclerFragmentView newInstance() {
        return new BakingRecyclerFragmentView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BakingConstants.BAKING_ARRAY_LIST, mBakingArrayResponse);
        mTotalScrolled = ((LinearLayoutManager) BakingRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(BakingConstants.BAKING_RECYCLER_INDEX, mTotalScrolled);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<BackingResponse> backingResponses;
            backingResponses = savedInstanceState.getParcelableArrayList(BakingConstants.BAKING_ARRAY_LIST);
            if (backingResponses != null) {
                mBakingArrayResponse = backingResponses;
                assignLocalAdapter(mBakingArrayResponse);
                BakingRecyclerView.getLayoutManager().scrollToPosition(savedInstanceState.getInt(BakingConstants.BAKING_RECYCLER_INDEX));
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mBakingPresenter = new BakingPresenterImpl(BakingRecyclerFragmentView.this);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBakingPresenter.loadDataList(functionName, baseURL);
            }
        });
        //mBakingArrayResponse = new ArrayList<>();
        BakingRecyclerFragmentViewComponent BakingRecyclerFragmentViewComponent = DaggerBakingRecyclerFragmentViewComponent.builder()
                .bakingRecyclerFragmentViewModule(new BakingRecyclerFragmentViewModule(getActivity()))
                .bakingAppComponent(BakingApp.get(getActivity()).getBakingAppComponent())
                .build();
        BakingRecyclerFragmentViewComponent.injectBakingRecyclerFragmentViewComponent(BakingRecyclerFragmentView.this);
        if (isTab) {
            setUpGridRecyclerView();
        } else {
            setUpRecycler();
        }

    }

    private void setUpGridRecyclerView() {
        GridLayoutManager lLayout;
        lLayout = new GridLayoutManager(getActivity(), 3);
        BakingRecyclerView.setHasFixedSize(true);
        BakingRecyclerView.setLayoutManager(lLayout);
        assignLocalAdapter(mBakingArrayResponse);
    }

    void setUpRecycler() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        BakingRecyclerView.setLayoutManager(mLayoutManager);
        BakingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        assignLocalAdapter(mBakingArrayResponse);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void assignAdapterData(ArrayList<BackingResponse> mResponse) {
        mBakingArrayResponse = mResponse;
        mBakingAdapter.swapAdapterData(mResponse);
        mBakingAdapter.notifyDataSetChanged();
        pullToRefreshView.setRefreshing(false);
    }

    @Override
    public void loadSelectedItem(BackingResponse mBackingResponse) {
        // not used and the returned data gone to BakingActivityView
    }

    void assignLocalAdapter(ArrayList<BackingResponse> mResponse) {
       /* mBakingAdapter = new BakingAdapter(getActivity());*/
        mBakingArrayResponse = mResponse;
        BakingRecyclerView.setAdapter(mBakingAdapter);
        mBakingAdapter.swapAdapterData(mResponse);
        mBakingAdapter.notifyDataSetChanged();
    }

    void setBakingResponse(ArrayList<BackingResponse> mResponse) {
        mBakingArrayResponse = mResponse;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected String getFragmentName() {
        return BakingRecyclerFragmentView.class.getSimpleName();
    }
}
