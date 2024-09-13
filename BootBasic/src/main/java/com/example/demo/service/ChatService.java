package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.model.ChatRoom;

@Service
public class ChatService {
   // 생성된 채팅 룸 정보 저장할 맵 <key(임의로 생성할 roomId-value)>
	// 		   키의 타입, 값의 타입
   private Map<String, ChatRoom> chatRooms = new HashMap<>();
   
   // 모든 채팅방 불러오기
   public List<ChatRoom> findAllRooms() {
	   return new ArrayList<>(chatRooms.values());
	   
   }
   
   // 새 채팅방 생성
   public ChatRoom createRoom(String roomName) {
      
      String roomId = UUID.randomUUID().toString();// 유일한 랜덤 문자열 사용
      
      // 채팅룸에 대한 객체 생성 
      ChatRoom room = ChatRoom.builder()
                  .roomId(roomId)
                  .roomName(roomName)
                  .build();
      
      // 이걸 챗룸스에 계속 저장?
      chatRooms.put(roomId, room);
      
      return room;
   }
   
}
