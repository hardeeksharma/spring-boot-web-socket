package com.rmehub.chat.model;

import com.rmehub.chat.constant.AttachmentType;
import com.rmehub.chat.constant.MessageType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class ChatMessage {

    @Id
    private String id;
    private MessageType type;
    private String message;

    private ChatUser sender= null;

    @CreatedDate
    private Date time;

    boolean isStared = false;

    boolean isAttachment = false;

    AttachmentType attachmentType;

    String attachmentURL;

}
