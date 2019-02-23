package com.example.kinso.testmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by kinso on 2019-01-09.
 */

public class ActivityCategory extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle saveInstanceState){
    super.onCreate(saveInstanceState);
    setContentView(R.layout.activity_category);

    ActionBar ab = getSupportActionBar();
    ab.show();
    ab.setTitle("글쓰기 카테고리(1/3)");
    ab.setDisplayHomeAsUpEnabled(true);//액션바에 뒤로가기 버튼을 보여줌.
    Toast.makeText(ActivityCategory.this, "게시글은 등록일시로부터 4일 후 자동 삭제됩니다.",Toast.LENGTH_SHORT);
    TextView book = (TextView) findViewById(R.id.book);
    TextView digital = (TextView) findViewById(R.id.digital);
    TextView female_fashion = (TextView) findViewById(R.id.female_fashion);
    TextView gita = (TextView) findViewById(R.id.gita);
    TextView game = (TextView) findViewById(R.id.game);
    TextView beauty = (TextView) findViewById(R.id.beauty);
    TextView male_fashion = (TextView) findViewById(R.id.male_fashion);
    TextView buy = (TextView) findViewById(R.id.buy);
    TextView bang_empty = (TextView)findViewById(R.id.bang_empty);
    TextView recruit = (TextView) findViewById(R.id.recruit);
    TextView gwiwei = (TextView)findViewById(R.id.gwiwei);
    TextView introduce =(TextView)findViewById(R.id.introduce);
    TextView junci=(TextView)findViewById(R.id.junci);
    book.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteBook.class);
            MovetoWriteAct.putExtra("category","도서");
            ActivityCategory.this.startActivity(MovetoWriteAct);
        }
    });
        digital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","디지털/가전");
                ActivityCategory.this.startActivity(MovetoWriteAct);
            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","게임/취미");
                ActivityCategory.this.startActivity(MovetoWriteAct);
            }
        });
        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","뷰티/미용");
                ActivityCategory.this.startActivity(MovetoWriteAct);
            }
        });
        male_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","남성의류/잡화");
                ActivityCategory.this.startActivity(MovetoWriteAct);
            }
        });
        female_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","여성의류/잡화");
                ActivityCategory.this.startActivity(MovetoWriteAct);
            }
        });
        gita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","기타 물품");
                ActivityCategory.this.startActivity(MovetoWriteAct);
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCategory.this,"준비 중 입니다...",Toast.LENGTH_SHORT).show();

                /*finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","삽니다.");
                ActivityCategory.this.startActivity(MovetoWriteAct);*/
            }
        });
        bang_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCategory.this,"준비 중 입니다...",Toast.LENGTH_SHORT).show();
                /*finish();
                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","방 비어요.");
                ActivityCategory.this.startActivity(MovetoWriteAct);*/
            }
        });
        recruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCategory.this,"준비 중 입니다...",Toast.LENGTH_SHORT).show();
                /*finish();.sho
                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","일 할 사람 구해요.");
                ActivityCategory.this.startActivity(MovetoWriteAct);*/
            }
        });
        gwiwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCategory.this,"준비 중 입니다...",Toast.LENGTH_SHORT).show();
                /*finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","과외/클래스/스터디 모집");
                ActivityCategory.this.startActivity(MovetoWriteAct);*/
            }
        });
        introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCategory.this,"준비 중 입니다...",Toast.LENGTH_SHORT).show();

                /*finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","꿀팁");
                ActivityCategory.this.startActivity(MovetoWriteAct);*/
            }
        });
        junci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityCategory.this,"준비 중 입니다...",Toast.LENGTH_SHORT).show();

                /*finish();

                Intent MovetoWriteAct = new Intent(ActivityCategory.this, ActivityWriteEtc.class);
                MovetoWriteAct.putExtra("category","전시/공연/행사");
                ActivityCategory.this.startActivity(MovetoWriteAct);*/
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
