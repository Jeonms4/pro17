package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public MemberDAO() {
//		System.out.println("MemberDAO 객체 생성");

		try {
			// 1. 연결을 하기 위한 컨텍스트(pro07)인식을 위한 Context 객체
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
			//// 이 코드는 JNDI를 사용하여 "java:/comp/env" 컨텍스트에서 "jdbc/oracle"라는 이름으로 등록된
			// 데이터 소스를 얻어와서 dataFactory 변수에 할당하는 것입니다.
			// 이렇게 얻어온 데이터 소스를 사용하여 데이터베이스 연결을 설정하고 관리할 수 있습니다.

		} catch (Exception e) {
			System.out.println("MemberDAO 객체에서 DB 연결 관련 에러");
		}

	}

	public List<MemberVO> listMembers() {

		List<MemberVO> list = new ArrayList<MemberVO>();

		try {

			// connDB();
			// 2. 연결
			conn = dataFactory.getConnection();

			// 3. 연결 객체가(conn) sql을 돌려야함, sql을 돌리기 위해서는 sql 관련 문구를 처리하는 Statement Interface
			// 사용

			// 4. SQL 작성

			String query = "select * from T_MEMBER";
			System.out.println("실행한 sql : " + query);
			// ResultSet rs =stmt.executeQuery(query); //ResultSet : DB에서 가져온 데이터를 읽음
			pstmt = conn.prepareStatement(query); // 매개변수화된 SQL 문을 데이터베이스로 전송하기 위한 객체를 생성합니다.

			ResultSet rs = pstmt.executeQuery();

			// rs.next(); //Moves the cursor forward one row from its current position

			while (rs.next()) {
				// 결과테이블(ResultSet)의 칼럼 인식 후 결과값 가져오기
				String id = rs.getString("id");
				System.out.println("나온 id" + id);
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");

				// MemberVO 객체를 만들어서 그 객체에 resultset의 결과를 set해야함
				MemberVO vo = new MemberVO();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);

				list.add(vo);
			}

			rs.close();
			pstmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("연결시 에러");
		}

		return list;

	}

	public void addMember(MemberVO memberVO) {
		try {
			conn = dataFactory.getConnection();

			String id = memberVO.getId();
			String pwd = memberVO.getPwd();
			String name = memberVO.getName();
			String email = memberVO.getEmail();

			String query = "insert into t_member(id,pwd,name,email) VALUES(?,?,?,?)";
			System.out.println("회원 추가 sql문 : " + query);

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);

			pstmt.executeUpdate(); // 추가시 executeUpdate

			pstmt.close();
		} catch (Exception e) {
			System.out.println("회원추가시 에러");
		}
	}

	// 회원 찾기
	public MemberVO findMember(String _id) {
		System.out.println("수정할 id 찾음 : " + _id);
		MemberVO memInfo = null;

		try {

			conn = dataFactory.getConnection();
			String query = "select * from  t_member where id=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, _id);
			ResultSet rs = pstmt.executeQuery();

			rs.next();
			String id = rs.getString("id");
			String pwd = rs.getString("pwd");
			String name = rs.getString("name");
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			memInfo = new MemberVO(id, pwd, name, email, joinDate);

		} catch (Exception e) {
			System.out.println("회원 찾는 도중 예외 발생");
		}
		return memInfo;
	}

	// 회원 수정
	public void modMember(MemberVO memberVO) {
		System.out.println("수정할 객체 : " + memberVO.toString());

		String id = memberVO.getId();
		String pwd = memberVO.getPwd();
		String name = memberVO.getName();
		String email = memberVO.getEmail();

		try {

			conn = dataFactory.getConnection();	
			
			String query = "UPDATE t_member SET pwd=?, name=?, email=? where id=? ";
			System.out.println("회원 수정 sql문 : " + query);
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, pwd);
			pstmt.setString(2, name);
			pstmt.setString(3, email);
			pstmt.setString(4, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {

		}

		

	}

	public void delMember(String id) {
		System.out.println("삭제하고자 하는 id + " + id);
		try {
			conn = dataFactory.getConnection();
			String query = "delete from t_member" + " where id=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
