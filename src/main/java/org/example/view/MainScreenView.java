package org.example.view;

import java.io.Console;

public class MainScreenView {
    /**
     * Show a list of action can be done in MainScreenView
     */
    public void show() {
        String option;

        Console console = System.console();
        do {
            System.out.println("____________________");
            System.out.println("|1. Manage menu     |");
            System.out.println("|2. Manage bill     |");
            System.out.println("|3. Exit            |");
            System.out.println("|___________________|");
            System.out.print("--> Enter your choice: ");
            option = console.readLine();

            switch (option) {
                case "1":
                    ManageMenuView manageMenuView = new ManageMenuView();
                    manageMenuView.show();
                    break;
                case "2": // Manage bill
                    ManageBillView manageBillView = new ManageBillView();
                    manageBillView.show();
                    break;
                case "3":
                    option = "-1";
                    break;
            }
        } while (!option.equals("-1"));
    }
}
