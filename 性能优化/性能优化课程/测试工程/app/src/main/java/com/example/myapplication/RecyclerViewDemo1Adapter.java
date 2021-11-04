package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecyclerViewDemo1Adapter extends RecyclerView.Adapter<RecyclerViewDemo1Adapter.ViewHolder> {

    private List<String> mData;

    public RecyclerViewDemo1Adapter(List<String> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(this.mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item点击事件
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mData != null ? this.mData.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private FlowLayout mFlow;
        public ViewHolder(View itemView) {
            super(itemView);
            mFlow =  itemView.findViewById(R.id.fl_test);
        }

        public void setData(String title) {
        }
    }
}
