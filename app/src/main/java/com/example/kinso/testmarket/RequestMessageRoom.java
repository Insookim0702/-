package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-02-02.
 */

public class RequestMessageRoom extends StringRequest {
    final static private String URL = "http://13.209.3.191/readmessageroom.php";
    //final static private String URL = "http://192.168.0.6:80/HTphp/messageroomread.php";
    Map<String, String> parameters;
    public RequestMessageRoom(String MyUserCode, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("MyUserCode", MyUserCode);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
