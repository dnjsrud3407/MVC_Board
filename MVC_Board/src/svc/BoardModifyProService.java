package svc;

import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.BoardDAO;
import vo.BoardBean;

public class BoardModifyProService {

	public boolean isArticleWriter(int board_num, String board_pass) {
		boolean isArticleWriter = false;
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		isArticleWriter = boardDAO.isArticleWriter(board_num, board_pass);
		
		
		close(con);
		return isArticleWriter;
	}

	public boolean modifyArticle(BoardBean boardBean) {
		boolean isModifySuccess = false;
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		int updateCount = boardDAO.updateArticle(boardBean);
		
		// 수정 성공 시 
		if(updateCount > 0) {
			isModifySuccess = true;
			commit(con);
		} else {
			rollback(con);
		}
		
		close(con);
		return isModifySuccess;
	}

}
