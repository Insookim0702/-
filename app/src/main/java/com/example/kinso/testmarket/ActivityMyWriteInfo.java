package com.example.kinso.testmarket;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kinso on 2019-01-30.
 */

public class ActivityMyWriteInfo extends AppCompatActivity implements AdapterMyWriteInfoRecyclerView.RecyclerViewClickListener, AdapterMyWriteInfoRecyclerView.OnLongCLickListener{
    private RecyclerView recycler_view_mywrite;
    private ArrayList<CardBookInfo> DataInfoArrayList;

    private String writenumber, getWriteNumber, getCategory;
    private String writeday;
    private String title;
    private String bcauthor;
    private String bcpublisher;
    private String bcprice;
    private String bcdiscountprice;
    private String userprice;
    private String viewimage;
    private String myusercode;
    private String life_day, life_time;
    private int pos;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_my_write_info);
        recycler_view_mywrite = (RecyclerView)findViewById(R.id.recycler_view_mywrite);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("내가 등록한 아이템");
        ab.setDisplayHomeAsUpEnabled(true);
        Intent getvalue = getIntent();
        myusercode = getvalue.getExtras().getString("MyUserCode");
        recycler_view_mywrite.setHasFixedSize(true);
        DataInfoArrayList = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("bookwrite");
                    String category;
                    for(int i =0; i<array.length();i++){
                        JSONObject item = array.getJSONObject(i);
                        category =item.getString("category");
                        if(category.equals("도서")){
                            System.out.println("내정보"+response);
                            writeday = item.getString("writedate");
                            title = item.getString("title");
                            viewimage = item.getString("bookcoverurl");
                            bcauthor = item.getString("bcauthor");
                            bcpublisher = item.getString("bcpublisher");
                            bcprice = item.getString("bcprice");
                            bcdiscountprice = item.getString("bcdiscountprice");
                            userprice = item.getString("userprice");
                            writenumber = item.getString("writenumber");
                            life_day = item.getString("life_day");
                            life_time = item.getString("life_time");
                        }else{
                            writeday = item.getString("writedate");
                            title = item.getString("title");
                            bcauthor = null;
                            bcpublisher = null;
                            bcprice = null;
                            bcdiscountprice = null;
                            userprice = item.getString("userprice");
                            viewimage = item.getString("imagename");
                            writenumber = item.getString("writenumber");
                            life_day = item.getString("life_day");
                            life_time = item.getString("life_time");
                        }
                        DataInfoArrayList.add(new CardBookInfo(category, title, writeday, bcauthor,bcpublisher,bcprice,bcdiscountprice,userprice, viewimage, writenumber, life_day, life_time));
                        AdapterMyWriteInfoRecyclerView adaptermy = new AdapterMyWriteInfoRecyclerView(DataInfoArrayList);
                        adaptermy.setOnClickListener(ActivityMyWriteInfo.this);
                        recycler_view_mywrite.setAdapter(adaptermy);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestReadMain requestReadMain = new RequestReadMain("My", myusercode, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityMyWriteInfo.this);
        queue.add(requestReadMain);
    }
    @Override
    public void onItemClicked(int position, String category, String writenum){
        if(category.equals("도서")){
            finish();
            Intent moveActivityReadBook = new Intent(ActivityMyWriteInfo.this, ActivityReadBook.class);
            moveActivityReadBook.putExtra("DataWriteNum",writenum);
            moveActivityReadBook.putExtra("ActionBarName","작성한 글");
            ActivityMyWriteInfo.this.startActivity(moveActivityReadBook);
        }else{
            Intent moveActivityReadEtc = new Intent(ActivityMyWriteInfo.this, ActivityReadEtc.class);
            moveActivityReadEtc.putExtra("DataWriteNum",writenum);
            moveActivityReadEtc.putExtra("ActionBarName","작성한 글");
            ActivityMyWriteInfo.this.startActivity(moveActivityReadEtc);
            finish();
        }
    }
    @Override
    public void onLongItemClicked(int position, String category, String writenum){
        getWriteNumber = writenum;
        getCategory = category;
        pos = position;
        String getwritenum =writenum;
        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityMyWriteInfo.this);
        alert.setPositiveButton("삭제하기", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityMyWriteInfo.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataInfoArrayList.remove(pos);
                        AdapterMyWriteInfoRecyclerView adaptermy = new AdapterMyWriteInfoRecyclerView(DataInfoArrayList);
                        adaptermy.setOnClickListener(ActivityMyWriteInfo.this);
                        recycler_view_mywrite.setAdapter(adaptermy);
                        /*Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {*/
                                Toast.makeText(ActivityMyWriteInfo.this, "삭제 완료",Toast.LENGTH_SHORT).show();
                           /* }
                        };*/
                        RequestRemove requestRemove = new RequestRemove(getWriteNumber/*, getCategory, responseListener*/);
                        RequestQueue queue = Volley.newRequestQueue(ActivityMyWriteInfo.this);
                        queue.add(requestRemove);
                        //데이터베이스 접속 요청

                    }
                });
                alert.setTitle("글 삭제").setMessage("글을 삭제합니다.");
                alert.show();
                //Toast.makeText(ActivityMyWriteInfo.this, writenum,Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("수정하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ActivityMyWriteInfo.this, "준비 중입니다... 삭제 후 다시 글을 작성해 주세요.",Toast.LENGTH_SHORT).show();
                /*Intent moveModifyAct = new Intent(ActivityMyWriteInfo.this, ActivityWriteEtc.class);
                moveModifyAct.putExtra("WriteNum", getWriteNumber);
                ActivityMyWriteInfo.this.startActivity(moveModifyAct);*/
            }
        });
        alert.setMessage("내 글 관리하기").setTitle("삭제/수정");
        alert.show();
    }
    @Override
    public void onBackPressed(){
        Intent moveActivityMain = new Intent(ActivityMyWriteInfo.this, ActivityMain.class);
        ActivityMyWriteInfo.this.startActivity(moveActivityMain);
        finish();
        //super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent moveActivityMain = new Intent(ActivityMyWriteInfo.this, ActivityMain.class);
                ActivityMyWriteInfo.this.startActivity(moveActivityMain);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
