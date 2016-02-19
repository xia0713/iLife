package com.szgs.bbs.answer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.szgs.bbs.find.FindCategoryResponse;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class MyInterestingQuestionPagerAdapter extends FragmentStatePagerAdapter {

	private List<String> titles = new ArrayList<String>();

	private List<Fragment> fragments = new ArrayList<Fragment>();

	private Context context;

	public MyInterestingQuestionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyInterestingQuestionPagerAdapter(FragmentManager fm, Context context) {
		this(fm);
		this.context = context;
	}

	/**
	 * 设置tabs
	 * 
	 * @param list
	 *            title
	 */
	public void setTabs(List<FindCategoryResponse> list) {
		if (list != null) {
			titles.clear();
			fragments.clear();
			for (int i = 0; i < list.size(); i++) {
				titles.add(list.get(i).name);
				Log.i("Tag", "id" + list.get(i).id);
				fragments.add(new MyInterestingQustionFragment(list.get(i).id, context));
			}
			// Iterator<FindCategoryResponse> iterator = list.iterator();
			// while (iterator.hasNext()) {
			// titles.add(iterator.next().name);
			// fragments.add(new MyInterestingQustionFragment(1));
			// }
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

	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}

//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		// TODO Auto-generated method stub
//		MyInterestingQustionFragment f = (MyInterestingQustionFragment) super.instantiateItem(container, position);
//		f.sendRequset();
//		return f;
//	}

}
