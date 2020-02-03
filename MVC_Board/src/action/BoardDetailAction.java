package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String page = request.getParameter("page");
//		System.out.println("board_num : " + board_num + ", page : " + page);
		
		// BoardDetailService의 getArticle() 메서드를 호출하여 게시물 정보 가져오기
		BoardDetailService boardDetailService = new BoardDetailService();
		BoardBean article = boardDetailService.getArticle(board_num);
		
		// BoardDetailService 객체가 null이 아닐 경우 조회수 1증가 필요!
		if(article != null) {
			boardDetailService.plusReadcount(board_num);
		}
		
		// 게시물 정보(BoardBean article), 페이지 번호(page)를 request객체에 저장
		request.setAttribute("article", article);
		request.setAttribute("page", page);
		
		// qna_board_view.jsp 로 이동 설정
		forward = new ActionForward();
		forward.setPath("./board/qna_board_view.jsp");
		
		
		return forward;
	}

}
