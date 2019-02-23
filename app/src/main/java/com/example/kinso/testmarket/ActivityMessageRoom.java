package com.example.kinso.testmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kinso on 2019-01-31.
 */

public class ActivityMessageRoom extends AppCompatActivity implements AdapterMessageRoom.RecyclerViewClickListener{
    private RecyclerView RecyclerView_Message_Room;
    private ArrayList<CardMessageInfo> DataInfoArray;
    private String DataWriteNum, DataTitle, DataReceiver, Sender, Message, SendDay;
    private String MyUserCode;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_room_message);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("쪽지함");
        ab.setDisplayHomeAsUpEnabled(true); //왼쪽 버튼 사용 여부 true
        Intent getintent = getIntent();

        MyUserCode = getintent.getExtras().getString("MyUserCode");

        //RecyclerView를 정의한다.
        RecyclerView_Message_Room = findViewById(R.id.RecyclerView_Message_Room);
        //조회할 데이터를 정의한다.
        DataInfoArray = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("ActvityMessageRoom!!!!!!!!!");
                Log.i("tagconverterstr",response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("message"); //[{},{},{}]
                    JSONObject item;
                    for(int i=0;i<array.length();i++){
                        item = array.getJSONObject(i);
                        //받아올 데이터
                        DataWriteNum = item.getString("WriteNumber");
                        DataTitle = item.getString("Title");
                        DataReceiver = item.getString("Receiver");
                        Sender = item.getString("Sender");
                        Message = item.getString("Message");
                        SendDay = item.getString("SendDay");
                        //arraylist를 만든다. <- Recyclerview에 들어가는 데이터
                        //조회할 데이터를 arraylist에 넣는다.
                        DataInfoArray.add(new CardMessageInfo(DataWriteNum, DataTitle, DataReceiver, Sender, Message, SendDay, MyUserCode));
                        AdapterMessageRoom adapterMessageRoom = new AdapterMessageRoom(DataInfoArray);
                        adapterMessageRoom.setOnClickListener(ActivityMessageRoom.this);
                        RecyclerView_Message_Room.setAdapter(adapterMessageRoom);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ActivityMessageRoom에서 받은 MyUserCode : " + MyUserCode);
        RequestMessageRoom requestMessageRoom = new RequestMessageRoom(MyUserCode,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityMessageRoom.this);
        queue.add(requestMessageRoom);


    }
    @Override
    public void onItemClicked(String DataWriteNum, String DataTitle, String DataReceiver, String Sender, int position){
        Intent moveSendMessageAct = new Intent(ActivityMessageRoom.this, ActivitySendMessage.class);
        moveSendMessageAct.putExtra("WriteNum", DataWriteNum);
        moveSendMessageAct.putExtra("Title", DataTitle);
        moveSendMessageAct.putExtra("Receiver", DataReceiver);
        moveSendMessageAct.putExtra("Sender", Sender);
        moveSendMessageAct.putExtra("MyUserCode",MyUserCode);
        ActivityMessageRoom.this.startActivity(moveSendMessageAct);

    }
    //액션바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
