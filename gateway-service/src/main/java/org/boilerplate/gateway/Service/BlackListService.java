package org.boilerplate.gateway.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class BlackListService {
    
    private final Set<String> blackListedTokens = ConcurrentHashMap.newKeySet();

    public void addTokenToBlackList(String token) {
        blackListedTokens.add(token);
    }

    public boolean isTokenBlackListed(String token) {
        return blackListedTokens.contains(token);
    }
}
