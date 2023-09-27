package com.orienting.common.entity;

public enum UserRole {
    COACH,
    COMPETITOR,
    ADMIN;

    public static boolean isEqualIgnoreCase(UserRole role1, UserRole role2) {
        return role1 != null && role2 != null && role1.toString().equalsIgnoreCase(role2.toString());
    }
}
