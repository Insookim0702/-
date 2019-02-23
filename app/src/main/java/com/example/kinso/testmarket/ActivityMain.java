package com.example.kinso.testmarket;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.os.Process.killProcess;

/**
 * Created by kinso on 2018-12-24.
 */

public class ActivityMain extends AppCompatActivity implements AdapterMainRecyclerView.RecyclerViewClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS;
    private View mainLayout;
    private RecyclerView mRecyclerView;

    private ArrayList<CardBookInfo> DataBookInfoArrayList;
    private String writenumber,
                    writeday,
                    title,
                    bcauthor,
                    bcpublisher,
                    bcprice,
                    bcdiscountprice,
                    userprice,
                    viewimage,
                    myusercode,
                    life_day,
                    life_time;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout =findViewById(R.id.mainlay);
        mRecyclerView = findViewById(R.id.recycler_view);
        myusercode="";
        final ClassSqlite classSqlite = new ClassSqlite(ActivityMain.this);
        ClassMappingNumber classMappingNumber = new ClassMappingNumber(ActivityMain.this);
        myusercode = classSqlite.select();
        ActionBar ab = getSupportActionBar();
        ab.setTitle("인천대학교 중고마켓");
        //ab.setDisplayHomeAsUpEnabled(true); //왼쪽 버튼 사용 여부 true
        REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE/*, Manifest.permission.ACCESS_FINE_LOCATION*/};
        int phoneNumberPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[0]);
        int cameraPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[1]);
        int writeExternalStorage = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[2]);
        System.out.println(phoneNumberPermission);
        System.out.println(cameraPermission);
        System.out.println(writeExternalStorage);
        if(phoneNumberPermission == PackageManager.PERMISSION_GRANTED
                && cameraPermission == PackageManager.PERMISSION_GRANTED
                && writeExternalStorage ==PackageManager.PERMISSION_GRANTED
                && myusercode.length()<4) { //이미 허용된 사용자 일 경우
            System.out.println("이미 허용된 사용자 일 경우");
            String usercode = classMappingNumber.getUserCode();
            classSqlite.insert();
            Toast.makeText(ActivityMain.this,"회원 코드가 다운로드되었습니다."+ usercode,Toast.LENGTH_SHORT).show();
        }else{// 허용 안된 사용자
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                    ||    ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])){
                Snackbar.make(mainLayout, "이 앱을 실행하려면 전화 걸기/받기 권한과 카메라 접근 권한이 필요합니다.",Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(ActivityMain.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            }else{
                if(myusercode.length()<4){
                    ActivityCompat.requestPermissions(ActivityMain.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                }
            }
            if(myusercode.length()<4){
                ActivityCompat.requestPermissions(ActivityMain.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
        //버튼 클릭 시 사용되는 리스너를 구현
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //어떤 메뉴 아이템이 터치되는지 확인
                switch (item.getItemId()){
                    case R.id.menuitem_bottombar_up:
                        Toast.makeText(ActivityMain.this, "길게 누르면 삭제/수정 기능", Toast.LENGTH_LONG).show();
                        Intent moveActivityMyWriteInfo = new Intent(ActivityMain.this, ActivityMyWriteInfo.class);
                        moveActivityMyWriteInfo.putExtra("MyUserCode",myusercode);
                        ActivityMain.this.startActivity(moveActivityMyWriteInfo);
                        finish();
                        return true;
                    case R.id.menuitem_bottombar_down:
                        System.out.println("글쓰기 눌림");
                        //오늘 날짜, 등록 게시물
                        RequestValidateWrite validatewrite = new RequestValidateWrite(myusercode,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("오늘 내가 작성한 횟수 : " + response);
                                if(Integer.parseInt(response)>=1){  // 작성 금지
                                    ClassAlertDialog classAlertDialog = new ClassAlertDialog(ActivityMain.this);
                                    classAlertDialog.dialogmethod("제한","하루 1개 작성 가능합니다.");
                                }else{
                                    Toast.makeText(ActivityMain.this, "글쓰기",Toast.LENGTH_SHORT).show();
                                    Intent moveCategoryAct = new Intent(ActivityMain.this,ActivityCategory.class);
                                    ActivityMain.this.startActivity(moveCategoryAct);
                                }
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                        queue.add(validatewrite);
                        /*if(){

                        }*/
                        return true;
                    case R.id.menuitem_bottombar_search:
                        Toast.makeText(ActivityMain.this, "쪽지함",Toast.LENGTH_SHORT).show();
                        Intent moveMessageRoomAct = new Intent(ActivityMain.this,ActivityMessageRoom.class);
                        moveMessageRoomAct.putExtra("MyUserCode", myusercode);
                        ActivityMain.this.startActivity(moveMessageRoomAct);
                        return true;
                }
                return false;
            }
        });





        /*ActivityLocationCheck activityLocationCheck = new ActivityLocationCheck(ActivityMain.this);
        String currentLocation = activityLocationCheck.startLocationService();
        //Tx_MyLocation.setText(currentLocation);
        System.out.println("현재위치"+currentLocation);
        if(currentLocation.contains("대한민국 인천광역시 연수구 송도") && myusercode.length()==0){

            Toast.makeText(ActivityMain.this,"위치 인증에 성공하였습니다.",Toast.LENGTH_SHORT).show();
            Toast.makeText(ActivityMain.this,"회원 코드가 다운로드되었습니다."+ usercode,Toast.LENGTH_SHORT).show();
        }
        else if(myusercode!=null){
        }
        else{
            Toast.makeText(ActivityMain.this,"회원코드를 받으려면 인천대학교에서 앱을 실행시켜 주세요.",Toast.LENGTH_SHORT).show();
        }*/

        mRecyclerView.setHasFixedSize(true);
        DataBookInfoArrayList = new ArrayList<>();
        Response.Listener<String> responselistner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("mainReadRequest에 onResponse메소드 동작");
                System.out.println(response);
                try {
                    JSONObject jo = new JSONObject(response);//{bookwrite:[{},{},{}]}
                    JSONArray array = jo.getJSONArray("bookwrite"); //[{},{},{}]
                    String category;
                    JSONObject item;
                    for(int i=0;i<array.length();i++){
                        item = array.getJSONObject(i); //{}
                        category = item.getString("category");
                        if(category.equals("도서")){
                            writeday = item.getString("writedate");
                            writenumber = item.getString("writenumber");
                            title = item.getString("title");
                            viewimage = item.getString("bookcoverurl");
                            bcauthor = item.getString("bcauthor");
                            bcpublisher = item.getString("bcpublisher");
                            bcprice = item.getString("bcprice");
                            bcdiscountprice = item.getString("bcdiscountprice");
                            userprice = item.getString("userprice");
                            life_day = item.getString("life_day");
                            life_time = item.getString("life_time");
                        }else{
                            writeday = item.getString("writedate");
                            title = item.getString("title");
                            bcauthor = "0";
                            bcpublisher = "0";
                            bcprice = "0";
                            bcdiscountprice = "0";
                            userprice = item.getString("userprice");
                            viewimage = item.getString("imagename");
                            writenumber = item.getString("writenumber");
                            life_day = item.getString("life_day");
                            life_time = item.getString("life_time");
                        }
                        DataBookInfoArrayList.add(new CardBookInfo(category, title, writeday, bcauthor,bcpublisher,bcprice,bcdiscountprice,userprice, viewimage, writenumber, life_day, life_time));
                        AdapterMainRecyclerView adapterMain= new AdapterMainRecyclerView(DataBookInfoArrayList);
                        adapterMain.setOnClickListener(ActivityMain.this);  //버튼 연결
                        mRecyclerView.setAdapter(adapterMain);
                }
            }catch (JSONException e) {
                  e.printStackTrace();
            }

            }
        };
        RequestReadMain mainReadRequest = new RequestReadMain("Main",myusercode,responselistner);
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(mainReadRequest);
    }


    @Override
    public void onItemClicked(int position, String category, String writenum) {
        System.out.println("ActivityMain.java의 onItemClicked 메소드에서 parameter값으로 받은 writenum : >>>>>>>>>>>>>>>" + writenum);
        if (category.equals("도서")) {
            finish();
            Intent moveActivityReadBook = new Intent(ActivityMain.this, ActivityReadBook.class);
            moveActivityReadBook.putExtra("DataWriteNum",writenum);
            moveActivityReadBook.putExtra("ActionBarName", "판매 중...");
            ActivityMain.this.startActivity(moveActivityReadBook);
        }else{
            finish();
            Intent moveActivityReadEtc = new Intent(ActivityMain.this, ActivityReadEtc.class);
            moveActivityReadEtc.putExtra("DataWriteNum",writenum);
            moveActivityReadEtc.putExtra("ActionBarName","판매 중...");
            ActivityMain.this.startActivity(moveActivityReadEtc);
        }

    }
    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("종료하기", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
        alert.setNegativeButton("취소하기",null);
        alert.setTitle("종료").setMessage("앱을 종료하시겠습니까?");
        alert.show();

    }
    @Override //사용자가 권한 부여 결과가 반환된다.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults){
        if(requestCode ==PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length){
            //요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result =true;
            //모든 퍼미션이 체크되었는지 확인
            for(int result : grandResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    check_result =false;
                    break;
                }
            }

            if(check_result){
                //모든 퍼미션이 허용되었으면 메인액티비티 실행
                //1. 위치가 변해서 이벤트가 발생하기까지 딜레이를 건다.
                //2. 위치가
                /*final ClassSqlite classSqlite = new ClassSqlite(ActivityMain.this);
                ActivityLocationCheck activityLocationCheck = new ActivityLocationCheck(ActivityMain.this);
                String currentLocation = activityLocationCheck.startLocationService();
                myusercode = classSqlite.select();
                System.out.println("현재위치"+currentLocation);
                if(currentLocation.contains("대한민국 인천광역시 연수구 송도") && myusercode.length()<4){
                    ClassMappingNumber classMappingNumber = new ClassMappingNumber(ActivityMain.this);
                    String usercode = classMappingNumber.getUserCode();
                    classSqlite.insert(usercode);
                    Toast.makeText(ActivityMain.this,"위치 인증에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(ActivityMain.this,"회원 코드가 다운로드되었습니다."+ usercode,Toast.LENGTH_SHORT).show();
                }
                else if(myusercode!=null){
                }
                else{
                    Toast.makeText(ActivityMain.this,"회원코드를 받으려면 인천대학교에서 앱을 실행시켜 주세요.",Toast.LENGTH_SHORT).show();
                }*/
                finish();
                Intent movemain = new Intent(ActivityMain.this, ActivityMain.class);
                ActivityMain.this.startActivity(movemain);

            }else{
                //거부한 퍼미션이 있으면 앱을 사용할 수 없는 이유를 설명하고 앱 종료
                if(    ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])){
                    Snackbar.make(mainLayout, "권한 허용이 거부되었다. 앱을 다시 실행해서 퍼미션을 허용해 주세요.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            finish();
                        }
                    }).show();
                }else{
                    //"다시묻지 않음"을 사용자가 체크하고 거부를 선택할 경우에는 설정(앱 정보)에서 퍼미션을
                    //허용해야 앱을 사용할 수 있다.
                    Snackbar.make(mainLayout, "권한 허용이 거부되었다. 설정(앱 정보)에서 퍼미션을 허용해야 한다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

}