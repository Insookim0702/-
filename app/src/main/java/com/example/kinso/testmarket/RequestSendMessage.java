package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-01-31.
 */

public class RequestSendMessage extends StringRequest {
    //final static private String URL = "http://10.93.10.93:80/HTphp/messagewrite.php";
    //final static private String URL = "http://192.168.0.10:80/HTphp/messagewrite.php";
    final static private String URL = "http://13.209.3.191/writemessage.php";

    //final static private String URL = "http://192.168.0.6:80/HTphp/messagewrite.php";


    private Map<String, String> parameters;
    public RequestSendMessage(String RoomNumber,
                                   String WriteNum,
                                   String Title,
                                   String Receiver,
                                   String Sender,
                                   String Message,
                                   String SendDay,
                                   Response.Listener<String> listener){
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("RoomNumber", RoomNumber);
        parameters.put("WriteNum", WriteNum);
        parameters.put("Title", Title);
        parameters.put("Receiver", Receiver);
        parameters.put("Sender",Sender);
        parameters.put("Message",Message);
        parameters.put("SendDay", SendDay);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }
}
