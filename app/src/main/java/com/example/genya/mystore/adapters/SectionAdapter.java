package com.example.genya.mystore.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.genya.mystore.R;
import com.example.genya.mystore.objects.sections;

import java.util.ArrayList;

//Адаптер для заполнения списка категорий

public class SectionAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    ArrayList<sections> objects;

    public SectionAdapter(Context context, ArrayList<sections> sections){
        ctx= context;
        objects = sections;
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
            view = inflater.inflate(R.layout.templateforsections, parent, false);
        }
        sections p = getSection(position);

        ((TextView) view.findViewById(R.id.tvSections)).setText(p.name);


        return view;
    }

    private sections getSection(int position) {
        Log.i("prof_adapter", "товар по позиции");
        return ((sections) getItem(position));
    }
}
