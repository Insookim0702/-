package com.example.kinso.testmarket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
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

public class ActivityReadBook extends AppCompatActivity {
    private String getbooktitle, writeusercode;
    private TextView Tx_category, Tx_writenumber, Tx_usercode, Tx_writeday, Tx_bcbooktitle, Tx_bcauthor, Tx_bcpublisher, Tx_bcprice, Tx_bcdiscountprice, Tx_userprice, Tx_subject, Tx_description;
    private TextView Tx_bookstautsline, Tx_bookstatusnote, Tx_bookstatuscover, Tx_bookstatuswritename, Tx_bookstatuspage;
    private ImageView Iv_bcbookcoverimageurl;
    private ImageView Iv_image1, Iv_image2, Iv_image3;
    private JSONObject jsonob;
    private Button fix, SendMessage;
    private ClassSqlite classSqlite;
    private LinearLayout Layout_DelOrModi;
    private String myusercode, DataWriteNumber;
    private String actionbarname;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_read_book);
        Tx_category = (TextView) findViewById(R.id.Tx_category);
        Tx_bcauthor = (TextView) findViewById(R.id.Tx_bcauthor);
        Tx_writenumber = (TextView) findViewById(R.id.Tx_writenumber);
        Tx_usercode = (TextView) findViewById(R.id.Tx_usercode);
        Tx_writeday = (TextView) findViewById(R.id.Tx_writeday);
        Tx_bcbooktitle = (TextView) findViewById(R.id.Tx_bcbooktitle);
        Tx_bcauthor = (TextView) findViewById(R.id.Tx_bcauthor);
        Tx_bcpublisher = (TextView) findViewById(R.id.Tx_bcpublisher);
        Tx_bcprice = (TextView) findViewById(R.id.Tx_bcprice);
        Tx_bcdiscountprice = (TextView) findViewById(R.id.Tx_bcdiscountprice);
        Tx_userprice = (TextView) findViewById(R.id.Tx_userprice);
        Tx_subject = (TextView) findViewById(R.id.Tx_subject);
        Tx_description = (TextView) findViewById(R.id.Tx_description);
        Iv_bcbookcoverimageurl = (ImageView) findViewById(R.id.Iv_bcbookcoverimageurl);
        Tx_bookstautsline = (TextView) findViewById(R.id.Tx_bookstautsline);
        Tx_bookstatusnote = (TextView) findViewById(R.id.Tx_bookstatusnote);
        Tx_bookstatuscover = (TextView) findViewById(R.id.Tx_bookstatuscover);
        Tx_bookstatuswritename = (TextView) findViewById(R.id.Tx_bookstatuswritename);
        Tx_bookstatuspage = (TextView) findViewById(R.id.Tx_bookstatuspage);
        Iv_image1 = (ImageView) findViewById(R.id.Iv_image1);
        Iv_image2 = (ImageView) findViewById(R.id.Iv_image2);
        Iv_image3 = (ImageView) findViewById(R.id.Iv_image3);
        fix = (Button) findViewById(R.id.fix);
        SendMessage = (Button) findViewById(R.id.SendMessage);
        Layout_DelOrModi = findViewById(R.id.Layout_DelOrModi);
        classSqlite = new ClassSqlite(ActivityReadBook.this);
        myusercode = classSqlite.select();


        Intent getDataWriteNum = getIntent();
        DataWriteNumber = getDataWriteNum.getExtras().getString("DataWriteNum");

        ActionBar ab = getSupportActionBar();
        actionbarname = getDataWriteNum.getExtras().getString("ActionBarName");
        ab.setTitle(actionbarname);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityReadBook.this);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RequestRemove requestRemove = new RequestRemove(DataWriteNumber);
                                RequestQueue queue = Volley.newRequestQueue(ActivityReadBook.this);
                                queue.add(requestRemove);
                                //데이터베이스 접속 요청
                                Toast.makeText(ActivityReadBook.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                Intent moveActivityMyWriteInfo = new Intent(ActivityReadBook.this, ActivityMyWriteInfo.class);
                                moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
                                ActivityReadBook.this.startActivity(moveActivityMyWriteInfo);
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
                        Toast.makeText(ActivityReadBook.this, "준비 중입니다...", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = ProgressDialog.show(ActivityReadBook.this, "데이터 모셔오는 중...", "잠시만 기다려 주세요.", true);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    if (!writeusercode.equals(myusercode)) {
                                        SendMessage.setVisibility(View.VISIBLE);
                                        SendMessage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AsyncAlarm asyncAlarm = new AsyncAlarm(ActivityReadBook.this);
                                                asyncAlarm.execute();
                                                Intent moveActivitysendMessage = new Intent(ActivityReadBook.this, ActivitySendMessage.class);
                                                moveActivitysendMessage.putExtra("Title", getbooktitle);
                                                moveActivitysendMessage.putExtra("WriteNum", DataWriteNumber);
                                                moveActivitysendMessage.putExtra("Receiver", writeusercode);
                                                classSqlite = new ClassSqlite(ActivityReadBook.this);
                                                moveActivitysendMessage.putExtra("MyUserCode", myusercode);
                                                ActivityReadBook.this.startActivity(moveActivitysendMessage);
                                            }
                                        });
                                    }else{
                                        Layout_DelOrModi.setVisibility(View.VISIBLE);
                                        Button Btn_Delete = findViewById(R.id.Btn_Delete);
                                        Button Btn_Modify = findViewById(R.id.Btn_Modify);
                                        //삭제하기 버튼
                                        Btn_Delete.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityReadBook.this);
                                                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        RequestRemove requestRemove = new RequestRemove(DataWriteNumber);
                                                        RequestQueue queue = Volley.newRequestQueue(ActivityReadBook.this);
                                                        queue.add(requestRemove);
                                                        //데이터베이스 접속 요청
                                                        Toast.makeText(ActivityReadBook.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                                        Intent moveActivityMyWriteInfo = new Intent(ActivityReadBook.this, ActivityMyWriteInfo.class);
                                                        moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
                                                        ActivityReadBook.this.startActivity(moveActivityMyWriteInfo);
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
                                                Toast.makeText(ActivityReadBook.this, "준비 중입니다...", Toast.LENGTH_SHORT).show();
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
        System.out.println("BookReadActivity.java로 넘어온 DataWriteNumber>>>>>>>>>>>>>>" + DataWriteNumber);
        Response.Listener<String> responselistener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonob = new JSONObject(response);
                    System.out.println("response>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response);
                    jsonob = new JSONObject(jsonob.getString("bookwrite").replace("[", "").replace("]", ""));
                    String category = jsonob.getString("category");
                    Tx_category.setText(category);
                    Tx_writenumber.setText("글 등록 번호 : " + jsonob.getString("writenumber"));
                    writeusercode = jsonob.getString("usercode");
                    Tx_usercode.setText("등록 유저 : " + writeusercode);
                    Tx_writeday.setText("글 쓴 날짜 : " + jsonob.getString("writeday"));
                    getbooktitle = jsonob.getString("bcbooktitle");
                    Tx_bcbooktitle.setText(getbooktitle);
                    Tx_bcauthor.setText("책 저자 : " + jsonob.getString("bcauthor"));
                    Tx_bcpublisher.setText("출판사 : " + jsonob.getString("bcPublisher"));
                    DecimalFormat df = new DecimalFormat("#,##0");
                    Tx_userprice.setText(df.format(Integer.parseInt(jsonob.getString("userprice"))));
                    Tx_bcprice.setText("(정상가 : " + jsonob.getString("bcprice") + ")");
                    Tx_bcdiscountprice.setText("(할인가 : " + jsonob.getString("bcdiscountprice") + ")");
                    Tx_subject.setText("수업 : " + jsonob.getString("subject"));
                    Tx_description.setText(jsonob.getString("description"));
                    JSONObject js = new JSONObject(jsonob.getString("bookstatus"));
                    Tx_bookstautsline.setText("밑줄 여부 : " + js.getString("line"));
                    Tx_bookstatusnote.setText("필기 여부 : " + js.getString("note"));
                    Tx_bookstatuscover.setText("표지 훼손 :" + js.getString("cover"));
                    Tx_bookstatuswritename.setText("이름/학번 기입 : " + js.getString("writename"));
                    Tx_bookstatuspage.setText("페이지 훼손 : " + js.getString("page"));
                    AsyncReadBookcover bookReadcoverAsync = new AsyncReadBookcover(jsonob.getString("bcbookcoverimageurl"), Iv_bcbookcoverimageurl);
                    bookReadcoverAsync.execute();
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
        RequestQueue queue = Volley.newRequestQueue(ActivityReadBook.this);
        queue.add(brr);
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent moveMainIntent = new Intent(ActivityReadBook.this, ActivityMain.class);
                ActivityReadBook.this.startActivity(moveMainIntent);

            }
        });
    }//메인 스레드

    @Override
    public void onBackPressed() {
        if (actionbarname.equals("판매 중...") || actionbarname.equals("등록 완료 (3/3)")) {
            finish();
            Intent moveActivityMyWriteInfo = new Intent(ActivityReadBook.this, ActivityMain.class);
            ActivityReadBook.this.startActivity(moveActivityMyWriteInfo);
        }
        if (actionbarname.equals("작성한 글")) {
            finish();
            Intent moveActivityMyWriteInfo = new Intent(ActivityReadBook.this, ActivityMyWriteInfo.class);
            moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
            ActivityReadBook.this.startActivity(moveActivityMyWriteInfo);
        }
    }

    //액션바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (actionbarname.equals("판매 중...") || actionbarname.equals("등록 완료 (3/3)")) {
                    finish();
                    Intent moveActivityMyWriteInfo = new Intent(ActivityReadBook.this, ActivityMain.class);
                    ActivityReadBook.this.startActivity(moveActivityMyWriteInfo);
                }
                if (actionbarname.equals("작성한 글")) {
                    finish();
                    Intent moveActivityMyWriteInfo = new Intent(ActivityReadBook.this, ActivityMyWriteInfo.class);
                    moveActivityMyWriteInfo.putExtra("MyUserCode", myusercode);
                    ActivityReadBook.this.startActivity(moveActivityMyWriteInfo);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
