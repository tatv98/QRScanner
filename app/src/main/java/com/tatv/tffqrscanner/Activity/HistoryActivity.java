package com.tatv.tffqrscanner.Activity;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tatv.tffqrscanner.Adapter.HistoryAdapter;
import com.tatv.tffqrscanner.Entity.History;
import com.tatv.tffqrscanner.R;
import com.tatv.tffqrscanner.SQLite.ORM.HistoryORM;

import java.util.List;


public class HistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    // Init ui elements
    SwipeRefreshLayout historySwipeRefreshLayout;
    ListView historyListView;
    ImageView imgBack, imgClear;

    // Variables
    HistoryORM h = new HistoryORM();
    List<History> historyList;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historySwipeRefreshLayout = findViewById(R.id.historySwipeRefreshLayout);
        historyListView = findViewById(R.id.historyListView);
        imgBack = findViewById(R.id.backButton);
        imgClear = findViewById(R.id.clearButton);
        imgBack.setOnClickListener(this);
        imgClear.setOnClickListener(this);

        historySwipeRefreshLayout.setOnRefreshListener(this);

        getData();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                getData();
                historySwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void getData() {
        historyList = h.getAll(getApplicationContext());
        adapter = new HistoryAdapter(this,historyList);
        historyListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.clearButton:
                h.clearAll(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Đã xóa lịch sử!", Toast.LENGTH_LONG).show();
                historyList.clear();
                historyListView.deferNotifyDataSetChanged();
                break;
        }

    }
}
