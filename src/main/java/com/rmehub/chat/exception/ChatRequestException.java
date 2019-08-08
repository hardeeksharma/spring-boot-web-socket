package com.rmehub.chat.exception;

import com.rmehub.chat.constant.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class ChatRequestException extends Exception {

    private String message;
    private ResponseCode responseCode;

    public ChatRequestException(String message, ResponseCode responseCode) {
        super(message);
        this.message = message;
        this.responseCode = responseCode;
    }

}
