package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {
	
	// enum => 정해져 있는 값(따로 정의) 중 하나만 선택하고 싶을 때
	public enum MessageType{
		ENTER, QUIT, TALK
		
	}
	
	private MessageType messageType;
	private String roomId; // 채팅방 번호
	private String sender; // 채팅 보낸 사람
	private String message;  // 채팅 내용
	

}
