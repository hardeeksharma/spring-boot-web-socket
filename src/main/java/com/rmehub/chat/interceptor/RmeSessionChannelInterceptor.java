package com.rmehub.chat.interceptor;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.repository.ChatUserRepository;
import com.rmehub.chat.service.WebSocketAuthenticatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;

@Slf4j
public class RmeSessionChannelInterceptor implements ChannelInterceptor {

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        final ChatUser chatUser;

        switch (accessor.getCommand()) {
            case CONNECT:
                List<String> uuidList = accessor.getNativeHeader("uuid");
                List<String> fullNameList = accessor.getNativeHeader("fullName");
                List<String> jwtList = accessor.getNativeHeader("jwt");
                //TODO check is these value are null and do exp handling
//                if (uuidList.isEmpty() || fullNameList.isEmpty() || jwtList.isEmpty()) {
//                }

                String uuid = uuidList.get(0);
                String fullName = fullNameList.get(0);
                String jwt = jwtList.get(0);
                System.out.println("i am in connect");
                // checking if we have saved the user in DB of chat or not
                Optional<ChatUser> optionalChatUser = chatUserRepository.findByUuid(uuid);
                if (!optionalChatUser.isPresent()) {
                    chatUser = ChatUser.builder().
                            uuid(uuid).isOnline(true)
                            .fullName(fullName)
                            .build();
                    System.out.println("New User Saved");
                } else {
                    chatUser = optionalChatUser.get();
                    chatUser.setOnline(true);
                }
                chatUserRepository.save(chatUser);

                System.out.println(chatUser);
                Authentication user = webSocketAuthenticatorService.getAuthenticatedOrFail(chatUser, jwt);
                //setting principal user in header accessor
                accessor.setUser(user);
                break;
            case CONNECTED:

                break;
            case DISCONNECT:
                //   toggleUserPresence(uuid, false);
                log.info("User Disconnected");
                break;
            default:
                break;
        }
        // String host = accessor.getHost();
        // List tokenList = accessor.getNativeHeader("sec-websocket-key");
        // System.out.println(host);
//		Map<String, Object> sessionHeaders = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
//		String sessionId = (String) sessionHeaders.get("session.id");
//		
//		System.out.println(sessionId);
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor stompDetails = StompHeaderAccessor.wrap(message);

        if (stompDetails.getCommand() == null) {
            return;
        }

    }

    private void toggleUserPresence(String uuid, boolean isConnected) {

        Optional<ChatUser> optionalChatUser = chatUserRepository.findByUuid(uuid);
        ChatUser user = optionalChatUser.get();

        if (!isConnected) {
            user.setOnline(false);
        } else {
            user.setOnline(true);
        }
        user = chatUserRepository.save(user);
        log.info(user.toString());
    }
}
