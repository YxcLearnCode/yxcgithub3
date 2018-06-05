package com.example.hucx.myapplication.beans;

public class Answer {
	
	private String exam_id;
	private int topic_id;
	private String answer;
	private int iserror;
	
	public int getIserror() {
		return iserror;
	}
	public void setIserror(int iserror) {
		this.iserror = iserror;
	}
	public String getExam_id() {
		return exam_id;
	}
	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
