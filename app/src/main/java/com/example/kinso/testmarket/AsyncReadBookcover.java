package com.example.kinso.testmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kinso on 2019-01-25.
 */

public class AsyncReadBookcover extends AsyncTask<String, Void, Bitmap> {
    ImageView iv = null;
    private String geturl;

    public AsyncReadBookcover(String geturl, ImageView iv){this.geturl = geturl;this.iv = iv;}
    @Override
    protected Bitmap doInBackground(String... params){
        return download_image(geturl);
    }

    @Override
    protected void onPostExecute(Bitmap result){
        iv.setImageBitmap(result);
    }

    private Bitmap download_image(String url){
        Bitmap bmp = null;
        try{
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            con.setDoInput(true);
            con.connect();
            System.out.println("이미지 가져오는 네트워크 정상 연결됨");
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if(null!=bmp){
                return bmp;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bmp;
    }
}

