package com.example.kinso.testmarket;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by kinso on 2019-02-05.
 */

public class ActivityLocationCheck{
    private final int PERMISSION =1;
    private LocationListener locationListener;
    private Context Activity;
    private Double Lat, Lng;
    private String RealLocation;
    public ActivityLocationCheck(Context Activity){
        this.Activity = Activity;
    }
    public String startLocationService(){
        String nowAddress ="현재 위치 알 수 없음.";
        ActivityCompat.requestPermissions((android.app.Activity) Activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION);
        if(ActivityCompat.checkSelfPermission(Activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            final LocationManager locationManager = (LocationManager)Activity.getSystemService(Context.LOCATION_SERVICE);
            //String provider = LocationManager.GPS_PROVIDER;
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, true);
            int minTime = 5000;//ms
            int minDistance = 0; //metre

            locationListener = new LocationListener(){
                @Override
                public void onLocationChanged(Location loc){
                    if(loc !=null){
                        //Toast에 의해서 화면 하단에 위도, 경도가 출력됨
                        Lat = loc.getLatitude();
                        Lng = loc.getLongitude();
                        System.out.println("Location changed >>> Lat :" +Lat +"Lng : " + Lng);
                        RealLocation = getAddress(Activity,Lat,Lng);
                        System.out.println("실제 주소 : " + RealLocation);
                        locationManager.removeUpdates(locationListener);
                    }else{
                        Toast.makeText(Activity, "Location changed >>> Lat : null Lng : null", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    //TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProviderDisabled(String s) {
                    // TODO Auto-generated method stub
                }
            };
            locationManager.requestLocationUpdates(provider, minTime,minDistance, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime,minDistance, locationListener);//1000은 1초마다, 1은 1미터마다 해당 값을 갱신한다는 뜻으로, 딜레이마다 호출하기도 하지만
            }else{
            System.out.println("동의 안한 사용자.");
            if(ActivityCompat.shouldShowRequestPermissionRationale((android.app.Activity) Activity,android.Manifest.permission.READ_PHONE_NUMBERS)){
                new AlertDialog.Builder(Activity).setTitle("알람").setMessage("디바이스상태 권한 설정이 필요합니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용해주세요.").setNeutralButton("설정", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        //intent.setData(Uri.parse("package"+ getPackageName()));
                        Activity.startActivity(intent);
                    }
                }).setPositiveButton("확인", null);/*new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i){

                    }
                }).setCancelable(false).create().show();*/
            }
        }
        return nowAddress;
    }

    public static String getAddress(Context mContext, double lat, double lng){
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        System.out.println("geocoder : " + geocoder);
        try{
           if(geocoder!=null){
               System.out.println("geocoder는 null이 아니다.");
               //세번쨰 파라미터는 좌표에 대해 주소를 리턴 받는 개수로
               //한좌표에 대해 두개이상의 이름이 존재할 수 있어서 주소배열을 리턴 받기 위해 최대
               //갯수 설정
               System.out.println(">>>경도 : " + lat);
               System.out.println(">>>>위도 : " + lng);

               address =geocoder.getFromLocation(lat,lng,1);
               System.out.println("주소 : " + address);
               if(address != null && address.size() > 0){
                   System.out.println("address는 null이 아니다. ");
                   //주소받아오기
                   String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                   nowAddress = currentLocationAddress;
               }
           }
        }catch (IOException e){
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return nowAddress;
    }
}
