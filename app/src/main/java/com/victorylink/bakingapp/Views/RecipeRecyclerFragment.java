package com.victorylink.bakingapp.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.victorylink.bakingapp.Adapters.RecipeDetailsAdapter;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Interfaces.Component.DaggerRecipeRecyclerFragmentComponent;
import com.victorylink.bakingapp.Interfaces.Component.RecipeRecyclerFragmentComponent;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.Views.BaseViews.BaseFragment;
import com.victorylink.bakingapp.modules.RecipeRecyclerFragmentModule;
import com.victorylink.bakingapp.utilities.BakingApp;
import com.victorylink.bakingapp.utilities.BakingConstants;
import com.yalantis.pulltomakesoup.PullToRefreshView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aayman on 5/24/2017.
 */

public class RecipeRecyclerFragment extends BaseFragment {
    static BackingResponse mBackingResponse;
    @Inject
    RecipeDetailsAdapter mRecipeDetailsAdapter;
    @Inject
    RetrofitInterface retrofitInterface;
    int mTotalScrolled = 0;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefreshView;
    @BindView(R.id.recycler_view_backing)
    RecyclerView recipeRecyclerView;

    public static RecipeRecyclerFragment newInstance(BackingResponse baking) {
        mBackingResponse = baking;
        return new RecipeRecyclerFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTotalScrolled = ((LinearLayoutManager) recipeRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(BakingConstants.BAKING_RECYCLER_INDEX, mTotalScrolled);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            (recipeRecyclerView.getLayoutManager()).scrollToPosition(savedInstanceState.getInt(BakingConstants.BAKING_RECYCLER_INDEX));
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected String getFragmentName() {
        return RecipeRecyclerFragment.class.getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .repeat(1)
                .playOn(view);
        pullToRefreshView.setRefreshing(false);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshView.setRefreshing(false);
            }
        });
        setUpRecycler();
        RecipeRecyclerFragmentComponent fragmentComponent = DaggerRecipeRecyclerFragmentComponent.builder()
                .recipeRecyclerFragmentModule(new RecipeRecyclerFragmentModule(getActivity()))
                .bakingAppComponent(BakingApp.get(getActivity()).getBakingAppComponent())
                .build();
        fragmentComponent.injectRecipeRecyclerFragmentComponent(RecipeRecyclerFragment.this);

        recipeRecyclerView.setAdapter(mRecipeDetailsAdapter);
        mRecipeDetailsAdapter.swapAdapterData(mBackingResponse);
        mRecipeDetailsAdapter.notifyDataSetChanged();
    }

    void setUpRecycler() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recipeRecyclerView.setLayoutManager(mLayoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
