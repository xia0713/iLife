package com.szgs.bbs.ui;

import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.daohang.StartActivity;
import com.szgs.bbs.index.HomeActivity;
import com.szgs.bbs.mine.NotificationActivity;

/**
 * 进入程序欢迎页面
 * 
 * @author db
 * 
 */
public class WelcomeActivity extends BaseActivity {

	private Boolean isFirstIn;
	private String installationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		isFirstIn = SharedPranceUtils.GetBolDate("isFirstIn", this, true);
			new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new Thread();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (isFirstIn) {
					LggsUtils.StartIntent(WelcomeActivity.this,
							StartActivity.class);
					WelcomeActivity.this.finish();
				} else {
					if (CacheUtils.getIsRememberPwd(WelcomeActivity.this)) {
						LggsUtils.StartIntent(WelcomeActivity.this,
								HomeActivity.class);
					} else {
						LggsUtils.StartIntent(WelcomeActivity.this,
								SelectLoginorRegisActivity.class);
					}
					WelcomeActivity.this.finish();
				}

			}
		}).start();
		;
	}
}
