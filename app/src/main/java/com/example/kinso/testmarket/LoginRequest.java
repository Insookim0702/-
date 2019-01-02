package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-01-02.
 */

public class LoginRequest  extends StringRequest{
    final static private String URL = "http://192.168.0.6:80/HTphp/UserLogin.php";
    private Map<String, String> parameters;
    public LoginRequest(String stdnum, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null); // 해당 URL에 POST방식으로 파라미터들을 전송한다.
        parameters = new HashMap<>();
        parameters.put("stdnum", stdnum);
        parameters.put("userPassword", userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
