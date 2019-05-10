package com.rmehub.chat.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class RmeWebSocketHandShakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		System.out.println("HandshakeInterceptor");

//		if (request instanceof ServletServerHttpRequest) {
//			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//			HttpSession session = servletRequest.getServletRequest().getSession();
//			//if (session == null) {
//				attributes.put("session.id", session.getId());
//			//}
//		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}

}
