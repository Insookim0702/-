package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-01-18.
 */

public class RequestWriteEtc extends StringRequest {
    //final static private String URL = "http://10.93.10.93:80/HTphp/etcwrite.php";
    //final static private String URL = "http://192.168.0.10:80/HTphp/etcwrite.php";
    //final static private String URL = "http://10.93.253.20:80/HTphp/etcwrite.php";
    final static private String URL = "http://13.209.3.191/writeetc.php";
    //final static private String URL = "http://192.168.0.6:80/HTphp/etcwrite.php";

    private Map<String, String> parameters;
    //1 . 등록 제품 카테고리//2. 글 등록 번호 (Primary key)//3.  등록 유저ID//4. 글 쓴 날짜//5. 제품 상태//6. 제품 제목//7. 제품 가격//8. 제품 설명//9. 제품 이미지 이름 1//10. 제품 이미지 이름 2//11. 제품 이미지 이름 3//12. 인코딩된 이미지1//13. 인코딩된 이미지2//14. 인코딩된 이미지3
    public RequestWriteEtc(
                           String DataWriteNum,
                           String DateDay,
                           String DataUserId,
                           String DataCategory,
                           String DataTitle,
                           String DataPrice,
                           String DataDescription,
                           String DataStatus,
                           String DataImage1Name,
                           String DataImage2Name,
                           String DataImage3Name,
                           String DataEncoded_imageString1,
                           String DataEncoded_imageString2,
                           String DataEncoded_imageString3,
                           Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        parameters = new HashMap<>();
        parameters.put("DataWriteNum", DataWriteNum);
        parameters.put("DataDay", DateDay);
        parameters.put("DataUserId", DataUserId);
        parameters.put("DataCategory", DataCategory);
        parameters.put("DataTitle", DataTitle);
        parameters.put("DataPrice", DataPrice);
        parameters.put("DataDescription", DataDescription);
        parameters.put("DataStatus", DataStatus);
        parameters.put("DataImageName1", DataImage1Name);
        parameters.put("DataImageName2", DataImage2Name);
        parameters.put("DataImageName3", DataImage3Name);
        parameters.put("DataEncoded_imageString1", DataEncoded_imageString1);
        parameters.put("DataEncoded_imageString2", DataEncoded_imageString2);
        parameters.put("DataEncoded_imageString3", DataEncoded_imageString3);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        System.out.println("되라~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return parameters;
    }
}
