package com.adropofliquid.magiclinkauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { //home page controller
    //the view in resources/templates/index.html
    //it contains rules conditions that decides what different users see
    //i.e authenticated and guest users see different views

    @GetMapping("/") //homepage route
    public String home(){
        return "index";//return homepage view
    }
}
