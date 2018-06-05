package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.beans.AnalysisData;
import com.beans.Course;
import com.beans.Plan;
import com.beans.Study;
import com.dao.Dao;

import utils.SpringUtils;

@Service
public class StudyService {

	//spring 注入的数据库操作类dao,
	@Autowired
	private Dao dao;
	
	
    //获取计划
	public List<Plan> getPlans(int userid)
	{
		String sql = "select * from tab_plan where user_id=?";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(Plan.class), userid);
	}	
	
	
	//增加计划
	public boolean addPlan(Plan plan)
	{
		String sql = "insert into tab_plan(user_id, course_id, time) values(?,?,?)";  			
		if(1 == dao.add(sql, plan.getUser_id(), plan.getCourse_id(), plan.getTime()))
			return true;
		return false;
	}
		
	
	/*** 删除计划 */
	public boolean deletePlan(int planid) 
	{
		String sql = "delete from tab_plan where plan_id=?";
		if(1 == dao.delete(sql, planid))
			return true;
		return false;		
	}	
	
	
	/**获取学习状态*/
	public List<Study> getUserStudy(int userid)
	{
		String sql = "select * from tab_study where user_id=?";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(Study.class), userid);
	}
	
	
	/**修改学习状态*/
	public boolean modifyStudyState(Study study) 
	{
		String sql = "update tab_study set percent=? where study_id=?";	
		dao.modify(sql, study.getPercent(), study.getStudy_id());			
		return true;		
	}
	
	
	/** 增加学习记录 */
	public boolean addStudy(Study study)
	{		
		String sql = "select * from tab_study where course_id=? and user_id=?";
		List<Study> studys = dao.selectList(sql, new BeanPropertyRowMapper<>(Study.class), study.getCourse_id(), study.getUser_id());
		if(null != studys && studys.size()>0) {
			System.out.println("addStudy" + studys.size());	
			study.setStudy_id(studys.get(0).getStudy_id());
			modifyStudyState(study);
		}else {	
	    	sql = "insert into tab_study(user_id, course_id, percent) values(?,?,?)";  			
	    	if(1 == dao.add(sql, study.getUser_id(), study.getCourse_id(), study.getPercent()))
    			return true;
    		}
		return false;
	}
	
	/** 删除计划 */
	public boolean deleteStudy(int studyid) 
	{
		String sql = "delete from tab_study where study_id=?";
		if(1 == dao.delete(sql, studyid))
			return true;
		return false;		
	}	
	
	
	/**获取科目学习进度
	 * @return */
	public AnalysisData getCourseProgress(Study study) {
		String sql = "select course_id,count(course_id) as count from tab_progress where user_id=? and course_id=? group by course_id";
		return dao.selectObject(sql, new BeanPropertyRowMapper<>(AnalysisData.class), study.getUser_id(), study.getCourse_id());		
	}
	
	/**写入科目学习进度
	 * @return */
	public boolean addCourseProgress(AnalysisData data) {
		String sql = "select course_id,user_id from tab_progress where user_id=? and course_id=? and content_id=?";
		List<AnalysisData> datas = dao.selectList(sql, new BeanPropertyRowMapper<>(AnalysisData.class), data.getUser_id(), data.getCourse_id(), data.getContent_id());		
	    if(null == datas || datas.size()==0) {
	    	sql = "insert into tab_progress(user_id, course_id, content_id) values(?,?,?)"; 
	    	dao.add(sql, data.getUser_id(),data.getCourse_id(),data.getContent_id());
	    	return true;
	    }	   
		return false;
	}

	/**获取用户学习课程*/
	public List<Course> getStudyCourse(int userid) {
		// TODO Auto-generated method stub
		String sql = "select a.* from tab_course a,tab_study b where a.course_id=b.course_id and b.user_id=?";
		List<Course> courses = dao.selectList(sql, new BeanPropertyRowMapper<>(Course.class), userid);
		return courses;
	}
	
	
}