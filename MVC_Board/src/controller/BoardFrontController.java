package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.BoardDeleteAction;
import action.BoardDeleteProAction;
import action.BoardDetailAction;
import action.BoardListAction;
import action.BoardModifyFormAction;
import action.BoardModifyProAction;
import action.BoardReplyFormAction;
import action.BoardReplyProAction;
import action.BoardWriteProAction;
import vo.ActionForward;



@WebServlet("*.bo")
public class BoardFrontController extends HttpServlet {

	// Get방식, Post방식 상관없이 doProcess 함수가 호출됨!!
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("BoardFrontController");
		request.setCharacterEncoding("utf-8");
		
		//------------------예전 방식----------------------
//		String requestURI = request.getRequestURI();
//		String contextPath = request.getContextPath();
//		String command = requestURI.substring(contextPath.length());
//		
//		System.out.println("requestURI : " + requestURI);
//		System.out.println("contextPath : " + contextPath);
//		System.out.println("command : " + command);
		
		// 요청된 서블릿 주소를 바로 추출
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		// Action 인터페이스 - 다형성을 이용하여 다른 Action클래스 선언 시 사용
		Action action = null;			
		// Action 클래스의 execute() 메서드의 리턴 형
		// (path와 포워딩 방식을 저장함)
		ActionForward forward = null;	
		
		
		
		//=================================================================================
		
		// 서블릿 주소에 따라 각각 다른 작업을 수행
		if(command.equalsIgnoreCase("/BoardWriteForm.bo")) {
//			System.out.println("qna_board_write.jsp 페이지로 이동");
//			response.sendRedirect("./board/qna_board_write.jsp");	// 현재 폴더 : WebContent	/ 	./ : 현재 폴더의 최상위  
			// ----------- 위 방법은 코드가 길어지기 때문에 사용안함 ------------
			
			// 글쓰기 페이지(DB) 작업이 필요없기 때문에 Action 클래스 생성 X			
			forward = new ActionForward();
			forward.setPath("./board/qna_board_write.jsp");
//			forward.setRedirect(false);	= 기본값
		} else if(command.equalsIgnoreCase("/BoardWritePro.bo")) {
			// 글등록하는 DB작업이 필요하기 때문에 BoardWriteProAction 객체 생성
			// 객체 내에서 포워딩 방식 설정
			action = new BoardWriteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardList.bo")) {
			action = new BoardListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardDetail.bo")) {
//			System.out.println("BoardDetail.bo!!");
			action = new BoardDetailAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardFileDown.bo")) {
			forward = new ActionForward();
			// file_down.jsp 다운로드 창 열기 - 저절로 창 닫혀짐
			forward.setPath("./board/file_down.jsp");
		} else if(command.equalsIgnoreCase("/BoardModifyForm.bo")) {
			// --- 수정하기 폼
			// DB 작업을 통해 원본 게시물을 가져와야함
			action = new BoardModifyFormAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardModifyPro.bo")) {
			// --- 수정하기 작업
//			System.out.println("BoardModifyPro.bo");
			// 수정하기는 DB작업이 필요하기 때문에 BoardModifyProAction 클래스가 필요하다
			action = new BoardModifyProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardDeleteForm.bo")) {
			// --- 삭제하기 폼
			// password 값만 입력받으면 되기 때문에 Action클래스 필요 X
			forward = new ActionForward();
			forward.setPath("./board/qna_board_delete.jsp");
		} else if(command.equalsIgnoreCase("/BoardDeletePro.bo")) {
			// ---  삭제하기 작업
			action = new BoardDeleteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardReplyForm.bo")) {
			// --- 답변글 폼
			// 원본 글을 들고와야하기 때문에 BoardReplyFormAction에서 DetailService가 필요하다!
			// 글쓰는 jsp 페이지로 이동(dispatch 방식)
			// -> BoardReplyAction에서 DetailService 수행
			// => Form 이라고 해서 전부 jsp작업으로 가는 것은 아님!
			action = new BoardReplyFormAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equalsIgnoreCase("/BoardReplyPro.bo")) {
			// --- 답변글 등록 작업
			action = new BoardReplyProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		//=================================================================================
		// ActionForward 객체의 포워딩 방식
		if(forward != null) {
			if(forward.isRedirect()) {	// true일 경우 -> Redirect 방식
				// redirect 는 페이지 이동시 
				// 서블릿 주소 -> 서블릿 주소
				// - 주소가 새롭게 변하는 경우 사용 ( 예) writepro -> list로 페이지 넘어가야하는 경우)
				// - 전 페이지의 request 객체를 전달할 필요가 없을 경우
				response.sendRedirect(forward.getPath());
			} else {					// false일 경우 -> Dispatch 방식
				// 서블릿 주소 -> VIEW페이지 (jsp)
				// dispatch 는 페이지 이동시 
				// - 주소가 똑같이 유지되는 사용
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		} else {
			System.out.println("ActionForward 값이 null입니다");
		}
	}	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
}
