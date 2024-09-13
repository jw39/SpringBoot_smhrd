package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BootMember;
import com.example.demo.service.MemberService;

@RestController
public class MemberRestController {
	@Autowired
	MemberService service;

	@GetMapping("/member")
	public List<BootMember> getAllMembers() {
		// Jackson 라이브러리 : 자바 Object => JSON Object
		// -> Boot 에서 기본으로 가지고 있음!
		List<BootMember> list = service.getAllMembers();
		return list;
	}
	
	
	
	// 전체 회원 정보 조회
	@PostMapping("/joinasync")
	public String joinAsync(@ModelAttribute BootMember member) {
//		System.out.println(member.getId());
//		System.out.println(member.getPw());
//		System.out.println(member.getNick());
		service.join(member);
		return "ok";
	}
	
	// @DeleteMapping : 경로 변수를 표시하기 위해 메서드에 매개변수에 사용된다
	// 경로 변수는 중괄호{id}로 둘러싸인 값을 나타낸다
	@DeleteMapping("/delete/{uid}")// uid의 데이터 타입 Long
	public String delete(@PathVariable("uid")Long uid) {
		service.delete(uid);
		
		return "ok";
	}
	
	// 수정 위한 patch
	@PatchMapping("/update")
	public String update(@ModelAttribute BootMember member) {
		service.update(member);
		
		return "ok";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
