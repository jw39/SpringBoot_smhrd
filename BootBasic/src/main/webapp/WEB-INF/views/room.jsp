<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<input type="text" id="room-name">
	<button onclick="createRoom()">채팅방 생성</button>
	<table id="rooms"></table>
	
	<script>
	
	// 화면이 준비되면 바로 showRooms 함수 실행
	$(document).ready(function(){
		showRooms()
	})
	
	function showRooms(){
		$.ajax({
			url : "rooms",
			type : "get",
			success : roomsHtml,
			error : function(){
				alert("오류")
			}
		})
	}
	
	
	function roomsHtml(data){
		let html = ""
		for(i in data){  // i에 저장되는 값은 인덱스
			html += "<tr>"
			html += "<td>"+data[i].roomName+"</td>"
			// get과 비슷, 주소창의 주소를 바꾸는 역할
			html += "<td><button onclick='location.href=\"chat/"+data[i].roomId+"\"'>입장</button></td>"
			html += "</tr>"
			
		}
		$("#rooms").html(html)
	}
	
	
		// 서버로 요청하는 거 생성
		function createRoom(){
			$.ajax({
				url : "create",
				type : "post",
				// data는 json 방식으로 직접 전달, key는 roomName, value는 input 태그의 value만 보내겠다.
				data : {"roomName" : $("#room-name").val()},
				success : function(data){
					showRooms()
				},
				error : function(){
					alert("ers")
				}
			})
		}
	
	</script>

</body>
</html>