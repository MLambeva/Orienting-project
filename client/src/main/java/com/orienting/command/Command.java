package com.orienting.command;

public class Command implements Action {
    private Runnable function;

    public Runnable getFunction() {
        return function;
    }

    public void setFunction(Runnable function) {
        this.function = function;
    }

    public Command() {
    }

    public Command(Runnable function) {
        this.function = function;
    }

    @Override
    public void doAction() {
        function.run();
    }
}
