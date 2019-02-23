package com.example.kinso.testmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kinso on 2019-01-15.
 */

public class ClassNaverApi extends Thread{
        Bitmap bitmapFromUrl;
        String res;
        private String isbn;
        ActivityWriteBook callbackInstance =null;
        public ClassNaverApi(String isbn, ActivityWriteBook callback){
            this.isbn = isbn;
            this.callbackInstance = callback;
        }

        public void run(){
            System.out.println("메인쓰레드에서  : " + isbn);
            String clientId ="ArjpAW39F6QlwzhJ_6bN";
            String clientSecret = "Z_w1lMRswa";
            try{
                String apiURL = "https://openapi.naver.com/v1/search/book.json?query="+isbn+"&display=1&start=1"; // json 결과
                URL url = new URL(apiURL);
                System.out.println("NaverAPI에서 isbn=======" + url);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200){//서버에서 정상 호출
                    System.out.println("서버에서 정상 호출");
                    //네트워크 특성상 가져오는 데이터의 양이 정해져 있지 않기 때문에 Thread를 별도로 돌려야 한다.
                    //그렇지 않으면 데이터를 가져오는 동안 프로그램이 멈춰 있는다.
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));//InputStreamReader : 데이터를 읽어온다.
                }else{
                    br= new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer JsonString = new StringBuffer();
                while((inputLine = br.readLine()) !=null){
                    JsonString.append(inputLine);
                }
                br.close();
                JSONObject tempjsonObj = new JSONObject(String.valueOf(JsonString));
                JSONObject jsonObj = new JSONObject(String.valueOf(tempjsonObj.getString("items").replace("[","").replace("]","")));
                res = jsonObj.toString();
                String imageurl="";
                imageurl = jsonObj.getString("image");
                System.out.println("이미지 유알엘================: " + imageurl);
                URL RealImageUrl = new URL(imageurl);
                //web에서 이미지 가져오고, ImageView에 지정할 Bitmap을 만든다.
                HttpURLConnection imagecon = (HttpURLConnection) RealImageUrl.openConnection();
                imagecon.setDoInput(true);//서버로부터 응답 수신
                imagecon.connect();
                System.out.println("이미지 가져오는 네트워크 정상 연결됨");
                InputStream is = imagecon.getInputStream(); //InputStream값으로 이미지 가져오기
                bitmapFromUrl = BitmapFactory.decodeStream(is);
                this.callbackInstance.callbackThread(bitmapFromUrl,res);

            }catch (Exception e){
                System.out.println("실패실패실패실패실패실패실패실패실패실패실패");
                System.out.println(e);
            }
        }


    }

