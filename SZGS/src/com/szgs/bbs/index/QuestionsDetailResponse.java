package com.szgs.bbs.index;

public class QuestionsDetailResponse {
	private String username;
	private String releasetime;
	private String tag;
	private String area;
	private String responsecount;
	private String content;
	private String response;
	private String avatar;
	private long areaid;
	private long questionid;
	private int responsesum;
	private int zansum;
	private String title;
	private String askTime;
	

	public String getAskTime() {
		return askTime;
	}

	public void setAskTime(String askTime) {
		this.askTime = askTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getAreaid() {
		return areaid;
	}

	public void setAreaid(long areaid) {
		this.areaid = areaid;
	}

	public long getQuestionid() {
		return questionid;
	}

	public void setQuestionid(long questionid) {
		this.questionid = questionid;
	}

	public int getResponsesum() {
		return responsesum;
	}

	public void setResponsesum(int responsesum) {
		this.responsesum = responsesum;
	}

	public int getZansum() {
		return zansum;
	}

	public void setZansum(int zansum) {
		this.zansum = zansum;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getResponsecount() {
		return responsecount;
	}

	public void setResponsecount(String responsecount) {
		this.responsecount = responsecount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
