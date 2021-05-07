package com.example.PuzzleAndAdventure.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PuzzleAndAdventure.MainActivity;
import com.example.PuzzleAndAdventure.R;
import com.example.PuzzleAndAdventure.model.ListData;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private static final String TAG = "Game03"; //log中的String
    private List<ListData> mListDataSet;
    private Context context;
    private String playerID = MainActivity.playerID; //玩家ID

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
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
            case 1:
                holder.mView.setBackgroundColor(context.getColor(R.color.orange));
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/index2nd.html"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
            case 2:
                holder.mView.setBackgroundColor(context.getColor(R.color.yellow));
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/index3rd.html"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
            case 3:
                holder.mView.setBackgroundColor(context.getColor(R.color.green));
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/index4th.html"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
            case 4:
                holder.mView.setBackgroundColor(context.getColor(R.color.blue));
                holder.tvTask.setTextColor(context.getColor(R.color.white));
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/index5th.html"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
            case 5:
                holder.mView.setBackgroundColor(context.getColor(R.color.navy));
                holder.tvTask.setTextColor(context.getColor(R.color.white));
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/index6th.html"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
            case 6:
                holder.mView.setBackgroundColor(context.getColor(R.color.purple));
                holder.tvTask.setTextColor(context.getColor(R.color.white));
                holder.btnUnlock.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW); //開啟瀏覽器的Intent
                    intent.setData(Uri.parse("http://s07.tku.edu.tw/~407410942/index7th.html"+"?id="+playerID));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                break;
        }

        Log.i(TAG,"position="+position+" isFinished="+mListDataSet.get(position).isFinished());

        //判斷isFinished更改checkBox圖片
        if (mListDataSet.get(position).isFinished()){
            if (position < 4)
                holder.imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_black_36);
            else
                holder.imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_36);

        }else if (!mListDataSet.get(position).isFinished()){
            if(position < 4)
                holder.imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_black_36);
            else
                holder.imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_36);
        }
        //Button Unlock VISIBLE
        if (mListDataSet.get(position).isSearched() && !mListDataSet.get(position).isFinished()){
            holder.btnUnlock.setVisibility(View.VISIBLE);
        }else {
            holder.btnUnlock.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mListDataSet.size();
    }

}
