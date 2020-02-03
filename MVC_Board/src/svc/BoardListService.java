package svc;

import static db.JdbcUtil.getConnection;

import java.sql.Connection;
import java.util.ArrayList;

import dao.BoardDAO;
import vo.BoardBean;

import static db.JdbcUtil.*;

public class BoardListService {

	public int getListCount() {
		int listCount = 0;
//		System.out.println("BoardListService의 getListCount");
		
		// ServiceClass 에서 반드시 해야 할 것! (1,2,3,5)
		// 1. Connection 객체 가져오기
		Connection con = getConnection();
		
		// 2. DAO 객체 가져오기(싱글톤 패턴)
		BoardDAO boardDAO = BoardDAO.getInstance();
		
		// 3. DAO 객체에 Connection 객체 전달하기
		boardDAO.setConnection(con);
		
		// 4. DB 작업을 위한 DAO 객체의 메서드 호출
		// 총 게시글 갯수 구하기
		listCount = boardDAO.selectListCount();
		
		// 5. Connection 객체 반환하기
		close(con);

		return listCount;
	}

	public ArrayList<BoardBean> getArticleList(int page, int limit) {
		ArrayList<BoardBean> articleList = null;
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		articleList = boardDAO.selectArticleList(page, limit);
		
//		for(int i = 0; i < articleList.size(); i++) {
//			BoardBean boardBean = articleList.get(i);
//			System.out.println(boardBean.getBoard_subject());
//		}
		
		close(con);
		
		return articleList;
	}

}
