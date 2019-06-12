package com.rmehub.chat.service;

import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.repository.ChatRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ChatRequestService {

    // mode of payment
    // processor kon gateway kon
    // UPI ?
    // charges? as per card and card type net banking
    // wallet integration
    // how will we validate cash deposit and cheques, imps rtgs etc
    // escrow facility for real estate transaction

    @Autowired
    ChatRequestRepository chatRequestRepository;

    public ChatRequest save(ChatRequest chatRequest) {
        return chatRequestRepository.save(chatRequest);
    }

    public void acceptOrRejectChatRequest(String chatRequestId, String toUuid, boolean isAccepted) {
        Optional<ChatRequest> request = chatRequestRepository.findById(chatRequestId);
        if (!request.isPresent()) {
            log.error("No request found for this request ID");
            // TODO handle chat request not found by ID
        }
        log.info("Chat Request Found");
        ChatRequest chatRequest1 = request.get();
        if (!chatRequest1.getRequestToUuid().equals(toUuid)) {
            //TODO this chat ID is not his chat Request
            log.error("Chat request does not belong to him");
        }

        if (isAccepted) {
            chatRequest1.setAccepted(true);

        } else {
            chatRequest1.setAccepted(false);
        }
        log.info(chatRequestRepository.save(chatRequest1).toString());
        log.info("Request Updated");

    }
}
