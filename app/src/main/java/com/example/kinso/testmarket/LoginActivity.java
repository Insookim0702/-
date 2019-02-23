package com.example.kinso.testmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by kinso on 2018-12-26.
 */

public class LoginActivity extends AppCompatActivity {
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar ab = getSupportActionBar();
        ab.hide();

        TextView registerBtn = (TextView)findViewById(R.id.registerBtn);

        //버튼 눌르면 RegisterActivity로 가게 한다.
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent MoveRegister = new Intent(LoginActivity.this, ActivityRegister.class);
                LoginActivity.this.startActivity(MoveRegister);
            }
        });

        final EditText stdnum_id =(EditText)findViewById(R.id.id);
        final EditText passwordText = (EditText)findViewById(R.id.login_password);
        final Button loginButton =(Button)findViewById(R.id.LoginButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String stdnum = stdnum_id.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            System.out.println("====================================");
                            Log.i("tagconvertstr",response);
                            System.out.println("====================================");
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                /*AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인에 성공했습니다.").setTitle("로그인").setPositiveButton("OK",null).create();
                                dialog.show();*/
                                Intent MainIntent = new Intent(LoginActivity.this, ActivityMain.class);
                                LoginActivity.this.startActivity(MainIntent);
                                finish();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("계정을 다시 확인하세요.").setTitle("로그인 거부").setNegativeButton("relogin", null).create();
                                dialog.show();

                                /*Intent intent = new Intent(LoginActivity.this, ActivityMain.class);
                                LoginActivity.this.startActivity(intent);
                                finish();*/
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RequestLogin loginRequest = new RequestLogin(stdnum, userPassword, responseListner);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {//다이얼로그가 켜져있을 때 함부로 종료가 되지 않게 함
            dialog.dismiss();
            dialog = null;

        }
    }
}
