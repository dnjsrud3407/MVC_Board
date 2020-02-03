package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcUtil {
	// DB접속
	// => 모든 메서드는 인스턴스 생성없이 호출 가능하도록 static 메서드로 정의
	public static Connection getConnection() {
		Connection con = null;
		
		try {
			// META-INF 내의 context.xml 파일을 들고옴
			Context initCtx = new InitialContext();
			
			// <Resorce> 태그 부분 가져오기(변하지 않음)
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			// <Resorce> 태그 내의 name 속성 가져오기(name 속성값에 따라 변함)
			DataSource ds = (DataSource) envCtx.lookup("jdbc/MySQL");
			// DataSource의 객체 = 커넥션 풀
			
			// 커넥션 풀로 부터 Connection 객체 가져오기
			con = ds.getConnection();
			
			// autoCommit 해제
			con.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//==========================자원반환 작업===============================
	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(PreparedStatement pstmt) {
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void close(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//==========================commit, rollback 작업===============================
	// autoCommit 해제 시 필요
	public static void commit(Connection con) {
		try {
			con.commit();
			System.out.println("Commit Success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void rollback(Connection con) {
		try {
			con.rollback();
			System.out.println("RollBack Success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
