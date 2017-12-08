package com.tayfun.musicplayer;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;

/**
 * Created by tayfun on 03-Nov-17.
 */

public abstract class ActionBarCastActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;

    protected void initilizeToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.playlists:
                            Intent myIntent1 = new Intent(ActionBarCastActivity.this, MainActivity.class);
                            startActivity(myIntent1);
                            break;
                        case R.id.drawer_options:
                            Intent myIntent2 = new Intent(ActionBarCastActivity.this, OnlineOptionsActivity.class);
                            startActivity(myIntent2);
                            break;
                    }

                    return false;
                }
            });
        }

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

    }
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbar.setTitle(title);
    }
    protected void disableUserInput(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    protected void enableUserInput(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("hello","on option item selected");
        return super.onOptionsItemSelected(item);

    }
}


