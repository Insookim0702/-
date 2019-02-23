package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by kinso on 2019-02-17.
 */
//오늘 날짜, 유저 코드
public class RequestValidateWrite extends StringRequest{
    final static private String URL = "http://13.209.3.191/validatewrite.php";
    private Map<String, String> parameters;
    public RequestValidateWrite(String myusercode, Response.Listener<String> listener){
        super(Method.POST, URL,listener, null);
        parameters = new HashMap<>();
        parameters.put("myusercode", myusercode);
        System.out.println("Request 요청됨");
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
