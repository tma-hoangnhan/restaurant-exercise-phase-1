package org.example.controller;

import org.example.model.bill.Bill;
import org.example.model.bill.MenuItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BillController {
    private final String filePath = "data/bills.txt";
    MenuItemController menuItemController = MenuItemController.getMenuItemController();

    private static BillController billController;

    public static BillController getBillController() {
        if (billController == null) billController = new BillController();
        return billController;
    }

    /**
     * Load bill data from .txt file
     */
    public Map<Integer, Bill> loadBillsFromFile() {

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String newLine;

            Map<Integer, Bill> bills = new HashMap<>();
            while ((newLine = br.readLine()) != null) {
                String[] content = newLine.split(",");
                int id = Integer.parseInt(content[0]);
                String strDate = content[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate orderedTime = LocalDate.parse(strDate, formatter);
                bills.put(id, new Bill(id, orderedTime));
            }
            menuItemController.loadMenuItemFromFile(bills);
            return bills;
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " NOT FOUND");
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
         return new HashMap<>();
    }

    /**
     * Write current bill data to .txt file
     */
    public void saveToFile(Map<Integer, Bill> billMap) {
        String pattern = "{0},{1}\n";
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Integer i : billMap.keySet()) {
                Bill bill = billMap.get(i);
                bw.write(MessageFormat.format(pattern, bill.getId(), bill.getOrderedTime()));
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("I/O error occurred");
        }
    }

    int generateId(Map<Integer, Bill> billMap) {
        int max = 0;
        for (Integer key : billMap.keySet()) {
            if (key > max)
                max = key;
        }
        return max + 1;
    }

    /**
     * Create a new bill then saving to file
     */
    public void save() {
        Map<Integer, Bill> billMap = loadBillsFromFile();
        int nextID = generateId(billMap);
        billMap.put(nextID, new Bill(nextID, LocalDate.now()));
        saveToFile(billMap);
    }

    /**
     * Delete a bill by billId
     */
    public void delete(int billId) {
        // 1. Delete all MenuItem appearing in a bill
        Map<Integer, MenuItem> menuItemsInBill = menuItemController.getListMenuItemsByBillId(billId);
        Map<Integer, MenuItem> dbMenuItems = menuItemController.loadMenuItemFromFile();

        for (Integer key : menuItemsInBill.keySet()) {
            dbMenuItems.remove(key);
        }
        menuItemController.saveToFile(dbMenuItems);

        // 2. Then delete bill
        Map<Integer, Bill> billMap = loadBillsFromFile();
        billMap.remove(billId);
        saveToFile(billMap);
    }

    /**
     * Find a bill by billId
     */
    public Bill findById(int billId) {
        Map<Integer, Bill> billMap = loadBillsFromFile();
        Bill bill = billMap.get(billId);
        bill.setMenuItems(menuItemController.getListMenuItemsByBillId(billId));
        return billMap.get(billId);
    }
}
