package com.bruce.leanote.ui.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * 不需要输入情况下提示
 * Created by Bruce on 2017/2/9.
 */
public class MAppCompatAutoCompleteTextView extends AppCompatAutoCompleteTextView{

    public MAppCompatAutoCompleteTextView(Context context) {
        super(context);
    }

    public MAppCompatAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MAppCompatAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        //默认为输入两个字符进行提示。
        return true;
    }
}
