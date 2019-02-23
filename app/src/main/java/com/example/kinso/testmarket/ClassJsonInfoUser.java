package com.example.kinso.testmarket;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by kinso on 2019-01-16.
 */

public class ClassJsonInfoUser {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Calendar calendar = Calendar.getInstance();
    StringBuffer writeno;

    public JSONObject userjsoninfo() throws JSONException, ParseException {
        JSONObject jsoninfo;
        String today ="";
        String userdescription ="";
        jsoninfo = new JSONObject();
        //오늘 날짜
        today = today();
        Date day = format.parse(today);
        Date day2= format.parse(tomorrow(today));
        System.out.println("오늘 날짜 초로 구한 값." + day);
        System.out.println("오늘 날짜 초로 구한 값." + day2);


        //게시글 번호 생성
        writeno = makeWriteNumber(today, day,day2);
        //유저코멘트
        userdescription ="연락 바랍니다.";
        jsoninfo.put("writeno",writeno);
        jsoninfo.put("userdescription",userdescription);
        jsoninfo.put("today",today);
        return jsoninfo;
    }
    public String today(){
        String today = format.format(calendar.getTime());
        System.out.println("오늘 날짜 :==================" + today);
        return today;
    }
    public String tomorrow(String today){
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd 00:00");
        try {
            calendar.setTime(format.parse(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE,1);
        String tomorrow = format2.format(calendar.getTime());
        System.out.println("내일 날짜 :==================" + tomorrow);
        return tomorrow;
    }
    public StringBuffer makeWriteNumber(String today, Date day, Date day2){
        //게시글 번호 생성
        //원리 : #(M2+2)d1그날0번부터~등록순count(y+9)d2(random)(M1+1)
        writeno = new StringBuffer();
        Random random = new Random();
        int count=0;

        if(day2.getTime()-day.getTime()/1000<=60){// 빼면 1000이 더 붙어서 나오므로 1000을 나눔.
            count =0;
        }else{
            writeno.append("#");
            writeno.append(Integer.parseInt(today.substring(6,7))+2);
            writeno.append(today.substring(8,9));
            writeno.append(count);
            writeno.append(today.substring(3,4)+9);
            writeno.append(today.substring(9,10));
            writeno.append(random.nextInt(100));//0~99
            writeno.append(today.substring(5,6)+1);
            count++;
        }
        return writeno;
    }
}
