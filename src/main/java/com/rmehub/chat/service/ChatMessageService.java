package com.rmehub.chat.service;

import com.rmehub.chat.dto.request.ChatMessageDto;
import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.model.ChatMapper;
import com.rmehub.chat.model.ChatMessage;
import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.repository.ChatChannelRepo;
import com.rmehub.chat.repository.ChatMapperRepo;
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

    @Autowired
    ChatMapperRepo chatMapperRepo;

    @Autowired
    ChatChannelRepo chatChannelRepo;

    public ChatMessage save(ChatMessageDto dto, String channelId) {

        Optional<ChatUser> userOptional = chatUserRepository.findByUuid(dto.getSenderUuid());
        Optional<ChatChannel> optionalChatChannel = chatChannelRepo.findByChannelId(channelId);
        ChatMessage message = ChatMessage.builder()
                .message(dto.getMessage())
                .sender(userOptional.get())
                .type(dto.getMessageType())
                .build();

        chatMessageRepo.save(message);

        System.out.println(message);

        ChatMapper chatMapper = new ChatMapper();
        chatMapper.setChannelId(channelId);
        chatMapper.setMessage(message);
        chatMapperRepo.save(chatMapper);



        return message;

    }

}
