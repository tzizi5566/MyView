package com.kop.myview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功    能: //TODO
 * 创 建 人: KOP
 * 创建日期: 2017/5/5 17:28
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<String> mList;

  interface OnClickListener {
    void click(int position);
  }

  private OnClickListener mOnClickListener;

  public void setOnClickListener(OnClickListener onClickListener) {
    mOnClickListener = onClickListener;
  }

  public MainAdapter(ArrayList<String> list) {
    mList = list;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MainViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof MainViewHolder) {
      MainViewHolder viewHolder = (MainViewHolder) holder;
      viewHolder.mTvView.setText(mList.get(position));
    }
  }

  @Override public int getItemCount() {
    return mList.size();
  }

  class MainViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_view) TextView mTvView;

    public MainViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (mOnClickListener != null) {
            mOnClickListener.click(getAdapterPosition());
          }
        }
      });
    }
  }
}
