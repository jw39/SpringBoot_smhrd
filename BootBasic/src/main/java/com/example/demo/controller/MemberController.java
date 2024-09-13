package com.example.demo.controller;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.BootMember;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller  // View(.jsp)를 반환 => 화면 자체가 새롭게 응답이 된다
// 비동기 통신 => 전체 화면이 바뀌는게 아니라 일부만 바뀌는 통신 방법
			// 뷰 전체를 응답받는게 아니라 일부 데이터만 응답 받음
			// @COntroller + @ResponseBody
			// => @ResController : data(model) 반환이 기본!

public class MemberController {
	
	@Autowired // 의존성 주입 , service 객체..를 찾기 위해서 작성
	MemberService service;
	
	
	// @PostMapping : http 통신할 때 post의 body에 data를 넣어서 보내겠다는 의미
	// @ModelAttribute : 사용자가 요청시 전달하는 값을 오브젝트 형태로 매핑해주는 어노테이션
	@PostMapping("/join") // 폼 태그에서 포스트로 전달. 주소도 join으로..
	// model 안에 있는 이름이랑 컬럼이랑 같네? 전달 ㄱㄱ
	public String join(@ModelAttribute BootMember member) {
//		System.out.println(member.getId());
//		System.out.println(member.getPw());
//		System.out.println(member.getNick());
//		Mybatis : controller(요청/응답(view or data)) -> service -> mapper
//		JPA     : controller -> service -> repository(Interface -> sql 작성 X, 정의 되어있는 메서드 호출)
		service.join(member); // 서비스 조인 메서드에 멤버를 전달하겠다!
		
//		return "index"; 페이지 변환 없이 join에서 myapp 화면이 보이는 거고
		return "redirect:/"; // 얘는 페이지 변환!, 기본 경로로 가겠다~
		
	}
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute BootMember member, HttpSession session) {
		BootMember result = service.login(member);
		session.setAttribute("member", result);
		return "redirect:/";
		
	}
	

}
