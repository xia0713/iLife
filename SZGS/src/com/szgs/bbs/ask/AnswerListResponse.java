package com.szgs.bbs.ask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.Usermsg;
import com.szgs.bbs.find.FindCategoryResponse;

public class AnswerListResponse implements Serializable {

	/**
	 * 是否是第一页
	 */
	public boolean first;
	/**
	 * 是否是最后一页
	 */
	public boolean last;
	/**
	 * 当前页码，后台实际是从0开始算的
	 */
	public int number;
	/**
	 * content中包含元素的总数
	 */
	public int numberOfElements;
	/**
	 * 没页记录数
	 */
	public int size;
	/**
	 * 总记录数
	 */
	public int totalElements;
	/**
	 * 总页数
	 */
	public int totalPages;

	public List<Content> content;

	public class Content implements Serializable {
		public long id;
		public int againstCount;
		public int agreeCount;
		public boolean anonymous;
		public String answerTime;
		public int commentCount;
		public String content;
		public String excerpt;
		public int favouritesCount;
		public int viewCount;
		public String updateTime;
		public Usermsg answerBy;
		public QuestionListResponse.Question question;

	}
}
