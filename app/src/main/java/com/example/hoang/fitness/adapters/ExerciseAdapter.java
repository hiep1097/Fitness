package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.ExerciseDescriptionActivity;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.utils.AssetsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> arrayList;


    public ExerciseAdapter(Context context, List<Exercise> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_exercise,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {
        holder.mImage.setImageDrawable(AssetsUtil.getDrawable(
                context,"exercise_pic/"+arrayList.get(position).getPic()));
        holder.mName.setText(arrayList.get(position).getName());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseDescriptionActivity.class);
                intent.putExtra("EXERCISE_ID",arrayList.get(position).getId());
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
        @BindView(R.id.img_item_exercise)
        ImageView mImage;
        @BindView(R.id.tv_name_item_exercise)
        TextView mName;
        @BindView(R.id.rl_item_exercise)
        RelativeLayout mLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}