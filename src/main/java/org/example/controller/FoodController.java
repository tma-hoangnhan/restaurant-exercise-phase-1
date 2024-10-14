package org.example.controller;

import org.example.model.food.Breakfast;
import org.example.model.food.Dinner;
import org.example.model.food.Food;
import org.example.model.food.Lunch;
import org.example.model.menu.FoodMenu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FoodController {
    private final String filePath = "data/foods.txt";
    private static FoodController foodController;

    public static FoodController getFoodController() {
        if(foodController == null) foodController = new FoodController();
        return foodController;
    }

    public FoodMenu getFoodMenu() {
        return loadFoodMenuFromFile();
    }

    /**
     * Load FoodMenu data from .txt file
     */
    public FoodMenu loadFoodMenuFromFile() {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String newLine;
            Map<Integer, Breakfast> breakfastMap = new HashMap<>();
            Map<Integer, Lunch> lunchMap = new HashMap<>();
            Map<Integer, Dinner> dinnerMap = new HashMap<>();

            while ((newLine = br.readLine()) != null) {
                String[] content = newLine.split(",");
                int id = Integer.parseInt(content[0]);
                String name = content[1];
                String description = content[2];
                String img = content[3];
                double price = Double.parseDouble(content[4]);
                String type = content[5];

                if (type.equals("Breakfast"))
                    breakfastMap.put(id, new Breakfast(id, name, description, img, price));
                else if (type.equals("Lunch"))
                    lunchMap.put(id, new Lunch(id, name, description, img, price));
                else
                    dinnerMap.put(id, new Dinner(id, name, description, img, price));
            }
            br.close();
            return new FoodMenu(breakfastMap, lunchMap, dinnerMap);
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " NOT FOUND");
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
        return new FoodMenu();
    }

    /**
     * Save current FoodMenu data into .txt file
     */
    public void saveToFile(FoodMenu foodMenu) {
        Map<Integer, Breakfast> breakfastMap = foodMenu.getBreakfasts();
        Map<Integer, Lunch> lunchMap = foodMenu.getLunches();
        Map<Integer, Dinner> dinnerMap = foodMenu.getDinners();

        String pattern = "{0},{1},{2},{3},{4},{5}\n";
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Integer i : breakfastMap.keySet()) {
                Breakfast br = breakfastMap.get(i);
                bw.write(MessageFormat.format(pattern, br.getId(), br.getName(), br.getDescription(), br.getImg(), br.getPrice(), br.getClass().getSimpleName()));
            }
            for (Integer i : lunchMap.keySet()) {
                Lunch lu = lunchMap.get(i);
                bw.write(MessageFormat.format(pattern, lu.getId(), lu.getName(), lu.getDescription(), lu.getImg(), lu.getPrice(), lu.getClass().getSimpleName()));
            }
            for (Integer i : dinnerMap.keySet()) {
                Dinner di = dinnerMap.get(i);
                bw.write(MessageFormat.format(pattern, di.getId(), di.getName(), di.getDescription(), di.getImg(), di.getPrice(), di.getClass().getSimpleName()));
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
    }

    /**
     * Create a new Food then save
     */
    public void save(Food food) {
        FoodMenu foodMenu = loadFoodMenuFromFile();

        String type = food.getClass().getSimpleName();
        if (type.equals("Breakfast"))
            foodMenu.getBreakfasts().put(food.getId(), (Breakfast) food);
        else if (type.equals("Lunch"))
            foodMenu.getLunches().put(food.getId(), (Lunch) food);
        else
            foodMenu.getDinners().put(food.getId(), (Dinner) food);

        saveToFile(foodMenu);
    }

    /**
     * Delete a Food out of FoodMenu
     */
    public void delete(Food food) {
        FoodMenu foodMenu = loadFoodMenuFromFile();

        String type = food.getClass().getSimpleName();
        if (type.equals("Breakfast"))
            foodMenu.getBreakfasts().remove(food.getId());
        else if (type.equals("Lunch"))
            foodMenu.getLunches().remove(food.getId());
        else
            foodMenu.getDinners().remove(food.getId());

        saveToFile(foodMenu);
    }

    /**
     * Find Food by its ID
     */
    public Food findById(int id) {
        FoodMenu foodMenu = loadFoodMenuFromFile();

        Food food = foodMenu.getBreakfasts().get(id);
        if (food == null) food = foodMenu.getLunches().get(id);
        if (food == null) return foodMenu.getDinners().get(id);
        return food;
    }

}
