package com.example.kinso.testmarket;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by kinso on 2019-01-09.
 */

public class WriteActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle saveInstanceState){
    super.onCreate(saveInstanceState);
    setContentView(R.layout.activity_write);
    ActionBar ab = getSupportActionBar();
    ab.setTitle("중고거래 등록하기(2/2)");
    ab.setDisplayHomeAsUpEnabled(true);

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

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent date){
        super.onActivityResult(requestCode, resultCode, date);
        Log.e("TAG", ">>> requestCode = "+requestCode+", resultCode = "+ resultCode);
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,date);
            Toast.makeText(this,"출력 ISBN: " + result.getContents(), Toast.LENGTH_SHORT).show();
            Log.i("TAG", ">>> result.getContents() :" + result.getContents());
            Log.i("TAG", ">>> result.getFormatName() : " + result.getFormatName());
            Intent write = new Intent(WriteActivity.this, WriteActivity.class);
            WriteActivity.this.startActivity(write);

        }

    }
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
