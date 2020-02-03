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
#registForm {
	width: 500px;
	height: 600px;
	border: 1px solid red;
	margin: auto;
}
h2 {
	text-align: center;
} 
table {
	margin: auto;
	width: 450px;
}
textarea {
	resize: none;
}
.td_left {
	width: 150px;
	background: orange;
}
.td_right {
	width: 300px;
	background: skyblue;
}
#commandCell {
	text-align: center;
}
</style>
</head>
<body>

<!-- 게시판 등록 -->
<section id="writeForm">
<h2>게시판글수정</h2>
<form action="BoardModifyPro.bo" method="post" name="modifyform">
<input type="hidden" name="board_num" value="<%=article.getBoard_num()%>">
<input type="hidden" name="page" value="<%=nowPage%>"> 
<table>
	<tr>
		<td class="td_left">
			<label for="board_name">글쓴이</label>
		</td>
		<td class="td_right">
<%-- 			<%=article.getBoard_name()%> --%>
			<input type="text" name="board_name" id="board_name" value="<%=article.getBoard_name()%>" readonly="readonly" required="required">
		</td>
	</tr>
	<tr>
		<td class="td_left">
			<label for="board_pass">비밀번호</label>
		</td>
		<td class="td_right">
			<input type="password" name="board_pass" id="board_pass" required="required">
		</td>
	</tr>
	<tr>
		<td class="td_left">
			<label for="board_subject">제목</label>
		</td>
		<td class="td_right">
			<input type="text" name="board_subject" id="board_subject" value="<%=article.getBoard_subject()%>" required="required">
		</td>
	</tr>
	<tr>
		<td class="td_left">
			<label for="board_content">내용</label>
		</td>
		<td class="td_right">
			<textarea name="board_content" id="board_content" required="required" rows="15" cols="40"><%=article.getBoard_content()%></textarea>
		</td>
	</tr>
</table>
<section id="commandCell">
	<input type="submit" value="[수정]">&nbsp;&nbsp;
	<input type="button" value="[뒤로]" onclick="history.go(-1)">
</section>
</form>


</section>
</body>
</html>



























































