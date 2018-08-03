package com.example.genya.mystore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.genya.mystore.adapters.SectionAdapter;
import com.example.genya.mystore.controllers.ConnDB;
import com.example.genya.mystore.controllers.ListProducts;
import com.example.genya.mystore.objects.sections;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<sections> sectionList = new ArrayList<sections>();
    //String name, id;
    SectionAdapter adapter;
    ListView lvSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("store", "+ worker");
        ConnDB connDB = new ConnDB();
        procc_answer(connDB.downloadDatas(0));

        adapter = new SectionAdapter(this, sectionList);
        lvSection = (ListView) findViewById(R.id.lvSection);
        lvSection.setAdapter(adapter);
        registerForContextMenu(lvSection);

        lvSection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Object object = adapter.getItem(position);
               String[] s = object.toString().split(" ");
               String id_sect = s[0];
               newActivity(id_sect);
            }
        });

    }

    public void newActivity(String id){
        Intent intent = new Intent(this, ListProducts.class);
        System.out.print("idSection1" + id);
        intent.putExtra("idSection", id);
        startActivity(intent);
    }

    private void procc_answer(JSONArray ja)
    {
        try {
            JSONObject jo;

            Integer i = 0;
            Log.i("chat", String.valueOf(ja.length()));

            while (i < ja.length()) {
                Log.i("chat", String.valueOf(ja.getJSONObject(i)));
                // разберем JSON массив построчно
                jo = ja.getJSONObject(i);

                Log.i("chat",
                        "=================>>> "
                                + jo.getString("id")
                                + " " + jo.getString("name"));

                String id = jo.getString("id");
                String name = jo.getString("name");
                Log.i("chat", "id = " + id);
                Log.i("chat", "name = " + name);
                sectionList.add(new sections(id, name));
                i++;
            }
        }
        catch (Exception e)
        {
            Log.i("Profile",
                    "ошибка ответа сервера:\n"
                            + e.getMessage());
        }


    }
}
