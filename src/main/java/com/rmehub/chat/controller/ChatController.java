package com.rmehub.chat.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.dto.request.NewChat;
import com.rmehub.chat.dto.response.GenericResponse;
import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.service.ChatRequestService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ChatController {


    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ChatRequestService chatRequestService;

    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public NewChat sendMessage(@Payload NewChat chatMessage) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        chatMessage.setTime(new ObjectId().getDate());
        return chatMessage;
    }

    @MessageMapping("/chat/request.send.{toUuid}")
    public void sendChatRequest(@Payload ChatRequest chatRequest, @DestinationVariable String toUuid, StompHeaderAccessor accessor) {

        GenericResponse genericResponse;

        log.info(chatRequest.toString());
        chatRequestService.save(chatRequest);
        log.info("Chat request saved");
        genericResponse = GenericResponse.builder()
                .isError(false)
                .statusCode(201)
                .responseCode(ResponseCode.CHAT_REQUEST_SENT)
                .build();
        simpMessagingTemplate.convertAndSend("/topic/request/ack." + chatRequest.getRequestFromUuid(), genericResponse);
    }

    @MessageMapping("/chat/request.accept")
    private void acceptChatRequest(@Payload ChatRequest chatRequest) {
        log.info(chatRequest.toString());
    }

}
