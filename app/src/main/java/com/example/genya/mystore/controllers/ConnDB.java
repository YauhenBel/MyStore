package com.example.genya.mystore.controllers;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

//Класс для подключения к БД

public class ConnDB {
    //переменная, определяющая какой из запросов к БД будет выполнен
    Integer num;
    //Адрес для подключения к локальной БД
    //для того, что бы это работало на эмуляторе android - нужен ip =10.0.2.2, аналог 127.0.0.1
    String server_name = "http://10.0.2.2/scripts";
    String id, name, surname, phone, address;
    private HttpURLConnection conn;

    //загружаем список категорий продуктов
    public JSONArray downloadDatas(Integer _num)
    {   //здесь num = 0;
        num = _num;
        GetDatas getDatas = new GetDatas();
        getDatas.execute();
        try {
            return getDatas.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    //загружаем информацию о продуктах/продукте
    public JSONArray downloadDatas(Integer _num, String _id)
    {   // здесь num = 1/2
        num = _num;
        id = _id;
        GetDatas getDatas = new GetDatas();
        getDatas.execute();
        try {
            return getDatas.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    //отправляем заказ на сервер
    public Boolean sendOrder(String _id, String _name, String _surname, String _phone, String _address){
        id = _id;
        name = _name;
        surname = _surname;
        phone = _phone;
        address = _address;
        name = name.replace(" ", "%20");
        surname = surname.replace(" ", "%20");
        phone = phone.replace(" ", "%20");
        address = address.replace(" ", "%20");

        SendOrder sendOrder = new SendOrder();
        sendOrder.execute();
        try {
            if (sendOrder.get() == 1){
            return true;}else{
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }


    private class GetDatas extends AsyncTask<Object, Object, JSONArray>
    {
        String ansver;
        JSONArray ja;

        @Override
        protected JSONArray doInBackground(Object... voids) {
            try {
                String input = "";
                if (num == 0)
                {
                    //запрос на получение списка категрий продуктов
                    //запрос для активити MainActivity
                    input = server_name
                            + "/store.php?action=select";
                }
                if (num == 1)
                {
                    //запрос на получение списка продуктов определенной категрии
                    //запрос для активити ListProducts
                    input = server_name
                            + "/store.php?action=getproducts&section=" + id;
                }

                if (num == 2)
                {
                    //запрос на получения информации о продукте
                    //запрос для активити AboutProduct
                    input = server_name
                            + "/store.php?action=getproduct&product=" + id;
                }

                Log.i("ConnDB",
                        "+ ChatActivity - send request on the server "
                                + input);
                URL url = new URL(input);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setDoInput(true);
                conn.connect();
                Integer res = conn.getResponseCode();
                Log.i("ConnDB", "+ MainActivity - answer from server (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("ConnDB",
                        "+ MainActivity - answer from server ERROR: "
                                + e.getMessage());
            }
            try {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                Log.i("ConnDB", "+ FoneService - Full answer from server:\n"
                        + sb.toString());
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("["), ansver.indexOf("]") + 1);

                Log.i("ConnDB", "+ FoneService answer: " + ansver);

                is.close();
                br.close();
            }
            catch (Exception e) {
                Log.i("ConnDB", "+ FoneService error: " + e.getMessage());
            }
            finally {
                conn.disconnect();
            }
            // запишем ответ в БД ---------------------------------->
            if (ansver != null && !ansver.trim().equals("")) {
                Log.i("ConnDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                try {
                    // ответ превратим в JSON массив
                    if (num == 0 || num == 1 || num == 2)
                    {
                        ja = new JSONArray(ansver);
                    }
                }
                catch (Exception e) {
                    Log.i("ConnDB",
                            "+ ConnDB ---------- server response error:\n"
                                    + e.getMessage());
                }
            }

            if (num == 0 || num == 1 || num == 2)
            {

                return ja;
            }
            return null;

        }
    }

    private class SendOrder extends AsyncTask<Void, Void, Integer>
    {
        Integer res;
        String ansver;
        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                String input = "";
                    //запрос для активити DoOrder
                    input = server_name
                            + "/store.php?action=setorder&id=" + id
                            +"&name="
                            + name
                            +"&surname="
                            + surname
                            +"&phone="
                            + phone
                            +"&address="
                            + address;



                Log.i("ConnDB",
                        "+ ChatActivity - send request on the server "
                                + input);
                URL url = new URL(input);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setDoInput(true);
                conn.connect();
                res = conn.getResponseCode();
                Log.i("ConnDB", "+ MainActivity - answer from server (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("ConnDB",
                        "+ MainActivity - answer from server ERROR: "
                                + e.getMessage());
            }
            try {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                Log.i("ConnDB", "+ FoneService - Full answer from server:\n"
                        + sb.toString());
                ansver = sb.toString();

                Log.i("ConnDB", "+ FoneService answer: " + ansver);

                is.close();
                br.close();
            }
            catch (Exception e) {
                Log.i("ConnDB", "+ FoneService error: " + e.getMessage());
            }
            finally {
                conn.disconnect();
            }
            return Integer.parseInt(ansver);

        }
    }
}
