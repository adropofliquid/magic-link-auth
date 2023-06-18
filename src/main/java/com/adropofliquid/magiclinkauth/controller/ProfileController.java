package com.adropofliquid.magiclinkauth.controller;

import com.adropofliquid.magiclinkauth.exception.NotFoundException;
import com.adropofliquid.magiclinkauth.security.AuthenticatorService;
import com.adropofliquid.magiclinkauth.useraccount.Account;
import com.adropofliquid.magiclinkauth.useraccount.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    //Spring Security disables access to /profile unless authenticated
    //when user is successfully authenticated
    //they can view the profile on /profile
    //The authenticated user is gotten from the authenticatorService
    //there I can extract the email and use it to get their account info
    //from accountService
    private final AuthenticatorService authenticatorService;
    private final AccountService accountService;

    //add dependencies
    public ProfileController(AuthenticatorService authenticatorService, AccountService accountService) {
        this.authenticatorService = authenticatorService;
        this.accountService = accountService;
    }

    @GetMapping("/profile")
    public String profile(Model model) throws NotFoundException {
        //
        //get authentation details
        Authentication authentication = authenticatorService.getAuthentication();

        //get account of the user using the email(Principal)
        //we will use this to display details of the user on the front
        Account account = accountService.getAccountByEmail(authentication.getName()); //get account with email present in the authentication object

        model.addAttribute("user", account); //allows me to use the ${user} variable in the profile.html
        return "profile";
    }
}
