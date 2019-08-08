package com.rmehub.chat.controller;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.dto.response.GenericResponse;
import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.repository.ChatRequestRepository;
import com.rmehub.chat.service.ChatRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
