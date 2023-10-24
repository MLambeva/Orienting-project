package com.orienting.menu;

import com.orienting.command.Action;
import com.orienting.common.enums.UserRole;

import java.util.List;

public class MenuEntry {
    private String name;
    private Action action;
    private List<UserRole> accessRoles;

    public MenuEntry() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<UserRole> getAccessRoles() {
        return accessRoles;
    }

    public void setAccessRoles(List<UserRole> accessRoles) {
        this.accessRoles = accessRoles;
    }

    public MenuEntry(String name, Action action, List<UserRole> accessRoles) {
        this.name = name;
        this.action = action;
        this.accessRoles = accessRoles;
    }
}
