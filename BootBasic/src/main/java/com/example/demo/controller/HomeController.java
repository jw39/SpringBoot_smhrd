package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class HomeController {

//	[GET]localhost:8090/myapp/
	@GetMapping(value="/")
	// get 방식으로 요청이 왔을 때 받아줄 거야!
	public String indexPage() {
		return "index";
	}
	
	// room.jsp 리턴하는 함수 생성? 여기는 뭐하는 곳인거지?
	@GetMapping(value="/room")
	public String roomPage() {
		return "room";
	}
	
	@GetMapping("/chat")
	public String chatPage() {
		return "chat";
	}
	
	@GetMapping("/chat/{roomId}")
	public String chatpage(@PathVariable("roomId")String roomId, Model model) {
		// 룸 아이디를 따로 저장하고 있는 상태에서 보낸다? 모델에 저장할 거임
		model.addAttribute("roodId", roomId);
		return "chat";
	}
	
	
	
}
