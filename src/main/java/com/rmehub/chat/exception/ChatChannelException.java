package com.rmehub.chat.exception;

import com.rmehub.chat.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatChannelException extends Exception {

    String message;
    ResponseCode responseCode;

    public ChatChannelException(String message, ResponseCode responseCode) {
        super(message);
        this.message = message;
        this.responseCode = responseCode;
    }
}
