package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.WorkoutDetailActivity;
import com.example.hoang.fitness.models.Workout;
import com.example.hoang.fitness.utils.AssetsUtil;
import com.example.hoang.fitness.utils.DrawableUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private Context context;
    private List<Workout> arrayList;


    public WorkoutAdapter(Context context, List<Workout> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_workout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ViewHolder holder, int position) {
        holder.mName.setText(arrayList.get(position).getName());
        holder.mDetail.setText(arrayList.get(position).getLevel()+" • "+
                arrayList.get(position).getTime()+" mins • "+ arrayList.get(position).getCalorie()+" calories");
        holder.mImage.setImageDrawable(AssetsUtil.getDrawable(
                context,"workout_pic/"+arrayList.get(position).getPicPhone()));
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkoutDetailActivity.class);
                intent.putExtra("WORKOUT_ID",arrayList.get(position).getId());
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
        @BindView(R.id.tv_workout_name)
        TextView mName;
        @BindView(R.id.tv_workout_detail)
        TextView mDetail;
        @BindView(R.id.img_workout)
        ImageView mImage;
        @BindView(R.id.fr_item_workout)
        FrameLayout mLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}