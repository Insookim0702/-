package com.example.kinso.testmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kinso on 2019-01-30.
 */

public class ActivitySendMessage extends AppCompatActivity {
    private EditText Ed_message;
    private Button Btn_send;
    private ClassJsonInfoUser classJsonInfoUser;
    private String RoomNumber, Title, Receiver, Sender, Message, SendDay, WriteNum, InputTitle, InputReceiver, InputSender, MyUserCode, FirstSender;
    private ArrayList<CardMessageInfo> DataInfoArray;
    private RecyclerView RecyclerView_Send_Message;
    private AdapterMessageRecyclerView adapterMessageRecyclerView;
private String Today, InputMessage;
    ClassSqliteChat classSqliteChat;
    //보내는 사람 회원코드
    //받는 사람 회원 코드
    //보내는 메시지 내용
    //보내는 시간
    @Override
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_send_message);
        Ed_message = (EditText)findViewById(R.id.Ed_message);
        Btn_send =(Button)findViewById(R.id.Btn_send);
        RecyclerView_Send_Message = findViewById(R.id.RecyclerView_Send_Message);
        Intent getintent = getIntent();
        InputTitle = getintent.getExtras().getString("Title");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(InputTitle);
        ab.setDisplayHomeAsUpEnabled(true);
        RecyclerView_Send_Message.setHasFixedSize(true);

        WriteNum = getintent.getExtras().getString("WriteNum");
        InputReceiver = getintent.getExtras().getString("Receiver");
        MyUserCode = getintent.getExtras().getString("MyUserCode");
        InputSender = MyUserCode;/*getintent.getExtras().getString("Sender");*/
        System.out.println("ActivitySendMessage가 받은 파라미터 값!!!!!!!!!!!!!!!!!!");
        System.out.println(InputTitle);
        System.out.println(WriteNum);
        System.out.println(InputReceiver);
        System.out.println(InputSender);


        DataInfoArray = new ArrayList<>();

        //Sqlite에서 읽어온 데이터베이스
        classSqliteChat = new ClassSqliteChat(ActivitySendMessage.this,"chat_table",null,1);
        String json = "{ \"message\" : [" + classSqliteChat.getResult(WriteNum, MyUserCode)+"]}";
        System.out.println("Sqlite에서 읽어온 데이터베이스 " + json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray arr = jsonObject.getJSONArray("message");
            JSONObject item;
            for(int i=0;i<arr.length();i++){
                item = arr.getJSONObject(i);
                RoomNumber = item.getString("No");
                Title = item.getString("Title");
                Receiver = item.getString("Receiver");
                Sender = item.getString("Sender");
                if(!Sender.equals(MyUserCode)){
                    FirstSender =Sender;
                }
                Message = item.getString("Message");
                SendDay = item.getString("SendDate");
                System.out.println(RoomNumber);
                System.out.println("제목 : " +Title);
                System.out.println("보낸이 : " + Sender);
                System.out.println("메시지 : " + Message);
                System.out.println("받은이 : " + Receiver);
                System.out.println("쓴날짜 : " + SendDay);
                //이걸 DataInfoArray에 add하고, setAdapter한다.
                DataInfoArray.add(new CardMessageInfo(WriteNum, Title, Receiver, Sender, Message, SendDay, MyUserCode));
                adapterMessageRecyclerView = new AdapterMessageRecyclerView(MyUserCode, DataInfoArray);
                RecyclerView_Send_Message.setAdapter(adapterMessageRecyclerView);
                System.out.println("==========================");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // 데이터베이스에서 메시지 정보를 읽어온다.
        Response.Listener<String> readresponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    Log.i("tagconvertstr",response);
                    // 읽어온 정보를 JSONObject -> JSONArray로 바꿔준다.
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("message");//[{},{},{}]
                    JSONObject item;
                    int count=0;
                    // 얻은 정보를 for문 돌리면서 파라미터 값을 추출한다.
                    for(int i=0;i<array.length();i++){
                        item = array.getJSONObject(i);
                        RoomNumber = item.getString("RoomNumber");
                        Title = item.getString("Title");
                        Receiver = item.getString("Receiver");
                        Sender = item.getString("Sender");
                        if(!Sender.equals(MyUserCode)){
                            FirstSender =Sender;
                        }
                        Message = item.getString("Message");
                        SendDay = item.getString("SendDay");
                        System.out.println(RoomNumber);
                        System.out.println(Title);
                        System.out.println(Receiver);
                        System.out.println(Sender);
                        System.out.println(Message);

                        //이걸 DataInfoArray에 add하고, setAdapter한다.
                        DataInfoArray.add(new CardMessageInfo(WriteNum, Title, Receiver, Sender, Message, SendDay, MyUserCode));
                        adapterMessageRecyclerView = new AdapterMessageRecyclerView(MyUserCode, DataInfoArray);
                        RecyclerView_Send_Message.setAdapter(adapterMessageRecyclerView);
                        System.out.println("==========================");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        System.out.println("!!!!!!!!!!!!!!!!!!Activity!!!!!!!!!!!!!!!!!!");
        System.out.println("글 등록 번호"+WriteNum);
        System.out.println("받는 사람"+InputReceiver);
        System.out.println("보내는 사람"+InputSender);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        RequestReadMessage requestReadMessage = new RequestReadMessage(WriteNum,InputReceiver,InputSender,readresponseListener);
        RequestQueue readqueue = Volley.newRequestQueue(ActivitySendMessage.this);
        readqueue.add(requestReadMessage);

        Btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Random random = new Random();
                //1. 글 번호 +난수(0~60)
                String InputRoomNumber =  WriteNum+ random.nextInt(60);
                InputMessage = Ed_message.getText().toString();
                Ed_message.setText("");
                //6. 보내는 시간
                classJsonInfoUser = new ClassJsonInfoUser();
                Today = classJsonInfoUser.today();
                System.out.println("FirstSender : " + FirstSender);
                if(InputReceiver.equals(MyUserCode)){
                    InputReceiver= FirstSender;
                }
                System.out.println("InputReceiver : " + InputReceiver);
                Response.Listener<String> writeresponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        classSqliteChat.insert(WriteNum, MyUserCode, InputTitle,InputReceiver,InputMessage,Today);
                        Toast.makeText(ActivitySendMessage.this,response,Toast.LENGTH_SHORT).show();
                    }
                };
                RequestSendMessage requestSendMessage = new RequestSendMessage(InputRoomNumber,
                        WriteNum,
                        InputTitle,
                        InputReceiver,
                        InputSender,
                        InputMessage,
                        Today, writeresponseListener);
                RequestQueue writequeue = Volley.newRequestQueue(ActivitySendMessage.this);
                writequeue.add(requestSendMessage);
                DataInfoArray.add(new CardMessageInfo(WriteNum, InputTitle, InputReceiver, InputSender, InputMessage, Today,MyUserCode));
                adapterMessageRecyclerView = new AdapterMessageRecyclerView(MyUserCode, DataInfoArray);
                RecyclerView_Send_Message.setAdapter(adapterMessageRecyclerView);
                //adapterMessageRecyclerView.notifyDataSetChanged();
            }
        });
    }
    //액션바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
