package com.example.kinso.testmarket;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinso on 2019-01-24.
 */

public class RequestWrite extends StringRequest {
    final static private String URL = "http://13.209.3.191/write.php";
    private Map<String, String>  parameters;
    public RequestWrite(String DataCategory,                         //1. 등록 제품 카테고리
                        String DataWriteNum,                         //2. 등록 번호 (Primary key)
                        String DataUserId,                           //3. 등록 유저 ID
                        String DataDate,                             //4. 등록 날짜
                        String DataTitle,                            //5. 글 제목
                        String DataBcBookImageUrl,                   //6. 책이미지url
                        String DataBcAuthor,                         //7. 책 저자.
                        String DataBcPublisher,                      //8. 책 출판사
                        String DataBcPrice,                             //9. 책 가격
                        String DataBcDiscountPrice,                      //10. 책 할인가격
                        String DataUserPrice,                           //11. 유저가 등록한 가격
                        String DataSubjectName,                      //12. 수강과목 명
                        String DataDescription,                      //13. 등록자 코멘트
                        String DataJsonStatus,                   //14. 책 상태
                        String DataImgName1,                         //15. 제품 이미지1
                        String DataImgName2,                         //16. 제품 이미지2
                        String DataImgName3,                         //17. 제품 이미지3
                        String DataEncoded_imageString1,             //18. 이미지 디코딩1
                        String DataEncoded_imageString2,             //19. 이미지 디코딩2
                        String DataEncoded_imageString3,             //20. 이미지 디코딩3
                        Response.Listener<String> listener){
        super(Method.POST, URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("DataCategory", DataCategory);                                            //1
        parameters.put("DataWriteNum", DataWriteNum);                                            //2
        parameters.put("DataUserId", DataUserId);                                            //3.
        parameters.put("DataDate", DataDate);                                                    //4.
        parameters.put("DataTitle", DataTitle);                                      //5.
        parameters.put("DataBcBookImageUrl", DataBcBookImageUrl);                                //6.
        parameters.put("DataBcAuthor", DataBcAuthor);                                            //7.
        parameters.put("DataBcPublisher", DataBcPublisher);                                      //8.
        parameters.put("DataBcPrice", DataBcPrice);                                              //9.
        parameters.put("DataBcDiscountPrice", DataBcDiscountPrice);                              //10.
        parameters.put("DataUserPrice", DataUserPrice);                                          //11.
        parameters.put("DataSubject", DataSubjectName);                                      //12.
        parameters.put("DataDescription", DataDescription);                                      //13.
        parameters.put("DataJsonStatus", DataJsonStatus);                                //14.
        parameters.put("DataImgName1", DataImgName1);                                            //15.
        parameters.put("DataImgName2", DataImgName2);                                            //16.
        parameters.put("DataImgName3", DataImgName3);                                            //17.
        parameters.put("DataEncoded_imageString1", DataEncoded_imageString1);                    //18.
        parameters.put("DataEncoded_imageString2", DataEncoded_imageString2);                    //19.
        parameters.put("DataEncoded_imageString3", DataEncoded_imageString3);                    //20.
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return parameters;
    }

}
