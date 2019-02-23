package com.example.kinso.testmarket;

import android.graphics.Bitmap;

import java.text.DecimalFormat;

/**
 * Created by kinso on 2018-12-25.
 */

public class CardBookInfo {
    public String category,
     writeday,
     title,
     bcauthor,
     bcpublisher,
     bcprice,
     bcdiscountprice,
     userprice,
     viewimage,    // 사,
     writenumber,
     life_day,
     life_time;

    public CardBookInfo(String category, String title, String writeday, String bcauthor,
                        String bcpublisher, String bcprice, String bcdiscountprice, String userprice,
                        String viewimage, String writenumber, String life_day, String life_time){
        this.category = category;
        this.writeday =writeday;
        if(title.length()>40){
            System.out.println("40 넘는거 맞음.....");
            title = title.substring(0,38)+"...";
            System.out.println(title);
            this.title = title;
        }
        this.title = title;
        this.bcauthor = bcauthor;
        this.bcpublisher = bcpublisher;
        DecimalFormat df = new DecimalFormat("#,##0");
        if(category.equals("도서")){
            this.bcprice = df.format(Integer.parseInt(bcprice));
            this.bcdiscountprice = df.format(Integer.parseInt(bcdiscountprice));
            this.userprice = df.format(Integer.parseInt(userprice));
        }else{
            this.bcprice = bcprice;
            this.bcdiscountprice = bcdiscountprice;
            this.userprice = df.format(Integer.parseInt(userprice));
        }

        this.viewimage =viewimage;
        this.writenumber =writenumber;
        this.life_day = life_day;
        this.life_time = life_time;

    }
}
