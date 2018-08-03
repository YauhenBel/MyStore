package com.example.genya.mystore.objects;

public class products {
    public String id, name, description, photo, cost, available,
    id_sectin;

    public products(String _id, String _name, String _description,
                    String _photo, String _cost,
                    String _available, String _id_sectin){
        id = _id;
        name = _name;
        description = _description;
        photo = _photo;
        cost = _cost;
        available = _available;
        id_sectin = _id_sectin;
    }
    public products(String _id, String _name,
                    String _photo, String _cost){
        id = _id;
        name = _name;
        photo = _photo;
        cost = _cost;
    }

    @Override
    public String toString() {
        return id + "&" + name + "&" + description + "&"
                +photo + "&" + cost
                + "&" + available + "&" + id_sectin;
    }
}
