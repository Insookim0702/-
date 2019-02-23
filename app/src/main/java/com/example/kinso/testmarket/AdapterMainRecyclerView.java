package com.example.kinso.testmarket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kinso on 2019-01-28.
 */

public class AdapterMainRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<CardBookInfo> DataInfoArrayList;
    private RecyclerViewClickListener recyclerViewClickListener;
    public AdapterMainRecyclerView(ArrayList<CardBookInfo> DataInfoArrayList) {
        this.DataInfoArrayList = DataInfoArrayList;
    }

    public interface RecyclerViewClickListener{
        //아이템 전체 부분 클릭
        void onItemClicked(int position, String category, String writenum);
        //share 버튼 클릭
        //void onShareButtonClicked(int position);
    }
    public void setOnClickListener(ActivityMain listener){
        recyclerViewClickListener = listener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Tx_writeday,
                 Tx_title,
                 Tx_bcauthor,
                 Tx_bcpublisher,
                 Tx_bcprice,
                 Tx_bcdiscountprice,
                 Tx_userprice,
                 Tx_lifetime;
        ImageView Iv_image;
        LinearLayout Layout_AuthorPublisher;
        MyViewHolder(View view){
            super(view);
            Tx_writeday = view.findViewById(R.id.Tx_writeday);
            Tx_title = view.findViewById(R.id.Tx_title);
            Tx_bcauthor = view.findViewById(R.id.Tx_bcauthor);
            Tx_bcpublisher = view.findViewById(R.id.Tx_bcpublisher);
            Tx_bcprice = view.findViewById(R.id.Tx_bcprice);
            Tx_bcdiscountprice  = view.findViewById(R.id.Tx_bcdiscountprice);
            Tx_userprice  = view.findViewById(R.id.Tx_userprice);
            Iv_image = view.findViewById(R.id.Iv_image);
            Tx_lifetime = view.findViewById(R.id.Tx_lifetime);
            Layout_AuthorPublisher = view.findViewById(R.id.Layout_AuthorPublisher);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,parent,false);
        return new AdapterMainRecyclerView.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterMainRecyclerView.MyViewHolder myViewHolder = (AdapterMainRecyclerView.MyViewHolder)holder;
        myViewHolder.Tx_title.setText(DataInfoArrayList.get(position).title);
        myViewHolder.Tx_writeday.setText("등록일 : "+DataInfoArrayList.get(position).writeday);
        myViewHolder.Tx_lifetime.setText(" (삭제까지 ..." +DataInfoArrayList.get(position).life_day+"일, "+ DataInfoArrayList.get(position).life_time.toString().substring(0,2) +"시간 남음)");

        if(!DataInfoArrayList.get(position).bcauthor.equals("0")){
            myViewHolder.Layout_AuthorPublisher.setVisibility(View.VISIBLE);
            myViewHolder.Tx_bcprice.setVisibility(View.VISIBLE);
            myViewHolder.Tx_bcdiscountprice.setVisibility(View.VISIBLE);
            myViewHolder.Tx_bcauthor.setText(" 저자 : "+DataInfoArrayList.get(position).bcauthor);
            myViewHolder.Tx_bcpublisher.setText("출판사 : "+DataInfoArrayList.get(position).bcpublisher);
            myViewHolder.Tx_bcprice.setText(" (정상가 : "+DataInfoArrayList.get(position).bcprice);
            myViewHolder.Tx_bcdiscountprice.setText("할인가 : "+DataInfoArrayList.get(position).bcdiscountprice+")");
            AsyncReadBookcover asyncReadBookcover = new AsyncReadBookcover(DataInfoArrayList.get(position).viewimage,myViewHolder.Iv_image);
            asyncReadBookcover.execute();
        }else {
            //Drawable drawable = getResources().getDrawable(R.drawable.noimage);
            if (DataInfoArrayList.get(position).viewimage.equals("0")) {
                myViewHolder.Iv_image.setImageResource(R.drawable.noimage);
            } else {
                AsyncReadImage asyncReadImage = new AsyncReadImage(DataInfoArrayList.get(position).viewimage, myViewHolder.Iv_image);
                asyncReadImage.execute();
            }
        }
        myViewHolder.Tx_userprice.setText(DataInfoArrayList.get(position).userprice);
        //클릭 이벤트 발생
        if(recyclerViewClickListener !=null){
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    recyclerViewClickListener.onItemClicked(pos, DataInfoArrayList.get(pos).category, DataInfoArrayList.get(pos).writenumber);
                }
            });
            /*holder.shareBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    recyclerViewClickListener.onShareButtonClicked(pos);
                }
            });*/
        }
        System.out.println("메인의 포지션 : >>>>>>>>>>"+position);
    }

    @Override
    public int getItemCount() {
        return DataInfoArrayList.size();
    }
}

