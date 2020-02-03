<%@page import="vo.BoardBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	BoardBean article = (BoardBean)request.getAttribute("article");
	String nowPage = (String)request.getAttribute("page");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#articleForm {
	width: 500px;
	height: 500px;
	border: 1px solid red;
	margin: auto;
}
h2 {
	text-align: center;
}
#basicInfoArea {
	height: 40px;
	text-align: center;
}
#articleContentArea {
	background: orange;
	margin-top: 20px;
	height: 350px;
	text-align: center;
	margin: auto;
}
#commandList {
	margin: auto;
	width: 500px;
	text-align: center;
}
</style>
</head>
<body>
	<!-- 게시판 수정 -->
	<section id="articleForm">
		<h2>글 내용 상세보기</h2>
		<section id="basicInfoArea">
			제목 :
			<%=article.getBoard_subject() %>
			첨부파일 :
			<%if(article.getBoard_file() != null){ %>
				<!-- 파일 이름 클릭시 새 창에서 다운로드 작업 -->
				<a href="BoardFileDown.bo?file_name=<%=article.getBoard_file()%>" target="blank">
					<%=article.getBoard_file() %>
				</a>
			<%} %>
		</section>
		<section id="articleContentArea">
			<%=article.getBoard_content() %>
		</section>
	</section>
	<section id="commandList">
		<a href="BoardReplyForm.bo?board_num=<%=article.getBoard_num()%>&page=<%=nowPage%>">[답변] </a>
		<a href="BoardModifyForm.bo?board_num=<%=article.getBoard_num()%>&page=<%=nowPage%>">[수정] </a>
		<a href="BoardDeleteForm.bo?board_num=<%=article.getBoard_num()%>&page=<%=nowPage%>">[삭제]</a>
		<a href="BoardList.bo?page=<%=nowPage%>">[목록] </a>
		&nbsp;&nbsp;
	</section>
</body>
</html>