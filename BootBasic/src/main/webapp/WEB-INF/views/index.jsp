<%@page import="com.example.demo.model.BootMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="#">
<meta charset="UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<button onclick="getAllMembers()">전체 회원 정보 (비동기-js(jquery))</button>
	<table border=1 id="list">
	<!-- id가 list인 놈인 테이블에게 PringList for문 돌린 거 삽입해 프론트에 뿌려줄 예정 -->
	
	</table>
	<!-- 회원 정보 수정 -->
	<form id="frm2">
		<input type="hidden" name="uid" id="fuid">
		아이디 : <input type="text" name="id" id="fid" readonly> <br> <!-- id는 수정 불가능하게 readonly 속성 넣었음 -->
		비밀번호 : <input type="text" name="pw" id="fpw"> <br> 
		닉네임 : <input type="text" name="nick" id="fnick"> <br> 
		<input type="button" onclick="update()" value="정보수정(비동기)">
	</form>
	
	<!-- 동기방식, 이름이 login이고 매핑 메서드가 post 놈을 알아서 찾아감 -->
	<form action="login" method="post"> 
		아이디 : <input type="text" name="id"> <br> 
		비밀번호 : <input type="text" name="pw"> <br> 
		<input type="submit" value="로그인(동기)">
	</form>
	
	<!-- 로그인 쪽 코드 -->
	<% BootMember member = (BootMember)session.getAttribute("member"); %>
	<% if(member!=null) { %>
		<b>닉네임 : <%= member.getNick() %></b>
		<button onclick="location.href='room'"><b>채팅룸 페이지 이동</b></button>
	<%} %>


	<form action="join" method="post"> 
		아이디 : <input type="text" name="id"> <br> 
		비밀번호 : <input type="text" name="pw"> <br> 
		닉네임 : <input type="text" name="nick"> <br> 
		<input type="submit" value="회원가입(동기)">
	</form>
	<!--  action와 submit은 동기 통신을 위한 거라 없애야함 -->
	
	
	<form id="frm">
		아이디 : <input type="text" name="id"> <br> 
		비밀번호 : <input type="text" name="pw"> <br> 
		닉네임 : <input type="text" name="nick"> <br> 
		<input type="button" onclick="joinAsync()" value="회원가입(비동기)">
	</form>

	<script>
	// 전체 회원 정보 조회
	// getAllMembers 이 함수 실행하면 쿼리문이 내장되어있는 함수를 불러오는 것
		function getAllMembers(){
			$.ajax({
				url : "member" , //member로 요청하면 전체 회원 정보 조회
				type : "get",
				success : printList,  // 성공하면 콘솔에 찍히는 거 확인했으니까 pringList 함수 실행하도록 바꿈
					// function(data){
					// console.log(data)
				error : function(){
					alert("오류 발생")
				}
			})
		}
	
	
	// 회원 정보를 불러오면 실행될 함수
	function printList(data){  // data: 서버에서 가져온 회원정보 리스트
		let html = ""
		
		for(i in data) { // i => index를 의미함 (회원정보 3개면 0, 1, 2 가져옴)
			html += "<tr>"
			html += "<td>"+data[i].id+"</td>"
			html += "<td>"+data[i].pw+"</td>"
			html += "<td>"+data[i].nick+"</td>"
			html += "<td><button onclick='deleteMember("+data[i].uid+")'>삭제</button></td>"
			html += "<td><button onclick='updateMember("+JSON.stringify(data[i])+")'>수정</button></td>"
													// object 형태라서 
			html += "</tr>"
		} 
		// html을 id가 list인 테이블에 넣겠다
		$("#list").html(html)
	}
	
	function updateMember(member){
		console.log(member)
		// 각 id, pw, nick에 대한 value 꺼내서 form태그에 보여줌
		$("#fuid").val(member.uid)
		$("#fid").val(member.id)
		$("#fpw").val(member.pw)
		$("#fnick").val(member.nick)
	}
	
    function update() {
        let frmData = $("#frm2").serialize()
        
        $.ajax({
           url : "update",
           type : "patch",
           data : frmData, // frm에 있는 값 보내기
           success : function(data) {
              console.log(data)
              	$("#fuid").val("")
				$("#fid").val("")
				$("#fpw").val("")
				$("#fnick").val("")
				getAllMembers()
           },
           error : function() {
              alert("오류 발생")
           }
        })
     }
	
	// 비동기 통신 회원가입
   	function joinAsync() {
		// 해당 form 태그 안에 있는 인풋 태그들에 작성되어있는 값 가져오기!
		let frmData = $("#frm").serialize()
		// serialize : form 객체 내용 한번에 받기
		// (표준 URL 인코딩 표기법 -> get 요청시에 사용하는 파라미터와 비슷)
		console.log(frmData);

		$.ajax({
			url : "joinasync",
			type : "post",
			data : frmData,
			success : {},
			error : function() {
				alert("오류발생")
			}

		})
	}

	
	function deleteMember(uid){
		
		// HTTP 요청 메서드
		// GET : READ (Selete) 빠르다
		// POST : CREATE (Insert) 보안 굿
		// DELETE : DELETE (Delete) -> GET과 비슷 
		// (GET과 비슷 GET은 데이터를 URL에 넣어서 보냄, POST는 BODY에 데이터를 넣음)
		// PUT : UPDATE (Update) -> 전체 수정할 때
		// PATCH : UPDATE (Update) -> 일부 수정할 때 (일반적으로 패치 사용) -> POST와 비슷, 데이터를 BODY에 실어서 보냄
		$.ajax({
			// 삭제할 때 url에 uid까지 실어서 보낸다!, 선택한 놈의 uid
			url : "delete/"+uid,
			type : "delete" , 
			success : function(data){
				console.log(data)
				getAllMembers()
			},
			error : function(){
				console.log("오류 발생")
			}
			
		
		})

	}
		
	
	</script>

</body>
</html>