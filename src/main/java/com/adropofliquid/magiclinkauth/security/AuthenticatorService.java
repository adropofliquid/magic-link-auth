package com.adropofliquid.magiclinkauth.security;

import com.adropofliquid.magiclinkauth.exception.NotFoundException;
import com.adropofliquid.magiclinkauth.token.Token;
import com.adropofliquid.magiclinkauth.token.TokenService;
import com.adropofliquid.magiclinkauth.useraccount.AccountService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
public class AuthenticatorService {
/* "Simply put, Spring Security hold the principal information of each
authenticated user in a ThreadLocal – represented as an Authentication object.
    In order to construct and set this Authentication object – we need to use the same approach
    Spring Security typically uses to build the object on a standard authentication." - baeldung*/

    //basically we programmatically perform authentication
    //spring security does not have the mechanism required to use magic links
    //so I manually create an Authentication object and
    //set it as the authentication on the SecurityContext
    //to logout, we would just need to clear the context
    private final TokenService tokenService;
    private final AccountService accountService;

    //added dependencies
    public AuthenticatorService(TokenService tokenService, AccountService accountService) {
        this.tokenService = tokenService;
        this.accountService = accountService;
    }

    public void authenticateUser(String t) throws BadCredentialsException, NotFoundException {
        //user authentication is performed here using token t from parameter
        //we verify the token, build an authentication instance, and set it on the securityContext.

        //Token verification
        Token token = tokenService.getToken(t); //NotFoundException is thrwon here if token doesn't exist

        // check if token is expired
        // the Token class has a method to check expiry
        if(token.isExpired()) {
            throw new BadCredentialsException("Token is expired"); //error if so
        }

        //Authenticate user\\
        String email = accountService.getUserById(token.getUserId()).getEmail(); //get user email
        // create an authentication class
        // because we do not perform any authorization control
        //an empty ArrayList is used in place of an actual list of authorities
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, token.getToken(),new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(authentication); // setContext to authenticated user
    }

    public Authentication getAuthentication(){ //return the current authenticated in the SecurityContext
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void logout(HttpServletRequest request) {
        //to logout
        //we need to clear the SecurityContext we set when we login
        //and also invalidate the session

        // Clear the authentication object from the security context
        SecurityContextHolder.clearContext();

        // Invalidate the current session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

    }
}
