package org.example.view;

import org.example.controller.DrinkController;
import org.example.model.drink.Alcohol;
import org.example.model.drink.Drink;
import org.example.model.drink.SoftDrink;

import javax.naming.directory.InvalidAttributesException;
import java.io.Console;
import java.util.Map;

public class DrinkMenuView {
    DrinkController drinkController = DrinkController.getDrinkController();
    private final Console console = System.console();

    /**
     * Show a list of Drink into screen
     */
    public void showDrinkMenu() {
        Map<Integer, SoftDrink> softDrinkMap = drinkController.getDrinkMenu().getSoftDrinks();
        Map<Integer, Alcohol> alcoholMap = drinkController.getDrinkMenu().getAlcohols();

        String fmt = "\t\t %1$2s | %2$20s | %3$20s | %4$20s | %5$20s |%n";
        System.out.println("\t\t DRINK MENU");
        console.format(fmt, "ID", "Name", "Description", "Price", "Type");
        for(Integer i : softDrinkMap.keySet()) {
            Drink drink = softDrinkMap.get(i);
            console.format(fmt, drink.getId(), drink.getName(), drink.getDescription(), drink.getPrice(), drink.getClass().getSimpleName());
        }

        for(Integer i : alcoholMap.keySet()) {
            Drink drink = alcoholMap.get(i);
            console.format(fmt, drink.getId(), drink.getName(), drink.getDescription(), drink.getPrice(), drink.getClass().getSimpleName());
        }
    }

    /**
     * Show the steps for collecting information of the new Drink then save
     */
    public void addNewDrink() {
        try {
            int id;
            String name;
            String description;
            String img;
            double price;
            int type;

            System.out.print("\t\t\t ID: ");
            id = Integer.parseInt(console.readLine());

            System.out.print("\t\t\t Name: ");
            name = console.readLine();

            System.out.print("\t\t\t Description: ");
            description = console.readLine();

            System.out.print("\t\t\t Image: ");
            img = console.readLine();

            System.out.print("\t\t\t Price: ");
            price = Double.parseDouble(console.readLine());
            System.out.print("\t\t\t Type (1 for Soft Drink, 2 for Alcohol): ");
            type = Integer.parseInt(console.readLine());

            Drink newDrink;
            if(type == 1)
                newDrink = new SoftDrink(id, name, description, img, price);
            else if (type == 2)
                newDrink = new Alcohol(id, name, description, img, price);
            else
                throw new InvalidAttributesException("Type must be 1 (SoftDrink) or 2 (Alcohol)");

            drinkController.save(newDrink);
        } catch (NumberFormatException e) {
            System.out.println("Wrong format input for number fields");
        } catch (InvalidAttributesException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Show the steps for choosing an Drink's ID of existing Drink then update new information
     */
    public void updateDrink() {
        showDrinkMenu();
        try {
            System.out.print("\t\t Choose an ID to update: ");
            int id = Integer.parseInt(console.readLine());
            Drink drink = drinkController.findById(id);

            if (drink == null) {
                System.out.println("\t\t NOT FOUND!");
                return;
            }

            String fmt = "\t\t %1$2s | %2$20s | %3$20s | %4$20s | %5$20s |%n";
            console.format(fmt, "ID", "Name", "Description", "Price", "Type");
            console.format(fmt, drink.getId(), drink.getName(), drink.getDescription(), drink.getPrice(), drink.getClass().getSimpleName());

            System.out.print("\t\t\t Name: ");
            String name = console.readLine();
            name = name.trim().isEmpty() ? drink.getName() : name;

            System.out.print("\t\t\t Description: ");
            String description = console.readLine();
            description = description.trim().isEmpty() ? drink.getDescription() : description;

            System.out.print("\t\t\t Image: ");
            String img = console.readLine();
            img = img.trim().isEmpty() ? drink.getImg() : img;

            System.out.print("\t\t\t Price: ");
            String strPrice = console.readLine();
            double price = strPrice.trim().isEmpty() ? drink.getPrice() : Double.parseDouble(strPrice.trim());

            drink.setName(name);
            drink.setDescription(description);
            drink.setImg(img);
            drink.setPrice(price);

            drinkController.save(drink);
        } catch (NumberFormatException e) {
            System.out.println("Wrong format input for number fields");
        }
    }

    /**
     * Show the steps for choosing an existing Drink's ID to delete
     */
    public void deleteDrink() {
        showDrinkMenu();
        try {
            System.out.print("\t\t Choose an ID to delete: ");
            int id = Integer.parseInt(console.readLine());
            Drink drink = drinkController.findById(id);

            if (drink == null) {
                System.out.println("\t\t Not found!");
                return;
            }

            System.out.print("\t\t (0. Back, 1. Delete): ");
            int option = Integer.parseInt(console.readLine());
            if (option == 1) drinkController.delete(drink);

        } catch (NumberFormatException e) {
            System.out.println("Wrong format input for number fields");
        }
    }

    /**
     * Show a list of actions can be done in DrinkMenuView
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
                    addNewDrink();
                    break;
                case "2": // update
                    updateDrink();
                    break;
                case "3": // delete
                    deleteDrink();
                    break;
                case "4": // show
                    showDrinkMenu();
                    break;
                case "5":
                    option = "-1";
                    break;
            }
        } while (!option.equals("-1"));
    }
}
