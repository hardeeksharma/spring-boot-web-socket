package com.rmehub.chat.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rmehub.chat.dto.request.NewChat;
import com.rmehub.chat.model.ChatRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.rmehub.chat.model.ChatMessage;

@Controller
public class ChatController {


    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public NewChat sendMessage(@Payload NewChat chatMessage) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        chatMessage.setTime(new ObjectId().getDate());
        return chatMessage;
    }

    @MessageMapping("/chat/request.send.{fromUuid}")
    public void sendChatRequest(@Payload ChatRequest chatRequest, @DestinationVariable String fromUuid, StompHeaderAccessor accessor) {
//        Authentication principal = (Authentication) accessor.getUser();
//        System.out.println(principal.getName());
        chatRequest.setRequestFromUuid(fromUuid);
        simpMessagingTemplate.convertAndSend("/topic/public." + chatRequest.getRequestToUuid(), chatRequest);
    }

}
