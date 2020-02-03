package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDeleteProService;
import vo.ActionForward;

public class BoardDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String board_pass = request.getParameter("board_pass");
		String page = request.getParameter("page");
		
		BoardDeleteProService boardDeleteProService = new BoardDeleteProService();
		// BoardModifyService의 isArticleWriter과 동일
		boolean isRightUser = boardDeleteProService.isArticleWriter(board_num, board_pass);
		
		// 리턴된 isArticleWriter 가 false 일 경우
		// 자바스크립트 사용
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(!isRightUser) {
			out.println("<script>");
			out.println("alert('삭제 권한이 없습니다!!')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			boolean isDeleteSuccess = boardDeleteProService.removeArticle(board_num);
			if(!isDeleteSuccess) {
				out.println("<script>");
				out.println("alert('삭제 실패!!')");
				out.println("history.back()");
				out.println("</script>");
			} else {
				forward = new ActionForward();
				forward.setPath("BoardList.bo?page=" + page);
				forward.setRedirect(true);
			}
		}
		
		return forward;
	}

}
