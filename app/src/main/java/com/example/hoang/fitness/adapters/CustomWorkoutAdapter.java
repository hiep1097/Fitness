package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.WorkoutDetailActivity2;
import com.example.hoang.fitness.models.CustomWorkout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomWorkoutAdapter extends RecyclerView.Adapter<CustomWorkoutAdapter.ViewHolder> {
    private Context context;
    private List<CustomWorkout> arrayList;


    public CustomWorkoutAdapter(Context context, List<CustomWorkout> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomWorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_custom_workout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomWorkoutAdapter.ViewHolder holder, int position) {
        holder.mName.setText(arrayList.get(position).getName());
        holder.mDetail.setText(arrayList.get(position).getTime()+" mins • "+ arrayList.get(position).getCalorie()+" calories");
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkoutDetailActivity2.class);
                intent.putExtra("WORKOUT_NAME",arrayList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_item_custom_workout)
        TextView mName;
        @BindView(R.id.tv_detail_custom_workout)
        TextView mDetail;
        @BindView(R.id.rl_item_custom_workout)
        RelativeLayout mLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}