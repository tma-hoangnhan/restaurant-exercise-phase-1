package org.example.view;

import java.io.Console;

public class ManageMenuView {
    public void show() {
        String option;

        Console console = System.console();
        do {
            System.out.println("\t____________________");
            System.out.println("\t|1. Drink Menu     |");
            System.out.println("\t|2. Food Menu      |");
            System.out.println("\t|3. Back           |");
            System.out.println("\t|__________________|");
            System.out.print("\t --> Enter your choice: ");
            option = console.readLine();

            switch (option) {
                case "1":
                    DrinkMenuView drinkMenuView = new DrinkMenuView();
                    drinkMenuView.show();
                    break;
                case "2": //Food Menu
                    FoodMenuView foodMenuView = new FoodMenuView();
                    foodMenuView.show();
                    break;
                case "3":
                    option = "-1";
                    break;
            }
        } while(!option.equals("-1"));
    }
}
