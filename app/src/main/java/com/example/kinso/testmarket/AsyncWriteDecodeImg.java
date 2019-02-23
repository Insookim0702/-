package com.example.kinso.testmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by kinso on 2019-01-24.
 */

public class AsyncWriteDecodeImg extends AsyncTask<String,Void,String> {
    public String result;
    private String str;
    public AsyncWriteDecodeImg(String str){
        this.str = str;
    }
    @Override
    protected String doInBackground(String... params) {
        Uri uri = Uri.parse(str);
        System.out.println("imageUri가 정상적으로 넘어왔나>>>>>>>>>>>>>>>>?"+str);
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] array = stream.toByteArray();
        String Encoded_string = Base64.encodeToString(array, 0);
        System.out.println("Encoded_string는 : "+Encoded_string.substring(0,19));
        result = Encoded_string;
        System.out.println("result >>>>>>>>>>>>>>>>>"+result.substring(0,9));
        return result;
    }

}
