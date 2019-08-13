package com.rmehub.chat.controller;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.dto.request.ChatMessageDto;
import com.rmehub.chat.dto.request.NewChat;
import com.rmehub.chat.dto.response.GenericResponse;
import com.rmehub.chat.exception.ChatRequestException;
import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.model.ChatMessage;
import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.service.ChatMessageService;
import com.rmehub.chat.service.ChatRequestService;
import com.sun.tools.javah.Gen;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ChatRequestService chatRequestService;

    @Autowired
    ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public NewChat sendMessage(@Payload NewChat chatMessage) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        chatMessage.setTime(new ObjectId().getDate());
        return chatMessage;
    }

    @MessageMapping("/chat/request.send.{toUuid}")
    public void sendChatRequest(@Payload ChatRequest chatRequest, @DestinationVariable String toUuid, StompHeaderAccessor accessor) {

        log.info("=========sendChatRequest========= START");

        GenericResponse genericResponse;

        log.info(chatRequest.toString());
        ChatRequest chatRequestResponse = chatRequestService.save(chatRequest);

        // if chat request is duplicate
        if (chatRequestResponse.isDuplicate()) {
            genericResponse = GenericResponse.builder()
                    .statusCode(409)
                    .responseCode(ResponseCode.DUPLICATE_REQUEST)
                    .build();

            log.info("Duplicate chat Request");

            simpMessagingTemplate.convertAndSend("/topic/request/ack." + chatRequest.getRequestFromUuid(), genericResponse);


        } else {
            genericResponse = GenericResponse.builder()
                    .isError(false)
                    .statusCode(201)
                    .responseCode(ResponseCode.CHAT_REQUEST_SENT)
                    .build();

            log.info("Chat request saved");

            // chat request ack to the sender
            simpMessagingTemplate.convertAndSend("/topic/request/ack." + chatRequest.getRequestFromUuid(), genericResponse);

            genericResponse = GenericResponse.builder()
                    .isError(false)
                    .statusCode(200)
                    .responseCode(ResponseCode.NEW_CHAT_REQUEST)
                    .build();

            //sending notification to the chat request receiver via WS
            simpMessagingTemplate.convertAndSend("/topic/request/ack." + toUuid, genericResponse);
        }

        log.info("=========sendChatRequest========= END");

    }

    @MessageMapping("/chat/request.acceptOrReject.{requestId}")
    private void acceptOrRejectChatRequest(@DestinationVariable String requestId, @Payload ChatRequest chatRequest, StompHeaderAccessor accessor) {

        log.info(requestId);
        //System.out.println(chatRequest);
        GenericResponse genericResponse;
        boolean isAccepted = Boolean.parseBoolean(chatRequest.getAccept());
        log.info(Boolean.toString(isAccepted));
        //getting current User uuid from headers to check that the request Id if for him.
        try {
            List<String> uuidList = accessor.getNativeHeader("uuid");
            Optional<ChatRequest> optionalChatRequest = chatRequestService.findByRequestId(requestId);
            ChatChannel chatChannel = chatRequestService.acceptOrRejectChatRequest(requestId, uuidList.get(0), isAccepted, chatRequest);

            if (isAccepted) { // handle if chat request is accepted

                genericResponse = GenericResponse.builder()
                        .statusCode(200)
                        .isError(false)
                        .responseCode(ResponseCode.CR_ACCEPT_SUCCESS)
                        .build();

                simpMessagingTemplate.convertAndSend("/topic/request/ack." + optionalChatRequest.get().getRequestFromUuid(), genericResponse);
                simpMessagingTemplate.convertAndSend("/topic/request/ack." + optionalChatRequest.get().getRequestToUuid(), genericResponse);


            } else { // handling if chat request is rejected

                HashMap<String, Object> data = new HashMap<>();
                data.put("data", optionalChatRequest.get());
                genericResponse = GenericResponse.builder()
                        .isError(false)
                        .statusCode(200)
                        .message("Chat Request Rejected by the user")
                        .responseCode(ResponseCode.CHAT_REQUEST_REJECTED)
                        .payload(data)
                        .build();

                log.info("ACK : " + genericResponse.toString());

                // sending notification to sender
                simpMessagingTemplate.convertAndSend("/topic/request/ack." + optionalChatRequest.get().getRequestFromUuid(), genericResponse);

                genericResponse = GenericResponse.builder()
                        .isError(false)
                        .statusCode(200)
                        .message("Chat Request Rejected by the user")
                        .responseCode(ResponseCode.CHAT_REQUEST_REJECT_SUCCESS)
                        .payload(data)
                        .build();
                // sending notification to receiver
                simpMessagingTemplate.convertAndSend("/topic/request/ack." + optionalChatRequest.get().getRequestToUuid(), genericResponse);
            }

        } catch (ChatRequestException ex) {

            genericResponse = GenericResponse.builder()
                    .isError(true)
                    .responseCode(ResponseCode.NOT_FOUND)
                    .statusCode(404)
                    .message("Chat Request not found for this ChatId")
                    .build();
            // this message will go to receiver of chat request if there is some messing around with the WS request
            simpMessagingTemplate.convertAndSend("/topic/request/ack." + chatRequest.getRequestToUuid(), genericResponse);
        }

//
//        TODO send notification about chat channel in ACK socket connection
//        TODO send notification to both user about the CR
    }

    @MessageMapping("/chat/message.send.{channelId}")
    @SendTo("/queue/chat.{channelId}")
    private ChatMessage sendChatMessage(@Payload ChatMessageDto chatMessageDto, @DestinationVariable String channelId) {

        log.info("=========== SEND CHAT MESSAGE ========== START");

        System.out.println(chatMessageDto);

        ChatMessage chatMessage = chatMessageService.save(chatMessageDto, channelId);

        log.info("=========== SEND CHAT MESSAGE ========== END");

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
        System.out.println("in request received");
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
