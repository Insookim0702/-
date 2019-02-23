package com.example.kinso.testmarket;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by kinso on 2019-01-26.
 */

public class ClassImageEdit {
    //이미지 사진 회전
    //상수를 받아 각도로 변환시켜주는 메소드
    protected int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }
    //비트맵을 각도대로 회전시켜 결과를 반환해주는 메소드
    protected Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix, true);
    }
    protected void saveExifFile(Bitmap bitmap, String savePath){
        FileOutputStream fos = null;
        File saveFile = null;
        try{
            saveFile = new File(savePath);
            fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
