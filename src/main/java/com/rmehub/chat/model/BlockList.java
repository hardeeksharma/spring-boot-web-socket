package com.rmehub.chat.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@Data
@Document
public class BlockList {

    @Id
    private String id;

    private ChatUser blockedBy;
    private ChatUser blockedTo;

    private String blockReason;

    private Date blockDate = new Date();

}
