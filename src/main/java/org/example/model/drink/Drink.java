package org.example.model.drink;

import org.example.model.Item;

public abstract class Drink extends Item {
    public Drink(int id, String name, String description, String img, double price) {
        super(id, name, description, img, price);
    }
}
