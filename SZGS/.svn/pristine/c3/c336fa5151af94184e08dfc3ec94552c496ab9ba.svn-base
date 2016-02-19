package com.szgs.bbs;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.index.HomeActivity;

public class LggsApplication extends Application {

	/**
	 * 自定义管理aictivity栈，为了方便程序的完全退出操作
	 */
	private List<Activity> activityStack;
	private static LggsApplication mInstance;

	public LggsApplication() {
		activityStack = new LinkedList<Activity>();
		if (mInstance == null) {
			mInstance = this;
		}
	}

	public synchronized static LggsApplication getInstance() {
		if (null == mInstance) {
			mInstance = new LggsApplication();
		}
		return mInstance;
	}

	/** 将activity压入栈中 */
	public void pushActivity(Activity activity) {

		// LoginActivity和HomeActivity被创建时会将其加入activity栈中，为什么不在BaseActivity$onCreate()中添加？
		// 因为不是所有的activity都需要压栈，为了内存考虑所以不必在BaseActivity中添加

		if (activityStack.contains(activity)) {
			return;
		}
		activityStack.add(activity);
	}

	/** finish activity并且将其从栈中移除，需要结束一个activity时不要直接finish，最好调用该方法去结束它 */
	public void popActivity(Activity activity) {
		if (activity == null || !activityStack.contains(activity)) {
			return;
		}
		if (!activity.isFinishing()) {

			activity.finish();
		}
		activityStack.remove(activity);
	}

	/** 从任意界面返回到login界面 */
	public void backToLogin() {
		for (Iterator<Activity> iterator = activityStack.iterator(); iterator
				.hasNext();) {

			Activity activity = iterator.next();
			if (activity instanceof LoginActivity) {
				continue;
			}
			// 先finish再remove
			activity.finish();
			iterator.remove();
		}
	}

	/** 从任意界面返回到login界面 */
	public void backToHome() {
		for (Iterator<Activity> iterator = activityStack.iterator(); iterator
				.hasNext();) {

			Activity activity = iterator.next();
			if (activity instanceof HomeActivity) {
				continue;
			}
			// 先finish再remove
			activity.finish();
			iterator.remove();
		}
	}

	/** 清除所有activity并退出程序 */
	public void exit() {

		for (Iterator<Activity> iterator = activityStack.iterator(); iterator
				.hasNext();) {
			Activity activity = iterator.next();
			// 先finish再remove
			activity.finish();
			iterator.remove();
		}
		Runtime.getRuntime().exit(0);
	}

	public void initImageLoader() {

		File file = new File(Environment.getExternalStorageDirectory() + "/"
				+ getPackageName() + "/cache/image");
		if (!file.exists()) {
			file.mkdirs();
		}
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), Constans.DIR + "/cache/image");
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPriority(Thread.NORM_PRIORITY)
				// 加载图片的线程数
				.denyCacheImageMultipleSizesInMemory()
				// 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置加载显示图片队列进程
				.memoryCache(new LRULimitedMemoryCache(20 * 512 * 512))
				// 15mb 改变缓存策略
				.diskCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader();
		AVOSCloud.initialize(this,
				"o63lfoypttcs8hgpygrr61046ghtcqqgtpmibc1bzyw7lc27",
				"h9m6817a4anpj34zk64b3lueyi2ibuos5v59lf90sdcnyl4q");
		AVOSCloud.setDebugLogEnabled(true);
	}
}
