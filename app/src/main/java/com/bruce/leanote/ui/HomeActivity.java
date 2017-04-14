package com.bruce.leanote.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.global.C;
import com.bruce.leanote.net.HttpMethods;
import com.bruce.leanote.net.ObserverAdapter;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.LoginActivity;
import com.bruce.leanote.ui.widgets.BookAdapterHelper;
import com.bruce.leanote.utils.L;
import com.bumptech.glide.Glide;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tb_home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nv_home_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.dl_home_drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.rv_home_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.fl_home_content)
    FrameLayout mFrameLayout;

    private HttpMethods mHttpMethods;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {

        mHttpMethods = HttpMethods.getInstance();

        mToolbar.setTitle(getResources().getString(R.string.app_name));

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        if(TextUtils.isEmpty(mSharedPreferences.getString(C.EXTRA_TOKEN, ""))) {
            startActivity(LoginActivity.class);
            HomeActivity.this.finish();
        } else {
            loadUserInfo();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        BookAdapter adapter = new BookAdapter();
        mRecyclerView.setAdapter(adapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_logout:
                logout();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        final String token = mSharedPreferences.getString(C.EXTRA_TOKEN, "");
        mHttpMethods.logout(token, new ObserverAdapter<Result>(this, false) {
            @Override
            public void onComplete() {
                super.onComplete();
                startActivity(LoginActivity.class);
                HomeActivity.this.finish();
                putString(C.EXTRA_TOKEN, "");
            }
        });
    }

    @Override
    public void onBackPressed() {
        
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 获取用户信息
     */
    private void loadUserInfo() {
        final String userId = mSharedPreferences.getString(C.EXTRA_USER_ID, "");
        final String token = mSharedPreferences.getString(C.EXTRA_TOKEN, "");
        mHttpMethods.getUserInfo(userId, token, new ObserverAdapter<UserInfo>(this ,false) {
            @Override
            public void onNext(UserInfo userInfo) {
                super.onNext(userInfo);
                View navHeaderView = mNavigationView.getHeaderView(0);
                ImageView icon = (ImageView) navHeaderView.findViewById(R.id.civ_nav_home_header_icon);
                Glide.with(HomeActivity.this).load(userInfo.getLogo()).into(icon);
                TextView name = (TextView) navHeaderView.findViewById(R.id.tv_nav_home_header_name);
                name.setText(userInfo.getUsername());
                TextView email = (TextView) navHeaderView.findViewById(R.id.tv_nav_home_header_email);
                email.setText(userInfo.getEmail());
            }

            @Override
            public void requestFailed(String msg) {
                super.requestFailed(msg);
                if(msg.equals("NOTLOGIN")) {
                    startActivity(LoginActivity.class);
                }
            }
        });
    }

    class BookHolder extends RecyclerView.ViewHolder {

        public BookHolder(View itemView) {
            super(itemView);
        }
    }

    class BookAdapter extends RecyclerView.Adapter<BookHolder> {

        private BookAdapterHelper mBookAdapterHelper;

        public BookAdapter() {
            mBookAdapterHelper = new BookAdapterHelper();
        }

        @Override
        public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_layout, parent, false);
            mBookAdapterHelper.onCreateViewHolder(parent, itemView);
            L.i("onCreateViewHolder");
            return new BookHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BookHolder holder, int position) {
            mBookAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        }

        @Override
        public int getItemCount() {
            return 8;
        }
    }
}
