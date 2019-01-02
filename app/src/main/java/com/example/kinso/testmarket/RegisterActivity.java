package com.example.kinso.testmarket;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kinso on 2018-12-27.
 */

public class RegisterActivity extends AppCompatActivity{
    private ArrayAdapter adapter;
    private Spinner spinner;
    private String stdnum;
    private String userName;
    private String userGender;
    private String userphone;
    private String userPassword;
    private String userMajor;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //전공 과 - 스피너 객체 선언 및 리소스를 가져오는 부분
        spinner = (Spinner)findViewById(R.id.major);
        adapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText userName = (EditText)findViewById(R.id.name);

        RadioGroup genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        //public int getCheckedRadioButtonId() : 선택된 라디오버튼의 ID값을 반환한다.
        int userGenderID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton)findViewById(userGenderID)).getText().toString();//초기화 값을 지정해준다.
        //라디오 버튼이 눌리면 값을 바꿔주는 부분
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                userGender = ((RadioButton)findViewById(i)).getText().toString();
            }
        });

        final EditText phone = (EditText)findViewById(R.id.phone);
        Spinner userMajor = (Spinner) findViewById(R.id.major);
        final EditText pw = (EditText)findViewById(R.id.pw);
        final EditText ch_pww = (EditText)findViewById(R.id.ch_pw);

        //회원가입시 아이디가 사용가능한지 검증하는 부분
        final Button validateButton = (Button)findViewById(R.id.validateBtn);
        validateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String stdnum = idText.getText().toString();
                // 1) 검증이 된 경우.
                if(validate){
                    return; //검증완료
                }
                // 정규표현식 숫자 8자리.
                String p = "^[0-9]{9}$"; //^ : 문자열의 시작, $ : 문자열의 종료
                boolean m = Pattern.matches(p,stdnum);
                // 2) ID값을 입력안했으면
                if(stdnum.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setTitle("학번 인증").setMessage("ID is empty").setCancelable(false)
                            .setPositiveButton("OK", null).create();
                    dialog.show();
                    return;
                }
                // 3) 학번 패턴 검증 : 8자리 and 숫자만 and 앞의 4자리 2011~2019까지
                else if(m==false || 2011 > Integer.parseInt(stdnum.substring(0,4)) || 2019 < Integer.parseInt(stdnum.substring(0,4))){
                    System.out.println("학번 유효성 검사 실패.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setTitle("학번 유효 검사").setMessage("input the your Student Number").setCancelable(false)
                            .setNegativeButton("OK", null).create();
                    dialog.show();
                }
                // 4) 검증시작
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                                System.out.println("====================================");
                                Log.i("tagconvertstr", response);
                                System.out.println("====================================");
                                JSONObject jsonResponse = new JSONObject(response); // 중괄호에 들어갈 속성 정의
                                boolean success = jsonResponse.getBoolean("success");
                                System.out.print(success);
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                if (success) {//사용할 수 있는 아이디면
                                    dialog = builder.setTitle("학번 인증").setMessage("you can use Student Number").setCancelable(false)
                                            .setPositiveButton("OK", null).create();
                                    dialog.show();
                                    idText.setEnabled(false);//아이디값을 바꿀 수 없도록 한다.
                                    validate = true; //검증완료
                                    idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                } else { //사용할 수 없는 아이디라면
                                    dialog = builder.setTitle("학번 인증").setMessage("already used Student Number ㅗ").setCancelable(false)
                                            .setNegativeButton("OK", null).create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };//Response.Listener 완료
                    //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                    ValidateRequest validateRequest = new ValidateRequest(stdnum, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(validateRequest);
                }
            }
        });

        //회원가입 버튼 눌렀을 때
        Button registerButton = (Button)findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stdnum = idText.getText().toString();
                String userPassword = ch_pww.getText().toString();
                String userMajor = spinner.getSelectedItem().toString();
                String userphone =  phone.getText().toString();
                String name = userName.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);


                // 정규표현식 사용자 이름 한글 검증.
                String HanglePattern = "^[가-힣]*$"; //^ : 문자열의 시작, $ : 문자열의 종료
                boolean HangleMatch = Pattern.matches(HanglePattern,name);

                // 정규표현식 전화번호 숫자 검증.
                String PhonePattern = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$"; //^ : 문자열의 시작, $ : 문자열의 종료
                boolean PhoneMatch = Pattern.matches(PhonePattern,userphone);

                // 1) 한칸이라도 입력 안된 경우
                if(stdnum.equals("")|| userPassword.equals("")||userMajor.equals("")
                        ||userphone.equals("")||name.equals("")||userGender.equals("")){
                    System.out.println("기입 안한 부분이 있음.");
                    dialog = builder.setTitle("").setMessage("Write it all")
                            .setCancelable(false).setNegativeButton("OK", null).create();
                    dialog.show();
                }
                // 2) CHECK 버튼 안누를 경우. (validate가 'false'일 경우..)
                else if(validate == false){
                    System.out.println("중복 체크가 안 됐다.");
                    dialog = builder.setMessage("Click the \'Click\' Button").setTitle("학번 유효 검사").setNegativeButton("OK", null).create();
                    dialog.show();
                }
                // 3) name에 한글 입력이 아닌 경우.
                else if(HangleMatch==false || name.length()<2 || name.length()>=6){
                    System.out.println("이름 부정 입력.");
                    dialog = builder.setMessage("Exactlly full the your name!").setTitle("회원가입 오류.").setNegativeButton("OK", null).create();
                    dialog.show();
                }
                // 4) 전화번호 검사.
                else if(!PhoneMatch){
                    System.out.println("전화번호 부정 입력.");
                    dialog = builder.setMessage("Exactlly full the your phone number!").setTitle("회원가입 오류.").setNegativeButton("OK", null).create();
                    dialog.show();
                }
                else {
                    //회원가입 시작
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                //Log.i("tagconvertstr", response);  -> php에서 반환되는 log를 보여준다.
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                System.out.println(success);
                                if (success) {//사용할 수 있는 아이디라면..
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog = builder.setTitle("알림").setMessage("회원가입을 축하합니다.").setPositiveButton("OK", null).create();
                                    dialog.show();
                                    finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                                } else {//사용 불가능한 아이디일 경우
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    dialog = builder.setTitle("알림").setMessage("등록 실패").setNegativeButton("OK", null).create();
                                    dialog.show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }; //Response.Listener 완료.
                    //Volley 라이브러리를 써서 실제 서버와 통신을 구현하는 부분
                    //파라미터 입력하는 순서대로 데이터베이스에 저장된다.
                    RegisterRequest registerRequest = new RegisterRequest(stdnum, name, userGender, userMajor, userphone, userPassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(dialog !=null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
