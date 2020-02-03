package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardListService;
import vo.ActionForward;
import vo.BoardBean;
import vo.PageInfo;

public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		System.out.println("BoardListAction");
		ActionForward forward = null;
		
		// 현재 페이지 번호 및 한 페이지당 게시글 수
		int page = 1;
		int limit = 10;
		
		// 파라미터에 page값이 있는 경우
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} 
		
		// BoardListService 에서 게시글 수 가져오기
		BoardListService boardListService = new BoardListService();
		int listCount = boardListService.getListCount();
//		System.out.println("총 게시글 갯수 : " + listCount);
		
		// BoardListService 객체의 getArticleList() 메서드 호출
		ArrayList<BoardBean> articleList = boardListService.getArticleList(page, limit);
//		System.out.println("articleList 들고옴");
		
		// 페이지 계산
		// 1. 총 페이지 수 계산
		int maxPage = (int)((double)listCount / limit + 0.95);
		// 2. 시작 페이지 번호
		int startPage = (((int)((double)page / 10 + 0.9)) - 1) * 10 + 1;
		// 3. 마지막 페이지 번호
		int endPage = startPage + 10 - 1;
		
		// 마지막 페이지 번호가 총 페이지 수보다 클 경우
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		PageInfo pageInfo = new PageInfo(page, maxPage, startPage, endPage, listCount);
		
		// request 객체의 setAttribute() 메서드를 호출하여 페이지 정보, 게시물 목록 저장
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("article", articleList);
		
		// ActionForward 객체를 생성하여 qna_boardList.jsp 페이지로 이동
		forward = new ActionForward();
		forward.setPath("/board/qna_boardList.jsp");
		
		return forward;
	}

}
