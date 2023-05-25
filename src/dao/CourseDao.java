package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ConnUtils;
import vo.Course;
import vo.Student;

public class CourseDao {

	private static CourseDao courseDao = new CourseDao();
	private CourseDao() {}
	public static CourseDao getInstane() {
		return courseDao;
	}
	
	public void insertCourse(Course course) {
		String sql = "insert into academy_courses "
				   + "(course_no, course_name, course_quota, teacher_id) "
				   + "values "
				   + "(courses_seq.nextval, ?, ?, ?)";
		
		try {
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, course.getName());
			pstmt.setInt(2, course.getQuota());
			pstmt.setString(3, course.getTeacherId());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public List<Map<String, Object>> getCourses(String status) {
		String sql = "select A.course_no, A.course_name, "
				   + "		 A.course_quota, A.course_req_cnt, "
				   + "		 A.teacher_id, B.teacher_name "		 
				   + "from academy_courses A, academy_teacher B "
				   + "where course_status = ? "
				   + "and A.teacher_id = B.teacher_id "
				   + "order by A.course_no asc "; 
		
		try {
			List<Map<String, Object>> courses = new ArrayList<>();
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, status);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				
				map.put("no", rs.getInt("course_no"));
				map.put("name", rs.getString("course_name"));
				map.put("quota", rs.getInt("course_quota"));
				map.put("reqCnt", rs.getInt("course_req_cnt"));
				map.put("teacherId", rs.getString("teacher_id"));
				map.put("teacherName", rs.getString("teacher_name"));
				
				courses.add(map);
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return courses;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public Course getCourse(int courseNo) {
		String sql = "select * "
				   + "from academy_courses "
				   + "where course_no = ? ";
		
		try {
			Course course = null;
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, courseNo);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				course = new Course();
				course.setNo(rs.getInt("course_no"));
				course.setName(rs.getString("course_name"));
				course.setQuota(rs.getInt("course_quota"));
				course.setReqCnt(rs.getInt("course_req_cnt"));
				course.setStatus(rs.getString("course_status"));
				course.setCreateDate(rs.getDate("course_create_date"));
				course.setTeacherId(rs.getString("teacher_id"));
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return course;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public void updateCourse(Course course) {
		String sql = "update academy_courses "
				   + "set "
				   + "		 course_name = ?, "
				   + "		 course_quota = ?, "
				   + "  	 course_req_cnt = ?, "
				   + "       course_status = ? "
				   + "where course_no = ? ";
		
		try {
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, course.getName());
			pstmt.setInt(2, course.getQuota());
			pstmt.setInt(3, course.getReqCnt());
			pstmt.setString(4, course.getStatus());
			pstmt.setInt(5, course.getNo());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close(); 
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public List<Map<String, Object>> getCoursesByTeacher(String teacherId) {
		String sql = "select course_no, course_quota, course_req_cnt, course_status, course_name "
				   + "from academy_courses "
				   + "where teacher_id = ? ";
		
		try {
			List<Map<String, Object>> courses = new ArrayList<>();
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, teacherId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				
				map.put("no", rs.getInt("course_no"));
				map.put("quota", rs.getInt("course_quota"));
				map.put("reqCnt", rs.getInt("course_req_cnt"));
				map.put("status", rs.getString("course_status"));
				map.put("name", rs.getString("course_name"));
				
				courses.add(map);
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return courses;
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<Student> getStudentByCourseNo(int courseNo) {
		
		String sql = "select S.student_id, S.student_name, S.student_phone, S.student_email "
				   + "from academy_students S, academy_course_registrations R, academy_courses C "
				   + "where S.student_id = R.student_id "
				   + "and R.course_no = C.course_no "
				   + "and C.course_No = ? ";
		
		try {
			List<Student> studentList = new ArrayList<>();
			
			Connection con = ConnUtils.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, courseNo);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Student student = new Student();
				student.setId(rs.getString("student_id"));
				student.setName(rs.getString("student_name"));
				student.setPhone(rs.getString("student_phone"));
				student.setEmail(rs.getString("student_email"));
				
				studentList.add(student);
			}
			
			rs.close();
			pstmt.close();
			con.close();
			
			return studentList;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
}















