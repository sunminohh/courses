package dto;

import java.util.List;

import vo.Course;
import vo.Student;

public class CourseDto {

	private Course course;
	private List<Student> studentList;
	
	public CourseDto() {}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}
	
}
