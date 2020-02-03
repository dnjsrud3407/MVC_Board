package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		// 파라미터 들고오기
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String page = request.getParameter("page");
		String board_pass = request.getParameter("board_pass");
		

		// 본인 확인 작업 => BoardModifyProService 클래스의 isArticleWriter() 메서드 호출
		// 파라미터 : 폼에서 입력받은 board_pass와 board_num 
		boolean isRightUser = false;
		BoardModifyProService boardModifyProService = new BoardModifyProService();
		isRightUser = boardModifyProService.isArticleWriter(board_num, board_pass);
		
		
		// 리턴된 isArticleWriter 가 false 일 경우
		// 자바스크립트 사용
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(!isRightUser) {
			out.println("<script>");
			out.println("alert('수정 권한이 없습니다!!')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			// BoardBean 객체에 수정된 게시물 내용 저장
			BoardBean boardBean = new BoardBean();
			boardBean.setBoard_num(board_num);
//			boardBean.setBoard_name(request.getParameter("board_name"));
			boardBean.setBoard_subject(request.getParameter("board_subject"));
			boardBean.setBoard_content(request.getParameter("board_content"));
			
			// BoardModifyProService 클래스의 modifyArticle() 메서드를 호출
			// 파라미터 BoardBean 객체, 리턴 타입 : boolean(isModifySuccess)
			boolean isModifySuccess = boardModifyProService.modifyArticle(boardBean);
			
			
			// isModifySuccess가 false 인 경우
			// => 자바스크립트 사용
			// isModifySuccess가 true 인 경우
			// BoardDetail.bo 로 이동
			if(!isModifySuccess) {
				out.println("<script>");
				out.println("alert('수정실패!!')");
				out.println("history.back()");
				out.println("</script>");
			} else {
				forward = new ActionForward();
				forward.setPath("BoardDetail.bo?board_num=" + board_num + "&page=" + page);
				forward.setRedirect(true);
			}
			
		}
		
		return forward;
	}

}
