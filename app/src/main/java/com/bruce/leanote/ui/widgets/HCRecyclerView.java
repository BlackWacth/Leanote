package com.bruce.leanote.ui.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.bruce.leanote.utils.L;

/**
 *
 * Created by Bruce on 2017/4/14.
 */
public class HCRecyclerView extends RecyclerView {

    public HCRecyclerView(Context context) {
        super(context);
        init();
    }

    public HCRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HCRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

}
