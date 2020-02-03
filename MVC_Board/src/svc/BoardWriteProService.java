package svc;

import vo.BoardBean;
import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.BoardDAO;

public class BoardWriteProService {

	public boolean registArticle(BoardBean boardBean) {
		System.out.println("BoardWriteProService!!");
//		Connection con = JdbcUtil.getConnection();		// import static 적지 않았을 때
		
		// 1. Connection 객체 가져오기
		Connection con = getConnection();
		
		// 2. DAO 객체 가져오기(싱글톤 패턴)
		BoardDAO boardDAO = BoardDAO.getInstance();
			
		// 3. DAO 객체에 Connection 객체 전달하기
		boardDAO.setConnection(con);
		
		// 4. DB 작업을 위한 DAO 객체의 메서드 호출
		int insertCount = boardDAO.insertArticle(boardBean);
		
		boolean isWriteSuccess = false;
		if(insertCount > 0) {
			// 글쓰기 성공 시 commit 작업 수행  
			isWriteSuccess = true;
			commit(con);
			
			// 글쓰기 성공 후 참조글 번호를 업데이트 해야 함
			boardDAO.updateBoard_re_ref(boardBean);
			commit(con);
		} else {
			// 글쓰기 실패 시 rollback 작업 수행
			rollback(con);
		}
		
		
		
		
		
		
		// 5. Connection 객체 반환하기
		close(con);

		return isWriteSuccess;
	}

}
