package com.example.genya.mystore.controllers;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.genya.mystore.R;

public class DoOrder extends AppCompatActivity {

    String id, name, cost;
    TextView tvName, tvCost;
    EditText etName, etSurname, etPhone, etAddres;
    Button sendOrder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_order);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        cost = intent.getStringExtra("cost");

        tvName = (TextView) findViewById(R.id.tvNameOrderProduct);
        tvCost = (TextView) findViewById(R.id.tvCostOrderProduct);

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etAddres = (EditText) findViewById(R.id.etAddress);

        sendOrder = (Button) findViewById(R.id.btnSendOrder);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnDB connDB = new ConnDB();
                Boolean bool;
                bool = connDB.sendOrder(3, id, etName.getText().toString(), etSurname.getText().toString(),
                        etPhone.getText().toString(), etAddres.getText().toString());
                if (bool){
                    Log.i("DoOrder","Заказ оформлен");
                    Toast.makeText(getApplicationContext(),
                            "Заказ успешно оформлен", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i("DoOrder","Ошибка оформления заказа");
                    Toast.makeText(getApplicationContext(),
                            "Ошибка", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        };

        sendOrder.setOnClickListener(oclBtnOk);

        tvName.setText(name);
        tvCost.setText(cost + "р.");
    }


}
