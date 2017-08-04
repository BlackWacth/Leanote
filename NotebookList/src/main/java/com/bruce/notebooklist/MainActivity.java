package com.bruce.notebooklist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bruce.notebooklist.bean.Notebook;
import com.bruce.notebooklist.widgets.PopupRecyclerView;
import com.bruce.notebooklist.widgets.RecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PopupRecyclerView mPopupRecyclerView;
    private ArrayList<Notebook> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPopupRecyclerView = (PopupRecyclerView) findViewById(R.id.prv_popup_recycler_view);

        mList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mList.add(new Notebook(Notebook.TYPE_FOLDER, "folder " + i, "/Android"));
        }
        for (int i = 0; i < 5; i++) {
            mList.add(new Notebook(Notebook.TYPE_FILE, "file " + i, "/Android"));
        }
        mPopupRecyclerView.bindRecyclerAdapter(new RecyclerAdapter(mList));

        mPopupRecyclerView.setOnPopupItemClickListener(new PopupRecyclerView.OnPopupItemClickListener() {
            @Override
            public void onItemClick(String text, boolean isFile) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPopupRecyclerView.isExtended()) {
            mPopupRecyclerView.zoomOut();
        } else {
            super.onBackPressed();
        }
    }
}
