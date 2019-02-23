package com.example.kinso.testmarket;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.text.TextUtils.isEmpty;

/**
 * Created by kinso on 2019-01-29.
 */

public class ActivitySignUp extends AppCompatActivity {
    private TextView phone,sum;
    private String my_phonenum;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_signup);
        phone = (TextView) findViewById(R.id.phone);
        sum = (TextView)findViewById(R.id.sum);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION);
        //System.out.println("checkSelfPermission >>>>>>>>>>>>>>>>>"+ ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE));
        getPhoneNum();
        ClassSqlite classSqlite = new ClassSqlite(this);
        //classSqlite.insert(my_phonenum);
        //delete();
        sum.setText(classSqlite.select());
    }




    final int PERMISSION =1;
    public void getPhoneNum() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(ActivitySignUp.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            System.out.println("동의한 사용자.");
            my_phonenum = tm.getLine1Number();
            if(my_phonenum !=null){
                my_phonenum = my_phonenum.replace("+82","0");
                System.out.println(my_phonenum);
                phone.setText(my_phonenum);
            }
            return;
        }else{
            System.out.println("동의 안한 사용자.");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_PHONE_NUMBERS)){
                new AlertDialog.Builder(this).setTitle("알람").setMessage("디바이스상태 권한 설정이 필요합니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용해주세요.").setNeutralButton("설정", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package"+ getPackageName()));
                        startActivity(intent);
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        finish();
                    }
                }).setCancelable(false).create().show();

            }
        }

    }
}
