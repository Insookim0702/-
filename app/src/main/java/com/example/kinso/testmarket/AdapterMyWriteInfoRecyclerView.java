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
 * Created by kinso on 2019-01-30.
 */

public class AdapterMyWriteInfoRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardBookInfo> DataInfoArrayList;
    private ActivityMyWriteInfo recyclerViewClickListener;
    public AdapterMyWriteInfoRecyclerView(ArrayList<CardBookInfo> DAtaInfoArrayList){
        this.DataInfoArrayList = DAtaInfoArrayList;
    }
    public interface RecyclerViewClickListener{
        void onItemClicked(int position, String category, String writenum);
        void onLongItemClicked(int position, String category, String writenum);

    }
    public void setOnClickListener(ActivityMyWriteInfo listener){
        recyclerViewClickListener = listener;
    }

    public interface OnLongCLickListener {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
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
        ViewHolder(View view){
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,parent,false);
        return new AdapterMyWriteInfoRecyclerView.ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        AdapterMyWriteInfoRecyclerView.ViewHolder viewHolder = (AdapterMyWriteInfoRecyclerView.ViewHolder) holder;
        viewHolder.Tx_title.setText(DataInfoArrayList.get(position).title);
        viewHolder.Tx_writeday.setText("등록일 : "+DataInfoArrayList.get(position).writeday );//삭제일(등록일 +4일)-현재시간
        viewHolder.Tx_lifetime.setText(" (삭제까지 ..." +DataInfoArrayList.get(position).life_day+"일, "+ DataInfoArrayList.get(position).life_time.toString().substring(0,2) +"시간 남음)");
        if(DataInfoArrayList.get(position).bcauthor!=null){
            viewHolder.Layout_AuthorPublisher.setVisibility(View.VISIBLE);
            viewHolder.Tx_bcprice.setText(DataInfoArrayList.get(position).bcprice);
            viewHolder.Tx_bcdiscountprice.setText(DataInfoArrayList.get(position).bcdiscountprice);
            viewHolder.Tx_bcauthor.setText(" 저자 : "+DataInfoArrayList.get(position).bcauthor);
            viewHolder.Tx_bcpublisher.setText("/출판사 : "+DataInfoArrayList.get(position).bcpublisher);
            viewHolder.Tx_bcprice.setText(" (정상가 : "+DataInfoArrayList.get(position).bcprice);
            viewHolder.Tx_bcdiscountprice.setText("/할인가 : "+DataInfoArrayList.get(position).bcdiscountprice+")");
            AsyncReadBookcover asyncReadBookcover = new AsyncReadBookcover(DataInfoArrayList.get(position).viewimage, viewHolder.Iv_image);
            asyncReadBookcover.execute();
        }else {
            //Drawable drawable = getResources().getDrawable(R.drawable.noimage);
            if (DataInfoArrayList.get(position).viewimage.equals("0")) {
                viewHolder.Iv_image.setImageResource(R.drawable.noimage);
            } else {
                AsyncReadImage asyncReadImage = new AsyncReadImage(DataInfoArrayList.get(position).viewimage, viewHolder.Iv_image);
                asyncReadImage.execute();
            }
        }
        viewHolder.Tx_userprice.setText(DataInfoArrayList.get(position).userprice);
        if(recyclerViewClickListener !=null){
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    recyclerViewClickListener.onItemClicked(pos,DataInfoArrayList.get(pos).category, DataInfoArrayList.get(pos).writenumber);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClickListener.onLongItemClicked(pos,DataInfoArrayList.get(pos).category, DataInfoArrayList.get(pos).writenumber);
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount(){
        return DataInfoArrayList.size();
    }

}
