package com.rmehub.chat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.dto.request.NewChat;
import com.rmehub.chat.dto.response.GenericResponse;
import com.rmehub.chat.model.ChatChannel;
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
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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
    @SendTo("/chat/request.receive.{toUuid}")
    public GenericResponse sendChatRequest(@Payload ChatRequest chatRequest, @DestinationVariable String toUuid, StompHeaderAccessor accessor) {

        GenericResponse genericResponse;

        log.info(chatRequest.toString());
        ChatRequest chatRequestResponse = chatRequestService.save(chatRequest);
        log.info("Chat request saved");
        genericResponse = GenericResponse.builder()
                .isError(false)
                .statusCode(201)
                .responseCode(ResponseCode.CHAT_REQUEST_SENT)
                .build();
        //TODO send request notification to receiver via web socket
        simpMessagingTemplate.convertAndSend("/topic/request/ack." + chatRequest.getRequestFromUuid(), genericResponse);

        genericResponse = GenericResponse.builder().statusCode(201)
                .responseCode(ResponseCode.NEW_CHAT_REQUEST).build();

        return genericResponse;
    }

    @MessageMapping("/chat/request.acceptOrReject.{requestId}")
    private void acceptOrRejectChatRequest(@DestinationVariable String requestId, @Payload ChatRequest chatRequest, StompHeaderAccessor accessor) {

        log.info(requestId);
        boolean isAccepted = Boolean.parseBoolean(chatRequest.getAccept());
        log.info(Boolean.toString(isAccepted));
        //getting current User uuid from headers to check that the request Id if for him.
        List<String> uuidList = accessor.getNativeHeader("uuid");
        ChatChannel chatChannel = chatRequestService.acceptOrRejectChatRequest(requestId, uuidList.get(0), isAccepted);

        //TODO send notification about chat channel in ACK socket connection
        //TODO send notification to both user about the CR
    }

    @MessageMapping("/chat/message.send.{channelId}")
    @SendTo("/queue/chat.{channelId}")
    private ChatMessage sendChatMessage(@Payload ChatMessage chatMessage, @DestinationVariable String channelId) {

        System.out.println(chatMessage);

        return chatMessage;
    }

    @SubscribeMapping("/chat/request.sent.{uuid}")
    private GenericResponse mySentChatRequest(@DestinationVariable String uuid) {

        Optional<?> mySentChatRequest = chatRequestService.findMySentChatRequest(uuid);
        GenericResponse response = null;

        HashMap<String, Object> data = new HashMap<>();
        if (mySentChatRequest.isPresent()) {
            data.put("data", mySentChatRequest.get());
            response = GenericResponse.builder()
                    .responseCode(ResponseCode.API_SUCCESS)
                    .statusCode(200)
                    .payload(data)
                    .build();
        } else {
            response = GenericResponse.builder()
                    .responseCode(ResponseCode.NOT_FOUND)
                    .statusCode(204)
                    .payload(null)
                    .build();
        }
        return response;

    }

    @SubscribeMapping("/chat/request.received.{uuid}")
    private GenericResponse myReceivedChatRequest(@DestinationVariable String uuid) {

        Optional<?> mySentChatRequest = chatRequestService.findMyReceivedChatRequest(uuid);
        GenericResponse response = null;

        HashMap<String, Object> data = new HashMap<>();
        if (mySentChatRequest.isPresent()) {
            data.put("data", mySentChatRequest.get());
            response = GenericResponse.builder()
                    .responseCode(ResponseCode.API_SUCCESS)
                    .statusCode(200)
                    .payload(data)
                    .build();
        } else {
            response = GenericResponse.builder()
                    .responseCode(ResponseCode.NOT_FOUND)
                    .statusCode(204)
                    .payload(null)
                    .build();
        }
        return response;

    }

}
