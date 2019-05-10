package com.rmehub.chat.service;

import com.rmehub.chat.model.ChatUser;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class WebSocketAuthenticatorService {

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final ChatUser user, final String jwt){
        if (StringUtils.isEmpty(user.getFullName())) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (StringUtils.isEmpty(jwt)) {
            throw new AuthenticationCredentialsNotFoundException("Authentication token was null or empty.");
        }

        return new UsernamePasswordAuthenticationToken(user,jwt);
    }
}
