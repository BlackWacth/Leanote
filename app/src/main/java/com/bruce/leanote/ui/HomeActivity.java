package com.bruce.leanote.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Notebook;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.global.C;
import com.bruce.leanote.net.HttpMethods;
import com.bruce.leanote.net.ObserverAdapter;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.LoginActivity;
import com.bruce.leanote.ui.widgets.BookAdapterHelper;
import com.bruce.leanote.ui.widgets.BookScaleHelper;
import com.bruce.leanote.utils.L;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tb_home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nv_home_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.dl_home_drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.rv_recycler_view)
    RecyclerView mRecyclerView;

    private HttpMethods mHttpMethods;

    private List<Notebook> mNotebooks = new ArrayList<>();
    private NotebookAdapter mNotebookAdapter;

    private static final String ARRAY_JSON = "[\n" +
            "  {\n" +
            "    \"NotebookId\": \"585f767dab64417326002874\",\n" +
            "    \"UserId\": \"585f767dab64417326002873\",\n" +
            "    \"ParentNotebookId\": \"\",\n" +
            "    \"Seq\": -1,\n" +
            "    \"Title\": \"life\",\n" +
            "    \"UrlTitle\": \"life\",\n" +
            "    \"IsBlog\": false,\n" +
            "    \"CreatedTime\": \"2016-12-25T15:34:21.239+08:00\",\n" +
            "    \"UpdatedTime\": \"2016-12-25T15:34:21.239+08:00\",\n" +
            "    \"Usn\": 7,\n" +
            "    \"IsDeleted\": true\n" +
            "  },\n" +
            "  {\n" +
            "    \"NotebookId\": \"585f767dab64417326002876\",\n" +
            "    \"UserId\": \"585f767dab64417326002873\",\n" +
            "    \"ParentNotebookId\": \"\",\n" +
            "    \"Seq\": 0,\n" +
            "    \"Title\": \"Android\",\n" +
            "    \"UrlTitle\": \"work\",\n" +
            "    \"IsBlog\": false,\n" +
            "    \"CreatedTime\": \"2016-12-25T15:34:21.242+08:00\",\n" +
            "    \"UpdatedTime\": \"2016-12-25T17:01:45.337+08:00\",\n" +
            "    \"Usn\": 11,\n" +
            "    \"IsDeleted\": false\n" +
            "  },\n" +
            "  {\n" +
            "    \"NotebookId\": \"585f8af9ab644175ca002b6a\",\n" +
            "    \"UserId\": \"585f767dab64417326002873\",\n" +
            "    \"ParentNotebookId\": \"\",\n" +
            "    \"Seq\": 1,\n" +
            "    \"Title\": \"Java\",\n" +
            "    \"UrlTitle\": \"Java\",\n" +
            "    \"IsBlog\": false,\n" +
            "    \"CreatedTime\": \"2016-12-25T17:01:45.675+08:00\",\n" +
            "    \"UpdatedTime\": \"2016-12-25T17:01:45.675+08:00\",\n" +
            "    \"Usn\": 12,\n" +
            "    \"IsDeleted\": false\n" +
            "  }]";

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
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNotebookAdapter = new NotebookAdapter();
        mRecyclerView.setAdapter(mNotebookAdapter);

        BookScaleHelper bookScaleHelper = new BookScaleHelper();
        bookScaleHelper.attachRecyclerView(mRecyclerView);

        getNotebooks();

        Gson gson = new Gson();
        List<Notebook> json = gson.fromJson(ARRAY_JSON, new TypeToken<List<Notebook>>() {}.getType());
        for (Notebook notebook: json) {
            L.i("notebook = " + notebook.toString());
        }

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

    private void getNotebooks() {
        final String token = mSharedPreferences.getString(C.EXTRA_TOKEN, "");
        mHttpMethods.getNotebooks(token, new ObserverAdapter<List<Notebook>>(this, false) {
            @Override
            public void onNext(List<Notebook> notebooks) {
                super.onNext(notebooks);
                if(notebooks == null) {
                    return;
                }
                L.i("notebooks = " + notebooks);
                mNotebooks.clear();
                for (Notebook notebook : notebooks) {
                    if(TextUtils.isEmpty(notebook.getParentNotebookId()) && !notebook.isIsDeleted()) {
                        mNotebooks.add(notebook);
                    }
                }
                mNotebookAdapter.update(mNotebooks);
            }
        });
    }
}
