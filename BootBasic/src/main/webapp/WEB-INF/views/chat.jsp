<%@page import="com.example.demo.model.BootMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>Insert title here</title>
<link rel="shortcut icon" href="#">
<style>
   #msgArea{
      width : 200px;
      height : 300px;
      /*background-color: whitesmoke;*/
      background-color: pink;
      overflow:auto;
   }
</style>
</head>
<body>
	<%
		BootMember member = (BootMember)session.getAttribute("member");
		String nick = member.getNick();
	%>


	<div class="container">
		<div class="col-6">
			<label><b>채팅방</b></label>
			<div class="input-group-append">
				<button type="button" id="button-quit" onclick="quit()">방나가기</button>
			</div>
		</div>
		<div>
			<div id="msgArea" class="col"></div>
			<div class="col-6">
				<div class="input-group mb-3">
					<input type="text" id="msg" class="form-control" aria-label="Recipient's username" aria-describedby="button-addon2">
					<div class="input-group-append">
						<button class="btn btn-outline-secondary" type="button" onclick="send()" id="button-send">전송</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script>
		// 클라이언트 행동들은 여기다 작성
		// 1. 소켓 연결 (소켓 객체)
		const webSocket = new WebSocket("ws://localhost:8090/myapp/ws/chat")
		
		// 2. 연결 / 해제 / 메세지
		webSocket.onopen = onOpen; // 채팅방 입장
		webSocket.onmessage = onMessage; // 서버로부터 메시지를 받았을 때 ENTER, TALK
		
		function onOpen(){ // 연결(입장) 했을 때를 정의
			// 서버에 메시지 보내기 (ENTER, 채팅방 아이디 (roomId), sender)
		    // 									모델의 키 값을 적어야 모델에서 값을 꺼내올 수 있음
			var msg = {"messageType":"ENTER", "roomId":"${roomId}", "sender":"<%=nick%>"}  // 값이 여러 개라 json 타입으로 정의, 
			//model/ChatMessage에서 지정한 필드 이름 따라서 해야 함. 키 값이 그 형태로 지정되어야 함
			webSocket.send(JSON.stringify(msg))
			// json 문자열로 보내기?
		}
		
		
		function quit(){ // 채팅방 나갈 때
			var msg = {"messageType":"QUIT", "roomId":"${roomId}", "sender":"<%=nick%>"}  // 값이 여러 개라 json 타입으로 정의, 
			webSocket.send(JSON.stringify(msg))
			webSocket.close()	// 연결 해제
			location.href="/myapp/room"
		}
		
		// 클라이언트가 서버에게 메시지를 보낼 때 호출
		function send(){
			let msg = $("#msg").val()
			let talkMsg = {"messageType":"TALK", "roomId":"${roomId}", "sender":"<%=nick%>", "message":msg}
			webSocket.send(JSON.stringify(talkMsg))
			$("#msg").val("")
		}
		
		
		// 서버러부터 메시지를 받았을 때 호출
		function onMessage(msg){
			var data = msg.data
	          var msgData = JSON.parse(data)
	          
	       // 누가 입장했는지 파악
	          if(msgData.sender == "<%=nick%>"){ // 내가 입장했을 때 (ENTER X , TALK O)
	          	if(msgData.messageType != 'ENTER'){
	          		var str = "<div align ='right'>";
	 	            str += "<b>" + msgData.message + " : " +  msgData.sender + "</b>";
	 	            str += "</div>";
	 	            $("#msgArea").append(str);
	          	}
	          }else{ // 다른 사람들이 입장하거나 나가거나 채팅을 보냈을 때
	        	  if(msgData.messageType == 'ENTER'){ // 입장했을 때
	        		  var str = "<div>";
	  	              str += "<b>" + msgData.sender +"님이 입장했습니다</b>";
	  	              str += "</div>";
	  	              $("#msgArea").append(str);
	        	  } else if (msgData.messageType=='QUIT'){ // 퇴장했을 때
	        		  var str = "<div>";
	  	              str += "<b>" + msgData.sender +"님이 퇴장했습니다</b>";
	  	              str += "</div>";
	  	              $("#msgArea").append(str);
	        	  }else { //채팅 보냈을 때
	        		  var str = "<div>";
	  	           	  str += "<b>" + msgData.sender + " : " + msgData.message + "</b>";
	  	              str += "</div>";
	  	              $("#msgArea").append(str);
	        		  
	          	  }
	          }
		}
	</script>

</body>
</html>