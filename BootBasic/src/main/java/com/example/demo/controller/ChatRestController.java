package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ChatRoom;
import com.example.demo.service.ChatService;

@RestController
public class ChatRestController {
	
	@Autowired
	ChatService service;
	
	// 채팅방 만들어줘 라는 요청이 들어왔을 때!
	// 그럼 여기는 myapp/room 부터 시작하는 건가?
	
	@PostMapping("create")  // room.jsp에서 data 보낼 때 적은 키 값을 적으면 된다. 데이터 형식은 String
	public ChatRoom createRoom(@RequestParam("roomName")String roomName) {
		System.out.println(roomName);
		return service.createRoom(roomName);
		// 사용자가 적은 roomName을 출력해보니 잘 나온다	
	}
	
	
	@GetMapping("rooms")
	// < 객체 ?>
	public List<ChatRoom> findAllRoom() {
		return service.findAllRooms();
	}
	

}
