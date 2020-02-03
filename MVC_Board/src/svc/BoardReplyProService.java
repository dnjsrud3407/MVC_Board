package svc;

import java.sql.Connection;

import dao.BoardDAO;

import static db.JdbcUtil.*;
import vo.BoardBean;

public class BoardReplyProService {

	public boolean replyArticle(BoardBean article) {
		boolean isReplySuccess = false;
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		// BoardDAO 에 article을 저장
		int insertCount = 0;
		insertCount = boardDAO.insertReplyArticle(article);
		
		
		if(insertCount > 0) {
			isReplySuccess = true;
			commit(con);
		} else {
			rollback(con);
		}
		
		
		close(con);
		return isReplySuccess;
	}

}
