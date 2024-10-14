package org.example.controller;

import org.example.model.Item;
import org.example.model.bill.Bill;
import org.example.model.bill.MenuItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class MenuItemController {
    private final String filePath = "data/menu_items.txt";
    private static MenuItemController menuItemController;

    public static MenuItemController getMenuItemController() {
        if (menuItemController == null) menuItemController = new MenuItemController();
        return menuItemController;
    }

    /**
     * Load all MenuItem then assign it respectively to specific Bill
     */
    public void loadMenuItemFromFile(Map<Integer, Bill> billMap) {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String newLine;

            while ((newLine = br.readLine()) != null) {
                String[] content = newLine.split(",");
                int id = Integer.parseInt(content[0]);
                int billID = Integer.parseInt(content[1]);
                int itemId = Integer.parseInt(content[2]);
                String type = content[3];
                int quantity = Integer.parseInt(content[4]);

                Item item;
                if (type.equals("SoftDrink") || type.equals("Alcohol"))
                    item = DrinkController.getDrinkController().findById(itemId);
                else
                    item = FoodController.getFoodController().findById(id);

                Bill bill = billMap.get(billID);
                bill.getMenuItems().put(id, new MenuItem(id, billID, item, quantity));
                billMap.put(billID, bill);
            }
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " NOT FOUND");
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
    }

    /**
     * Load all MenuItem from .txt file
     * @return Map of MenuItem
     */
    public Map<Integer, MenuItem> loadMenuItemFromFile() {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String newLine;

            Map<Integer, MenuItem> menuItemMap = new HashMap<>();
            while ((newLine = br.readLine()) != null) {
                String[] content = newLine.split(",");
                int id = Integer.parseInt(content[0]);
                int billID = Integer.parseInt(content[1]);
                int itemId = Integer.parseInt(content[2]);
                String type = content[3];
                int quantity = Integer.parseInt(content[4]);

                Item item;
                if (type.equals("SoftDrink") || type.equals("Alcohol"))
                    item = DrinkController.getDrinkController().findById(itemId);
                else
                    item = FoodController.getFoodController().findById(id);

                menuItemMap.put(id, new MenuItem(id, billID, item, quantity));
            }
            return menuItemMap;
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " NOT FOUND");
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
        return new HashMap<>();
    }

    /**
     * Save all current MenuItem data into .txt file
     * @param menuItemMap MenuItem data
     */
    public void saveToFile(Map<Integer, MenuItem> menuItemMap) {
        String pattern = "{0},{1},{2},{3},{4}\n";
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.TRUNCATE_EXISTING)) {
            for (MenuItem mi : menuItemMap.values()) {
                bw.write(MessageFormat.format(pattern, mi.getId(), mi.getBillId(), mi.getItem().getId(), mi.getItem().getClass().getSimpleName(), mi.getQuantity()));
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
    }

    /**
     * Generate an ID for new MenuItem by finding max ID from Map then plus 1
     */
    int generateMenuItemId(Map<Integer, MenuItem> menuItemMap) {
        int max = 0;
        for (Integer key : menuItemMap.keySet()) {
            if (key > max)
                max = key;
        }
        return max + 1;
    }

    /**
     * Save new MenuItem added into a bill.
     */
    public void save(MenuItem newMenuItem) {
        Map<Integer, MenuItem> dbMenuItems = loadMenuItemFromFile();

        boolean isAdded = false;
        // If an Item has already existed in a Bill, then increasing quantity by 1
        for (MenuItem mi : dbMenuItems.values()) {
            if (mi.getItem().getId() == newMenuItem.getItem().getId() && mi.getBillId() == newMenuItem.getBillId()) {
                mi.setQuantity(mi.getQuantity() + 1);
                isAdded = true;
            }
        }

        // else add a new Item into a Bill
        if (!isAdded) {
            newMenuItem.setId(generateMenuItemId(loadMenuItemFromFile()));
            dbMenuItems.put(newMenuItem.getId(), newMenuItem);
        }
        saveToFile(dbMenuItems);
    }

    /**
     * Update the quantity of MenuItem in a Bill
     */
    public void update(MenuItem menuItem) {
        Map<Integer, MenuItem> menuItemMap = loadMenuItemFromFile();

        // If the updated quantity < 1, then deleting the Item out of the Bill
        // Else update the new quantity
        if (menuItem.getQuantity() < 1)
            menuItemMap.remove(menuItem.getId());
        else
            menuItemMap.put(menuItem.getId(), menuItem);
        saveToFile(menuItemMap);
    }

    /**
     * Find a MenuItem by its ID
     */
    public MenuItem findMenuItemById(int id) {
        Map<Integer, MenuItem> menuItemMap = loadMenuItemFromFile();
        return menuItemMap.get(id);
    }

    /**
     * Get a Map of MenuItems included in a Bill by billId
     */
    public Map<Integer, MenuItem> getListMenuItemsByBillId(int billId) {
        Map<Integer, MenuItem> dbMenuItems = loadMenuItemFromFile();
        Map<Integer, MenuItem> menuItemsInBill = new HashMap<>();
        for (MenuItem mi : dbMenuItems.values()) {
            if (mi.getBillId() == billId)
                menuItemsInBill.put(mi.getId(), mi);
        }
        return  menuItemsInBill;
    }
}
