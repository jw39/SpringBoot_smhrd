package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BootMember;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {
	// service 쿼리문이 내장되어있는 함수들 집합소
	
	@Autowired //의존성 주입, member 레파지토리를 찾아서 repo라는 이름으로 쓰겠다
	MemberRepository repo;
	
	// 회원 가입
	public void join(BootMember member) {
		// 컨트롤러에서 요청 받을 거임. 사용자가 입력한 값을 넘겨주는 것.
		// 그니까 값을 가지고 있는 부트 멤버를 () 안에 적는 거임
		
		// insert into boot_member values (id, pw, nick)
		// 멤버 객체가 가지고 있는 값을 저장할 거야! 라는 의미
		repo.save(member); // 반환 타입 확인 필수
		// void니까 리턴 안 하고 멈춰~~
	}
	
	
	// 전체 회원 정보
	public List getAllMembers() {
		List<BootMember> list = repo.findAll(); //select * from boot_member = 레파지토리에서 BootMember를 가져와서 그 테이블 안 데이터를 가져올 수 있는 것
		// 테이블의 각 컬럼인 부트멤버들이 들어있는 list 형식으로 반환할 거야
		return list;
	}
	
	// 로그인
	public BootMember login(BootMember member) {
		return repo.findByIdAndPw(member.getId(), member.getPw());
	}
	
	// 삭제
	public void delete(Long uid) {
		repo.deleteById(uid);
		// 반환타입 없음, 호출만 하고 끝내주면 된다.
	}
	
	// 수정
	public void update(BootMember member) {
		// JPA 활용 update 하기
		// 1. 수정하고 싶은 행을 가져오기 (read) -> select
		// Optional<> => NPE 방지 (넘기는 기본키 값이 잘못된 경우)
		Optional<BootMember> find =  repo.findById(member.getUid());
		
		// 2. setter 메서드 사용해서 find 객체의 필드값 수정하기
		find.get().setPw(member.getPw());
		find.get().setNick(member.getNick());
		
		// 3. 수정된 값을 가진 find 객체의 값을 DB에 저장 (save)
		// * save : 무조건 insert를 호출하는 것은 아님
		// => uid를 사용해서 기존에 존재하는 uid를 가진 객체를 저장하는 경우에는 insert가 아닌 update를 해줌
		// 기존에 존재하지 않는 uid를 가진 객체를 저장하는 경우에는 insert해줌
		repo.save(find.get());
	}

}
