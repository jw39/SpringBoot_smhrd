package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 클래스 명으로 테이블 생성된 것을 확인할 수 있음
@NoArgsConstructor 
@Setter
@Getter
// JPA (Java Persistence A)
// : ORM (Object-Relational Mapping) 기술의 표준 (JAVA)
@Entity //JPA Entity 추가!!! (테이블 생성 어노테이션)
public class BootMember {
	
	@Id //primary key를 나타내기 위해 사용
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 1부터 자동으로 uid가 들어감
	
	private Long uid; // 숫자 형태
	private String id;
	private String pw;
	private String nick;

}
