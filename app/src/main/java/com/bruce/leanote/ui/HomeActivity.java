package com.bruce.leanote.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Notebook;
import com.bruce.leanote.entity.Result;
import com.bruce.leanote.entity.UserInfo;
import com.bruce.leanote.global.C;
import com.bruce.leanote.net.HttpMethods;
import com.bruce.leanote.net.ObserverAdapter;
import com.bruce.leanote.ui.base.BaseActivity;
import com.bruce.leanote.ui.login.LoginActivity;
import com.bruce.leanote.ui.notebook.NotebookActivity;
import com.bruce.leanote.ui.widgets.BookScaleHelper;
import com.bruce.leanote.utils.L;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, NotebookAdapter.OnClickListener {

    @BindView(R.id.tb_home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nv_home_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.dl_home_drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.rv_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_home_add_action_button)
    FloatingActionButton mActionButton;

    @BindView(R.id.cl_content_container)
    RelativeLayout mFrameLayout;

    private HttpMethods mHttpMethods;

    private List<Notebook> mNotebooks = new ArrayList<>();
    private NotebookAdapter mNotebookAdapter;

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

        if (TextUtils.isEmpty(mSharedPreferences.getString(C.EXTRA_TOKEN, ""))) {
            startActivity(LoginActivity.class);
            HomeActivity.this.finish();
        } else {
            C.TOKEN = mSharedPreferences.getString(C.EXTRA_TOKEN, "");
            loadUserInfo();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNotebookAdapter = new NotebookAdapter();
        mRecyclerView.setAdapter(mNotebookAdapter);

        BookScaleHelper bookScaleHelper = new BookScaleHelper();
        bookScaleHelper.attachRecyclerView(mRecyclerView);

        mNotebookAdapter.setOnClickListener(this);

        getNotebooks();

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
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
        mHttpMethods.logout(new ObserverAdapter<Result>(this, false) {
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

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
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
        mHttpMethods.getUserInfo(userId, new ObserverAdapter<UserInfo>(this, false) {
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
                if (msg.equals("NOTLOGIN")) {
                    startActivity(LoginActivity.class);
                }
            }
        });
    }

    private void getNotebooks() {
        mHttpMethods.getNotebooks(new ObserverAdapter<List<Notebook>>(this, false) {
            @Override
            public void onNext(List<Notebook> notebooks) {
                super.onNext(notebooks);
                if (notebooks == null) {
                    return;
                }
                L.i("notebooks = " + notebooks);
                mNotebooks.clear();
                for (Notebook notebook : notebooks) {
                    if (TextUtils.isEmpty(notebook.getParentNotebookId()) && !notebook.isIsDeleted()) {
                        mNotebooks.add(notebook);
                    }
                }
                mNotebookAdapter.update(mNotebooks);
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
//        show(view);
        startActivity(view, NotebookActivity.class);
    }

    private AnimatorSet mAnimatorSet;
    private Rect mStartRect = new Rect();
    private Rect mEndRect = new Rect();
    private Point mGlobalOffset = new Point();
    private View mBookFaceView, mBookContentView;

    private void show(View view) {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }

        mStartRect.setEmpty();
        mEndRect.setEmpty();
        mGlobalOffset.set(0, 0);

        view.getGlobalVisibleRect(mStartRect);
        findViewById(R.id.cl_content_container).getGlobalVisibleRect(mEndRect, mGlobalOffset);
        L.i("startRect = " + mStartRect.toString());
        L.i("mEndRect = " + mEndRect.toString());
        L.i("mGlobalOffset = " + mGlobalOffset.toString());

        mStartRect.offset(-mGlobalOffset.x, -mGlobalOffset.y);
        mEndRect.offset(-mGlobalOffset.x, -mGlobalOffset.y);

        float scaleX = mEndRect.width() * 1.0f / mStartRect.width();
        float scaleY = mEndRect.height() * 1.0f / mStartRect.height();
        L.i("scaleX = " + scaleX + ", scaleY = " + scaleY);

        if (mBookContentView == null) {
            mBookContentView = createView(view);
            mFrameLayout.addView(mBookContentView);
        }

        if (mBookFaceView == null) {
            mBookFaceView = createView(view);
            mFrameLayout.addView(mBookFaceView);
        }

        mBookContentView.setPivotX(0);
        mBookContentView.setPivotY(0);
        mBookFaceView.setPivotX(0);
        mBookFaceView.setPivotY(0);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mBookFaceView, View.ROTATION_Y, 0, -90))
        .with(ObjectAnimator.ofFloat(mBookContentView, View.SCALE_X, scaleX))
        .with(ObjectAnimator.ofFloat(mBookContentView, View.SCALE_Y, scaleY));

        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(1000);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorSet = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimatorSet = null;
            }
        });
        mAnimatorSet = set;
        set.start();
    }

    private View createView(View view) {

        View copyView = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(view.getWidth(), view.getHeight());
        copyView.setLayoutParams(params);
        copyView.setBackgroundColor(getResources().getColor(R.color.md_amber_400));

        copyView.setX(mStartRect.left);
        copyView.setY(mStartRect.top);

        return copyView;
    }

}
