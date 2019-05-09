package com.rmehub.chat.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
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
    private String content;

    private ChatUser sender= null;

    private Date time;

    boolean isStared;

    boolean isAttachment;

    AttachmentType attachmentType;

    String attachmentURL;

    public enum AttachmentType {
        PDF, WORD, EXCEL, IMAGE
    }

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

}
