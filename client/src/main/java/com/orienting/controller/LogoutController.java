package com.orienting.controller;

import com.orienting.context.UserContext;

public class LogoutController extends MainController{
    private final String LOGOUT_URL = "http://localhost:8080/api/tokens";

    public void logout() {
        System.out.println(makeRequest(null, LOGOUT_URL + "/logout", "POST", ""));
        UserContext.restart();
    }
}
