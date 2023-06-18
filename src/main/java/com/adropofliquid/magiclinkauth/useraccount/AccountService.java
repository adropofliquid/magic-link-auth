package com.adropofliquid.magiclinkauth.useraccount;

import com.adropofliquid.magiclinkauth.exception.NotFoundException;
import org.springframework.stereotype.Service;

import static com.adropofliquid.magiclinkauth.utils.Info.USER_NOT_FOUND;

@Service
public class AccountService {
    //User management service
    //user is stored in database as id, name and email
    //id is int and auto-generated
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void registerUser(Account account){
        if(accountRepository.existsAccountByEmail(account.getEmail())) // if email is already registered
            return; // ignore registration //better to have an exception ofcourse.

        accountRepository.save(account); //this saves an account/user.
    }

    public Account getUserById(int userId) throws NotFoundException {
        //I'm using Optional class here so I can do something when it returns an error
        //in this case it throws NotFound
        return accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public Account getAccountByEmail(String email) throws NotFoundException {
        //I'm using Optional class here, so I can do something when it returns an error
        //in this case it throws NotFound
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

    }
}
