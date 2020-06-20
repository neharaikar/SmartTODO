package com.example.project_testing;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Items extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       String pos =  getIntent().getStringExtra("position");
       int position=Integer.parseInt(pos);
        String itemlist =  getIntent().getStringExtra("itemlist");
       System.out.println(position+"..........................notification class");
        System.out.println(itemlist+"..........................notification class");
        setContentView(R.layout.item_list);
        super.onCreate(savedInstanceState);
    }

}
