package com.example.demo.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Entity
@Table(name="USERS")
public class Users implements UserDetails{ //Security 에서 사용자 정보를 표현할 때 구현해줘야함! 
	
	@Id //pk 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) //1부터 들어감
	@Column(name="user_id")
	private Long id;
	
	@Column(nullable=false, unique=true, length=30)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING) // 지정된 값 그대로 문자열로 테이블에 추가
	private Role role; // 무조건 두 개(ADMIN, USER) 중 하나의 값만 가지게 할 예정. 타입을 하나 생성할 거임

	
	// 로그인 시에 자동으로 내부적으로 호출되는 메서드들 ** 재정의 필수!!
	// implements UserDetails 해서 생신 친구들
	// 권한 목록을 리턴해주는 메서드
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	// 사용자 패스워드 리턴
	@Override
	public String getPassword() {
		return password;
	}

	// 사용자 username(이메일) 리턴
	@Override
	public String getUsername() { // 로그인 시 입력되는 값이어야 함. 우리는 아이디에 이메일을 입력함!!
		return email;
	}
	
	public Long getId() {
		return id;
	}
	
	public Role getRole() {
		return role;
	}

}
