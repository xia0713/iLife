package com.szgs.bbs.common;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;

/**
 * 问题列表的response
 * 
 * @author db
 * 
 */
public class QuestionListResponse {
	/**
	 * 问题详情的列表
	 */
	public ArrayList<Question> content;
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
	 * 
	 * 总页数
	 */
	public int totalPages;

	@SuppressLint("ParcelCreator")
	public static class Question implements Serializable {
		public long id;
		public int againstCount;
		public int agreeCount;
		public boolean anonymous;
		public int answerCount;
		public String askTime;
		public String answerTime;
		public int commentCount;
		public String excerpt;
		public Categoryd category;
		public String title;
		public Usermsg askBy;
		public String comments;
		public int rewardScore;
		public String tag;
		public AnswerBy answerBy;
		public QuestionDetail question;
		public BestAnswer bestAnswer;
		public Status status;

		public class Status implements Serializable {
			public int id;
			public String description;
		}

		public class BestAnswer implements Serializable {

		}

		public class Categoryd implements Serializable {
			public int id;
			public String name;
		}

		public class AnswerBy implements Serializable {
			public int id;
			public String nickname;
			public String avatar;
			public String mobilePhone;
		}

		public class QuestionDetail implements Serializable {
			public int id;
			public String content;
			public String excerpt;
			public int commentCount;
			public Categoryd category;
			public Usermsg askBy;
			public String title;
			public int answerCount;
			public int rewardScore;
			public Status status;
			public String askTime;
		}
	}

}
