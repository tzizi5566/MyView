package com.kop.myview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kop.myview.ui.ProgressBarActivity;
import com.kop.myview.ui.TemperatureActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnClickListener {

  @BindView(R.id.rv_view) RecyclerView mRvView;

  private MainAdapter mAdapter;
  private ArrayList<String> mList;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setData();
    fillAdapter();
  }

  private void setData() {
    mList = new ArrayList<>();
    mList.add("进度条");
    mList.add("温度旋转按钮");
    mList.add("View1");
    mList.add("View1");
    mList.add("View1");
    mList.add("View1");
  }

  private void fillAdapter() {
    if (mAdapter == null) {
      mAdapter = new MainAdapter(mList);
      mAdapter.setOnClickListener(this);
      mRvView.setLayoutManager(new GridLayoutManager(this, 4));
      mRvView.addItemDecoration(new MyItemDecoration(this));
      mRvView.setAdapter(mAdapter);
    }
  }

  @Override public void click(int position) {
    switch (position) {
      case 0:
        startActivity(new Intent(this, ProgressBarActivity.class));
        break;

      case 1:
        startActivity(new Intent(this, TemperatureActivity.class));
        break;

      default:

        break;
    }
  }
}
