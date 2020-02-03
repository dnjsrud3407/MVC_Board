package svc;

import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.BoardDAO;

public class BoardDeleteProService {
	public boolean isArticleWriter(int board_num, String board_pass) {
		boolean isArticleWriter = false;
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		isArticleWriter = boardDAO.isArticleWriter(board_num, board_pass);
		
		close(con);
		return isArticleWriter;
	}

	public boolean removeArticle(int board_num) {
		boolean isDeleteSuccess = false;
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		int deleteCount = boardDAO.deleteArticle(board_num);
		if(deleteCount > 0) {
			isDeleteSuccess = true;
			commit(con);
		} else {
			rollback(con);
		}
		close(con);
		return isDeleteSuccess;
	}
}
