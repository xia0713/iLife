package com.szgs.bbs.common.interfaces;

/** 如果需要对布局控件等初始化，让对应的Activity或Fragment实现该接口（为了代码的规范化） */
public interface Initialization {
	/** 执行findviewbyid等操作 */
	public void onInitViews();

	/** 绑定监听器 */
	public void onInitListener();

	/** 请求网络数据等 */
	public void onInitData();

}
