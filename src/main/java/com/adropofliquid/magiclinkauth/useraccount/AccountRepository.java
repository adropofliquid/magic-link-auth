package com.adropofliquid.magiclinkauth.useraccount;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    Optional<Account> findByEmail(String email); //find the account by email
    boolean existsAccountByEmail(String email); //return true if account exists
}
