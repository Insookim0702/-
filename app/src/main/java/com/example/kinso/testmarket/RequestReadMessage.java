package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-01-31.
 */

public class RequestReadMessage extends StringRequest{
    final static private String URL = "http://13.209.3.191/readmessage.php";
    //final static private String URL = "http://192.168.0.6:80/HTphp/messageread.php";
    //final static private String URL = "http://10.93.10.93:80/HTphp/messageread.php";
    //final static private String URL = "http://192.168.0.10:80/HTphp/messageread.php";
    private Map<String, String> parameters;
    public RequestReadMessage(String WriteNumber, String Receiver, String Sender, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("DataWriteNum",WriteNumber);
        parameters.put("WriteUser",Receiver);
        parameters.put("MyUserCode",Sender);
        System.out.println("!!!!!!!!!!!!!!!!!!RequestReadMessage!!!!!!!!!!!!!!!!!!");
        System.out.println(WriteNumber);
        System.out.println(Receiver);
        System.out.println(Sender);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
