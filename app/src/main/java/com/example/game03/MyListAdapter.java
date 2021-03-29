package com.example.game03;

import android.content.Context;
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
    private Context context;

    public MyListAdapter(String[] dataSet, Context context) {

        mDataSet = dataSet;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTask;
        private Button btnUnlock;
        private ImageView imgCheckbox;

        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tvTask);
            btnUnlock = itemView.findViewById(R.id.btnUnlock);
            imgCheckbox = itemView.findViewById(R.id.imgCheckbox);
            mView = itemView;
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
        //holder.tvTask.setBackgroundColor(context.getColor(R.color.white));
        holder.tvTask.setTextColor(context.getColor(R.color.black));

        //change color num
        switch (position){
            case 0:
                holder.mView.setBackgroundColor(context.getColor(R.color.red));
                break;
            case 1:
                holder.mView.setBackgroundColor(context.getColor(R.color.orange));
                break;
            case 2:
                holder.mView.setBackgroundColor(context.getColor(R.color.yellow));
                break;
            case 3:
                holder.mView.setBackgroundColor(context.getColor(R.color.green));
                break;
            case 4:
                holder.mView.setBackgroundColor(context.getColor(R.color.blue));
                break;
            case 5:
                holder.mView.setBackgroundColor(context.getColor(R.color.navy));
                break;
            case 6:
                holder.mView.setBackgroundColor(context.getColor(R.color.purple));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

}
