package com.adropofliquid.magiclinkauth.controller;

import com.adropofliquid.magiclinkauth.email.EmailService;
import com.adropofliquid.magiclinkauth.exception.NotFoundException;
import com.adropofliquid.magiclinkauth.security.AuthenticatorService;
import com.adropofliquid.magiclinkauth.token.TokenService;
import com.adropofliquid.magiclinkauth.useraccount.Account;
import com.adropofliquid.magiclinkauth.useraccount.AccountService;
import com.adropofliquid.magiclinkauth.utils.ServerInfo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.adropofliquid.magiclinkauth.utils.Info.LOGIN_SUCCESS;
import static com.adropofliquid.magiclinkauth.utils.Info.LOGOUT_SUCCESS;


@Controller
public class LoginController {
    //this controller handles login operations on /login routes
    //also handles logout on /logout
    //some routes contain try/catch block
    //usually I have a separate class to handle controller exception
    //for clarity and better error control
    private final AccountService accountService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final AuthenticatorService authenticatorService;

    //    Inject dependencies
    public LoginController(AccountService accountService, TokenService tokenService, EmailService emailService, AuthenticatorService authenticatorService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.authenticatorService = authenticatorService;
    }

    @GetMapping("/login")
    public String login(){
        //this page should only be accessible if a user is not authenticated
        //if the user is authenticated, we should send them to profile page
        if(!authenticatorService.getAuthentication().isAuthenticated()) //check
            return "redirect:/"; //redirect them to homepage if authenticated

        return "login"; //else let them log in
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email) { // for when login form is submitted
        //login operations
        try{
            //get user if the email exists.
            //It throws exception if it doesn't
            Account account = accountService.getAccountByEmail(email);

            //create a token for the user
            //attach with their ID for easy identification
            String token = tokenService.createToken(account.getId());

            //send an email with the link
            emailService.sendMagicLink(email, ServerInfo.URL+"login/"+token);

        } catch (NotFoundException e) { //if the user is not found, we return INVALID_USER
            return "redirect:/login?error=" + e.getMessage(); // return error message
        }

        return "redirect:/login?success=" + LOGIN_SUCCESS; // return success
    }

    @GetMapping("/login/{token}")
    public String loginWithMagicLink(@PathVariable String token){ // when the link in the mail is clicked
        //we get the token from the GET request on this route
        //then we can use that to authenticate users if the token is valid & hasn't expired
        //token validity is set to 10mins in the Token class
        try{
            //the authentication method is called to perform authentication on the token
            authenticatorService.authenticateUser(token); // attempt authentication
        } catch (BadCredentialsException | NotFoundException e) { //errors are caught and displayed back to user
            return "redirect:/login?error=" + e.getMessage(); //return bad credentials error
        }

        return "redirect:/"; //redirect to homepage if successful
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){ // handle logout

        //logout function is called
        authenticatorService.logout(request);

        // Return a redirect view to the home page
        return "redirect:/login?success="+ LOGOUT_SUCCESS;
    }


}
