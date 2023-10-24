package com.orienting.context;

import com.orienting.common.enums.UserRole;

public class UserContext {
    private static String token = "";
    private static String email;
    private static UserRole role;

    public static UserRole getRole() {
        return role;
    }

    public static void setRole(UserRole role) {
        UserContext.role = role;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserContext.token = token;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserContext.email = email;
    }

    public static void restart() {
        UserContext.email = "";
        UserContext.role = null;
        UserContext.token = "";
    }
}
