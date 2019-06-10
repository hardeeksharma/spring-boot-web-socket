package com.rmehub.chat.service;

import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.repository.ChatRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void save(ChatRequest chatRequest){
        chatRequestRepository.save(chatRequest);
    }

    public void acceptChatRequest(ChatRequest chatRequest){
        Optional<ChatRequest> request = chatRequestRepository.findById(chatRequest.getId());
        if(!request.isPresent())
        {
            // TODO handle chat request not found by ID
        }
        ChatRequest chatRequest1 = request.get();
        chatRequest1.setAccepted(true);
        chatRequestRepository.save(chatRequest1);
    }
}
