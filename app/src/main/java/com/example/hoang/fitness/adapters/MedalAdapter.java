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
import com.example.hoang.fitness.models.Medal;
import com.example.hoang.fitness.utils.DrawableUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedalAdapter extends RecyclerView.Adapter<MedalAdapter.ViewHolder> {
    private Context context;
    private List<Medal> arrayList;
    private int wallpaperPos;
    ItemOnClick itemOnClick;

    public MedalAdapter(Context context, List<Medal> arrayList, ItemOnClick itemOnClick) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemOnClick = itemOnClick;
        readMedalPosFromFireBase();
    }

    @NonNull
    @Override
    public MedalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_medal,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedalAdapter.ViewHolder holder, int position) {
        if (wallpaperPos == position){
            holder.mCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mCheck.setVisibility(View.GONE);
        }
        holder.mWallpaper.setImageDrawable(DrawableUtil.getInstance().getDrawable(
                context,arrayList.get(position).getImage()));
        if (arrayList.get(position).isDaMua()){
            holder.mNen3.setVisibility(View.VISIBLE);
        } else {
            holder.mNen3.setVisibility(View.GONE);
        }
        if (position==wallpaperPos){
            holder.mKhung.setVisibility(View.VISIBLE);
            holder.mNen1.setVisibility(View.VISIBLE);
            holder.mNen2.setVisibility(View.GONE);
        } else {
            holder.mKhung.setVisibility(View.GONE);
            holder.mNen1.setVisibility(View.GONE);
            holder.mNen2.setVisibility(View.VISIBLE);
        }
        holder.mCoin.setText(arrayList.get(position).getPrice()+"");
        holder.mWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallpaperPos = position;
                itemOnClick.clickItem(position,false);
                notifyDataSetChanged();
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
        @BindView(R.id.imageWallpaper) ImageView mWallpaper;
        @BindView(R.id.imageCheck) ImageView mCheck;
        @BindView(R.id.img_khung) ImageView mKhung;
        @BindView(R.id.img_nen1) ImageView mNen1;
        @BindView(R.id.img_nen2) ImageView mNen2;
        @BindView(R.id.img_nen3) ImageView mNen3;
        @BindView(R.id.tv_coin) TextView mCoin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void readMedalPosFromFireBase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users");
        DatabaseReference currentUserDB = databaseReference.child(user.getUid());
        DatabaseReference myMedalPosRef = currentUserDB.child("medalpos");
        myMedalPosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    long currentPos = (long) dataSnapshot.child("MEDAL_POS").getValue();
                    wallpaperPos = (int) currentPos;
                    notifyDataSetChanged();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}