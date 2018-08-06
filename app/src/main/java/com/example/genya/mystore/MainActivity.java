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

//Активити, отображающее список категорий продукции

public class MainActivity extends AppCompatActivity {
    //список категорий продукции
    ArrayList<sections> sectionList = new ArrayList<sections>();
    //адаптер, с помощью которого заполняется listview
    SectionAdapter adapter;
    //listview, отображающий все существующие категории
    ListView lvSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //создаем новый объект класса подключения к БД
        ConnDB connDB = new ConnDB();
        //отправляем запрос в БД на получение списка существующих категорий
        procc_answer(connDB.downloadDatas(0));
        //заполняем адаптер полученными данными
        adapter = new SectionAdapter(this, sectionList);
        //находим наш listview по id и привязываем к соответсвующему объекту
        lvSection = (ListView) findViewById(R.id.lvSection);
        //передаем в наш listview адаптер, для того, чтобы он мог вывести информацию на экран
        lvSection.setAdapter(adapter);
        //задаем обработчик нажатия на элемен listview
        lvSection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //получаем объект из нажатого элемента
               Object object = adapter.getItem(position);
               //извлекаем содержимое объекта в массив string
               String[] s = object.toString().split(" ");
               //получаем из массива идентификатор категории продукции
                //с помощью него в новом активити будем выводить список продуктов, которые входят в эту категорию
               String id_sect = s[0];
               //запускаем метод и передаем в него идентификатор категории
               newActivity(id_sect);
            }
        });

    }
    //метод создания нового активити
    public void newActivity(String id){
        Intent intent = new Intent(this, ListProducts.class);
        intent.putExtra("idSection", id);
        startActivity(intent);
    }
    //метод для обработки результата запроса
    private void procc_answer(JSONArray ja)
    {
        try {
            //создаем объект JSON
            JSONObject jo;

            Integer i = 0;
            Log.i("MainActivity", String.valueOf(ja.length()));

            while (i < ja.length()) {
                Log.i("MainActivity", String.valueOf(ja.getJSONObject(i)));
                // разберем JSON массив построчно
                jo = ja.getJSONObject(i);

                Log.i("MainActivity",
                        "=================>>> "
                                + jo.getString("id")
                                + " " + jo.getString("name"));

                String id = jo.getString("id");
                String name = jo.getString("name");
                //добавим в ArrayList новую запись
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
