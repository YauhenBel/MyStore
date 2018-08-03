package com.example.genya.mystore.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genya.mystore.R;
import com.example.genya.mystore.objects.products;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    ArrayList<products> objects;

    public ProductAdapter(Context context, ArrayList<products> products){
        ctx = context;
        objects = products;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.template_list_products, parent, false);
        }
        products p = getProducts(position);

        ((TextView) view.findViewById(R.id.nameProducts)).setText(p.name);
        ((TextView) view.findViewById(R.id.costProducts)).setText(p.cost + "р.");
        ImageView imageView = (ImageView) view.findViewById(R.id.ivProducts);
        Log.d("Link download image", "http://10.0.2.2//my%20portable%20files/image/"+p.photo);
        Picasso.get().load("http://10.0.2.2//my%20portable%20files/image/"+p.photo)
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .error(R.drawable.ic_cloud_off_black_24dp)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error download image", e.getStackTrace().toString());
                        e.printStackTrace();
                        ;                    }
                });
        return view;
    }

    private products getProducts(int position) {
        Log.i("prof_adapter", "товар по позиции");
        return ((products) getItem(position));
    }
}
