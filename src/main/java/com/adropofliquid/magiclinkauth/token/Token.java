package com.adropofliquid.magiclinkauth.token;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Token {
    @Id
    @GeneratedValue
    private int id;

    private int userId;

    private String token;
    private long createdAt;
    public static final long TOKEN_VALIDITY_MILLIS = 60 * 1000 * 10;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isExpired(){
        // if the current time is more than the (tokenCreation time + validity)
        //return false
        return System.currentTimeMillis() > (getCreatedAt() + TOKEN_VALIDITY_MILLIS);
    }
}
