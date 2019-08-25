package com.rmehub.chat.service;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.exception.ChatChannelException;
import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.repository.ChatChannelRepo;
import com.rmehub.chat.responseDto.ChatChannelListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatChannelService {

    @Autowired
    ChatChannelRepo chatChannelRepo;

    public boolean isChannelExist(String channelId) {

//        Optional<ChatChannel> optional = chatChannelRepo.findByChannelId(channelId);

        return chatChannelRepo.findByChannelId(channelId).isPresent() ? true : false;

//        return optional.isPresent() ? true : false;
//        return true;
    }

    public ChatChannel createChatChannel(ChatChannel chatChannel) {
        return chatChannelRepo.save(chatChannel);
    }

    public List<ChatChannelListResponse> findAllMyChannels(String uuid) throws ChatChannelException {

        Optional<List<ChatChannel>> optional = chatChannelRepo.findBySender_UuidOrReceiver_UuidOrderByLastChatTimeStampDesc(uuid, uuid);

        if (!optional.isPresent() || optional.get().size() == 0) {
            log.error("No chat channel found for " + uuid);
            throw new ChatChannelException("No Chat channel found", ResponseCode.NOT_FOUND);
        }

        log.info("Chat Channels Found for " + uuid);

        List<ChatChannelListResponse> list = new ArrayList<>();


        optional.get().stream().forEach(o -> {

            ChatChannelListResponse object = null;

            ChatUser senderLocal;
            ChatUser receiverLocal;
            String dispName;
            senderLocal = ChatUser.builder()
                    .uuid(o.getSender().getUuid())
                    .fullName(o.getSender().getFullName())
                    .build();

            receiverLocal = ChatUser.builder()
                    .uuid(o.getReceiver().getUuid())
                    .fullName(o.getReceiver().getFullName())
                    .build();

            if (o.getRequest().getRequestFromUuid().equals(uuid))
                dispName = receiverLocal.getFullName();
            else
                dispName = senderLocal.getFullName();

            String[] name = dispName.split(" ");

            object = ChatChannelListResponse.builder()
                    .channelId(o.getChannelId())
                    .partyHash(o.getPartyHash())
                    .createdAt(o.getCreatedAt())
                    .updatedAt(o.getUpdatedAt())
                    .lastChatTime(o.getLastChatTimeStamp())
                    .receiver(receiverLocal)
                    .sender(senderLocal)
                    .chatRequest(o.getRequest())
                    .displayName(dispName)
                    .initials(name[0].charAt(0) + "" + name[1].charAt(0))
                    .build();

            list.add(object);
        });

        return list;

    }
}
