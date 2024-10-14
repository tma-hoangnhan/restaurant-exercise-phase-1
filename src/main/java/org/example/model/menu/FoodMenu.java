package org.example.model.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.food.Breakfast;
import org.example.model.food.Dinner;
import org.example.model.food.Lunch;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenu {
    private Map<Integer, Breakfast> breakfasts;
    private Map<Integer, Lunch> lunches;
    private Map<Integer, Dinner> dinners;
}
