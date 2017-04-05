package com.bruce.leanote.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Login;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.global.C;
import com.bruce.leanote.net.HttpMethods;
import com.bruce.leanote.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tb_home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nv_home_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.dl_home_drawerLayout)
    DrawerLayout mDrawerLayout;

    private HttpMethods mHttpMethods;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mHttpMethods = HttpMethods.getInstance();

        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

    }

    public void onLogin(View view) {
        mHttpMethods.login("huazhongwei@yeah.net", "Hua19910622", new Observer<Login>() {
            @Override
            public void onSubscribe(Disposable d) {
                L.i("onSubscribe : d = " + d);
                showLog("onSubscribe : d = " + d);
            }

            @Override
            public void onNext(Login s) {
                L.i("onNext : s = " + s);
                userId = s.getUserId();
                C.TOKEN = s.getToken();
                showLog("onNext : s = " + s);
            }

            @Override
            public void onError(Throwable e) {
                L.i("onError : e = " + e);
                showLog("onError : e = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                L.i("onComplete");
                showLog("onComplete");
            }
        });
    }

    public void onGetUserInfo(View view) {
        mHttpMethods.getUserInfo(userId, new Observer<UserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UserInfo userInfo) {
                L.i("account = " + userInfo.toString());
                showLog("Account = " + userInfo.toString());
            }

            @Override
            public void onError(Throwable e) {
                showLog("e = " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void showLog(String text) {
//        mTvResult.append("\n" + text);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
