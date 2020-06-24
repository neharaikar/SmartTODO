package com.example.project_testing;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Items extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       String pos =  getIntent().getStringExtra("position");
       int position=Integer.parseInt(pos);
        String itemlist =  getIntent().getStringExtra("itemlist");
       System.out.println(position+"..........................notification class");
        System.out.println(itemlist+"..........................notification class");
        ArrayList<String> listItems=new ArrayList<String>();
        ArrayAdapter<String> adapter;
        setContentView(R.layout.item_list);
        final ListView itemlistview=(ListView)findViewById(android.R.id.list);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        itemlistview.setAdapter(adapter);
        itemlist=itemlist+",";
        itemlist=itemlist.substring(5,itemlist.length());
        int l=itemlist.length();
        String newstr="";

        for(int i=0;i<l;i++)
        {
            char ch=itemlist.charAt(i);
            if(ch==',')
            {
                listItems.add(newstr);
                newstr="";
                adapter.notifyDataSetChanged();
            }
            else
            {
                newstr=newstr+ch;
            }
        }
        // When list view item is clicked.
        itemlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
              /////// checkbox
            }
        });


        super.onCreate(savedInstanceState);
    }


}
