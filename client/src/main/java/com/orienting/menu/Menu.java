package com.orienting;

import java.awt.*;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private String name;
    private List<MenuEntry> entries;

    public Menu(String name, List<MenuEntry> entries) {
        this.name = name;
        this.entries = entries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<MenuEntry> entries) {
        this.entries = entries;
    }

    public void show() {
        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println(name);
        System.out.println("===============================");

        List<MenuEntry> permittedEntries = entries
                .stream()
                .filter(x -> x.getAccessRoles() == null || x.getAccessRoles().contains(UserContext.getRole()))
                .toList();
        for (int i = 0; i < permittedEntries.size(); i++) {
            System.out.printf("(%d) - %s\n", i+1, permittedEntries.get(i).getName());
        }

        int choice = -1;
        while (choice < 1 || choice > permittedEntries.size()) {
            try {
                System.out.print("Choose menu: ");
                choice = Integer.parseInt(sc.nextLine());

                if (choice < 1 || choice > permittedEntries.size()) {
                    System.out.printf("Your choice should be between %d and %d%n", 1, permittedEntries.size());
                } else {
                    permittedEntries.get(choice - 1).getAction().doAction();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
            }
        }
    }
}
