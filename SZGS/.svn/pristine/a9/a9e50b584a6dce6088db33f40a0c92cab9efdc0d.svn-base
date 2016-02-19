package com.szgs.bbs.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * listview adapter基类，数据集结构为arraylist
 * 该类中实现了所有数据集操作方法，继承该类的adapter只需实现getConvertView方法即可
 * 
 * @author liinfeng
 * 
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> dataList = new ArrayList<T>();
	protected int from;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            不能为null
	 * @param list
	 *            可以为null
	 */
	public MyBaseAdapter(Context context, List<T> list) {
		this.context = context;
		if (list != null) {
			this.dataList = list;
		}
	}
	/**
	 * 构造函数
	 * 
	 * @param context
	 *            不能为null
	 * @param list
	 *            可以为null
	 */
	public MyBaseAdapter(Context context, List<T> list,int from) {
		this.context = context;
		this.from=from;
		if (list != null) {
			this.dataList = list;
		}
	}
	/**
	 * 添加数据集到列表中
	 * 
	 * @param list
	 */
	public void addData(List<T> list) {
		if (list != null) {
			Iterator<T> iterator = list.iterator();
			if (iterator.hasNext()) {
				this.dataList.add(iterator.next());
			}
		}
	}

	/**
	 * 设置数据序列
	 * 
	 * @param list
	 */
	public void setData(List<T> list) {
		if (list != null) {
			this.dataList = list;
		}
	}

	/**
	 * 清除所有数据
	 */
	public void clearData() {
		if (dataList != null) {
			dataList.clear();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.dataList.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return getConvertView(position, convertView,
				LayoutInflater.from(this.context), parent);
	}

	/**
	 * 该方法返回的view即原来getview方法return 的view。
	 * 
	 * @param position
	 * @param convertView
	 * @param inflater
	 * @param parent
	 * @return
	 */
	public abstract View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent);
}
