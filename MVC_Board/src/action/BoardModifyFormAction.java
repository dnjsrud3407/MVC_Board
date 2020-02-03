package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String page = request.getParameter("page");
		
		
		// 원본 게시물 내용을 가져옴
		// -> 기존의 정의된 BoardDetailService 클래스 사용
		BoardDetailService boardDetaionService = new BoardDetailService();
		// BoardDetailService의 getArticle() 만 사용하기 때문에 조회수 증가는 안 됨
		BoardBean article = boardDetaionService.getArticle(board_num);
		
		// 원본 게시물과 페이지 번호를 request 객체에 저장
		request.setAttribute("article", article);
		request.setAttribute("page", page);
		
		
		// board 폴더 내의 qna_board_modify.jsp 페이지로 이동
		// DB 작업이 필요없기 때문에 dispatch 방식!
		forward = new ActionForward();
		forward.setPath("./board/qna_board_modify.jsp");
		
		return forward;
	}

}
