package com.bruce.leanote.ui.login.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.bruce.leanote.model.Account;

import java.util.List;

/**
 * Created by Bruce on 2017/2/9.
 */

public class EmailAutoCompleteAdapter extends ArrayAdapter<Account>{

    public EmailAutoCompleteAdapter(Context context, int resource, List<Account> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}
