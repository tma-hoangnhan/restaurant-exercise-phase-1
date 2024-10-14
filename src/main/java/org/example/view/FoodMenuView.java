package org.example.view;

import org.example.controller.FoodController;
import org.example.model.food.Breakfast;
import org.example.model.food.Dinner;
import org.example.model.food.Food;
import org.example.model.food.Lunch;

import javax.naming.directory.InvalidAttributesException;
import java.io.Console;
import java.util.Map;

public class FoodMenuView {
    FoodController foodController = FoodController.getFoodController();
    Console console = System.console();

    /**
     * Show a list of actions can be done in FoodMenuView
     */
    public void show() {
        String option;

        do {
            System.out.println("\t\t ____________________");
            System.out.println("\t\t |1. Add            |");
            System.out.println("\t\t |2. Update         |");
            System.out.println("\t\t |3. Delete         |");
            System.out.println("\t\t |4. Show           |");
            System.out.println("\t\t |5. Back           |");
            System.out.println("\t\t |__________________|");
            System.out.print("\t\t --> Enter your choice: ");
            option = console.readLine();
            switch (option) {
                case "1": // create
                    addNewFood();
                    break;
                case "2": // update
                    updateFood();
                    break;
                case "3": // delete
                    deleteFood();
                    break;
                case "4":
                    showFoodMenu();
                    break;
                case "5":
                    option = "-1";
                    break;
            }
        } while (!option.equals("-1"));
    }

    /**
     * Show all Food included in FoodMenu
     */
    public void showFoodMenu() {
        Map<Integer, Breakfast> breakfastMap = foodController.getFoodMenu().getBreakfasts();
        Map<Integer, Lunch> lunchMap = foodController.getFoodMenu().getLunches();
        Map<Integer, Dinner> dinnerMap = foodController.getFoodMenu().getDinners();

        String fmt = "\t\t %1$2s | %2$20s | %3$20s | %4$20s | %5$20s |%n";
        System.out.println("\t\t Food Menu");
        console.format(fmt, "ID", "Name", "Description", "Price", "Type");
        for(Integer i : breakfastMap.keySet()) {
            Food food = breakfastMap.get(i);
            console.format(fmt, food.getId(), food.getName(), food.getDescription(), food.getPrice(), food.getClass().getSimpleName());
        }

        for(Integer i : lunchMap.keySet()) {
            Food food = lunchMap.get(i);
            console.format(fmt, food.getId(), food.getName(), food.getDescription(), food.getPrice(), food.getClass().getSimpleName());
        }

        for(Integer i : dinnerMap.keySet()) {
            Food food = dinnerMap.get(i);
            console.format(fmt, food.getId(), food.getName(), food.getDescription(), food.getPrice(), food.getClass().getSimpleName());
        }
    }

    /**
     * Show the steps for collecting information of a new Food to save
     */
    public void addNewFood() {
        try {
            System.out.print("\t\t\t ID: ");
            int id = Integer.parseInt(console.readLine());

            System.out.print("\t\t\t Name: ");
            String name = console.readLine();

            System.out.print("\t\t\t Description: ");
            String description = console.readLine();

            System.out.print("\t\t\t Image: ");
            String img = console.readLine();

            System.out.print("\t\t\t Price: ");
            double price = Double.parseDouble(console.readLine());

            System.out.print("\t\t\t Type(1 for Breakfast, 2 for Lunch or 3 for Dinner): ");
            int type = Integer.parseInt(console.readLine());

            Food newFood;
            if (type == 1)
                newFood = new Breakfast(id, name, description, img, price);
            else if (type == 2)
                newFood = new Lunch(id, name, description, img, price);
            else if (type == 3)
                newFood = new Dinner(id, name, description, img, price);
            else
                throw new InvalidAttributesException("Type must be 1 (Breakfast) or 2 (Lunch) or 3 (Dinner)");

            foodController.save(newFood);
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format for number fields");
        } catch (InvalidAttributesException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Show the steps for choosing Food's ID to update new information
     */
    public void updateFood() {
        showFoodMenu();

        System.out.print("\t\t Choose an ID to update: ");
        int id = Integer.parseInt(console.readLine());
        Food food = foodController.findById(id);

        if (food == null) {
            System.out.println("\t\t Not found!");
            return;
        }

        String fmt = "\t\t %1$2s | %2$20s | %3$20s | %4$20s | %5$20s |%n";
        console.format(fmt, "ID", "Name", "Description", "Price", "Type");
        console.format(fmt, food.getId(), food.getName(), food.getDescription(), food.getPrice(), food.getClass().getSimpleName());

        System.out.print("\t\t\t Name: ");
        String name = console.readLine();
        name = name.trim().isEmpty() ? food.getName() : name;

        System.out.print("\t\t\t Description: ");
        String description = console.readLine();
        description = description.trim().isEmpty() ? food.getDescription() : description;

        System.out.print("\t\t\t Image: ");
        String img = console.readLine();
        img = img.trim().isEmpty() ? food.getImg() : img;

        System.out.print("\t\t\t Price: ");
        String strPrice = console.readLine();
        double price = strPrice.trim().isEmpty() ? food.getPrice() : Double.parseDouble(strPrice.trim());

        food.setName(name);
        food.setDescription(description);
        food.setImg(img);
        food.setPrice(price);

        foodController.save(food);
    }

    /**
     * Show the steps for choosing Food's ID to delete
     */
    public void deleteFood() {
        showFoodMenu();
        try {
            System.out.print("\t\t Choose an ID to delete: ");
            int id = Integer.parseInt(console.readLine());
            Food food = foodController.findById(id);

            if (food == null) {
                System.out.println("\t\t Not found!");
                return;
            }

            System.out.print("\t\t (0. Back, 1. Delete): ");
            int option = Integer.parseInt(console.readLine());
            if (option == 1) foodController.delete(food);
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format for number fields");
        }
    }
}
