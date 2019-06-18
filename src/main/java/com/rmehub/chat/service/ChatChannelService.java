package com.rmehub.chat.service;

import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.repository.ChatChannelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatChannelService {

    @Autowired
    ChatChannelRepo chatChannelRepo;

    ChatChannel createChatChannel(ChatChannel chatChannel){
        return  chatChannelRepo.save(chatChannel);
    }
}
