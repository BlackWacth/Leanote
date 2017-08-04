package com.bruce.leanote.ui.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Activity基类
 * Created by Bruce on 2017/2/9.
 */
public abstract class BaseActivity extends AppCompatActivity{

    protected SharedPreferences mSharedPreferences;
    protected SharedPreferences.Editor mSharedPreferencesEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        init();
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void putString(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.apply();
    }

    public void startActivity(View view, Class cls) {
        Pair pair = new Pair<>(view, ViewCompat.getTransitionName(view));
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);
        Intent intent = new Intent(this, cls);
        startActivity(intent, activityOptionsCompat.toBundle());
    }

}
