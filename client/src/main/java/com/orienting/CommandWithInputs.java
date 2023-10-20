package com.orienting;

import java.util.List;
import java.util.function.Consumer;

public class CommandWithInputs implements Action {
    private List<Argument> arguments;
    private Consumer<List<Argument>> function;

    public CommandWithInputs() {
    }

    public CommandWithInputs(List<Argument> arguments, Consumer<List<Argument>> function) {
        this.arguments = arguments;
        this.function = function;
    }

    @Override
    public void doAction() {
        arguments.forEach(Argument::enterValue);
        function.accept(arguments);
    }
}
