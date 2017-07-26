package com.victorylink.bakingapp.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Interfaces.BakingPresenter;
import com.victorylink.bakingapp.Interfaces.BakingView;
import com.victorylink.bakingapp.Presenters.ActivityPresenter.BakingPresenterImpl;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.utilities.BakingConstants;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by aayman on 5/23/2017.
 */

public class BakingActivityView extends AppCompatActivity implements BakingView {

    public static BackingResponse mBackingResponse;
    public static String currentFragment = "";
    public static ProgressDialog progressDialog;
    BakingPresenter mBakingPresenter;
    ArrayList<BackingResponse> mBakingArrayResponse;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindString(R.string.baseURL)
    String baseURL;
    @BindString(R.string.functionBaking)
    String functionName;

    @OnClick(R.id.lin_menu_home)
    void reload() {
        drawerLayout.closeDrawer(GravityCompat.START);
        mBakingPresenter.loadDataList(functionName, baseURL);

    }

    @OnClick(R.id.toolbar_menu_img)
    void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BakingConstants.BAKING_ARRAY_LIST, mBakingArrayResponse);
        outState.putParcelable(BakingConstants.BAKING_ITEM_LIST, mBackingResponse);


    }

    @Override
    public void failerLoadingData() {
        Toast.makeText(this, getResources().getString(R.string.errorLoading), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<BackingResponse> backingResponses;
            backingResponses = savedInstanceState.getParcelableArrayList(BakingConstants.BAKING_ARRAY_LIST);
            BackingResponse backingResponse = savedInstanceState.getParcelable(BakingConstants.BAKING_ITEM_LIST);
            if (currentFragment.equals(RecipeRecyclerFragment.class.getSimpleName())) {
                mBackingResponse = backingResponse;
                this.loadSelectedItem(mBackingResponse);
            } else if (currentFragment.equals(BakingRecyclerFragmentView.class.getSimpleName())) {
                mBakingArrayResponse = backingResponses;
                loadRecyclerView(mBakingArrayResponse);
                //TODO we have to store scroll position
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbarTitle.setText(getResources().getString(R.string.toolbarTitle));
        mBakingPresenter = new BakingPresenterImpl(this);
        if (savedInstanceState == null)
            mBakingPresenter.loadDataList(functionName, baseURL);
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(BakingActivityView.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingData));
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }


    @Override
    public void assignAdapterData(ArrayList<BackingResponse> mBackingResponse) {
        mBakingArrayResponse = mBackingResponse;
        loadRecyclerView(mBackingResponse);
    }

    @Override
    public void loadSelectedItem(BackingResponse mBackingResponse) {

        Intent intent = new Intent(BakingActivityView.this, RecipeActivityView.class);
        intent.putExtra(BakingConstants.BAKING_ITEM_LIST, mBackingResponse);
        startActivity(intent);
       /* Toast.makeText(BakingActivityView.this, "done", Toast.LENGTH_SHORT).show();
        this.mBackingResponse = mBackingResponse;
        //TODO will load card details to RecipeRecyclerFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeRecyclerFragment mRecipeRecyclerFragment = RecipeRecyclerFragment.newInstance(mBackingResponse);
        currentFragment = mRecipeRecyclerFragment.getFragmentName();
        fragmentTransaction.replace(R.id.baking_main_container, mRecipeRecyclerFragment, null);
        fragmentTransaction.commit();
*/
    }

    private void loadRecyclerView(ArrayList<BackingResponse> mBackingResponse) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BakingRecyclerFragmentView bakingRecyclerFragmentView = BakingRecyclerFragmentView.newInstance();
        currentFragment = BakingConstants.BAKING_ARRAY_LIST;
        bakingRecyclerFragmentView.setBakingResponse(mBackingResponse);
        fragmentTransaction.replace(R.id.baking_main_container, bakingRecyclerFragmentView, null);
        fragmentTransaction.commit();
    }

}
