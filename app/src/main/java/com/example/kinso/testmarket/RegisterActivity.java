package com.example.kinso.testmarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by kinso on 2018-12-27.
 */

public class RegisterActivity extends AppCompatActivity{
    private ArrayAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //스피너 객체 선언 및 리소스를 가져오는 부분
        spinner = (Spinner)findViewById(R.id.major);
        adapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

    }

    //스피너에 데이터를 연결하는 작업

}
