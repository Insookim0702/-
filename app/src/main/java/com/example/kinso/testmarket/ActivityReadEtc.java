package com.example.kinso.testmarket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by kinso on 2019-01-24.
 */

public class ActivityReadEtc extends AppCompatActivity {
    private String gettitle, writeusercode;
    private TextView Tx_writenumber, Tx_date, Tx_usercode, Tx_category, Tx_title, Tx_userprice, Tx_status, Tx_description;
    private ImageView Iv_image1, Iv_image2, Iv_image3;
    private Button fix, SendMessage;
    private LinearLayout Layout_DelOrModi;
    private ClassSqlite classSqlite;
    private String myusercode, DataWriteNumber;
    private String actionbarname;
    private JSONObject jsonob;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_read_etc);
        Tx_writenumber = (TextView) findViewById(R.id.Tx_writenum);
        Tx_date = (TextView) findViewById(R.id.Tx_date);
        Tx_usercode = (TextView) findViewById(R.id.Tx_userid);
        Tx_category = (TextView) findViewById(R.id.Tx_category);
        Tx_title = (TextView) findViewById(R.id.Tx_title);
        Tx_userprice = (TextView) findViewById(R.id.Tx_price);
        Tx_status = (TextView) findViewById(R.id.Tx_status);
        Tx_description = (TextView) findViewById(R.id.Tx_description);
        Iv_image1 = (ImageView) findViewById(R.id.Iv_image1);
        Iv_image2 = (ImageView) findViewById(R.id.Iv_image2);
        Iv_image3 = (ImageView) findViewById(R.id.Iv_image3);
        fix = (Button) findViewById(R.id.fix);
        SendMessage = (Button) findViewById(R.id.SendMessage);
        Layout_DelOrModi = findViewById(R.id.Layout_DelOrModi);
        classSqlite = new ClassSqlite(ActivityReadEtc.this);
        myusercode = classSqlite.select();


        Intent getIntent = getIntent();
        ActionBar ab = getSupportActionBar();
        actionbarname = getIntent.getExtras().getString("ActionBarName");
        ab.setTitle(getIntent.getExtras().getString("ActionBarName"));

        mHandler = new Handler();

        if (!actionbarname.equals("등록 완료 (3/3)")) {
            fix.setVisibility(View.GONE);
            if (actionbarname.equals("작성한 글")) {
                Layout_DelOrModi.setVisibility(View.VISIBLE);
                Button Btn_Delete = findViewById(R.id.Btn_Delete);
                Button Btn_Modify = findViewById(R.id.Btn_Modify);
                //삭제하기 버튼
                Btn_Delete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityReadEtc.this);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RequestRemove requestRemove = new RequestRemove(DataWriteNumber);
                                RequestQueue queue = Volley.newRequestQueue(ActivityReadEtc.this);
                                queue.add(requestRemove);
                                //데이터베이스 접속 요청
                                Toast.makeText(ActivityReadEtc.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                Intent moveActivityMyWriteInfo = new Intent(ActivityReadEtc.this, ActivityMyWriteInfo.class);
                                moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
                                ActivityReadEtc.this.startActivity(moveActivityMyWriteInfo);
                                finish();
                            }
                        });
                        alert.setTitle("글 삭제").setMessage("글을 삭제합니다.");
                        alert.show();

                    }
                });
                //수정하기 버튼
                Btn_Modify.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(ActivityReadEtc.this, "준비 중입니다...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = ProgressDialog.show(ActivityReadEtc.this, "데이터 모셔오는 중...", "잠시만 기다려 주세요...", true);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Button Btn_Delete = findViewById(R.id.Btn_Delete);
                                Button Btn_Modify = findViewById(R.id.Btn_Modify);
                                try {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    if (!writeusercode.equals(myusercode)) {
                                        SendMessage.setVisibility(View.VISIBLE);
                                        SendMessage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent moveActivitysendMessage = new Intent(ActivityReadEtc.this, ActivitySendMessage.class);
                                                moveActivitysendMessage.putExtra("Title", gettitle);
                                                moveActivitysendMessage.putExtra("WriteNum", DataWriteNumber);
                                                moveActivitysendMessage.putExtra("Receiver", writeusercode);
                                                moveActivitysendMessage.putExtra("MyUserCode", myusercode);
                                                ActivityReadEtc.this.startActivity(moveActivitysendMessage);
                                            }
                                        });
                                    }else{
                                        Layout_DelOrModi.setVisibility(View.VISIBLE);
                                        //삭제하기 버튼
                                        Btn_Delete.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityReadEtc.this);
                                                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        RequestRemove requestRemove = new RequestRemove(DataWriteNumber);
                                                        RequestQueue queue = Volley.newRequestQueue(ActivityReadEtc.this);
                                                        queue.add(requestRemove);
                                                        //데이터베이스 접속 요청
                                                        Toast.makeText(ActivityReadEtc.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                                        Intent moveActivityMyWriteInfo = new Intent(ActivityReadEtc.this, ActivityMyWriteInfo.class);
                                                        moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
                                                        ActivityReadEtc.this.startActivity(moveActivityMyWriteInfo);
                                                        finish();
                                                    }
                                                });
                                                alert.setTitle("글 삭제").setMessage("글을 삭제합니다.");
                                                alert.show();

                                            }
                                        });
                                        //수정하기 버튼
                                        Btn_Modify.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                Toast.makeText(ActivityReadEtc.this, "준비 중입니다...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);
                    }
                });
            }

        }


        ab.setDisplayHomeAsUpEnabled(true);
        DataWriteNumber = getIntent.getExtras().getString("DataWriteNum");
        System.out.println("EtcReadActivity에서 받은 DataWriteNum의 값은 : " + DataWriteNumber);
        Response.Listener<String> responselistener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonob = new JSONObject(response);
                    System.out.println("response>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response);
                    jsonob = new JSONObject(jsonob.getString("bookwrite").replace("[", "").replace("]", ""));
                    String category = jsonob.getString("category");
                    Tx_category.setText("분류 : " + category);
                    Tx_writenumber.setText("글 등록 번호 : " + jsonob.getString("writenumber"));
                    gettitle = jsonob.getString("bcbooktitle");
                    Tx_title.setText(gettitle);
                    writeusercode = jsonob.getString("usercode");
                    Tx_usercode.setText("등록 유저 : " + writeusercode);
                    Tx_date.setText("글 쓴 날짜 : " + jsonob.getString("writeday"));
                    DecimalFormat df = new DecimalFormat("#,##0");
                    Tx_userprice.setText(df.format(Integer.parseInt(jsonob.getString("userprice"))));
                    Tx_description.setText("설명 : " + jsonob.getString("description"));
                    Tx_status.setText("물건 상태 : " + jsonob.getString("bookstatus"));
                    String imagename1 = jsonob.getString("imagename1");
                    String imagename2 = jsonob.getString("imagename2");
                    String imagename3 = jsonob.getString("imagename3");
                    if (!imagename1.equals("null")) {
                        Iv_image1.setVisibility(View.VISIBLE);
                        AsyncReadImage imageReadAsync = new AsyncReadImage(imagename1, Iv_image1);
                        imageReadAsync.execute();
                    }
                    if (!imagename2.equals("null")) {
                        Iv_image2.setVisibility(View.VISIBLE);
                        AsyncReadImage imageReadAsync = new AsyncReadImage(imagename2, Iv_image2);
                        imageReadAsync.execute();
                    }
                    if (!imagename3.equals("null")) {
                        Iv_image3.setVisibility(View.VISIBLE);
                        AsyncReadImage imageReadAsync = new AsyncReadImage(imagename3, Iv_image3);
                        imageReadAsync.execute();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestReadBook brr = new RequestReadBook(DataWriteNumber, responselistener);
        RequestQueue queue = Volley.newRequestQueue(ActivityReadEtc.this);
        queue.add(brr);
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent moveMainIntent = new Intent(ActivityReadEtc.this, ActivityMain.class);
                ActivityReadEtc.this.startActivity(moveMainIntent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (actionbarname.equals("판매 중...") || actionbarname.equals("등록 완료 (3/3)")) {
            finish();
            Intent moveActivityMyWriteInfo = new Intent(ActivityReadEtc.this, ActivityMain.class);
            ActivityReadEtc.this.startActivity(moveActivityMyWriteInfo);
        }
        if (actionbarname.equals("작성한 글")) {
            finish();
            Intent moveActivityMyWriteInfo = new Intent(ActivityReadEtc.this, ActivityMyWriteInfo.class);
            moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
            ActivityReadEtc.this.startActivity(moveActivityMyWriteInfo);
        }
    }


    //액션바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (actionbarname.equals("판매 중...") || actionbarname.equals("등록 완료 (3/3)")) {
                    finish();
                    Intent moveActivityMyWriteInfo = new Intent(ActivityReadEtc.this, ActivityMain.class);
                    ActivityReadEtc.this.startActivity(moveActivityMyWriteInfo);
                }
                if (actionbarname.equals("작성한 글")) {
                    finish();
                    Intent moveActivityMyWriteInfo = new Intent(ActivityReadEtc.this, ActivityMyWriteInfo.class);
                    moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
                    ActivityReadEtc.this.startActivity(moveActivityMyWriteInfo);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
