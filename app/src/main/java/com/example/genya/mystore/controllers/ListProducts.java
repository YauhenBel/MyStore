package com.example.genya.mystore.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.genya.mystore.R;
import com.example.genya.mystore.adapters.ProductAdapter;
import com.example.genya.mystore.objects.products;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Активити, отображающее список продукции

public class ListProducts extends AppCompatActivity {

    //список продукции
    ArrayList<products> productsList = new ArrayList<products>();
    ProductAdapter adapter;
    ListView lvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        //получаем информацию, переданную из предедыщего активити
        Intent intent = getIntent();
        String idSection = intent.getStringExtra("idSection");
        //запрос к БД на возврат списка продукции
        ConnDB connDB = new ConnDB();
        procc_answer(connDB.downloadDatas(1, idSection));
        //заполняем listview
        adapter = new ProductAdapter(this, productsList);
        lvProducts = (ListView) findViewById(R.id.lvProducts);
        lvProducts.setAdapter(adapter);
        //присваиваем listview обработчик нажатия
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = adapter.getItem(position);
                Log.i("ListProducts", object.toString());
                String[] s = object.toString().split("&");
                String id_prod = s[0];
                String name = s[1];
                String photo = s[3];
                String cost = s[4];

                Log.i("ListProducts", id_prod + " " + name + " " + photo + " " + cost);
                //запускаем активити с подробной информацие о выбранном продукте
                newActivity(id_prod, name, photo, cost);
            }
        });


    }
    //
    public void newActivity(String id, String name, String photo, String cost){
        Intent intent = new Intent(this, AboutProduct.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("photo", photo);
        intent.putExtra("cost", cost);
        startActivity(intent);
    }
    //обрабатываем и возвращаем результат запроса
    private void procc_answer(JSONArray ja)
    {
        try {
            JSONObject jo;

            Integer i = 0;
            Log.i("ListProducts", String.valueOf(ja.length()));

            while (i < ja.length()) {
                Log.i("ListProducts", String.valueOf(ja.getJSONObject(i)));
                // разберем JSON массив построчно
                jo = ja.getJSONObject(i);

                Log.i("ListProducts",
                        "=================>>> "
                                + jo.getString("id")
                                + " " + jo.getString("name")
                                + " " + jo.getString("photo")
                                + " " + jo.getString("cost"));

                String id = jo.getString("id");
                String name = jo.getString("name");
                String photo = jo.getString("photo");
                String cost = jo.getString("cost");

                productsList.add(new products(id, name,
                        photo, cost));
                i++;
            }
        }
        catch (Exception e)
        {
            Log.i("ListProducts",
                    "ошибка ответа сервера:\n"
                            + e.getMessage());
        }


    }
}
