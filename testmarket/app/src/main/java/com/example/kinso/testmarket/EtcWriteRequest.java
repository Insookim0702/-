package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-01-18.
 */

public class EtcWriteRequest extends StringRequest {
    final static private String URL = "http://192.168.0.10:80/HTphp/connection.php";
    private Map<String, String> parameters;
    public EtcWriteRequest(String encoded_string, String image_name, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("encoded_string", encoded_string);
        //System.out.println("데이터베이스에 저장되는 인코딩된 스트링 : "+encoded_string);
        parameters.put("image_name", image_name);
        System.out.println("데이터베이스에 저장되는 이미지 이름 : "+image_name);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
