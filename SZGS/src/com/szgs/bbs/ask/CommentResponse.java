package com.szgs.bbs.ask;

import java.util.List;

import com.szgs.bbs.common.Usermsg;

public class CommentResponse {

	public boolean first;
	public boolean last;
	public int totalElements;

	public List<Content> content;

	public class Content {
		public long id;
		public String commentTime;
		public String message;
		public Usermsg commentBy;
	}
}
