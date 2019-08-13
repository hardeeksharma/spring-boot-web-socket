package com.rmehub.chat.responseDto;

import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.model.ChatUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatChannelListResponse {

    String channelId;
    String partyHash;
    ChatRequest chatRequest;
    ChatUser sender;
    ChatUser receiver;
    String requestId;
    Date lastChatTime;
    Date createdAt;
    Date updatedAt;
    String displayName;
    String displayTime;
    String initials;

}
