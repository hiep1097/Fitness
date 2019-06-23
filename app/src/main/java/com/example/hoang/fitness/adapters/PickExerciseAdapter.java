package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.activities.ExerciseDescriptionActivity;
import com.example.hoang.fitness.listener.ItemOnClick;
import com.example.hoang.fitness.models.ExercisePick;
import com.example.hoang.fitness.utils.AssetsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickExerciseAdapter extends RecyclerView.Adapter<PickExerciseAdapter.ViewHolder> {
    private Context context;
    private List<ExercisePick> arrayList;
    private int count;
    ItemOnClick listener;

    public PickExerciseAdapter(Context context, List<ExercisePick> arrayList, ItemOnClick listener, int count) {
        this.context = context;
        this.arrayList = arrayList;
        this.count = count;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PickExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_pick_exercise,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PickExerciseAdapter.ViewHolder holder, int position) {
//        if (count==3) holder.mLayout.setClickable(false);
//        else holder.mLayout.setClickable(true);
        holder.mImage.setImageDrawable(AssetsUtil.getDrawable(
                context,"exercise_pic/"+arrayList.get(position).getExercise().getPic()));
        holder.mName.setText(arrayList.get(position).getExercise().getName());
        if (!arrayList.get(position).isChoose()){
            holder.mChoose.setImageResource(R.drawable.ic_createmyworkout_choose_default);
        } else {
            holder.mChoose.setImageResource(R.drawable.ic_createmyworkout_choose_seleted);
        }
        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExerciseDescriptionActivity.class);
                intent.putExtra("EXERCISE_ID",arrayList.get(position).getExercise().getId());
                context.startActivity(intent);
            }
        });
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChoose = !arrayList.get(position).isChoose();
                if (count!=3 || !isChoose){
                    arrayList.get(position).setChoose(isChoose);
                    listener.clickItem(position, isChoose);
                    notifyDataSetChanged();
                }
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
        @BindView(R.id.img_choose_exercise)
        ImageView mChoose;
        @BindView(R.id.img_item_pick_exercise)
        ImageView mImage;
        @BindView(R.id.tv_name_item_pick_exercise)
        TextView mName;
        @BindView(R.id.btn_pick_exercise_info)
        Button mInfo;
        @BindView(R.id.rl_item_exercise)
        RelativeLayout mLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
