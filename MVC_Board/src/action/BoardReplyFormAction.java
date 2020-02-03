package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String page = request.getParameter("page");
		
		// BoardDetailService의 getArticle() 로 원본글 상세 정보 가져오기
		BoardDetailService boardDetailService = new BoardDetailService();
		BoardBean article = boardDetailService.getArticle(board_num);
		
		request.setAttribute("article", article);
		request.setAttribute("page", page);
		
		forward = new ActionForward();
		forward.setPath("./board/qna_board_reply.jsp");
		
		return forward;
	}

}
