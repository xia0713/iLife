package com.szgs.bbs.mine;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.BitmapUtils;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.register.UserMsgResponse;

public class MyInfoSettingActivity extends BaseActivity implements
		OnClickListener {

	private TextView top_left_tv;
	private TextView tv_title;
	private CircleImageView myinfo_setting_avatar_iv;
	private TextView myifo_setting_username_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo_setting);
		initHeaderView();
		initView();
	}

	private void initView() {
		myinfo_setting_avatar_iv = (CircleImageView) findViewById(R.id.myinfo_setting_avatar_iv);
		ImageLoader.getInstance().displayImage(
				CacheUtils.getAvatar(MyInfoSettingActivity.this),
				myinfo_setting_avatar_iv);
		myifo_setting_username_tv = (TextView) findViewById(R.id.myifo_setting_username_tv);
		findViewById(R.id.myinfo_setting_change_avatar)
				.setOnClickListener(this);
		findViewById(R.id.myinfo_setting_change_name).setOnClickListener(this);
		findViewById(R.id.myinfo_setting_change_pwd).setOnClickListener(this);
		findViewById(R.id.myinfo_setting_find_pwd_tv).setOnClickListener(this);

	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("个人信息设置");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.myinfo_setting_change_avatar:
			
			setWindowAlpha(0.4f);
			SelectPopupWindow popup = new SelectPopupWindow(
					MyInfoSettingActivity.this);
			popup.showAtLocation(myinfo_setting_avatar_iv, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			popup.setOutsideTouchable(true);
			popup.setFocusable(false);
			popup.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					setWindowAlpha(1f);
				}
			});
			break;
		case R.id.myinfo_setting_change_name:
			LggsUtils.StartIntent(MyInfoSettingActivity.this,
					ChangeUsernameAcitvity.class);
			break;
		case R.id.myinfo_setting_change_pwd:
			LggsUtils.StartIntent(MyInfoSettingActivity.this,
					ChangePwdActivity.class);
			break;
		case R.id.myinfo_setting_find_pwd_tv:

			break;
		}

	}

	private void sendData() {
		final ProgressDialog pd = new ProgressDialog(MyInfoSettingActivity.this);

		final AsyncHttpClient client = getClient();
		String url = Constans.URL + "avatar/edit";
		client.setConnectTimeout(5000);
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		try {
			params.put("avatarFile", avatarFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				pd.setCancelable(true);
				pd.setMessage("正在加载。。。");
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						client.cancelAllRequests(true);
					}
				});
				pd.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Gson gson = new Gson();
				if (statusCode == 200) {
					LggsUtils.ShowToast(MyInfoSettingActivity.this, "设置成功");
					UserMsgResponse userresponse = gson.fromJson(
							response.toString(), UserMsgResponse.class);
					CacheUtils.saveAvatar(MyInfoSettingActivity.this,
							userresponse.avatar);
					myinfo_setting_avatar_iv.setImageBitmap(avatarBitmap);
				} else {
					LggsUtils.ShowToast(MyInfoSettingActivity.this,
							"服务器异常，设置失败！");
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				LggsUtils.ShowToast(MyInfoSettingActivity.this, arg2);
				myinfo_setting_avatar_iv.setImageBitmap(avatarBitmap);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				if(arg0==401){
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(MyInfoSettingActivity.this, arg2);
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});
	}

	class SelectPopupWindow extends PopupWindow {
		private View mMenuView;

		public SelectPopupWindow(Context context) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.popupwindow_change_avatar,
					null);
			mMenuView.findViewById(R.id.popup_change_avatar_camera)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							doTakePhoto();
							dismiss();
						}
					});
			mMenuView.findViewById(R.id.popup_change_avatar_photo)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							doSelectImageFromLoacal();
							dismiss();
						}
					});
			mMenuView.findViewById(R.id.popup_change_avatar_cancel)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dismiss();
						}
					});

			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.MATCH_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.AnimBottoms);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(R.color.black);
			// 设置SelectPicPopupF
			this.setBackgroundDrawable(dw);
		}
	}

	// 标识
	private static final int CAMERA_WITH_DATA = 1001;
	private static final int PHOTO_PICKED_WITH_DATA = 1002;
	private static final int CROP_BITMAP = 1003;

	/**
	 * 拍照获取图片
	 * 
	 */
	private void doTakePhoto() {
		try {
			File dir = new File(PHOTO_FILE_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			photoFileName = System.currentTimeMillis() + ".jpeg";
			File f = new File(dir, photoFileName);
			Uri u = Uri.fromFile(f);
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从本地手机中选择图片
	 */
	private void doSelectImageFromLoacal() {
		Intent localIntent = new Intent();
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
		Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
		startActivityForResult(localIntent2, PHOTO_PICKED_WITH_DATA);
	}

	private File avatarFile;
	private Bitmap avatarBitmap;
	private Uri avatarUri;
	private static final String PHOTO_FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Constans.DIR
			+ "/image";
	private String photoFileName;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: // 从本地选择图片
			if (data != null && data.getData() != null) {
				avatarUri = data.getData();
				goCropBitmap(avatarUri, PHOTO_PICKED_WITH_DATA);
//				Bitmap bm = BitmapUtils.getBitmapFromUri(avatarUri, this);
//				bm = BitmapUtils.compressImage(bm);
//				photoFileName = System.currentTimeMillis() + ".jpeg";
//				if (bm != null) {
//					avatarBitmap = bm;
//					avatarFile = BitmapUtils.bitmap2File(bm, photoFileName);
//					sendData();
//				}
			} else {
				LggsUtils.ShowToast(this, "获取图片失败！请重新拍照或选择");
			}
			break;
		case CAMERA_WITH_DATA: // 拍照
			photoFileName = System.currentTimeMillis() + ".jpeg";
			Bitmap photo = BitmapUtils.getSmallBitmap(PHOTO_FILE_PATH + "/"
					+ photoFileName);
			avatarUri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), photo, null, null));
			goCropBitmap(avatarUri, CAMERA_WITH_DATA);
			break;
		case CROP_BITMAP:
//			Bitmap bitmap = BitmapUtils.getBitmapFromUri(avatarUri, this);
			 Bitmap bitmap = data.getParcelableExtra("data");
			photoFileName = System.currentTimeMillis() + ".jpeg";
			if (bitmap != null) {
				avatarBitmap = bitmap;
				avatarFile = BitmapUtils.bitmap2File(bitmap, photoFileName);
				sendData();
			}
		}

	}
	

	/**
	 * 跳转到系统图片剪切
	 * 
	 * @param uri
	 */
	private void goCropBitmap(Uri uri, int flag) {
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setType("image/*");
//		String[] str = uri.toString().split("/");
//		if(str[2].equals("media")){
			intent.setData(uri);
//			intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
//		}else{
//			Bitmap bm = BitmapUtils.getBitmapFromUri(uri, this);
//			File f = BitmapUtils.bitmap2File(bm, System.currentTimeMillis() + ".jpeg");
//			
//			intent.putExtra("data", bm);
//		}
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);// 输出图片大小
		intent.putExtra("outputY", 150);
		if (flag == CAMERA_WITH_DATA) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra("return-data", false);
		}
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_BITMAP);
	}
}
