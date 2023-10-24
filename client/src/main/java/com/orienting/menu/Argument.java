package com.orienting.menu;

import java.util.Scanner;

public class Argument {
    private String name;
    private String value;

    public Argument() {}
    public Argument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void enterValue() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter value for %s: ", name);
        String input = sc.nextLine();;
        this.value = input.replaceAll(" ", "%20");
    }
}
