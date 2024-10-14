package org.example.model.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.model.Item;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem {
    private int id;
    private int billId;
    private Item item;
    private int quantity;

    public MenuItem(int billId, Item item) {
        this.billId = billId;
        this.item = item;
        this.quantity = 1;
    }
}
