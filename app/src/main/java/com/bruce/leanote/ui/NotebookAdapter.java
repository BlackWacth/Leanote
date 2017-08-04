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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 首页适配器
 * Created by Bruce on 2017/5/5.
 */
public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookHolder>  {

    private List<Notebook> mNotebooks = new ArrayList<>();
    private BookAdapterHelper mBookAdapterHelper;
    private OnClickListener mOnClickListener;

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
    public void onBindViewHolder(final NotebookHolder holder, final int position) {
        mBookAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Notebook notebook = getItem(position);
        if(notebook == null) {
            return;
        }
        holder.title.setText(notebook.getTitle());
        holder.createTime.setText(formatDate(notebook.getCreatedTime()));
        holder.updateTime.setText(formatDate(notebook.getUpdatedTime()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(holder.cardView, position);
                }
            }
        });
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

    /**
     * 把日期从2016-12-25T15:34:21.239+08:00 格式化为 2016-12-25 15:34
     * @param strDate 原始日期
     * @return 格式化后日期
     */
    private String formatDate(String strDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.CHINA);
        try {
            Date parseDate = simpleDateFormat.parse(strDate);
            simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm");
            return simpleDateFormat.format(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
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

    public interface OnClickListener {
        void onClick(View view, int position);
    }
}
