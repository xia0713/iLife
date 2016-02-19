package com.szgs.bbs.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private Map<String, Integer> fragmentsId = new HashMap<String, Integer>();

	private int index = 0;

	public HomePagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public void addFragment(Fragment fragment, String name) {
		if (fragment != null) {
			fragments.add(fragment);
			fragmentsId.put(name, new Integer(index));
			++index;
		}
	}

	/**
	 * 获取name对应的fragment 的id
	 * 
	 * @param name
	 * @return <0则fragment不存在
	 */
	public int getFragmentsIdByName(String name) {
		if (!fragmentsId.containsKey(name)) {
			return -1;
		} else {
			return fragmentsId.get(name);
		}
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
