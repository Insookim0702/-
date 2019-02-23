package com.example.kinso.testmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kinso on 2019-01-25.
 */

public class AsyncReadImage extends AsyncTask<String, Void,Bitmap> {
    ImageView iv =null;
    private String imagename;
    String stringurl;
    public AsyncReadImage(String imagename, ImageView iv){
        this.imagename = imagename;
        this.iv=iv;
    }
    @Override
    protected Bitmap doInBackground(String... params){
        System.out.println("================doInBackground에서 받은 값 : ===========" + imagename);
        //if(category.equals("도서")){
        //}else{
        //    category = "image";
        //}
        return upload_image(imagename);
    }
    @Override
    protected void onPostExecute(Bitmap result){
        iv.setImageBitmap(result);

    }

    public Bitmap upload_image(String imgname){
        Bitmap btm =null;
        System.out.println("upload_image에서 파라미터로 받은 값은 : " +imgname);
        //stringurl = "http://10.93.10.93:80/HTphp/"+file+"/" + imgname;
        //stringurl = "http://192.168.0.10:80/HTphp/"+file+"/" + imgname;
        stringurl = "http://13.209.3.191/image/"+imgname;
        //stringurl = "http://192.168.0.6:80/HTphp/"+file+"/" + imgname;
        //stringurl = "http://10.93.253.20:80/HTphp/"+file+"/" + imgname;

        try {
            URL url = new URL(stringurl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream is = con.getInputStream();
            btm = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return btm;
    }

}
