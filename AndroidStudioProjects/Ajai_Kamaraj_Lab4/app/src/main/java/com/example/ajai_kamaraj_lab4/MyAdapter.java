package com.example.ajai_kamaraj_lab4;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
   List<String> mDataset;

   MyAdapter(List<String> myDataset) {
       mDataset= myDataset;
    }
static class MyViewHolder extends RecyclerView.ViewHolder{
       TextView textView;
       MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
           super(inflater.inflate(R.layout.list_layout, parent, false));
           textView = itemView.findViewById(R.id.txtList);
       }
}

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
holder.textView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
