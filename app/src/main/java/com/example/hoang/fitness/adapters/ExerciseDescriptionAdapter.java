package com.example.hoang.fitness.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.models.Exercise;
import com.example.hoang.fitness.utils.AssetsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseDescriptionAdapter extends RecyclerView.Adapter<ExerciseDescriptionAdapter.ViewHolder> {
    private Context context;
    private List<String> arrayList;


    public ExerciseDescriptionAdapter(Context context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ExerciseDescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_exercise_description,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseDescriptionAdapter.ViewHolder holder, int position) {
        holder.mSTT.setText("0"+(position+1));
        holder.mDescription.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_stt)
        TextView mSTT;
        @BindView(R.id.tv_exercise_item_description)
        TextView mDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}