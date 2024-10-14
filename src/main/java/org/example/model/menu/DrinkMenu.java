package org.example.model.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.drink.Alcohol;
import org.example.model.drink.SoftDrink;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkMenu {
    private Map<Integer, SoftDrink> softDrinks;
    private Map<Integer, Alcohol> alcohols;
}
