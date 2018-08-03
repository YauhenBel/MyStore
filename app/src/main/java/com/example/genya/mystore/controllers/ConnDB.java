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

public class ConnDB {

    Integer num;
    String server_name = "http://10.0.2.2/scripts";
    String id;
    private HttpURLConnection conn;

    public JSONArray downloadDatas(Integer _num)
    {
        num = _num;
        Input_profile input_profile = new Input_profile();
        input_profile.execute();
        try {
            return input_profile.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray downloadDatas(Integer _num, String _id)
    {
        num = _num;
        id = _id;
        Input_profile input_profile = new Input_profile();
        input_profile.execute();
        try {
            return input_profile.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class Input_profile extends AsyncTask<Object, Object, JSONArray>
    {
        String ansver;
        JSONArray ja;

        @Override
        protected JSONArray doInBackground(Object... voids) {
            try {
                String input = "";
                if (num == 0)
                {
                    //for class MainActivity
                    input = server_name
                            + "/store.php?action=select";
                }
                if (num == 1)
                {
                    //for class MainActivity
                    input = server_name
                            + "/store.php?action=getproducts&section=" + id;
                }

                if (num == 2)
                {
                    //for class MainActivity
                    input = server_name
                            + "/store.php?action=getproduct&product=" + id;
                }


                Log.i("chat",
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
                Log.i("chat", "+ MainActivity - answer from server (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("chat",
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
                Log.i("chat", "+ FoneService - Full answer from server:\n"
                        + sb.toString());
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("["), ansver.indexOf("]") + 1);

                Log.i("chat", "+ FoneService answer: " + ansver);

                is.close();
                br.close();
            }
            catch (Exception e) {
                Log.i("chat", "+ FoneService error: " + e.getMessage());
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
                    Log.i("chat",
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
}
