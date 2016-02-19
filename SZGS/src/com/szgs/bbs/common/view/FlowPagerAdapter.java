package com.szgs.bbs.common.view;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FlowPagerAdapter extends FragmentPagerAdapter {

	private int mSize;
	private List<Fragment> mFragments;

	public FlowPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return this.mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return this.mSize;

		// return 1;
	}

	public void setFragments(List<Fragment> fragments) {

		this.mFragments = fragments;
		this.mSize = fragments.size();

		notifyDataSetChanged();
	}
}
