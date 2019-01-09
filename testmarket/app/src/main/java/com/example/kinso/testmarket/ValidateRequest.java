package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2018-12-29.
 */
/*
 * 해당 클래스도 아래 URL에 POST방식으로 파라미터들을 전송한다. 여기서는 학번이 이미 가입된 학번인지 검증한다.
 */
public class ValidateRequest  extends StringRequest{
    final static private String URL = "http://192.168.0.131:80/HTphp/UserValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String stdnum, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파라미터들을 전송
        parameters = new HashMap<>();
        parameters.put("stdnum", stdnum);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }

}
