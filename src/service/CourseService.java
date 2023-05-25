package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import dao.CourseDao;
import dao.RegistrationDao;
import dto.CourseDto;
import vo.Course;
import vo.Registration;
import vo.Student;

public class CourseService {

	private static CourseService instane = new CourseService();
	private CourseService() {}
	public static CourseService getInstance() {
		return instane;
	}
	
	private CourseDao courseDao = CourseDao.getInstane();
	private RegistrationDao registrationDao = RegistrationDao.getInstance();
	
	public void 새과정등록(Course couse) {
		courseDao.insertCourse(couse);
	}
	
	public List<Map<String, Object>> 개설과정조회() {
		return courseDao.getCourses("모집중");
	} 
	
	public void 수강신청(int courseNo, String studentId) {
		// 과정정보 존재여부 체크하기
		Course course = courseDao.getCourse(courseNo);
		if (course == null) {
			throw new RuntimeException("과정정보가 존재하지 않습니다.");
		}
		
		// 과정상태 체크하기
		if (!"모집중".equals(course.getStatus())) {
			throw new RuntimeException("현재 모집중인 과정이 아닙니다.");
		}
		
		// 중복신청여부 체크하기 
		Registration registration = registrationDao.getRegistration(studentId, courseNo);
		if (registration != null) {
			throw new RuntimeException("이미 수강신청(취소)된 과정입니다.");
		}
		
		// 수강신청정보 저장
		registrationDao.insertRegistration(studentId, courseNo);
		
		// 개설과정정보 업데이트(신청자수, 과정상태)  
		course.setReqCnt(course.getReqCnt() + 1);
		if (course.getQuota() == course.getReqCnt()) {
			course.setStatus("모집완료");
		}
		courseDao.updateCourse(course);  
	}
	
	
	public List<Map<String, Object>> 수강신청현황(String studentId) {
		return registrationDao.getRegistrations(studentId);
	}
	
	
	public void 수강취소(int regNo, String studentId) {
		// 등록정보 조회, 존재여부 체크
		Registration registration = registrationDao.getRegistration(regNo);
		if (registration == null) {
			throw new RuntimeException("수강신청 정보가 존재하지 않습니다.");
		}
		
		// 이미 수강취소된 과정인지 체크
		if ("Y".equals(registration.getCanceled())) {
			throw new RuntimeException("이미 수강취소처리된 과정입니다.");
		}
		
		// 등록정보가 로그인한 학생의 등록정보인지 체크
		if (!registration.getStudentId().equals(studentId)) {
			throw new RuntimeException("본인이 신청한 과정만 수강취소 할 수 있습니다.");
		}
		
		// 등록정보의 취소여부를 'Y'로 변경
		registration.setCanceled("Y");
		registrationDao.updateRegistration(registration);
		
		// 과정정보 수정(신청자수, 과정상태)
		Course course = courseDao.getCourse(registration.getCourseNo());
		course.setReqCnt(course.getReqCnt() - 1);
		if ("모집완료".equals(course.getStatus())) {
			course.setStatus("모집중"); 
		}
		courseDao.updateCourse(course); 
	}
	
	
	public List<Map<String, Object>> 강사과정조회(String teacherId) {
		return courseDao.getCoursesByTeacher(teacherId);
	}
	
	
	public void 과정취소(int courseNo) {
		/*
			- 개설과정상태가 '과정취소'로 변경되면 해당 과정의 
			과정등록정보의 취소여부(REG_CANCELED)를 전부 'Y'로 변경한다.
		 */
		
		// 등록정보 조회, 존재여부 체크
		Course course = courseDao.getCourse(courseNo);
		if (course == null) {
			throw new RuntimeException("과정개설 정보가 존재하지 않습니다.");
		}
		
		// 이미 개설취소된 과정인지 체크
		if ("과정취소".equals(course.getStatus())) {
			throw new RuntimeException("이미 개설취소처리된 과정입니다.");
		}
		
		// 개설과정상태(COURSE_STATUS)를 '과정취소'로 변경
		course.setStatus("과정취소");
		courseDao.updateCourse(course);
		
		// 과정등록정보의 취소여부(REG_CANCELED)를 'Y'로 변경
		List<Registration> registrations = registrationDao.getRegByCourseNo(courseNo);
		for (Registration registration : registrations) {
			if(registration.getCourseNo() == courseNo) {
				registration.setCanceled("Y");
			} 
			registrationDao.updateRegistration(registration);
		}
		
		courseDao.updateCourse(course);
	}
	
	public CourseDto 과정현황(int courseNo, String teacherId) {
		// 과정에 대한 상세정보
		/*
		 select *
		 from courses
		 where course_no = ?
		 */
		
		// 이과정 신청자목록 조회
		/*
		 select s.student_id, s.student_name, r.cancel, r.create_date
		 from registrations r, students s, course s
		 where r.student_id = s.student_id
		 and r.course_no = ?
		 
		 public class CourseDto {
		 	Course course;
		 	List<Map<String, Object>> xx;
		 }
		 */
		
		Course course = courseDao.getCourse(courseNo);
		
		if (course == null) {
			System.out.println("존재하지 않는 과정입니다.");
		}
		
		if (!(teacherId.equals(course.getTeacherId()))) {
			System.out.println("다른 강사의 개설과정은 조회할 수 없습니다.");
		}
		
		List<Student> students = courseDao.getStudentByCourseNo(courseNo);
		
		CourseDto dto = new CourseDto();
		dto.setCourse(course);
		dto.setStudentList(students);
		
		return dto;
	}
} 










