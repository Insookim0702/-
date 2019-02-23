package com.example.kinso.testmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * Created by kinso on 2019-02-18.
 */

public class ActivitySplash extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);
        startLoading();

    }
    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent splashintent = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(splashintent);
            }
        },1000);
    }
}
