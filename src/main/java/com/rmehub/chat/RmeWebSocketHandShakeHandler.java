package com.rmehub.chat;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

//custom hand shake handler
public class RmeWebSocketHandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        System.out.println("Request"  + request);

        return super.determineUser(request, wsHandler, attributes);
    }

}// hand shake handler class end
