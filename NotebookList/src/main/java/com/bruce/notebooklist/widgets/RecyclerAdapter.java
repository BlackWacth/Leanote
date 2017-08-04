package com.bruce.notebooklist.widgets;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bruce.notebooklist.R;
import com.bruce.notebooklist.bean.Notebook;

import java.util.ArrayList;

/**
 *
 * Created by Bruce on 2017/7/6.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private ArrayList<Notebook> mList;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter(ArrayList<Notebook> list) {
        mList = list;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popup_recycler_view_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        if (mList.get(position).type.equals(Notebook.TYPE_FILE)) {
            holder.type.setImageResource(R.drawable.ic_file);
        } else if (mList.get(position).type.equals(Notebook.TYPE_FOLDER)) {
            holder.type.setImageResource(R.drawable.ic_folder);
        }
        holder.title.setText(mList.get(position).title);
        holder.path.setText(mList.get(position).path);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(RecyclerAdapter.this, holder.container, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public ArrayList<Notebook> getList() {
        return mList;
    }

    public void setList(ArrayList<Notebook> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView type;
        TextView title;
        TextView path;

        RecyclerHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.cv_item_container);
            type = (ImageView) itemView.findViewById(R.id.iv_item_type);
            title = (TextView) itemView.findViewById(R.id.tv_item_title);
            path = (TextView) itemView.findViewById(R.id.tv_item_path);
        }
    }

    interface OnItemClickListener {
        void onItemClick(RecyclerAdapter adapter, View view, int position);
    }
}

