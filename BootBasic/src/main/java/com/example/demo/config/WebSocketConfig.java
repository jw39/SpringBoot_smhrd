package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo.handler.WebSocketChatHandler;

import lombok.RequiredArgsConstructor;

@Configuration  // 설정파일
@EnableWebSocket // 웹소켓 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final WebSocketChatHandler webSocketChatHandler;
		
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// EndPoint : 서비스의 끝자락(핸드폰/PC) / Websocket 에서는 URI(경로)의 끝자락
		// => 채팅을 하기 위해서 접근해야 하는 URL
		// setAllowedOrigins : CORS 설정 -> 접근 권한 부여
		registry.addHandler(webSocketChatHandler, "/ws/chat").setAllowedOrigins("*");
	}

}
