package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
//@AllArgsConstructor -> 생성자 생성 2번째 방법 : lombok 사용해서 전체 필드 초기화하는 생성자 생성
public class ChatRoom {
	
	private String roomId; //식별자
	private String roomName; // 채팅방 이름
	
	// 생성사 호출하는 대신에 @Builder 방식으로 객체 생성
	// ex) new ChatRoom("1", "채팅방1"); 기존 생성자 호출하는 방식
	// => ChatRoom.builder().roomId("1").roomName("채팅방1")
	
	@Builder
	public ChatRoom(String roomId, String roomName) {
		// 생성자 생성 1번째 방법 : 생성자 지정
		this.roomId = roomId;
		this.roomName = roomName;
		
	}
	

}
