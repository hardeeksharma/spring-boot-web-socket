package com.rmehub.chat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessage {
	
	private MessageType type;
	private String content;
	private String sender;
	private String time;

	public enum MessageType {
		CHAT, JOIN, LEAVE
	}

}
