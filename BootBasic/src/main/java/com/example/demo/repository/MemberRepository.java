package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BootMember;

// JPA를 사용하기 위해서는 특정 객체를 다룰 Repository 인터페이스 필수로 만들기
// extends JpaRepository<객체 타입, 기본 키의 자료형 타입>
// 테이블의 각 한 줄이 부트 멤버..?
public interface MemberRepository extends JpaRepository<BootMember, Long>{
	// select * from boot_member where id=? and pw=?
	public BootMember findByIdAndPw(String id, String pw);
	

}
