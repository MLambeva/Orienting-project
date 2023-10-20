package com.orienting;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private String name;
    private List<MenuEntry> entries;

    public Menu() {
    }

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
        System.out.println("==================================");

        List<MenuEntry> permittedEntries = entries
                .stream()
                .filter(x -> x.getAccessRoles() == null || x.getAccessRoles().contains(UserContext.getRole()))
                .toList();
        for (int i = 0; i < permittedEntries.size(); i++) {
            System.out.printf("(%d) - %s\n", i+1, permittedEntries.get(i).getName());
        }

        System.out.print("Choose menu: ");
        int choice = Integer.parseInt(sc.nextLine());

        if (choice >= 1 && choice <= permittedEntries.size()) {
            permittedEntries.get(choice - 1).getAction().doAction();
        }
        else {
            throw new RuntimeException(String.format("Your choice should be between %d and %d", 1, permittedEntries.size()));
        }

    }
}
