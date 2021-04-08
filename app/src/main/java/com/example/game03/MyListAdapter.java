package com.example.game03;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game03.model.ListData;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private static final String TAG = "Game03"; //log中的String
    private List<ListData> mListDataSet;
    private Context context;

    public MyListAdapter(List<ListData> dataSet, Context context) {

        mListDataSet = dataSet;
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
        // Get element from your dataSet at this position and replace the contents of the view with that element
        holder.tvTask.setText(mListDataSet.get(position).getTaskName());
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

        Log.i(TAG,"position="+position+" isFinished="+mListDataSet.get(position).isFinished());

        //判斷isFinished更改checkBox圖片
        if (mListDataSet.get(position).isFinished() == true){
            holder.imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_36);
        }else if (mListDataSet.get(position).isFinished() == false){
            holder.imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_36);
        }

    }

    @Override
    public int getItemCount() {
        return mListDataSet.size();
    }

}
