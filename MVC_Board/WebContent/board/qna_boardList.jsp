<%@page import="vo.PageInfo"%>
<%@page import="vo.BoardBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	ArrayList<BoardBean> articleList = (ArrayList<BoardBean>)request.getAttribute("article");
	PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
	int listCount = pageInfo.getListCount();
	int nowPage = pageInfo.getPage();	// page 는 중복이므로 nowPage로 사용
	int maxPage = pageInfo.getMaxPage();
	int startPage = pageInfo.getStartPage();
	int endPage = pageInfo.getEndPage();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#registFrom {
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
#tr_top {
	background: orange;
	text-align: center;
}
#pageList {
	margin: auto;
	width: 500px;
	text-align: center;
}
#emptyArea {
	margin: auto;
	width: 500px;
	text-align: center;
}
</style>
</head>
<body>
	<section id="listForm">
		<h2>
			글목록<a href="BoardWriteForm.bo">게시판 글쓰기</a>
		</h2>
		<table>
			<%
			if(articleList != null && listCount > 0){
			%>
				<tr id="tr_top">
					<td>번호</td>
					<td>제목</td>
					<td>작성자</td>
					<td>날짜</td>
					<td>조회수</td>
				</tr>
				<%
				for(int i = 0; i < articleList.size(); i++){
				%>
				<tr>
					<td><%=articleList.get(i).getBoard_num() %></td>
					<td>
					
				<%
				if(articleList.get(i).getBoard_re_lev() != 0){%>
				<%for(int a = 0; a <= articleList.get(i).getBoard_re_lev() * 2; a++){%>
				&nbsp;
				<%}%> ▶
				<%}else{ %> ▶ <%}%>
					<!-- 게시물 제목 클릭 시 게시물 번호와 페이지 번호 파라미터로 전달 -->
					<a href="BoardDetail.bo?board_num=<%=articleList.get(i).getBoard_num()%>&page=<%=nowPage%>">
					<%=articleList.get(i).getBoard_subject() %>
					</a>
					</td>
					<td><%=articleList.get(i).getBoard_name() %></td>
					<td><%=articleList.get(i).getDate() %></td>
					<td><%=articleList.get(i).getBoard_readcount() %></td>
				</tr>
				<%
				}
				%>
		</table>
	</section>
	<section id="pageList">
		<%if(nowPage <= 1){ %>
			[이전]&nbsp;
		<%} else{ %>
			<a href="BoardList.bo?page=<%=nowPage-1%>">[이전]</a>&nbsp;
		<%}%>
		
		<%for(int a = startPage; a <= endPage; a++){
			// 현재 페이지이면 하이퍼링크 걸지 않음
			if(a == nowPage){ %>
				[<%=a %>]
			<%} else{ %>
				<a href="BoardList.bo?page=<%=a %>">[<%=a %>]
				</a>&nbsp;
			<%}
		}%>
		<%if(nowPage >= maxPage){ %>
			[다음]
		<%} else{ %>
			<a href="BoardList.bo?page=<%=nowPage+1 %>">[다음]</a>
		<%}%>
	</section>
	<section>
		<input type="button" value="게시판 글쓰기" onclick="location.href='BoardWriteForm.bo'">
	</section>
	<%
	} else {
	%>
		<section id="emptyArea">등록된 글이 없습니다.</section>
	<%	
	}
	%>
</body>
</html>