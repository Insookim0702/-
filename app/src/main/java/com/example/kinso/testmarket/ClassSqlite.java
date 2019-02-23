package com.example.kinso.testmarket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by kinso on 2019-01-29.
 */

public class ClassSqlite {
    private MySQLiteOpenHelper helper;
    String dbName = "usercodefile.db";
    int dbVersion =1;// 데이터베이스 버전
    private SQLiteDatabase db;
    String tag ="SQLite";  //Log에 사용할 태그
    private Context activity;

    public ClassSqlite(Context activity){
        this.activity = activity;
        //SQLite3 :모바일 용으로 제작된 경량화 DB C언어로 엔진이 제작되어 가볍다.
        //안드로이드에서 sqlite3을 쉽게 사용할 수 있도록 SQLiteOpenHelper 클래스 제공
        helper = new MySQLiteOpenHelper(
                                        activity,// 현재 화면 제어권자
                                        dbName,  //db 이름
                                        null,   //커서팩토리-null : 표준커서가 사용됨
                                        dbVersion);       //버전
        try{
            //데이터베이스 객체를 얻어오는 다른 간단한 방법
            //db = openOrCreateDatabase(dbName,// 데이터베이스 파일 이름
            //                          Context.MODE_PRIVATE, //파일 모드
            //                          null);              //커서 팩토리
            //String sql = "create table mytable(myusercode text primary key);";
           // db.execSQL(sql);
            db = helper.getWritableDatabase(); //읽고 쓸 수 있는 DB
            //db = helper.getReadableDatabase(); // 읽기 전용 DB select문
        }catch (SQLiteException e){
            e.printStackTrace();
            Log.e(tag, "데이터베이스를 얻어올 수 없음.");
        }
    }
    void alter(){
        String sql = "ALTER TABLE myusercodetable ADD MessageRoomNumber varchar(16);";
        db.execSQL(sql);
        Log.d(tag,"alter 완료");
    }
    void insert(){
        ClassMappingNumber classMappingNumber = new ClassMappingNumber(activity);
        String usercode = classMappingNumber.getUserCode();
        System.out.println("insert()메소드에 phonenum>>>>"+usercode);
        String sql ="insert into myusercodetable(myusercode) values('"+usercode+"');";
        db.execSQL(sql);
        Log.d(tag, "insert 성공 ~!");
    }
    void delete(){
        db.execSQL("DELETE FROM myusercodetable;");
        Log.d(tag, "delete 완료");
    }
    String select(){
        Cursor c = db.rawQuery("SELECT * FROM myusercodetable;",null);
        String sumString="";
        while(c.moveToNext()){
            String usercode = c.getString(0);
            sumString+=usercode;
            Log.d(tag,"usercode : " + usercode);
        }
        return sumString;
    }
}
