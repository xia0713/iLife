package com.szgs.bbs;

import android.os.Bundle;

import com.jianq.base.network.JqRequestAgent;
import com.jianq.base.ui.JQXmasBaseFragment;
import com.jianq.base.util.JQApplicationConfig;
import com.szgs.bbs.common.Constans;

/**
 * 基础fragment,配置一些共有特性
 * 
 * @author zwj
 * 
 */
public class BaseFragment extends JQXmasBaseFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 根据requestType的值决定返回requestAgent对象的类型
	 * 
	 * @return
	 */
	public JqRequestAgent getBaseRequestAgent() {

		String requestType = JQApplicationConfig.getConfigValue("requestType");
		if (requestType.equals("http")) {

			return getJqRequestAgent();

		} else if (requestType.equals("https")) {

			return getJqHttpsRequestAgent(
					getResources().openRawResource(
							Constans.CERTIFICATE_SOURCE_ID),
					Constans.CERTIFICATE_PASSWORD.toCharArray());
		}
		return getJqRequestAgent();
	}

	/**
	 * 根据请求类型获取不同的baseurl
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		String baseUrl = null;
		String requestType = JQApplicationConfig.getConfigValue("requestType");
		if (requestType.equals("http")) {

			baseUrl = JQApplicationConfig.getDomain();
		} else if (requestType.equals("https")) {

			baseUrl = JQApplicationConfig
					.getDomain(JQApplicationConfig.SCHEME_HTTPS);
		}
		return baseUrl;
	}

	/**
	 * 数据刷新
	 */
	public void onRefreshData() {

	}

}
