<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int board_num = Integer.parseInt(request.getParameter("board_num"));
	String nowPage = request.getParameter("page");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#passForm {
		width: 400px;
		margin: auto;
		border: 1px solid orange;
	}
</style>
</head>
<body>
	<section id="passForm">
		<form name="deleteForm" action="BoardDeletePro.bo?board_num=<%=board_num%>" method="post">
			<input type="hidden" name="page" value="<%=nowPage%>">
			<table>
				<tr>
					<td>
						<label>글 비밀번호 : </label>
					</td>
					<td>
						<input name="board_pass" type="password" required="required">
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" valud="삭제">&nbsp;&nbsp;
						<input type="button" value="돌아가기" onclick="history.back()"> 
					</td>
				</tr>
			</table>
		</form>
	</section>
</body>
</html>