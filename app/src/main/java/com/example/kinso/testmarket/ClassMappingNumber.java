package com.example.kinso.testmarket;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kinso on 2019-01-29.
 */

public class ClassMappingNumber {
    private Context activity;
    final int PERMISSION =1;
    private String my_phonenum;
    public ClassMappingNumber(Context activity){
        this.activity=activity;
    }

    public String getUserCode(){
        //ActivityCompat.requestPermissions((Activity)activity, new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION);
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            System.out.println("동의한 사용자.");
            my_phonenum = tm.getLine1Number();
            if(my_phonenum!=null){
                my_phonenum =my_phonenum.replace("+82","0");
                System.out.println("전화번호 >>>>>>>>>>>>>>>>"+my_phonenum);
                return mappingnumber(my_phonenum);
            }else{
                System.out.println("전화번호를 얻어올 수 없습니다.");
            }
        }else{
            System.out.println("동의 안한 사용자.");
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity,android.Manifest.permission.READ_PHONE_NUMBERS)){
                new AlertDialog.Builder(activity).setTitle("알람").setMessage("디바이스상태 권한 설정이 필요합니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용해주세요.").setNeutralButton("설정", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        //intent.setData(Uri.parse("package"+ getPackageName()));
                        activity.startActivity(intent);
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        ((Activity) activity).finish();
                    }
                }).setCancelable(false).create().show();

            }
            return "거절";
        }
        return mappingnumber(my_phonenum);
    }
    public String mappingnumber(String phonenumber){
        String ResultMappingNumber = "";
        Map<String, String> map = new HashMap<>();
        map.put("0","3");
        map.put("1","1");
        map.put("2","b");
        map.put("3","2");
        map.put("4","5");
        map.put("5","E");
        map.put("6","9");
        map.put("7","0");
        map.put("8","G");
        map.put("9","6");
        System.out.println("1. ClassMappingNumber에서 받아온 phonenumber : >>> " + phonenumber);
        char[] array =phonenumber.toCharArray();
        int arraylength =array.length;
        char[] shufflearray = new char[arraylength];
        if(arraylength ==11){
            shufflearray[0] = array[1];
            shufflearray[1] = array[10];
            shufflearray[2] = array[5];
            shufflearray[3] = array[7];
            shufflearray[4] = array[8];
            shufflearray[5] = array[6];
            shufflearray[6] = array[9];
            shufflearray[7] = array[0];
            shufflearray[8] = array[2];
            shufflearray[9] = array[3];
            shufflearray[10] = array[4];
        }else{
            shufflearray[0] = array[1];
            shufflearray[1] = array[5];
            shufflearray[2] = array[7];
            shufflearray[3] = array[8];
            shufflearray[4] = array[6];
            shufflearray[5] = array[9];
            shufflearray[6] = array[0];
            shufflearray[7] = array[2];
            shufflearray[8] = array[3];
            shufflearray[9] = array[4];
        }
        for(char a : shufflearray){
            System.out.println(a);
        }
        for(char a :shufflearray){
            String value = String.valueOf(a);
            ResultMappingNumber+=map.get(value);
        }
        System.out.println("ClassMappingNumber에서 나온 암호 값 : " + ResultMappingNumber);
        return ResultMappingNumber;
    }
}
