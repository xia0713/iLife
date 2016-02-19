package com.szgs.bbs.answer;

import java.io.Serializable;
import java.util.List;

public class CreditExchangeListResponse implements Serializable {

	public boolean last;

	public List<Content> content;

	public class Content implements Serializable {
		public long id;
		public String title;
		public int requireScore;
		public String image;
	}

}
