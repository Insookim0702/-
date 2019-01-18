package com.example.kinso.testmarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kinso on 2019-01-16.
 */

public class BookStatusJsonInfo extends AppCompatActivity{
    JSONObject GetBookStatusJson;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_write_book);

    }
        //Json처리
        public JSONObject GetBookStatusJsonInfo(String resultline,String resultnote, String resultcover, String resultwritename, String resultpage) {
            GetBookStatusJson = new JSONObject();
            try {
                GetBookStatusJson.put("line", resultline);
                GetBookStatusJson.put("note", resultnote);
                GetBookStatusJson.put("cover", resultcover);
                GetBookStatusJson.put("writename", resultwritename);
                GetBookStatusJson.put("page", resultpage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return GetBookStatusJson;
        }
}
