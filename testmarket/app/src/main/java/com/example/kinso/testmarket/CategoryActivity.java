package com.example.kinso.testmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by kinso on 2019-01-09.
 */

public class CategoryActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle saveInstanceState){
    super.onCreate(saveInstanceState);
    setContentView(R.layout.activity_category);

    ActionBar ab = getSupportActionBar();
    ab.show();
    ab.setTitle("글쓰기 카테고리(1/2)");
    ab.setDisplayHomeAsUpEnabled(true);//액션바에 뒤로가기 버튼을 보여줌.

    TextView book = (TextView) findViewById(R.id.book);
    book.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent MoveWrite = new Intent(CategoryActivity.this, WriteActivity.class);
            CategoryActivity.this.startActivity(MoveWrite);
        }
    });

    }
    //액션바 뒤로가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
