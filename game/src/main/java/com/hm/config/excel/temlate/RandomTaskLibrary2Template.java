package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("random_task_library2")
public class RandomTaskLibrary2Template {
	private Integer id;
	private String question;
	private String answer_right;
	private String answer_wrong1;
	private String answer_wrong2;
	private String answer_wrong3;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer_right() {
		return answer_right;
	}

	public void setAnswer_right(String answer_right) {
		this.answer_right = answer_right;
	}
	public String getAnswer_wrong1() {
		return answer_wrong1;
	}

	public void setAnswer_wrong1(String answer_wrong1) {
		this.answer_wrong1 = answer_wrong1;
	}
	public String getAnswer_wrong2() {
		return answer_wrong2;
	}

	public void setAnswer_wrong2(String answer_wrong2) {
		this.answer_wrong2 = answer_wrong2;
	}
	public String getAnswer_wrong3() {
		return answer_wrong3;
	}

	public void setAnswer_wrong3(String answer_wrong3) {
		this.answer_wrong3 = answer_wrong3;
	}
}
