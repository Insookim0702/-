package com.example.kinso.testmarket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kinso on 2018-12-25.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPicture;
        TextView tvPrice;

        MyViewHolder(View view){
            super(view);
            ivPicture = view.findViewById(R.id.iv_picture);
            tvPrice = view.findViewById(R.id.tv_price);
        }
    }

    private ArrayList<ItemInfo> ItemInfoArrayList;
    MyAdapter(ArrayList<ItemInfo> ItemInfoArrayList){
        this.ItemInfoArrayList = ItemInfoArrayList;
    }

    //필수로 만들어져야 하는 메소드1: 새로운 뷰생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //새로운 뷰를 만든다.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(v);
    }

    //필수로 만들어져야 하는 메소드2 : ListView의 getView부분을 담당하는 메소드
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        myViewHolder.ivPicture.setImageResource(ItemInfoArrayList.get(position).drawableId);
        myViewHolder.tvPrice.setText(ItemInfoArrayList.get(position).price);
    }

    //필수로 만들어져야 하는 메소드3
    @Override
    public int getItemCount(){
        return ItemInfoArrayList.size();
    }
}


