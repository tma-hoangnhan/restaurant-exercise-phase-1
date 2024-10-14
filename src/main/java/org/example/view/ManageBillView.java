package org.example.view;

import org.example.controller.BillController;
import org.example.controller.DrinkController;
import org.example.controller.FoodController;
import org.example.controller.MenuItemController;
import org.example.model.Item;
import org.example.model.bill.Bill;
import org.example.model.bill.MenuItem;

import java.io.Console;
import java.util.Map;
import java.util.Objects;

public class ManageBillView {
    Console console = System.console();
    BillController billController = BillController.getBillController();
    MenuItemController menuItemController = MenuItemController.getMenuItemController();


    public void show() {
        String option;

        do {
            System.out.println("\t____________________");
            System.out.println("\t|1. Add New Bill    |");
            System.out.println("\t|2. Update Bill     |");
            System.out.println("\t|3. Delete Bill     |");
            System.out.println("\t|4. Show All Bills  |");
            System.out.println("\t|5. Back            |");
            System.out.println("\t|___________________|");
            System.out.print("\t --> Enter your choice: ");
            option = console.readLine();

            switch (option) {
                case "1": // Add
                    addNewBill();
                    break;
                case "2": // Update
                    updateBill();
                    break;
                case "3": // Delete
                    deleteBill();
                    break;
                case "4": // Show
                    showAllBills();
                    break;
                case "5":
                    option = "-1";
                    break;
            }
        } while (!Objects.equals(option, "-1"));
    }

    void addNewBill() {
        billController.save();
    }

    void deleteBill() {
        try {
            System.out.print("\t\t Enter an ID to delete: ");
            int billId = Integer.parseInt(console.readLine());
            billController.delete(billId);

        } catch (NumberFormatException e) {
            System.out.println("\t\t Wrong input format for number fields");
        }
    }

    void showAllBills() {
        Map<Integer, Bill> billMap = billController.loadBillsFromFile();

        String fmt = "\t\t %1$2s | %2$20s |%n";
        console.format(fmt, "ID", "Ordered Date");
        for (Integer key : billMap.keySet()) {
            Bill bill = billMap.get(key);
            console.format(fmt, bill.getId(), bill.getOrderedTime());
        }
    }

    int showBillDetailById() {
        try {
            System.out.print("\t\t\t Enter an ID: ");
            int id = Integer.parseInt(console.readLine());

            Bill bill = billController.findById(id);
            if (bill == null) {
                System.out.println("\t\t\t NOT FOUND!");
                return -1;
            }

            double totalPrice = 0;

            String fmt = "\t\t\t %1$2s | %2$20s | %3$20s | %4$20s |%n";
            console.format(fmt, "ID", "Bill ID", "Item", "Quantity");
            for (MenuItem menuItem : bill.getMenuItems().values()) {
                console.format(fmt, menuItem.getId(), menuItem.getBillId(), menuItem.getItem().getName(), menuItem.getQuantity());
                totalPrice += menuItem.getQuantity() * menuItem.getItem().getPrice();
            }
            System.out.println("\t\t\t Total Price: " + totalPrice);
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format for number fields");
        }
        return -1;
    }

    void updateBill() {
        showAllBills();
        int billId = showBillDetailById();

        String option;
        do {
            System.out.println("\t\t\t ____________________");
            System.out.println("\t\t\t |1. Add New Item   |");
            System.out.println("\t\t\t |2. Update         |");
            System.out.println("\t\t\t |3. Back           |");
            System.out.println("\t\t\t |__________________|");
            System.out.print("\t\t\t --> Enter your choice: ");
            option = console.readLine();

            switch (option) {
                case "1": //Add
                    addMenuItems(billId);
                    break;
                case "2": //Update
                    updateMenuItem();
                    break;
                case "3":
                    option = "-1";
                    break;
            }

        } while (!option.equals("-1"));
    }

    void addMenuItems(int billId) {
        if (billId == -1) return;

        try {
            String option;
            Console console = System.console();
            do {
                System.out.println("\t\t\t\t____________________");
                System.out.println("\t\t\t\t|1. Drink Menu     |");
                System.out.println("\t\t\t\t|2. Food Menu      |");
                System.out.println("\t\t\t\t|3. Back           |");
                System.out.println("\t\t\t\t|__________________|");
                System.out.print("\t\t\t\t --> Enter your choice: ");
                option = console.readLine();

                switch (option) {
                    case "1":
                        DrinkMenuView drinkMenuView = new DrinkMenuView();
                        drinkMenuView.showDrinkMenu();
                        addDrink(billId);
                        break;
                    case "2": //Food Menu
                        FoodMenuView foodMenuView = new FoodMenuView();
                        foodMenuView.showFoodMenu();
                        addFood(billId);
                        break;
                    case "3":
                        option = "-1";
                        break;
                }
            } while(!option.equals("-1"));
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format for number fields");
        }
    }

    void addDrink(int billId) {
        System.out.print("\t\t\t\t Enter an ID to add: ");
        int itemId = Integer.parseInt(console.readLine());
        Item item = DrinkController.getDrinkController().findById(itemId);

        menuItemController.save(new MenuItem(billId, item));
    }

    void addFood(int billId) {
        System.out.print("\t\t\t\t Enter an ID to add: ");
        int itemId = Integer.parseInt(console.readLine());
        Item item = FoodController.getFoodController().findById(itemId);

        menuItemController.save(new MenuItem(billId, item));
    }

    void updateMenuItem() {
        try {
            System.out.print("\t\t\t Enter an ID: ");
            int id = Integer.parseInt(console.readLine());

            MenuItem menuItem = menuItemController.findMenuItemById(id);
            if (menuItem == null) {
                System.out.println("\t\t\t NOT FOUND!");
                return;
            }

            String fmt = "\t\t\t %1$2s | %2$20s | %3$20s | %4$20s |%n";
            console.format(fmt, "ID", "Bill ID", "Item", "Quantity");
            console.format(fmt, menuItem.getId(), menuItem.getBillId(), menuItem.getItem().getName(), menuItem.getQuantity());

            System.out.print("\t\t\t Enter quantity (0 for deleting out of bill): ");
            int newQuantity = Integer.parseInt(console.readLine());
            menuItem.setQuantity(newQuantity);

            menuItemController.update(menuItem);

        } catch (NumberFormatException e) {
            System.out.println("Wrong input format for number fields");
        }
    }
}
