package svc;

import java.sql.Connection;

import dao.BoardDAO;

import static db.JdbcUtil.*;

import vo.BoardBean;

public class BoardDetailService {

	public BoardBean getArticle(int board_num) throws Exception{
//		System.out.println(board_num);
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		BoardBean article = null;
		
		// BoardDAO 클래스의 selectArticle() 호출
		article = boardDAO.selectArticle(board_num);
		
		close(con);
		
		return article;
	}
	
	
	// 조회 수 증가 메서드
	// 수정하기 할 때는 조회수 증가가 필요없기 때문에 따로 분리 
	public void plusReadcount(int board_num) throws Exception{
//		System.out.println(board_num);
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		
		// updateReadcount() 호출 - 리턴 int값
		// readcount 1 증가
		int updateCount = boardDAO.updateReadcount(board_num);

		
		// 리턴된 결과가 0보다 크면 commit
		if(updateCount > 0) {
			commit(con);
		} else {
			rollback(con);
		}
		
		close(con);
		
	}
}
