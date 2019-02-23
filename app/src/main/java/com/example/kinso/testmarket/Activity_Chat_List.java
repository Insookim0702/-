package com.example.kinso.testmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by kinso on 2019-02-19.
 */

public class Activity_Chat_List extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot();
    private String myusercode, writenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("채팅 목록");
        setContentView(R.layout.activity_chat_list);
        //내회원 코드 가져온다. 어디서? 1. ActivityMain에서,,
        Intent getIntent = getIntent();
        myusercode = getIntent.getExtras().getString("MyUserCode");
        writenumber = getIntent.getExtras().getString("WriteNumber");
        System.out.println("채팅리스트에서 내 회원 코드" + myusercode);
        listView =(ListView)findViewById(R.id.list);

        //채팅방 보여주기
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_roomList);
        listView.setAdapter(arrayAdapter);
        //특정 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기함
        //onDataChange는 Database가 변경되었을 떄 호출하고
        //onCancelled는 취소되었을 떄 호출한다.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                arr_roomList.clear();
                arr_roomList.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //리스트뷰의 채팅방을 클릭했을 때 반응
        //채팅방의 이름과 입장하는 유저의 이름을 전달
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent movechat = new Intent(getApplicationContext(), ActivitySendFire.class);
                movechat.putExtra("writenumber",writenumber);
                movechat.putExtra("myusercode", myusercode);
                startActivity(movechat);
            }
        });


    }
}
