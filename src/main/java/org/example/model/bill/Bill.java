package org.example.model.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Bill {
    private int id;
    private Map<Integer, MenuItem> menuItems;
    private LocalDate orderedTime;

    public Bill(int id, LocalDate orderedTime) {
        this.id = id;
        this.menuItems = new HashMap<>();
        this.orderedTime = orderedTime;
    }
}
