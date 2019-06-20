package com.rmehub.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rmehub.chat.constant.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@NoArgsConstructor
@Data
@Document
@AllArgsConstructor
@Builder
public class ChatRequest {

    @Id
    private String id;

    private String requestFromUuid;
    private String senderName;

    private String requestToUuid;
    private String receiverName;

    @JsonIgnore
    private String accept;

    private RequestStatus requestStatus = RequestStatus.PENDING;

    private boolean isAccepted;

    private String requestPurpose;

    private String rejectReason;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date UpdatedAt;

}
