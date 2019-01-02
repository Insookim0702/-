package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2018-12-28.
 */

public class RegisterRequest extends StringRequest {
    //해당클래스는 URL에 POST방식으로 파라미터들을 전송하는 역할을 수행.
    //회원가입정보를 PHP서버에 보내서 데이터베이스에 저장
    final static private String URL ="http://192.168.0.6:80/HTphp/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String stdnum, String userName, String userGender,
                           String userMajor, String userphone, String ch_pww, Response.Listener<String> listener) {
            super(Method.POST, URL, listener, null); //해당 URL에 POST방식으로 파라미터들을 전송함
            parameters = new HashMap<>();
            parameters.put("stdnum", stdnum);
            parameters.put("userName", userName);
            parameters.put("userGender", userGender);
            parameters.put("userMajor", userMajor);
            parameters.put("userphone", userphone);
            parameters.put("ch_pww", ch_pww);

        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }

}
