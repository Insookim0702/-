package com.example.kinso.testmarket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by kinso on 2019-02-19.
 */

public class ClassSqliteChat extends SQLiteOpenHelper{
    private  Context context;
    public ClassSqliteChat(Context context, String name,SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory,version);
        this.context = context;

    }
    //DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db){
        //새로운 테이블 생성
        //이름은 chat_table, 자동 증가 id값
        db.execSQL("CREATE TABLE chat_table (id INTEGER PRIMARY KEY AUTOINCREMENT, writenumber TEXT , title TEXT,sender TEXT, message TEXT, receiver TEXT, today TEXT)");
        Toast.makeText(context, "Table 생성 완료", Toast.LENGTH_SHORT).show();
    }
    //DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
    public void insert(String writenumber, String myusercode, String title,String receiver, String message, String today){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO chat_table(writenumber, title, sender, message, receiver, today) VALUES('"+writenumber+"', '"+title+ "', '"+ myusercode+"','"+message+"', '"+receiver+"', '"+today+"')");
        db.close();
    }
    public String getResult(String writenumber, String myusercode){
        //읽기가 가능하게 DB열기
        SQLiteDatabase db = getReadableDatabase();
        String result ="";

        //DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용해서 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM chat_table WHERE writenumber = '"+writenumber+ "' AND (receiver = '"+myusercode+"' OR sender ='"+ myusercode+"')", null);
        int count=0;
        while(cursor.moveToNext()){
            if(count==1){
                result+=",";
                count=0;
            }
            result +="{"
                    +" \"No\": \""+ cursor.getString(0)
                    +"\",\"WriteNumber\": \"" +cursor.getString(1)
                    +"\",\"Title\": \""+ cursor.getString(2)
                    +"\",\"Sender\": \""+ cursor.getString(3)
                    +"\",\"Message\": \"" + cursor.getString(4)
                    +"\",\"Receiver\": \"" + cursor.getString(5)
                    +"\",\"SendDate\": \"" + cursor.getString(6)
                    +"\"}";
            count++;
        }
        /*{
            message : [
                    {
                      No : 2019-02-19 20:24,
                      WriteNumber :#4109975201,
                      Title : 귤맛 우유 팜,
                      Sender: 1b3306333GG,
                      Message: ㅡㅡㅡㅡㅡ,
                      Receiver   : 1b130133300,
                      SendDate : 2019-02-19 20:24
                    },
                    {
                      No : 2019-02-19 20:32,
                      WriteNumber :#4109975201,
                      Title : 귤맛 우유 팜,
                      Sender: 1b3306333GG,
                      Message: ㅡㅡㅡㅡ,
                      Receiver   : 1b130133300,
                      SendDate : 2019-02-19 20:32
                    },
                    {
                      No : 2019-02-19 20:37,
                      WriteNumber :#4109975201,
                      Title : 귤맛 우유 팜,
                      Sender: 1b3306333GG,
                      Message: ㅡㅡ,
                      Receiver   : 1b130133300,
                      SendDate : 2019-02-19 20:37
                    }
            ]
        }*/
        return result;
    }
}
