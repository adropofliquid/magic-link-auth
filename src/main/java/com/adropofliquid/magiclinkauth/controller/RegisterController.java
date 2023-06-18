package com.adropofliquid.magiclinkauth.controller;

import com.adropofliquid.magiclinkauth.useraccount.Account;
import com.adropofliquid.magiclinkauth.useraccount.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.regex.Pattern;

import static com.adropofliquid.magiclinkauth.utils.Info.INVALID_EMAIL;


@Controller
public class RegisterController {
    //register operations on /register routes
    //unregistered emails cannot authenticate

    private final AccountService accountService;

    //inject accountservice
    public RegisterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Account account){
        //the parameter account is coming from the post request in the register.html view

        //If the email is wrongly formatted, an error message is returned to the user
        //this is not too strict
        if(!isEmailAddressValid(account.getEmail()))
            return "redirect:/register?error=" + INVALID_EMAIL;

        accountService.registerUser(account); //perform registration by saving user information to db

        return "redirect:/login"; //redirect to login page
    }

    public boolean isEmailAddressValid(String emailAddress) { //email address verification using regular expressions
        String regexPattern = "^(.+)@(\\S+)$"; // simply check for @ in the midddle
        //better regexPattern can be used if nrequired

        return Pattern.compile(regexPattern)//pattern matching
                .matcher(emailAddress)
                .matches();
    }
}
