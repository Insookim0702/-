import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.kinso.testmarket.R;

/**
 * Created by kinso on 2019-02-10.
 */

public class Activitytest extends AppCompatActivity {
    protected void onCreate(Bundle saveInstancestate) {
        super.onCreate(saveInstancestate);
        setContentView(R.layout.activity_test);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;
        int minTime = 5000;//ms
        int minDistance = 0; //metre

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                if (loc != null) {
                    //Toast에 의해서 화면 하단에 위도, 경도가 출력됨
                    Toast.makeText(Activitytest.this, "Location changed >>> Lat :" + loc.getLatitude() + "Lng : " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activitytest.this, "Location changed >>> Lat : null Lng : null", Toast.LENGTH_SHORT).show();

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
    }

}
