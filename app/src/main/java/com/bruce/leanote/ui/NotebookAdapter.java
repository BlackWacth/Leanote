package com.bruce.leanote.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bruce.leanote.R;
import com.bruce.leanote.entity.Notebook;
import com.bruce.leanote.ui.widgets.BookAdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce on 2017/5/5.
 */

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookHolder>  {

    private List<Notebook> mNotebooks = new ArrayList<>();
    private BookAdapterHelper mBookAdapterHelper;

    public NotebookAdapter() {
        mBookAdapterHelper = new BookAdapterHelper();
    }

    public NotebookAdapter(List<Notebook> notebooks) {
        mNotebooks.addAll(notebooks);
        mBookAdapterHelper = new BookAdapterHelper();
    }

    @Override
    public NotebookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_layout, parent, false);
        mBookAdapterHelper.onCreateViewHolder(parent, itemView);
        return new NotebookHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotebookHolder holder, int position) {
        mBookAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Notebook notebook = getItem(position);
        if(notebook == null) {
            return;
        }
        holder.title.setText(notebook.getTitle());
        holder.createTime.setText(notebook.getCreatedTime());
        holder.updateTime.setText(notebook.getUpdatedTime());
    }

    @Override
    public int getItemCount() {
        return mNotebooks == null ? 0 : mNotebooks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Notebook getItem(int position) {
        return mNotebooks.get(position);
    }

    public void update(List<Notebook> notebooks) {
        mNotebooks.clear();
        mNotebooks.addAll(notebooks);
        notifyDataSetChanged();
    }

    class NotebookHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView createTime;
        TextView updateTime;

        public NotebookHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_item_content_container);
            title = (TextView) itemView.findViewById(R.id.tv_item_book_title);
            createTime = (TextView) itemView.findViewById(R.id.tv_item_book_create_time);
            updateTime = (TextView) itemView.findViewById(R.id.tv_item_book_update_time);
        }
    }
}
