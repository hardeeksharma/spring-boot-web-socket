package com.rmehub.chat.service;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.exception.ChatChannelException;
import com.rmehub.chat.model.ChatMapper;
import com.rmehub.chat.repository.ChatMapperRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatMapperService {

    @Autowired
    ChatMapperRepo chatMapperRepo;

    @Autowired
    ChatChannelService chatChannelService;


    public List<ChatMapper> getChannelChat(String channelId) throws ChatChannelException {

        if (!chatChannelService.isChannelExist(channelId)) {
            log.info("Channel dont Exist");
            throw new ChatChannelException("Channel Dont exist for this channel Id", ResponseCode.CHANNEL_NOT_FOUND);
        }

        Optional<List<ChatMapper>> optional = chatMapperRepo.findByChannelIdOrderByCreationDateDesc(channelId);
        if (!optional.isPresent() || optional.get().isEmpty()) {
            throw new ChatChannelException("No Chat found for this channel", ResponseCode.NOT_FOUND);
        }

        return optional.get();
    }
}
