package com.example.demo.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.UserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private UserDetailsService detailService;
	private JwtUtil jwtUtil;
	   
	public JwtAuthenticationFilter(UserDetailsService userDetailService, JwtUtil jwtUtil) {
		this.detailService = userDetailService;
	    this.jwtUtil = jwtUtil;
	    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String autorizationHeader = request.getHeader("Authorization"); // 토큰
		
		if(autorizationHeader!=null &&  autorizationHeader.startsWith("Bearer ")) {
			
			String token = autorizationHeader.substring(7);
			
			// token 유효성 검증
			if(jwtUtil.validateToken(token, request)) {
				
				// token 에서 claim 파싱 후 이메일만 반환
				String email = jwtUtil.getUserId(token);
				
				UserDetails userDetails = detailService.loadUserByUsername(email);
				
				if(userDetails!=null) { //토큰 발급
					
					
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					
				}
				
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}

	

}
