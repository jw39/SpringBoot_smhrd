package com.example.demo.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// 소켓 통신 : 1(서버) : N(클라이언트) 관계
// 한 서버에서 여러 클라이언트가 발송한 메시지를 받아 처리해주는 역할 -> Handler (접속, 접속해제, 메시지 처리)
@Component // Autowired와 비슷. Autowired -> 따로 객체 정의서를 정의해주고, 외부에서 객체 생성해서 주입할 때 사용
// Component -> 따로 객체 (bean)를 등록할 필요 없이 객체(bean) 생성 어노테이션

@RequiredArgsConstructor //내가 필요한 아규먼크만 들어가는 생성자 만드는 어노테이션?
// 초기화가 되지 않은 필드를 초기화하는 생성자를 만들어주는 어노테이션
// -> objectMapper 변수 생성했는데 저 어노테이션 써서 objectMapper 객체 생성을 함 (@Autowired랑 비슷함)
// spring 5버전 이상의 버전을 사용시에는 @RequiredArgsConstructor를 쓰는 것을 권장함.
// why? 순환 참조 가능
// final 지원 => 값이 마음대로 변경되지 않도록 막을 수 있음
public class WebSocketChatHandler extends TextWebSocketHandler {
	
	// 현재까지 접속한 클라이언트 정보 저장 Set, 순서 파악 필요 없어서 set 이라는 구조로 저장함
	private Set<WebSocketSession> sessions = new HashSet<>();
	
	// 각 채팅 룸별(key) 클라이언트 정보 따로 저장, key에는 룸 id
	private Map<String, Set<WebSocketSession>> chatRoomSessions = new HashMap<>();
	
	// 1. new / 2. builder / 3. @Autowired 가 객체 생성법인데 @RequiredArgsConstructor 지금 써볼 거임
	private final ObjectMapper objectMapper;
	
	
	
	// 클라이언트 접속 시 호출 : sessions에 접속한 클라이언트 추가, 클라이언트가 접속했을 때 어떻게 할 지 호출
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session); // 정보 추가
		System.out.println("접속 : " + session.getId());
	}
	
	// 클라이언트 접속 해제 시 호출 : sessions에 접속 해제 클라이언트 제거
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		System.out.println("해제 :" + session.getId());
	}
	
	// 메시지 처리
	@Override					// (채팅을 보낸 클라이언트의 정보, 그 클라이언트가 보낸 메세지)
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트가 보낸 메세지 확인하기 (chatMessage(VO))
		String payload = message.getPayload(); // 클라이언트가 보낸 메시지 가져오기
		ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
		System.out.println(chatMessage.toString());
		
		String roomId = chatMessage.getRoomId();

		// 메시지 안에 채팅방에 대한 내용이 포함 
		// 만약 해당 채팅방이 MAP에 등록되어 있지 않으면 추가해주는 작업 수행
		// roomId가 map의 key로 등록되어 있지 않을 경우
		if(!chatRoomSessions.containsKey(roomId)) {
			chatRoomSessions.put(roomId, new HashSet<>());
		}
		
		// roomId로 해당 채팅방에 접속된 클라이언트 정보 저장해놓은 set 가져오기
		Set<WebSocketSession> chatRoomSession = chatRoomSessions.get(roomId);
		
		// 메시지 -> 클라이언트가 접속(ENTER), 해제(QUIT), 메시지(TALK)
		// 접속 메시지 : 해당 클라이언트를 채팅 중인 채팅방 MAP에 추가
		if(chatMessage.getMessageType().equals(chatMessage.getMessageType().ENTER)) {
			chatRoomSession.add(session);
		} else if (chatMessage.getMessageType().equals(chatMessage.getMessageType().QUIT)){
			chatRoomSession.remove(session);		
		}
		
		// 해제 메시지 : 채팅방 MAP에서 제거
		// 메시지(채팅) : 해당 채팅 내용은 해당 채팅방에 들어와있는 모든 클라이언트에게 공유하기
		chatRoomSession.parallelStream().forEach(cSession -> {
			// 특정 클라이언트가 서버한테 보낸 메시지를 해당 채팅방에 들어와있는
			// 모든 클라이언트에게 메시지로 전송
			try {
				cSession.sendMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
	}
	
	

}
