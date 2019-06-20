package com.rmehub.chat.dto.request;

import com.rmehub.chat.constant.MessageType;
import com.rmehub.chat.model.ChatMessage;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChatMessageDto {

    String message;
    MessageType messageType;
    String senderUuid;

}
