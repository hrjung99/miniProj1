package miniProj1.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

	private static Connection conn = null;

	private static PreparedStatement MemberListPstmt = null; // 검색X 리스트
	private static PreparedStatement MemberListPstmt2 = null; // 검색O 리스트

	private static PreparedStatement MemberInsertPstmt = null; // 회원 가입
	private static PreparedStatement MemberInsertHobbyPstmt = null; // 회원 가입(취미)

	private static PreparedStatement MemberDletePstmt = null; // 회원 탈퇴
	private static PreparedStatement MemberDetailPstmt = null; // 마이페이지
	private static PreparedStatement MemberUpdatePstmt = null;// 회원 수정
	private static PreparedStatement MemberUpdateHobbyPstmt = null;// 회원 수정(취미)

	// jdbc 설치 및 연결 필요
	static {
		// jdbc 드라이버 로딩
		try {
			// jdbc 드라이버 로딩
			Class.forName("org.mariadb.jdbc.Driver");

			// DB 연결
			conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/miniProj_db", "bituser", "1004");

			System.out.println("연결 성공");
			conn.setAutoCommit(false);

			// 전체 조회
			MemberListPstmt = conn.prepareStatement("select * from tb_member");
			// 검색 조회
			MemberListPstmt2 = conn.prepareStatement("select * from tb_member where like mname=?");
			// 회원 가입
			MemberInsertPstmt = conn.prepareStatement("insert into tb_member(mid, mpass, mname, mage, madd, mpno, mgender) values (?,?,?,?,?,?,?)"); // 취미
			// 회원 탈퇴
			MemberDletePstmt = conn.prepareStatement("delete from tb_member where=?");
			// 마이페이지
			MemberDetailPstmt = conn.prepareStatement("select * from tb_member where mid = ?");
			// 회원정보 수정
			MemberUpdatePstmt = conn. prepareStatement("update tb_member set mpass=?, mname=?, mage=?, madd=?, mpno=?, mgender=? where mid=?"); // 취미 추가

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// list 함수 구현(+검색 기능)
	public List<MemberVO> list(MemberVO memberVO) {
		List<MemberVO> list = new ArrayList<MemberVO>();

		try {
			int updated = 0;
			ResultSet rs = null;
			String searchKey = null;
			//String searchKey = memberVO.getSearchKey();
			// 검색O
			if (searchKey != null && searchKey.length() != 0) {
				MemberListPstmt2.setString(1, "%" + searchKey + "%");
				rs = MemberListPstmt2.executeQuery();

			} else { // 검색X

				rs = MemberListPstmt.executeQuery();
			}
			while (rs.next()) {
				MemberVO member = new MemberVO(rs.getString("mid"), rs.getString("mpass"), rs.getString("mname"),
						rs.getString("mage"), rs.getString("madd"), rs.getString("mpno"), rs.getString("mgender"));
				// 취미추가
				list.add(member);

			}

			conn.commit();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}

		return list;
	}

	// read 함수 구현
	public MemberVO read(MemberVO memberVO) {
		MemberVO view = null;
		try

		{
			System.out.println("mid = " + memberVO.getMid());
			MemberDetailPstmt.setString(1, memberVO.getMid());
			ResultSet rs = MemberDetailPstmt.executeQuery();
			if (rs.next()) {
				view = new MemberVO(
						memberVO.getMid()
						, rs.getString("mpass")
						, rs.getString("mname")
						, rs.getString("mage")
						, rs.getString("madd")
						, rs.getString("mpno")
						, rs.getString("mgender"));
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		System.out.println("view="+view);

		return view;
	}
	
	
//	// delete 함수 구현
//	public String delete(MemberVO memberVO) {
//		String mname = memberVO.getMname();
//
//		try
//
//		{
//
//			int updated = 0;
//
//			MemberDletePstmt.setString(1, "mid");
//			updated = MemberDletePstmt.executeUpdate();
//
//			conn.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.getMessage();
//		}
//
//		return mname;
//	}

//	// insert 함수 구현
//	public String insert(MemberVO memberVO) {
//		String mname = memberVO.getMname();
//
//		try {
//			int updated = 0;
//
//			MemberInsertPstmt.executeUpdate();
//			MemberInsertPstmt.setString(1, "mid");
//			MemberInsertPstmt.setString(2, "mpass");
//			MemberInsertPstmt.setString(3, "mname");
//			MemberInsertPstmt.setString(4, "mage");
//			MemberInsertPstmt.setString(5, "mpno");
//			MemberInsertPstmt.setString(6, "mgender");
//			MemberInsertPstmt.setSring(7, "hobby");
//
//			updated = MemberInsertPstmt.executeUpdate();
//
//			conn.commit();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.getMessage();
//		}
//
//		return mname;
//	}


//
//	// update 함수 구현
//
//	public String update(MemberVO memberVO) {
//		String mname = memberVO.getMname();
//		int updated = 0;
//
//		try {
//			MemberUpdatePstmt.setString(1, memberVO.getMpass());
//			MemberUpdatePstmt.setString(2, memberVO.getMname());
//			MemberUpdatePstmt.setString(3, memberVO.getMage());
//			MemberUpdatePstmt.setString(4, memberVO.getMadd());
//			MemberUpdatePstmt.setString(5, memberVO.getMpno());
//			MemberUpdatePstmt.setString(6, memberVO.getMgender());
//
//			updated = MemberUpdatePstmt.executeUpdate();
//
//			conn.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.getMessage();
//		}
//
//		return mname;
//	}
//
}
