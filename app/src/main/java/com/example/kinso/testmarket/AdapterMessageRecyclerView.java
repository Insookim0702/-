package com.example.kinso.testmarket;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kinso on 2019-01-31.
 */

public class AdapterMessageRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<CardMessageInfo> DataInfoArrayList;
    private String myusercode;
    public AdapterMessageRecyclerView(String myusercode,ArrayList<CardMessageInfo> DataInfoArrayList){
        this.DataInfoArrayList = DataInfoArrayList;
        this.myusercode = myusercode;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView Tx_SendDay;
        private TextView Tx_Message;
        private TextView Tx_Sender;
        MyViewHolder(View view){
            super(view);
            Tx_SendDay = view.findViewById(R.id.Tx_SendDay);
            Tx_Message = view.findViewById(R.id.Tx_Message);
            Tx_Sender = view.findViewById(R.id.Tx_Sender);
        }
    }

    @Override
    public AdapterMessageRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_message_row, parent, false);
        return new AdapterMessageRecyclerView.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        AdapterMessageRecyclerView.MyViewHolder viewHolder = (AdapterMessageRecyclerView.MyViewHolder) holder;
        viewHolder.Tx_SendDay.setText(DataInfoArrayList.get(position).SendDay);
        viewHolder.Tx_Message.setText(DataInfoArrayList.get(position).Message);
        //내 유저코드, 글 작성자 유저코드
        System.out.println("내 유저 코드 : " + myusercode);
        if(myusercode!=null){
            if(DataInfoArrayList.get(position).Sender.equals(myusercode)){
                viewHolder.Tx_Sender.setText("내가 보낸 것.");
            }else{
                viewHolder.Tx_Sender.setText("보낸 사람");
            }
        }else{
           System.out.println("ㅗ");
           System.out.println("ㅗ");
           System.out.println("ㅗ");
           System.out.println("ㅗ");
           System.out.println("ㅗ");
           System.out.println("ㅗ");
        }

    }
    @Override
    public int getItemCount(){
        return DataInfoArrayList.size();
    }
}
