package org.example.controller;

import org.example.model.drink.Alcohol;
import org.example.model.drink.Drink;
import org.example.model.drink.SoftDrink;
import org.example.model.menu.DrinkMenu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class DrinkController {
    private final String filePath = "data/drinks.txt";
    private static DrinkController drinkController;

    public static DrinkController getDrinkController() {
        if (drinkController == null) drinkController = new DrinkController();
        return drinkController;
    }

    public DrinkMenu getDrinkMenu() {
        return loadDrinkMenuFromFile();
    }

    /**
     * Load drink data from .txt file
     */
    private DrinkMenu loadDrinkMenuFromFile() {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String newLine;
            Map<Integer, SoftDrink> softDrinks = new HashMap<>();
            Map<Integer, Alcohol> alcohol = new HashMap<>();
            while ((newLine = br.readLine()) != null) {
                String[] content = newLine.split(",");

                int id = Integer.parseInt(content[0]);
                String name = content[1];
                String description = content[2];
                String img = content[3];
                double price = Double.parseDouble(content[4]);
                String type = content[5];

                if (type.equals("Alcohol")) alcohol.put(id, new Alcohol(id, name, description, img, price));
                else softDrinks.put(id, new SoftDrink(id, name, description, img, price));
            }
            br.close();
            return new DrinkMenu(softDrinks, alcohol);
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " NOT FOUND");
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
        return new DrinkMenu();
    }

    /**
     * Save run-time drink data to .txt file
     */
    public void saveToFile(DrinkMenu drinkMenu) {
        Map<Integer, SoftDrink> softDrinkMap = drinkMenu.getSoftDrinks();
        Map<Integer, Alcohol> alcoholMap = drinkMenu.getAlcohols();

        String pattern = "{0},{1},{2},{3},{4},{5}\n";
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Integer i : softDrinkMap.keySet()) {
                Drink sd = softDrinkMap.get(i);
                bw.write(MessageFormat.format(pattern, sd.getId(), sd.getName(), sd.getDescription(), sd.getImg(), sd.getPrice(), sd.getClass().getSimpleName()));
            }
            for (Integer i : alcoholMap.keySet()) {
                Alcohol al = alcoholMap.get(i);
                bw.write(MessageFormat.format(pattern, al.getId(), al.getName(), al.getDescription(), al.getImg(), al.getPrice(), al.getClass().getSimpleName()));
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
    }

    /**
     * Create a new Drink in DrinkMenu then save
     */
    public void save(Drink drink) {
        DrinkMenu drinkMenu = loadDrinkMenuFromFile();

        if (drink.getClass().getSimpleName().equals("SoftDrink"))
            drinkMenu.getSoftDrinks().put(drink.getId(), (SoftDrink) drink);
        else
            drinkMenu.getAlcohols().put(drink.getId(), (Alcohol) drink);

        saveToFile(drinkMenu);
    }

    /**
     * Delete a Drink from DrinkMenu
     */
    public void delete(Drink drink) {
        DrinkMenu drinkMenu = loadDrinkMenuFromFile();

        if (drink.getClass().getSimpleName().equals("SoftDrink"))
            drinkMenu.getSoftDrinks().remove(drink.getId());
        else
            drinkMenu.getAlcohols().remove(drink.getId());

        saveToFile(drinkMenu);
    }

    /**
     * Find a Drink by id
     */
    public Drink findById(int id) {
        DrinkMenu drinkMenu = loadDrinkMenuFromFile();

        Drink drink = drinkMenu.getSoftDrinks().get(id);
        if (drink == null) return drinkMenu.getAlcohols().get(id);
        return drink;
    }
}
