/*
package com.example.kinso.testmarket;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

*/
/**
 * Created by kinso on 2019-02-10.
 *//*


public class ActivityStarter extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS;
    private View mainLayout;
    private String myusercode;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_starter);
        mainLayout = findViewById(R.id.main_Layout);
        REQUIRED_PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
        int phoneNumberPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int LocationPermssion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(phoneNumberPermission ==PackageManager.PERMISSION_GRANTED && LocationPermssion ==PackageManager.PERMISSION_GRANTED){ //이미 허용된 사용자 일 경우
            //ActivityLocationCheck activityLocationCheck = new ActivityLocationCheck(ActivityStarter.this);
            //String currentLocation = activityLocationCheck.startLocationService();
            Intent movemain = new Intent(ActivityStarter.this, ActivityMain.class);
            ActivityStarter.this.startActivity(movemain);
            finish();
        }else{// 허용 안된 사용자
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])){
                    Snackbar.make(mainLayout, "이 앱을 실행하려면 위치권한과 전화 걸기/받기 권한이 필요합니다.",Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(ActivityStarter.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            }else{
                ActivityCompat.requestPermissions(ActivityStarter.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        ActivityCompat.requestPermissions(ActivityStarter.this, REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);

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
                final ClassSqlite classSqlite = new ClassSqlite(ActivityStarter.this);
                ActivityLocationCheck activityLocationCheck = new ActivityLocationCheck(ActivityStarter.this);
                String currentLocation = activityLocationCheck.startLocationService();
                myusercode = classSqlite.select();
                System.out.println("현재위치"+currentLocation);
                if(currentLocation.contains("대한민국 인천광역시 연수구 송도") && myusercode.length()<4){
                    ClassMappingNumber classMappingNumber = new ClassMappingNumber(ActivityStarter.this);
                    String usercode = classMappingNumber.getUserCode();
                    classSqlite.insert();
                    Toast.makeText(ActivityStarter.this,"위치 인증에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(ActivityStarter.this,"회원 코드가 다운로드되었습니다."+ usercode,Toast.LENGTH_SHORT).show();
                }
                else if(myusercode!=null){
                }
                else{
                    Toast.makeText(ActivityStarter.this,"회원코드를 받으려면 인천대학교에서 앱을 실행시켜 주세요.",Toast.LENGTH_SHORT).show();
                }
                Intent movemain = new Intent(ActivityStarter.this, ActivityMain.class);
                ActivityStarter.this.startActivity(movemain);
                finish();
            }else{
                //거부한 퍼미션이 있으면 앱을 사용할 수 없는 이유를 설명하고 앱 종료
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])){
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
*/
