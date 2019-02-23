package com.example.kinso.testmarket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by kinso on 2019-02-02.
 */

public class AdapterMessageRoom extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardMessageInfo> DataInfoArrayList;
    private AdapterMessageRoom.RecyclerViewClickListener recyclerViewClickListener;
    public AdapterMessageRoom(ArrayList<CardMessageInfo> DataInfoArrayList){
        this.DataInfoArrayList =DataInfoArrayList;
    }
    public interface RecyclerViewClickListener{
        void onItemClicked(String DataWriteNum, String DataTitle, String DataReceiver, String DataSender,int position);
    }
    public void setOnClickListener(ActivityMessageRoom listener){
        recyclerViewClickListener = listener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Tx_Title;
        TextView Tx_Day;
        TextView Tx_message;
        MyViewHolder(View view){
            super(view);
            Tx_Title = view.findViewById(R.id.Tx_Title);
            Tx_Day = view.findViewById(R.id.Tx_Day);
            Tx_message = view.findViewById(R.id.Tx_message);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_message_room_row,parent, false);
        return new AdapterMessageRoom.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        AdapterMessageRoom.MyViewHolder myviewholder = (AdapterMessageRoom.MyViewHolder)holder;
        String title = DataInfoArrayList.get(position).Title;
        if(title.length()>22){
            title = DataInfoArrayList.get(position).Title.substring(0,23) +"...";
        }
        myviewholder.Tx_Title.setText(title);
        myviewholder.Tx_Day.setText(DataInfoArrayList.get(position).SendDay);
        if(DataInfoArrayList.get(position).Sender.equals(DataInfoArrayList.get(position).MyUserCode)) {
            myviewholder.Tx_message.setText("나 : " + DataInfoArrayList.get(position).Message);
        }else{
            myviewholder.Tx_message.setText("보낸이 : " + DataInfoArrayList.get(position).Message);
        }
        if(recyclerViewClickListener!=null){
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    recyclerViewClickListener.onItemClicked(DataInfoArrayList.get(pos).WriteNum, DataInfoArrayList.get(pos).Title, DataInfoArrayList.get(pos).Receiver,DataInfoArrayList.get(pos).Sender,pos);
                }
            });
        }
    }


    @Override
    public int getItemCount(){
        return DataInfoArrayList.size();
    }
}
