package com.rmehub.chat.service;

import com.rmehub.chat.dto.request.ChatMessageDto;
import com.rmehub.chat.model.ChatMessage;
import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.repository.ChatMessageRepo;
import com.rmehub.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatMessageService {

    @Autowired
    ChatMessageRepo chatMessageRepo;

    @Autowired
    ChatUserRepository chatUserRepository;

    public ChatMessage save (ChatMessageDto dto){

        Optional<ChatUser> userOptional = chatUserRepository.findByUuid(dto.getSenderUuid());

        ChatMessage message = ChatMessage.builder()
                .message(dto.getMessage())
                .sender(userOptional.get())
                .type(dto.getMessageType())
                .build();

        return chatMessageRepo.save(message);
    }

}
