package com.example.genya.mystore.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genya.mystore.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

//Активити, отображающее информацию о конкретном продукте

public class AboutProduct extends AppCompatActivity {
    //информация о продукте: идентификатор в БД, наименование, описание, фото, цена, наличие
    String id, name, description, photo, cost, available;
    TextView tvName, tvDescription, tvAvailable, tvCost;
    ImageView ivPhoto;
    Button btnBuy;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_product);
        //возвращаем данные, полученные с предыдущего активити
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        photo = intent.getStringExtra("photo");
        cost = intent.getStringExtra("cost");
        //запрос к БД на получение недостающей информации о продукте
        ConnDB connDB = new ConnDB();
        procc_answer(connDB.downloadDatas(2, id));
        //когда получили все данные - выводим их на экран
        tvName = (TextView) findViewById(R.id.tvNameProduct);
        tvDescription = (TextView) findViewById(R.id.tvDescriptionproduct);
        tvAvailable = (TextView) findViewById(R.id.tvisAvailable);
        tvCost = (TextView) findViewById(R.id.tvCostProduct);
        ivPhoto = (ImageView) findViewById(R.id.ivPhotoProduct);
        btnBuy = (Button) findViewById(R.id.btnBuy);

        tvName.setText(name);
        tvDescription.setText(description);

        Log.i("AboutProduct", "available - " + available);
        if (available.equals("1")){
            tvAvailable.setText("Есть в наличии");
        }else{
            tvAvailable.setText("Нет в наличии");
        }
        tvCost.setText(cost + "р.");

        //загружаем картинку продукта
        Picasso.get().load("http://10.0.2.2//my%20portable%20files/image/"+photo)
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .error(R.drawable.ic_cloud_off_black_24dp)
                .into(ivPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error download image", e.getStackTrace().toString());
                        e.printStackTrace();
                        ;                    }
                });
        //обработчик нажатия
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //переходим на окно офомления заказа
                newActivity();
            }
        };

        btnBuy.setOnClickListener(oclBtnOk);



    }
    //создаем активити оформления заказа и передаем туда необходимые данные
    public void newActivity(){
        Intent intent = new Intent(this, DoOrder.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("cost", cost);
        startActivity(intent);
    }
    //обрабатываем ответ БД
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
                                + jo.getString("description")
                                + " " + jo.getString("available"));
                description = jo.getString("description");
                available = jo.getString("available");
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
