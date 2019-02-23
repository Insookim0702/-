package com.example.kinso.testmarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by kinso on 2019-01-29.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(Context context, String PhoneNum, SQLiteDatabase.CursorFactory  factory, int version){
        super(context,PhoneNum, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        //최초에 데이터베이스가 없을 경우, 데이터베이스 생성을 위해 호출됨
        //테이블 생성하는 코드를 작성한다.
        String sql ="CREATE TABLE myusercodetable(myusercode text primary key);";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //데이터베이스의 버전이 바뀌었을 때 호출되는 콜백 메서드
        //버전 바뀌었을 떄 기존 데이터베이스를 어떻게 변경할 것인지 작성한다.
        //각 버전의 변경 내용들을 버전마다 작성해야 한다.
        String sql = "INSERT INTO myusercodetable VALUES(00000);";
        db.execSQL(sql);
        onCreate(db);  //다시 테이블 생성
    }
}
