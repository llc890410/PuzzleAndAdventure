package com.example.game03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private String[] mDataSet;

    public MyListAdapter(String[] dataSet) {
        mDataSet = dataSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTask;
        private Button btnUnlock;
        private ImageView imgCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tvTask);
            btnUnlock = itemView.findViewById(R.id.btnUnlock);
            imgCheckbox = itemView.findViewById(R.id.imgCheckbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        holder.tvTask.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

}
