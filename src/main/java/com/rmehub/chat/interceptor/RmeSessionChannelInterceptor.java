package com.rmehub.chat.interceptor;

import java.util.List;
import java.util.Map;

import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.MultiValueMap;


public class RmeSessionChannelInterceptor implements ChannelInterceptor {

    @Autowired
    ChatUserRepository chatUserRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //System.out.println("Channel Interceptor");

        MessageHeaders headers = message.getHeaders();
        //System.out.println(headers);
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        ChatUser chatUser;

        switch (accessor.getCommand()) {
            case CONNECT:
                List<String> uuid = accessor.getNativeHeader("uuid");
                List<String> fullName = accessor.getNativeHeader("fullName");
                List<String> jwt = accessor.getNativeHeader("jwt");


                if (uuid.isEmpty() || fullName.isEmpty() || jwt.isEmpty()) {
                    //throw new Exception(); //TODO Throw Some Exception
                }

                if (!chatUserRepository.findByUuid(uuid.get(0)).isPresent()) {
                    chatUser = ChatUser.builder().
                            uuid(uuid.get(0))
                            .fullName(fullName.get(0))
                            .build();

                    chatUserRepository.save(chatUser);
                    System.out.println("User Saved");
                }

                //System.out.println(uuid.get(0));

//                MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
//                for (Map.Entry<String, List<String>> head : multiValueMap.entrySet()) {
//                    System.out.println(head.getKey() + "#" + head.getValue());
//                }

            case CONNECTED:
                //   toggleUserPresence(stompDetails.getUser().getName().toString(), true);
                break;
            case DISCONNECT:
                // toggleUserPresence(stompDetails.getUser().getName().toString(), false);
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
//        MessageHeaders headers = message.getHeaders();
//        //System.out.println(headers);
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        switch (stompDetails.getCommand()) {
//            case CONNECT:
//                System.out.println("i am in postSend");
//                List<String> suuid = accessor.getNativeHeader("uuid");
//                System.out.println(suuid.get(0));
//            case CONNECTED:
//                //   toggleUserPresence(stompDetails.getUser().getName().toString(), true);
//                break;
//            case DISCONNECT:
//                // toggleUserPresence(stompDetails.getUser().getName().toString(), false);
//                break;
//            default:
//                break;
//        }
//    }
    }

}
