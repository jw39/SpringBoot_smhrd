package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.jwt.CustomAuthenticationEntryPoint;
import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtExceptionFilter;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.UserDetailService;

import jakarta.servlet.DispatcherType;

@Configuration // 설정파일
@EnableWebSecurity // spring Security 활성화
				// 내부적으로 springSecurityFilterChain 동작
public class SecurityConfig {
	
	@Autowired 
	UserDetailsService userDetailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	
	// css, js, 이미지 (정적 리소스) => 정적 리소스가 제대로 적용되지 않는 상황 발생
	@Bean //수동으로 객체 생성
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/static/**"); //static 폴더 안에 있는 건 활성화시키지 않겠다 라는 의미의 코드
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
		.csrf(csrf -> csrf.disable()) //GET 요청 제외 다른 요청으로부터 보호, csrf 토큰 포함(토큰 없으면 위조 요청으로 판단), rest api 사용할 땐 설정할 필요 없음
		// http 세션에 대해 설정해주는 것. 세션 관리 상태 없음으로 구성하는 코드임! 유저 아이디 등 세션에 안 담을 거라서...
		.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS))
		.httpBasic(AbstractHttpConfigurer::disable) // http 기반 기본 인증 비활성화
		.addFilterBefore(new JwtAuthenticationFilter(userDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
		.formLogin(form -> form.disable()) // 로그인 폼 비활성화)
		.authorizeHttpRequests(auth -> auth
				//.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
				//.requestMatchers("/", "/join", "/login", "api/member/join").permitAll() // 인증 없이도 접근 가능한 경로 설정
				.requestMatchers("api/member/*").permitAll() // 인증 없이도 접근 가능한 경로 설정
				.requestMatchers("/admin").hasRole("ADMIN") //ADMIN 권한이 있을 때에만 접근 가능한 경로 설정
				.anyRequest().authenticated() // 그 외 요청은 무조건 인증 확인하겠다!
		)
		.exceptionHandling(exceptionHandling-> exceptionHandling
	            .AuthenticationEntryPoint(new CustomAuthenticationEntryPoint()))
		; // 특정한 경로에 대해 인증을 허용하거나 등등등
//		.formLogin(form -> form
//				.loginPage("/login") // 로그인 페이지 설정
//				.defaultSuccessUrl("/") //로그인 성공 시 요청 url (index.jsp)
//				.failureUrl("/login") //로그인 실패 시 요청 url 
//				.permitAll()
//		)
//		.logout(logout -> logout
//				.logoutUrl("/logout") // 로그아웃 url 설정
//				.logoutSuccessUrl("/login") // 로그아웃 후 url 
//				.permitAll()
//		)
//		.userDetailsService(userDetailService);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// CORS - Cross-origin resource sharing (교차 출처 리소스 공유)
	// 도메인 밖의 다른 도메인으로부터 요청할 수 있게 허용
	@Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3001");// 리액트 서버
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); //모든 경로에 대해 CORS 설정

        return new CorsFilter(source);
    }
	
	// 로그인 시 사용 => jwt(web token)
	// 인증 공급자 : userDetailService에서 username 사용해서 없으면 인셉션, 있으면 사용자 정보 userDetailService로 반환하는 메서드?
	// userDetailService, PasswordEncoder 활용 인증 논리 구현
	@Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }
	
	// 인증 관리자 : 인증 공급자를 활용 인증 처리
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	

}


