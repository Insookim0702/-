package com.example.kinso.testmarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by kinso on 2018-12-24.
 */

public class MainActivity extends AppCompatActivity{
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    //이 자바파일이 사용되면 가장 먼저 실행되는 부분은 onCreate부분이다.
    //이는 이 java파일 자체를 메모리에 올려서 객체화 시키는 부분이라고 생각하면 된다.

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main); //XML에 만들어 놓은 뷰들을 메모리 상에 객체화 시키는 것을 말한다.
        //layout폴더에 있는 activity_main을 사용할 것이니까 이를 객체화 시켜라!라는 의미

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);// 리스트를 일렬로 배치시킨다.

        //지정된 레이아웃 메니저를 RecyclerView에 set 해준다.
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<ItemInfo> ItemInfoArrayList = new ArrayList<>();
        ItemInfoArrayList.add(new ItemInfo(R.drawable.bread,"500원"));
        ItemInfoArrayList.add(new ItemInfo(R.drawable.compu, "800,000원"));
        ItemInfoArrayList.add(new ItemInfo(R.drawable.hack, "3600원"));
        ItemInfoArrayList.add(new ItemInfo(R.drawable.iot,"4000원"));
        MyAdapter myAdapter = new MyAdapter(ItemInfoArrayList);
        mRecyclerView.setAdapter(myAdapter);
    }
}
