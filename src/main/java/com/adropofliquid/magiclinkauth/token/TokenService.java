package com.adropofliquid.magiclinkauth.token;

import com.adropofliquid.magiclinkauth.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.adropofliquid.magiclinkauth.utils.Info.INVALID_TOKEN;

@Service
public class TokenService {
    //Responsible for managing tokens
    //Tokens are saved in the embedded database
    //they have a timestamp for validity
    //each contain a userId as foreignKey, although tables are not joined
    //An optimal way to do this might be a jwt, but that does not work well as session tokens
    //I did not provide mechanism for deletion of tokens to save storage.
    private final TokenRepository tokenRepository; //token store

    //inject token storage
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String createToken(int userId) {
        Token token = new Token(); //token creation

        token.setUserId(userId); //user id
        token.setCreatedAt(System.currentTimeMillis()); //current system time in millis
        token.setToken(String.valueOf(UUID.randomUUID())); //random UUID for token

        return tokenRepository.save(token).getToken(); // the save() method returns Token instance,
                                                        // so we send back the token string in it
    }

    public Token getToken (String token) throws NotFoundException {
        //I'm using Optional class here so I can do something when it returns an error
        //in this case it throws NotFound
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(INVALID_TOKEN));
    }
}
