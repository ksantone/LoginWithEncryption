package com.LoginWebApp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
	protected static final int MAX_ATTEMPTS = 5;
	protected static final long BLOCK_TIME = 4 * 60 * 1000; // 4 minutes
	
	protected Map<String, Integer> attemptsMap = new ConcurrentHashMap<>();
	protected Map<String, Long> blockMap = new ConcurrentHashMap<>();
	
	protected  String generateKey(String username, String action) {
        return username + "_" + action;
    }

    public boolean isBlocked(String username, String action) {
    	String key = generateKey(username, action);
        if (blockMap.containsKey(key)) {
            long blockedUntil = blockMap.get(key);
            if (System.currentTimeMillis() < blockedUntil) {
                return true;
            } else {
                // Unblock if block time has passed
                blockMap.remove(key);
                attemptsMap.remove(key);
            }
        }
        return false;
    }
    
    public String getBlockTimeMessage(String username, String action) {
    	String key = generateKey(username, action);
        long blockedUntil = blockMap.get(key);
        long minutesLeft = (blockedUntil - System.currentTimeMillis()) / 60000;
        return "You are blocked for another " + minutesLeft + " minutes.";
    }

    public void loginFailed(String username, String action) {
    	String key = generateKey(username, action);
        attemptsMap.put(key, attemptsMap.getOrDefault(key, 0) + 1);

        if (attemptsMap.get(key) >= MAX_ATTEMPTS) {
            blockMap.put(key, System.currentTimeMillis() + BLOCK_TIME);
        }
    }

    public void loginSucceeded(String username, String action) {
        String key = generateKey(username, action);
        attemptsMap.remove(key);
        blockMap.remove(key);
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