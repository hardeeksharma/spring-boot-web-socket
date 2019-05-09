package com.rmehub.chat.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;


@NoArgsConstructor
@Data
@Document
public class ChatRequest {

    @Id
    private String id;

    private String requestFromUuid;
    private String requestToUuid;

    private boolean isAccepted;

    private String requestPurpose;

    private String rejectReason;

    private LocalDateTime createdAt;

    private LocalDateTime UpdatedAt;

}
