package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardWriteProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		// 작성된 게시물 정보를 가져와서 DB추가를 위한 준비 작업 및 이동 작업 수행
		BoardBean boardBean = null;
		
		// 파일 업로드를 위한 업로드 정보 설정
		String saveFolder = "/boardUpload";		// 가상위치
		// 가상위치에 대한 실제폴더의 위치를 설정
		ServletContext context = request.getServletContext();	// 톰캣 객체를 들고옴
		String realFolder = context.getRealPath(saveFolder);
		
		// 파일 업로드 최대 크기
		int fileSize = 1024 * 1024 * 10;		// 최대 5MB까지 (단위 용량으로 표기)
		
		// 파일 업로드를 위한 MulipartRequest 객체 필요
		MultipartRequest multi = new MultipartRequest(
				request,
				realFolder,		 
				fileSize, 
				"UTF-8", 
				new DefaultFileRenamePolicy()  // 중복된 이름이 있을 때 처리
				);
		
		// MulipartRequest 객체로부터 전달된 파라미터들 가져오기
		// ======> multi.getParameter("");  사용해야함!
		boardBean = new BoardBean();
		boardBean.setBoard_name(multi.getParameter("board_name"));
		boardBean.setBoard_password(multi.getParameter("board_password"));
		boardBean.setBoard_subject(multi.getParameter("board_subject"));
		boardBean.setBoard_content(multi.getParameter("board_content"));
		
		// 파일명을 가지고 올때는 이렇게 사용하면 안된다
//		boardBean.setBoard_file(multi.getParameter("board_file"));
		boardBean.setBoard_file(multi.getOriginalFileName((String)multi.getFileNames().nextElement()));
		
		
		// ============= DB작업을 위한 boardWriteProService 객체 ==================
		BoardWriteProService boardWriteProService = new BoardWriteProService();
		// 게시물 등록 작업 성공 여부 리턴
		boolean isWriteSuccess = boardWriteProService.registArticle(boardBean);
		
		// 글쓰기 성공 여부에 따른 후속 처리 -> 이동 처리
		// 실패 : 자바스크립트로 실패 메세지 출력 후, 이전 페이지 이동
		// 성공 : ActionForward 객체를 사용하여 글목록(BoardList.bo) 페이지 요청(Redirect 방식)
		
		if(!isWriteSuccess) {	// 실패시!
			// ******< java 코드 내에서 HTML 코드 작성시 >******
			// HTML 문서 형식으로 출력하기 위해 타입 지정
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			// out 객체 사용하여 HTML 코드 작성
			out.println("<script>");		
			out.println("alert('게시물 등록 실패')");
			// 이전 페이지로 돌아가기
			out.println("history.back()");		// 또는 out.println("history.go(-1)"); 
			out.println("</script>");
		} else {	// 성공!
			forward = new ActionForward();
			forward.setPath("BoardList.bo");	// 새로운 servlet으로 이동
			forward.setRedirect(true);
		}

		
		// forward 객체 값 설정 
		return forward;
	}

}
