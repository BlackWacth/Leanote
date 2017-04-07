package com.bruce.leanote.ui.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void putString(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

}
