package com.rmehub.chat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.rmehub.chat.model.ChatMessage;

@Controller
public class ChatController {

	@MessageMapping("/chat")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		String time = new SimpleDateFormat("HH:mm").format(new Date());
		chatMessage.setTime(time);
		return chatMessage;
	}
}
