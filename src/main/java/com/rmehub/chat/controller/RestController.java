package com.rmehub.chat.controller;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.dto.response.GenericResponse;
import com.rmehub.chat.exception.ChatChannelException;
import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.model.ChatMapper;
import com.rmehub.chat.repository.ChatRequestRepository;
import com.rmehub.chat.responseDto.ChatChannelListResponse;
import com.rmehub.chat.responseDto.ChatResponseDto;
import com.rmehub.chat.service.ChatChannelService;
import com.rmehub.chat.service.ChatMapperService;
import com.rmehub.chat.service.ChatRequestService;
import com.rmehub.chat.service.UserChannelMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    ChatRequestRepository chatRequestRepository;

    @Autowired
    ChatRequestService chatRequestService;

    @Autowired
    UserChannelMapperService userChannelMapperService;

    @Autowired
    ChatChannelService chatChannelService;

    @Autowired
    ChatMapperService chatMapperService;

    @GetMapping("chatRequest/{uuid}")
    ResponseEntity<?> getMyChatRequests(@PathVariable("uuid") String uuid) {

        GenericResponse genericResponse = new GenericResponse();

        log.info("==========getMyChatRequests=========");

        Optional<?> myReceivedChatRequest = chatRequestService.findMyReceivedChatRequest(uuid);
//        System.out.println(myReceivedChatRequest.isPresent());
        if (!myReceivedChatRequest.isPresent()) {

            log.info(uuid + "There is no chat requests available");
            genericResponse = GenericResponse.builder()
                    .statusCode(404)
                    .responseCode(ResponseCode.NOT_FOUND)
                    .isError(true).build();
            return ResponseEntity.ok().body(genericResponse);
        }
        genericResponse.setError(false);
        genericResponse.setStatusCode(200);
        genericResponse.setResponseCode(ResponseCode.CR_FOUND);
        genericResponse.getPayload().put("data", myReceivedChatRequest.get());

        log.info(uuid + " Chat Requests Found");

        return ResponseEntity.ok().body(genericResponse);
    }

    @GetMapping("sent/chatRequest/{uuid}")
    ResponseEntity<?> getMySentChatRequests(@PathVariable("uuid") String uuid) {

        GenericResponse genericResponse = new GenericResponse();

        log.info("==========getMyChatRequests=========");

        Optional<?> mySentChatRequest = chatRequestService.findMySentReceivedChatRequest(uuid);
        if (!mySentChatRequest.isPresent()) {

            log.info(uuid + "There is no chat requests available");
            genericResponse = GenericResponse.builder()
                    .statusCode(404)
                    .responseCode(ResponseCode.NOT_FOUND)
                    .isError(true).build();
            return ResponseEntity.ok().body(genericResponse);
        }
        genericResponse.setError(false);
        genericResponse.setStatusCode(200);
        genericResponse.setResponseCode(ResponseCode.CR_FOUND);
        genericResponse.getPayload().put("data", mySentChatRequest.get());

        log.info(uuid + " Chat Requests Found");

        return ResponseEntity.ok().body(genericResponse);
    }

    @GetMapping("chatChannels/{uuid}")
    ResponseEntity<?> getMyChatChannels(@PathVariable("uuid") String uuid) {

        GenericResponse genericResponse;
        List<ChatChannelListResponse> allMyChannels = null;
        try {
            allMyChannels = chatChannelService.findAllMyChannels(uuid);
        } catch (ChatChannelException ex) {

            if (ex.getResponseCode() == ResponseCode.NOT_FOUND) {
                genericResponse = GenericResponse.builder()
                        .responseCode(ex.getResponseCode())
                        .isError(true)
                        .statusCode(404)
                        .message("No Chat Channel Found")
                        .build();
                return ResponseEntity.ok().body(genericResponse);
            }

        } catch (Exception ex) {

            genericResponse = GenericResponse.builder().statusCode(404).isError(true)
                    .responseCode(ResponseCode.FATAL_EXCEPTION).build();
            return ResponseEntity.ok().body(genericResponse);

        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("channels", allMyChannels);
        genericResponse = GenericResponse.builder()
                .responseCode(ResponseCode.CHANNEL_FOUND)
                .isError(false)
                .statusCode(200)
                .payload(data)
                .build();

        return ResponseEntity.ok().body(genericResponse);
    }

    @GetMapping("/channel/{channelId}/chat")
    ResponseEntity<?> getChannelChat(@PathVariable("channelId") String channelId ,
                                     @RequestParam Integer pageNo , @RequestParam Integer size) {
        GenericResponse response;
        ChatResponseDto chatMapper;
        try {
            chatMapper = chatMapperService.getChannelChat(channelId , pageNo , size);
        } catch (ChatChannelException ex) {
            response = GenericResponse.builder()
                    .isError(true)
                    .statusCode(404)
                    .responseCode(ex.getResponseCode())
                    .message(ex.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ex.printStackTrace();

            response = GenericResponse.builder()
                    .isError(true)
                    .statusCode(500)
                    .responseCode(ResponseCode.FATAL_EXCEPTION)
                    .message("Fatal Exception")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("chat", chatMapper);

        response = GenericResponse.builder()
                .message("Chat found")
                .responseCode(ResponseCode.API_SUCCESS)
                .isError(false)
                .statusCode(200)
                .payload(payload)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
