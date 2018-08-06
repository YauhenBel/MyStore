package com.example.genya.mystore.objects;

//Объект, хранящий информацию о категорииях продуктов

public class sections {
    public String name, id;

    public sections(String _id, String _name){
        id = _id;
        name = _name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
