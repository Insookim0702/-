package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-02-03.
 */

public class RequestRemove extends StringRequest{
    final static private String URL = "http://13.209.3.191/remove.php";
    //final static private String URL = "http://192.168.0.6:80/HTphp/remove.php";
    private Map<String, String> parameters;
    public RequestRemove(String WriteNumber/*, String Category, Response.Listener<String> listener*/){
        super(Method.POST, URL, null, null);
        parameters = new HashMap();
        parameters.put("WriteNumber", WriteNumber);
        /*parameters.put("Category", Category);*/
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
