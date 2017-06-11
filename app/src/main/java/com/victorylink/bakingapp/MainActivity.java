package com.victorylink.bakingapp;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @OnClick(R.id.toolbar_menu_img)
    void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();*/
    }
}
