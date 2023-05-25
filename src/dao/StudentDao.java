package dao;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConnUtils;
import vo.Student;

public class StudentDao {
	
	// 싱글턴(Singleton) 객체 
	private static StudentDao instance = new StudentDao();
	private StudentDao() {}
	public static StudentDao getInstacne() {
		return instance;
	}

	public Student getStudentById(String studentId) {
		String sql = "select * "
				   + "from academy_students "
				   + "where student_id = ? ";
		
		try {
			Student student = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, studentId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				student = new Student();
				student.setId(rs.getString("student_id"));
				student.setPassword(rs.getString("student_password"));
				student.setName(rs.getString("student_name"));
				student.setEmail(rs.getString("student_email"));
				student.setPhone(rs.getString("student_phone"));
				student.setDeleted(rs.getString("student_deleted"));
				student.setCreateDate(rs.getDate("student_create_date"));
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return student;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public Student getStudentByEmail(String studentEmail) {
		String sql = "select * "
				   + "from academy_students "
				   + "where student_email = ? ";
		
		try {
			Student student = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, studentEmail);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				student = new Student();
				student.setId(rs.getString("student_id"));
				student.setPassword(rs.getString("student_password"));
				student.setName(rs.getString("sudent_name"));
				student.setEmail(rs.getString("student_email"));
				student.setPhone(rs.getString("student_phone"));
				student.setDeleted(rs.getString("student_deleted"));
				student.setCreateDate(rs.getDate("student_create_date"));
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return student;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		
	}
	
	public void insertStudent(Student student) {
		String sql = "insert into academy_students "
				   + "(student_id, student_password, student_name, student_email, student_phone) "
				   + "values "
				   + "(?,?,?,?,?)";
		
		try {
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, student.getId());
			pstmt.setString(2, student.getPassword());
			pstmt.setString(3, student.getName());
			pstmt.setString(4, student.getEmail());
			pstmt.setString(5, student.getPhone());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	
}











