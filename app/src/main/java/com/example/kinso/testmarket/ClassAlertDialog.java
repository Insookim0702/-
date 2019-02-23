package com.example.kinso.testmarket;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by kinso on 2019-01-28.
 */

public class ClassAlertDialog {
    private AlertDialog dialog;
    private Context activity;
    public ClassAlertDialog(Context activity){
        this.activity = activity;
    }
    public void dialogmethod(String title ,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        dialog = builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("확인",null).create();
        dialog.show();
    }


}
