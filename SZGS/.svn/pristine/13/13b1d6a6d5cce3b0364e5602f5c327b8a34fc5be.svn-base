package com.szgs.bbs.common.util;


import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.index.QuestionsDetailResponse;

public class HomeDataUtils {
	public static QuestionsDetailResponse dealWithData(
			QuestionListResponse.Question question, String tag) {
		QuestionsDetailResponse response = new QuestionsDetailResponse();
		response.setTag(tag);
		response.setArea(question.category.name);
		response.setUsername(question.askBy.nickname);
		response.setAvatar(question.askBy.avatar);
		response.setAreaid(question.category.id);
		response.setTitle(question.title);
		response.setQuestionid(question.id);
		response.setResponsesum(question.commentCount);
		response.setZansum(question.agreeCount);
		response.setAskTime(question.askTime);
		return response;
	}
}
