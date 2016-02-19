package com.szgs.bbs.mine;

import java.util.List;

public class MyQuestionResponse {
	
	public List<Content> content;
	
	public class Content{
		public long id;
		public String askTime;
		public String excerpt;//问题摘要;
		public Category category;
		
		public class Category{
			public long id;
			public String name;
		}
	}

}
