package com.rmehub.chat.service;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.exception.ChatChannelException;
import com.rmehub.chat.model.ChatMapper;
import com.rmehub.chat.repository.ChatMapperRepo;
import com.rmehub.chat.responseDto.ChatResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatMapperService {

    @Autowired
    ChatMapperRepo chatMapperRepo;

    @Autowired
    ChatChannelService chatChannelService;


    public ChatResponseDto getChannelChat(String channelId, int pageNo, int size) throws ChatChannelException {


        if (!chatChannelService.isChannelExist(channelId)) {
            log.info("Channel does not Exist");
            throw new ChatChannelException("Channel does not exist for this channel Id", ResponseCode.CHANNEL_NOT_FOUND);
        }


        Pageable pageableRequest = PageRequest.of(pageNo, size);

        Page<ChatMapper> chatPage = chatMapperRepo.findByChannelIdOrderByCreationDateDesc(channelId, pageableRequest);
        ChatResponseDto chatResponseDto = new ChatResponseDto();

        List<ChatMapper> listOfChats = new ArrayList<>();

        chatPage.get().forEach(o -> listOfChats.add(o));
        chatResponseDto.setNoOfPages(chatPage.getTotalPages());
        chatResponseDto.setMapper(listOfChats);
        chatResponseDto.setTotalElements(chatPage.getTotalElements());

        if (!chatPage.hasContent()) {
            throw new ChatChannelException("No Chat found for this channel", ResponseCode.NOT_FOUND);
        }
        return chatResponseDto;
    }
}
