package com.adropofliquid.magiclinkauth.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

// A custom error controller that redirects to home on error
@Controller
public class CustomErrorController implements ErrorController {

    // Handle the error requests and redirect to home
    @RequestMapping(value = "/error")
    public RedirectView handleError() {
        return new RedirectView("/"); //404 redirects to home
    }
}
