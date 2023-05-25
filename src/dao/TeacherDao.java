package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConnUtils;
import vo.Teacher;

public class TeacherDao {
	
	private static TeacherDao instance = new TeacherDao();
	private TeacherDao() {}
	public static TeacherDao getInstance() {
		return instance;
	}

	public Teacher getteTeacherById(String teacherId) {
		String sql = "select * "
				   + "from academy_teacher "
				   + "where teacher_id = ? ";
		
		try {
			Teacher teacher = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, teacherId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				teacher = new Teacher();
				
				teacher.setId(rs.getString("teacher_id"));
				teacher.setPassword(rs.getString("teacher_password"));
				teacher.setName(rs.getString("teacher_name"));
				teacher.setPhone(rs.getString("teacher_phone"));
				teacher.setEmail(rs.getString("teacher_email"));
				teacher.setSalary(rs.getInt("teacher_salary"));
				teacher.setRetired(rs.getString("teacher_retired"));
				teacher.setCreateDate(rs.getDate("teacher_create_date"));
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return teacher;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	
	public Teacher getteTeacherByEmail(String teacherEmail) {
		String sql = "select * "
				   + "from academy_teacher "
				   + "where teacher_email = ? ";
		
		try {
			Teacher teacher = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, teacherEmail);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				teacher = new Teacher();
				
				teacher.setId(rs.getString("teacher_id"));
				teacher.setPassword(rs.getString("teacher_password"));
				teacher.setName(rs.getString("teacher_name"));
				teacher.setPhone(rs.getString("teacher_phone"));
				teacher.setEmail(rs.getString("teacher_email"));
				teacher.setSalary(rs.getInt("teacher_salary"));
				teacher.setRetired(rs.getString("teacher_retired"));
				teacher.setCreateDate(rs.getDate("teacher_create_date"));
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return teacher;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void insertTeacher(Teacher teacher) {
		String sql = "insert into academy_teacher "
				   + "(teacher_id, teacher_password, teacher_name, teacher_email, teacher_phone, teacher_salary)"
				   + "values "
				   + "(?, ?, ?, ?, ?, ?)";
		
		try {
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, teacher.getId());
			pstmt.setString(2, teacher.getPassword());
			pstmt.setString(3, teacher.getName());
			pstmt.setString(4, teacher.getEmail());
			pstmt.setString(5, teacher.getPhone());
			pstmt.setInt(6, teacher.getSalary());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}



