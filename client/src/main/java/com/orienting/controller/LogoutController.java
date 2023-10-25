package com.orienting.controller;

import com.orienting.context.UserContext;

public class LogoutController extends MainController{
    public LogoutController(String url) {
        super(url);
    }

    public void logout() {
        System.out.println(makeRequest(null, url + "/logout", "POST", ""));
        UserContext.restart();
    }
}
