package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static db.JdbcUtil.*;

import vo.BoardBean;

public class BoardDAO {
	
	// ------------------------------------------------------
	// 싱글톤 패턴을 활용한 BoardDAO 인스턴스 생성 및 리턴
	private static BoardDAO instance = new BoardDAO();
	private BoardDAO() {}
	
	public static BoardDAO getInstance() {
		return instance;
	}
	// ------------------------------------------------------
	
	
	Connection con;
	// 외부(BoardWriteProService, ...)로 부터 
	// Connection 객체를 전달 받아 저장할 메서드
	public void setConnection(Connection con) {
		this.con = con;
	}

	// 글쓰기 작업
	public int insertArticle(BoardBean boardBean) {
//		System.out.println("BoardDAO의 insertArticle");
		int insertCount = 0;
		PreparedStatement pstmt = null;
		
		
		// INSERT 구문 사용(글번호, 작성자, 패스워드, 제목, 본문, 파일명, 참조글번호, 들여쓰기레벨, 답글순서번호, 조회수, 작성일)
		String sql = "INSERT INTO board VALUES(null,?,?,?,?,?,?,?,?,?,now())";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, boardBean.getBoard_name());
			pstmt.setString(2, boardBean.getBoard_password());
			pstmt.setString(3, boardBean.getBoard_subject());
			pstmt.setString(4, boardBean.getBoard_content());
			pstmt.setString(5, boardBean.getBoard_file());
			
			// 글번호에 null을 줬기 때문에 num값을 지금 조회할 수 없음 - 나중에 update해줘야함
			// 임시번호 -1을 줌
			pstmt.setInt(6, -1);	
			pstmt.setInt(7, 0);
			pstmt.setInt(8, 0);
			pstmt.setInt(9, 0);
			insertCount = pstmt.executeUpdate();
			System.out.println("글 등록 성공");
		} catch (SQLException e) {
			System.out.println("insertArticle() 에러" + e.getMessage());
		} finally {
			if(pstmt != null) {close(pstmt);}
		}
		
		return insertCount;
	}
	
	// 글쓰고 난 후 Board_rd_ref를 num값이랑 동일화 시키기
	public void updateBoard_re_ref(BoardBean boardBean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int board_num = 0;
		// 새 글에 대한 번호 조회 => 작성자와 참조글번호(-1)을 사용
		String sql = "SELECT board_num FROM board WHERE board_name=? AND board_re_ref=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, boardBean.getBoard_name());
			pstmt.setInt(2, -1);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				board_num = rs.getInt("board_num");
				// board_re_ref (참조글 번호)에 board_num 값으로 update 시키기
				sql = "UPDATE board SET board_re_ref=? WHERE board_name=? AND board_re_ref=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, board_num);
				pstmt.setString(2, boardBean.getBoard_name());
				pstmt.setInt(3, -1);
				pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
			if(rs != null) {close(rs);}
		}
	}

	public int selectListCount() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int listCount = 0;
		
		String sql = "SELECT COUNT(*) FROM board";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				listCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
			if(rs != null) {close(rs);}
		}
		
		return listCount;
	}
	
	// boardList 가져오기
	public ArrayList<BoardBean> selectArticleList(int page, int limit) {
		ArrayList<BoardBean> articleList = new ArrayList<BoardBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int startRow = (page - 1) * 10;
		int endRow = startRow + limit;
		
		String sql = "SELECT * FROM board ORDER BY board_re_ref DESC, board_re_seq ASC LIMIT ?,?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				// 패스워드 제외한 데이터 들고오기
				BoardBean boardBean = new BoardBean();
				boardBean.setBoard_num(rs.getInt("board_num"));
				boardBean.setBoard_name(rs.getString("board_name"));
				boardBean.setBoard_subject(rs.getString("board_subject"));
				boardBean.setBoard_content(rs.getString("board_content"));
				boardBean.setBoard_file(rs.getString("board_file"));
				boardBean.setBoard_re_ref(rs.getInt("board_re_ref"));
				boardBean.setBoard_re_lev(rs.getInt("board_re_lev"));
				boardBean.setBoard_re_seq(rs.getInt("board_re_seq"));
				boardBean.setBoard_readcount(rs.getInt("board_readcount"));
				boardBean.setDate(rs.getDate("board_date"));
				
				// list에 boardBean 객체 넣기
				articleList.add(boardBean);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
			if(rs != null) {close(rs);}
		}
		
		return articleList;
	}
	
	// 게시물 상세 내용 조회
	public BoardBean selectArticle(int board_num) {
		BoardBean boardBean = new BoardBean();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM board WHERE board_num=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				boardBean.setBoard_num(rs.getInt("board_num"));
				boardBean.setBoard_name(rs.getString("board_name"));
				boardBean.setBoard_subject(rs.getString("board_subject"));
				boardBean.setBoard_content(rs.getString("board_content"));
				boardBean.setBoard_file(rs.getString("board_file"));
				boardBean.setBoard_re_ref(rs.getInt("board_re_ref"));
				boardBean.setBoard_re_lev(rs.getInt("board_re_lev"));
				boardBean.setBoard_re_seq(rs.getInt("board_re_seq"));
				boardBean.setBoard_readcount(rs.getInt("board_readcount"));
				boardBean.setDate(rs.getDate("board_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
			if(rs != null) {close(rs);}
		}
		
		return boardBean;
	}

	public int updateReadcount(int board_num) {
		int updateCount = 0;
		PreparedStatement pstmt = null;
		String sql = "UPDATE board SET board_readcount = board_readcount+1 WHERE board_num=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
		}
		
		return updateCount;
	}
	
	// =============== 글 수정 ===============
	
	public boolean isArticleWriter(int board_num, String board_pass) {
		boolean isArticleWriter = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 게시물 번호에 해당하는 패스워드를 조회해서
		// 입력받은 패스워드와 비교
		String sql = "SELECT board_pass FROM board WHERE board_num=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String getPass = rs.getString("board_pass");
				// 비밀번호가 일치하다면 
				if(getPass.equals(board_pass)) {
					isArticleWriter = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
			if(rs != null) {close(rs);}
		}
		
		
		return isArticleWriter;
	}

	public int updateArticle(BoardBean boardBean) {
		int updateCount = 0;
		PreparedStatement pstmt = null;
		String sql = "UPDATE board SET board_subject=?,board_content=? WHERE board_num=?";
		try {
			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, boardBean.getBoard_name());
			pstmt.setString(1, boardBean.getBoard_subject());
			pstmt.setString(2, boardBean.getBoard_content());
			pstmt.setInt(3, boardBean.getBoard_num());
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
		}
		
		return updateCount;
	}

	public int deleteArticle(int board_num) {
		int deleteCount = 0;
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM board WHERE board_num=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			deleteCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
		}
		
		
		return deleteCount;
	}

	public int insertReplyArticle(BoardBean article) {
		int insertCount = 0;
		PreparedStatement pstmt = null;
		// 답변 글 등록에 필요한 정보 가져오기
		int board_re_ref = article.getBoard_re_ref();
		int board_re_lev = article.getBoard_re_lev();
		int board_re_seq = article.getBoard_re_seq();
		
		String sql = "UPDATE board SET board_re_seq=board_re_seq+1 WHERE board_re_ref=? AND board_re_seq>?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_re_ref);
			pstmt.setInt(2, board_re_seq);
			int updateCount = pstmt.executeUpdate();
			
			// updateCount가 0보다 크면 commit(); 수행
			if(updateCount > 0) {
				commit(con);
			}
			
			// 순서번호(seq)와 들여쓰기(lev)을 + 1
			board_re_seq += 1;
			board_re_lev += 1;
			
			// 답변 글 등록 작업 - 주의! 답변글 등록에는 file이 없음!
			sql = "INSERT INTO board VALUES(null,?,?,?,?,?,?,?,?,?,now())";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, article.getBoard_name());
			pstmt.setString(2, article.getBoard_password());
			pstmt.setString(3, article.getBoard_subject());
			pstmt.setString(4, article.getBoard_content());
			pstmt.setString(5, "");
			pstmt.setInt(6, board_re_ref);
			pstmt.setInt(7, board_re_lev);	
			pstmt.setInt(8, board_re_seq);
			pstmt.setInt(9, 0);		// readcount
			insertCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {close(pstmt);}
		}
		
		return insertCount;
	}
}


































