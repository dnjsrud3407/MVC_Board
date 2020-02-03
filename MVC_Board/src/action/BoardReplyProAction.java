package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardReplyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		// 파라미터 가져오기, BoardBean 객체(article)에 저장
		String page = request.getParameter("page");
		BoardBean article = new BoardBean();
		article.setBoard_re_ref(Integer.parseInt(request.getParameter("board_re_ref")));
		article.setBoard_re_lev(Integer.parseInt(request.getParameter("board_re_lev")));
		article.setBoard_re_seq(Integer.parseInt(request.getParameter("board_re_seq")));
		article.setBoard_name(request.getParameter("board_name"));
		article.setBoard_password(request.getParameter("board_pass"));
		article.setBoard_subject(request.getParameter("board_subject"));
		article.setBoard_content(request.getParameter("board_content"));
		
		// BoardReplyProService 클래스의 replyArticle() 메서드 호출
		BoardReplyProService boardReplyProService = new BoardReplyProService();
		boolean isReplySuccess = boardReplyProService.replyArticle(article);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(!isReplySuccess) {	// 답변 글 달기 실패할 경우
			out.println("<script>");
			out.println("alert('답변글 등록 실패!!')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			forward = new ActionForward();
			forward.setPath("BoardList.bo?page" + page);
			forward.setRedirect(true);
		}
		
		
		return forward;
	}

}
