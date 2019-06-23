package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.listener.ItemOnClick;
import com.example.hoang.fitness.models.ExercisePick;
import com.example.hoang.fitness.utils.AssetsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickExerciseAdapter2 extends RecyclerView.Adapter<PickExerciseAdapter2.ViewHolder> {
    private Context context;
    private List<ExercisePick> arrayList;
    private int count;
    ItemOnClick listener;

    public PickExerciseAdapter2(Context context, List<ExercisePick> arrayList, ItemOnClick listener, int count) {
        this.context = context;
        this.arrayList = arrayList;
        this.count = count;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PickExerciseAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_pick_exercise_2,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PickExerciseAdapter2.ViewHolder holder, int position) {
        holder.mImage.setImageDrawable(AssetsUtil.getDrawable(
                context,"exercise_pic/"+arrayList.get(position).getExercise().getPic()));
        holder.mName.setText(arrayList.get(position).getExercise().getName());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_item_pick_exercise)
        ImageView mImage;
        @BindView(R.id.tv_name_item_pick_exercise)
        TextView mName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
