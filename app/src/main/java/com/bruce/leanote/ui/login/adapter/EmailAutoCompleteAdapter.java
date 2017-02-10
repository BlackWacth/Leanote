package com.bruce.leanote.ui.login.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.bruce.leanote.R;

import java.util.List;

/**
 * Created by Bruce on 2017/2/9.
 */
public class EmailAutoCompleteAdapter extends ArrayAdapter<String>{

    private int mResource;

    public EmailAutoCompleteAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmailHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            holder = new EmailHolder();
            holder.emailView = (TextView) convertView.findViewById(R.id.tv_auto_complete_text);
            convertView.setTag(holder);
        } else {
            holder = (EmailHolder) convertView.getTag();
        }

        holder.emailView.setText(getItem(position));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    class EmailHolder {
        TextView emailView;
    }
}
