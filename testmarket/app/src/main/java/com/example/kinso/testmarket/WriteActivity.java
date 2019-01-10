package com.example.kinso.testmarket;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kinso on 2019-01-09.
 */

public class WriteActivity extends AppCompatActivity{
    private String isbn;
    private String res;
    private TextView tx;

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            //super.handleMessage(msg);
            //데이터를 받는 쪽
            Bundle bun = msg.getData();
            String receive = bun.getString("DATA");
            System.out.println("===================리시브="+receive);
            tx.setText("결과값: "+receive);
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
    super.onCreate(saveInstanceState);
    setContentView(R.layout.activity_write);
    ActionBar ab = getSupportActionBar();
    ab.setTitle("중고거래 등록하기(2/2)");
    ab.setDisplayHomeAsUpEnabled(true);

    //바코드 인식 카메라 동작 버튼
    Button barcode_btn = (Button)findViewById(R.id.barcode_btn);
    barcode_btn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            IntentIntegrator integrator = new IntentIntegrator(WriteActivity.this);
            integrator.setCaptureActivity(captureActivityVerticalOrientation.class);
            integrator.setOrientationLocked(false);//zxing을 세로화면으로 사용할 수 있게 하였다.
            integrator.initiateScan();
        }
    });

    tx = (TextView)findViewById(R.id.text);

    }//main Thread.
    class A extends Thread{
        public void run(){
            System.out.println("메인쓰레드에서  : " + isbn);
            String clientId ="ArjpAW39F6QlwzhJ_6bN";
            String clientSecret = "Z_w1lMRswa";
            try{
                System.out.println("실행실행실행실행실행실행실행실행실행실행");
                //String text1 = URLEncoder.encode(isbn, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/book.json?query="+isbn+"&display=1&start=1"; // json 결과
                System.out.println("============================" + apiURL);
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200){//서버에서 정상 호출
                    System.out.println("서버에서 정상 호출");
                    //InputStreamReader : 데이터를 읽어온다.
                    //네트워크 특성상 가져오는 데이터의 양이 정해져 있지 않기 때문에 Thread를 별도로 돌려야 한다.
                    //그렇지 않으면 데이터를 가져오는 동안 프로그램이 멈췅 있는다.
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }else{
                    br= new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = br.readLine()) !=null){
                    response.append(inputLine);
                }
                br.close();
                res = response.toString();
                System.out.println("=========성공========" + res);
                //데이터를 보내는 쪽
                Message msg = handler.obtainMessage();
                Bundle bun = new Bundle();
                bun.putString("DATA", res);
                msg.setData(bun);
                handler.sendMessage(msg); //메인 스레드 신호 전달
                System.out.println("전달완료 : " + msg);
            }catch (Exception e){
                System.out.println("실패실패실패실패실패실패실패실패실패실패실패");
                System.out.println(e);
            }
        }
    }
    //바코드 스캔한 인식 값 받아오는 코드
    @Override
    //public void run(){}
    protected void onActivityResult(int requestCode, int resultCode, Intent date){
        super.onActivityResult(requestCode, resultCode, date);
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,date);
            isbn = result.getContents();
            //Toast.makeText(this,"출력 ISBN: " + result.getContents(), Toast.LENGTH_SHORT).show();
            //Log.i("TAG", ">>> result.getContents() :" + result.getContents());
            A th1 = new A();
            th1.start();
            /*try{
                th1.join();
            }catch (InterruptedException e){}*/
            //tx.setText(isbn);
            Intent write = new Intent(WriteActivity.this, WriteActivity.class);
            write.putExtra("ISBN", isbn);
            setResult(RESULT_OK,write);
            //finish();
            //WriteActivity.this.startActivity(write);
        }
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
