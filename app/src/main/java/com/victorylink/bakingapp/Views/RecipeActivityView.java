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

import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Interfaces.ActionTaken;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.utilities.BakingConstants;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by aayman on 5/30/2017.
 */

public class RecipeActivityView extends AppCompatActivity implements ActionTaken {
    String currentFragment = "";
    ProgressDialog progressDialog;
    BackingResponse mBackingResponse;
    int selectedItem = 0;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindString(R.string.baseURL)
    String baseURL;
    @BindString(R.string.functionBaking)
    String functionName;
    @BindBool(R.bool.isTablet)
    boolean isTab;

    @OnClick(R.id.lin_menu_home)
    void loadHomeActivity() {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(RecipeActivityView.this, BakingActivityView.class);
        startActivity(intent);

    }

    @OnClick(R.id.toolbar_menu_img)
    void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BakingConstants.BAKING_ITEM_LIST, mBackingResponse);
        outState.putString(BakingConstants.CURRENT_FRAGMENT, currentFragment);
        outState.putInt(BakingConstants.CURRENT_SELECTED_ITEM, selectedItem);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            BackingResponse backingResponse = savedInstanceState.getParcelable(BakingConstants.BAKING_ITEM_LIST);
            String currentFragmentName = savedInstanceState.getString(BakingConstants.CURRENT_FRAGMENT);
            selectedItem = savedInstanceState.getInt(BakingConstants.CURRENT_SELECTED_ITEM);
            mBackingResponse = backingResponse;
            if (isTab) {
                setUpMobileView(mBackingResponse);
                setUpVideoTabletView(mBackingResponse, selectedItem);
            } else {
                if (currentFragmentName.equals(StepsFragment.class.getSimpleName())) {
                    setUpVideoMobileView(mBackingResponse, selectedItem);
                } else {
                    setUpMobileView(mBackingResponse);
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (currentFragment.equals(RecipeRecyclerFragment.class.getSimpleName())) {
            Intent intent = new Intent(RecipeActivityView.this, BakingActivityView.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        ButterKnife.bind(this);
        /*if (savedInstanceState != null)
            return;*/
        Intent intent = getIntent();
        if (intent != null) {
            mBackingResponse = intent.getExtras().getParcelable(BakingConstants.BAKING_ITEM_LIST);
            toolbarTitle.setText(mBackingResponse.getName());
        }

        if (isTab) {
            setUpMobileView(mBackingResponse);
            setUpVideoTabletView(mBackingResponse, selectedItem);
        } else {
            if (currentFragment.equals(StepsFragment.class.getSimpleName())) {
                setUpVideoMobileView(mBackingResponse, selectedItem);
            } else {
                setUpMobileView(mBackingResponse);
            }
        }
    }

    private void setUpMobileView(BackingResponse backingResponse) {
        toolbarTitle.setText(backingResponse.getName());
        //TODO will load card details to RecipeRecyclerFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeRecyclerFragment mRecipeRecyclerFragment = RecipeRecyclerFragment.newInstance(backingResponse);

        fragmentTransaction.replace(R.id.recipe_items, mRecipeRecyclerFragment, null).addToBackStack(currentFragment);
        currentFragment = mRecipeRecyclerFragment.getFragmentName();
        fragmentTransaction.commit();
    }

    private void setUpVideoTabletView(BackingResponse backingResponse, int index) {

        //TODO will load card details to RecipeRecyclerFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StepsFragment mStepsFragment = StepsFragment.newInstance(backingResponse, index);

        fragmentTransaction.replace(R.id.recipe_details, mStepsFragment, null).addToBackStack(currentFragment);
        currentFragment = mStepsFragment.getFragmentName();
        fragmentTransaction.commit();
    }

    private void setUpVideoMobileView(BackingResponse backingResponse, int index) {
        //TODO will load card details to RecipeRecyclerFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StepsFragment mStepsFragment = StepsFragment.newInstance(backingResponse, index);
        fragmentTransaction.replace(R.id.recipe_items, mStepsFragment, null).addToBackStack(currentFragment);
        currentFragment = mStepsFragment.getFragmentName();

        fragmentTransaction.commit();
    }

    @Override
    public void loadSelectedItem(BackingResponse mBackingResponse, int index) {
        selectedItem = index;
        if (isTab) {
            setUpVideoTabletView(mBackingResponse, index);
        } else {
            setUpVideoMobileView(mBackingResponse, index);
        }
    }
}
