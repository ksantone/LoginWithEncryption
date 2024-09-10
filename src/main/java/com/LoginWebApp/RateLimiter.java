package com.LoginWebApp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
	protected final Map<String, LoginAttempt> loginAttempts = new HashMap<>();
	protected static final int MAX_ATTEMPTS = 5;
	protected static final long BLOCK_TIME = TimeUnit.MINUTES.toMillis(10); // 10 minutes

    public boolean isBlocked(String key) {
        if (loginAttempts.containsKey(key)) {
            LoginAttempt attempt = loginAttempts.get(key);
            if (attempt.getAttempts() >= MAX_ATTEMPTS) {
                if (System.currentTimeMillis() - attempt.getLastAttempt() < BLOCK_TIME) {
                    return true; // Block if within block time
                } else {
                    loginAttempts.remove(key); // Remove after block time
                }
            }
        }
        return false;
    }
    
    public String getBlockTimeMessage(String key) {
        if (loginAttempts.containsKey(key)) {
            LoginAttempt attempt = loginAttempts.get(key);
            long remainingBlockTime = BLOCK_TIME - (System.currentTimeMillis() - attempt.getLastAttempt());
            long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingBlockTime);
            long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingBlockTime) % 60;
            return "Too many failed attempts. Try again in " + remainingMinutes + " minutes and " + remainingSeconds + " seconds.";
        }
        return "";
    }

    public void loginFailed(String key) {
        LoginAttempt attempt = loginAttempts.getOrDefault(key, new LoginAttempt());
        attempt.setAttempts(attempt.getAttempts() + 1);
        attempt.setLastAttempt(System.currentTimeMillis());
        loginAttempts.put(key, attempt);
    }

    public void loginSucceeded(String key) {
        loginAttempts.remove(key);
    }

    protected static class LoginAttempt {
        private int attempts;
        private long lastAttempt;

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        public long getLastAttempt() {
            return lastAttempt;
        }

        public void setLastAttempt(long lastAttempt) {
            this.lastAttempt = lastAttempt;
        }
    }
}