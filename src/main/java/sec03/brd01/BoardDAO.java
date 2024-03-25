package sec03.brd01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public BoardDAO() {
		System.out.println("BoardDAO 객체 생성");

		try {

			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");

		} catch (Exception e) {
			System.out.println("MemberDAO 객체에서 DB 연결 관련 에러");
		}

	}

	// 전체 글 가져오기
	List<ArticleVO> selectAllArticles() {

		List<ArticleVO> articlesList = new ArrayList<>();

		try {
			conn = dataFactory.getConnection();

			String query = "select level,  articleNO,parentNO,title,content,id,writeDate " + "from t_board "
					+ "start with parentno=0 " + "connect by prior articleNo=parentno "
					+ "order SIBLINGS by articleno desc ";

			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int level = rs.getInt("level");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");

				ArticleVO article = new ArticleVO();

				article.setLevel(level);
				article.setArticleNO(articleNO);
				article.setParentNO(parentNO);
				article.setTitle(title);
				article.setContent(content);
				article.setId(id);
				article.setWriteDate(writeDate);
				articlesList.add(article);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("모든 게시판 글 가져오면서 예외 발생");
		}
		return articlesList;
	}

	// 새 글 추가
	void insertNewArticle(ArticleVO article) {
		System.out.println("DAO에서의 글 추가");
		try {
			conn = dataFactory.getConnection();
			int articleNO = getNewArticleNO();
			int parentNO = article.getParentNO();
			System.out.println("DAO에서의 parentNO :" + parentNO);
			String title = article.getTitle();
			String content = article.getContent();
			String id = article.getId();
			String imageFileName = article.getImageFileName();
			
			 String query= "insert into t_board(articleNo, parentNo, title, content, imageFileName, id) "
			+ " values (?,?,?,?,?,?) " ;
			 
			 
			 System.out.println(query);
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, articleNO);
				pstmt.setInt(2, parentNO);
				pstmt.setString(3, title);
				pstmt.setString(4, content);
				pstmt.setString(5, imageFileName);
				pstmt.setString(6, id);
				pstmt.executeUpdate();
				pstmt.close();
				conn.close();
			 
			 
			 
			 
			 
			 
			 

		} catch (Exception e) {
			System.out.println("새 글 추가시 예외 발생");
		}
	}
	
		
	//새 글 번호  가죠오기	
	public int getNewArticleNO() {
		try {
			conn = dataFactory.getConnection();
			
			String query = "select max(articleno) from t_board ";
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(query);
			if (rs.next()) {
				return (rs.getInt(1) + 1);
			}
			rs.close();
			pstmt.close();
			conn.close();
			
			
		}catch(Exception e) {
			
		}
		
		return 0;
	}
	
	
	
	//글 보기
	public ArticleVO selectArticle(int articleNO){
		ArticleVO article=new ArticleVO();
		try{
			conn = dataFactory.getConnection();
			String query ="select articleNO,parentNO,title,content, imageFileName,id,writeDate"
				                     +" from t_board" 
				                     +" where articleNO=?";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs =pstmt.executeQuery();
			rs.next();
			int _articleNO =rs.getInt("articleNO");
			int parentNO=rs.getInt("parentNO");
			String title = rs.getString("title");
			String content =rs.getString("content");
		    String imageFileName = rs.getString("imageFileName"); 
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writeDate");
			
			
			article.setArticleNO(_articleNO);
			article.setParentNO (parentNO);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setId(id);
			article.setWriteDate(writeDate);
			rs.close();
			pstmt.close();
			conn.close();
			
			
			
		}catch(Exception e) {
			System.out.println("DAO에서 글보기 중 예외 발생");
		}
		
		
		return article;
	}
	
	
	
	//글 수정
	public void updateArticle(ArticleVO article) {
		int articleNO = article.getArticleNO();
		String title = article.getTitle();
		String content = article.getContent();
		String imageFileName = article.getImageFileName();
		
		try {
			
			conn = dataFactory.getConnection();
			
			String query = "update t_board  set title=?,content=?";
			if (imageFileName != null && imageFileName.length() != 0) {
				query += ",imageFileName=?";
			}
			query += " where articleNO=?";
			
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if (imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}catch(Exception e) {
			System.out.println("글 수정시 예외 발생");
		}
		
		
	}
	
	
	//글 삭제할 목록 선택
	public List<Integer> selectRemovedArticles(int  articleNO) {
		List<Integer> articleNOList = new ArrayList<Integer>();
		
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT articleNO FROM  t_board  ";
			query += " START WITH articleNO = ?";
			query += " CONNECT BY PRIOR  articleNO = parentNO";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				articleNO = rs.getInt("articleNO");
				articleNOList.add(articleNO);
			}
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("글 삭제할 목록 선택시 예외 발생");
		}
		return articleNOList;
	}
	
	//글 삭제	
	public void deleteArticle(int  articleNO) {
		try {
			conn = dataFactory.getConnection();
			String query = "DELETE FROM t_board ";
			query += " WHERE articleNO in (";
			query += "  SELECT articleNO FROM  t_board ";
			query += " START WITH articleNO = ?";
			query += " CONNECT BY PRIOR  articleNO = parentNO )";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
		  System.out.println("글 삭제 시 예외 발생");
		}
	}
	

}
