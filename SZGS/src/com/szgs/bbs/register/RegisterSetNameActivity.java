package com.szgs.bbs.register;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.BitmapUtils;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.index.HomeActivity;

public class RegisterSetNameActivity extends BaseActivity implements
		OnClickListener {

	private View v_upload_avatar;
	private TextView tv_upload_avatar, tv_username_hint;
	private CircleImageView iv_avatar;
	private EditText et_set_username;
	private Button btn_set_complete;
	private ProgressDialog myProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_set_name);
		initHeaderView();
		initViews();
	}

	public void initViews() {
		v_upload_avatar = findViewById(R.id.v_upload_avatar);
		et_set_username = (EditText) findViewById(R.id.et_set_username);
		btn_set_complete = (Button) findViewById(R.id.btn_set_complete);
		tv_upload_avatar = (TextView) findViewById(R.id.tv_upload_avatar);
		tv_username_hint = (TextView) findViewById(R.id.tv_username_hint);
		iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);
		v_upload_avatar.setOnClickListener(this);
		btn_set_complete.setOnClickListener(this);
		et_set_username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 11) {
					LggsUtils.ShowToast(RegisterSetNameActivity.this, "用户昵称过长");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void initHeaderView() {
		findViewById(R.id.top_left_tv).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_title)).setText("完善个人信息");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.v_upload_avatar:
			photoFileName = System.currentTimeMillis() + ".jpeg";

			setWindowAlpha(0.4f);
			hideKeybord(v_upload_avatar);

			SelectPopupWindow popup = new SelectPopupWindow(
					RegisterSetNameActivity.this);
			popup.showAtLocation(v_upload_avatar, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			popup.setOutsideTouchable(true);
			popup.setFocusable(false);
			popup.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// 背景变亮
					setWindowAlpha(1f);
				}
			});
			break;
		case R.id.btn_set_complete:
			if (!TextUtils.isEmpty(et_set_username.getText().toString())) {
				sendData();
			} /*
			 * else if
			 * (LggsUtils.getStringLength(et_set_username.getText().toString())
			 * > 12) { LggsUtils.ShowToast(this, "请输入小于12个字符的昵称"); }
			 */else {
				LggsUtils.ShowToast(this, "请输入昵称");
			}
			break;
		}

	}

	private void sendData() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "signup/step/1";
		client.setConnectTimeout(5000);
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载数据。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		params.put("nickname", et_set_username.getText().toString());

		try {
			params.put("avatarFile", avatarFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Gson gson = new Gson();
				if (statusCode == 200) {
					LggsUtils.ShowToast(RegisterSetNameActivity.this, "设置成功");
					UserMsgResponse userresponse = gson.fromJson(
							response.toString(), UserMsgResponse.class);
					long userId = userresponse.id;
					CacheUtils.saveUserID(RegisterSetNameActivity.this, userId);
					CacheUtils.saveAvatar(RegisterSetNameActivity.this,
							userresponse.avatar);
					CacheUtils.saveUserName(RegisterSetNameActivity.this,
							userresponse.nickname);
					LggsUtils.StartIntent(RegisterSetNameActivity.this,
							HomeActivity.class);
					finish();
				} else {
					LggsUtils.ShowToast(RegisterSetNameActivity.this,
							"服务器异常，登录失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils
						.ShowToast(RegisterSetNameActivity.this, "服务器异常，登录失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(RegisterSetNameActivity.this,
						responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				LggsUtils
						.ShowToast(RegisterSetNameActivity.this, "服务器异常，登录失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
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
				// goCropBitmap(avatarUri, PHOTO_PICKED_WITH_DATA);
				Bitmap bm = BitmapUtils.getBitmapFromUri(avatarUri, this);
				bm = BitmapUtils.compressImage(bm);
				photoFileName = System.currentTimeMillis() + ".jpeg";
				if (bm != null) {
					iv_avatar.setImageBitmap(bm);
					iv_avatar.setVisibility(View.VISIBLE);
					tv_upload_avatar.setVisibility(View.GONE);
					avatarFile = BitmapUtils.bitmap2File(bm, photoFileName);
					sendData();
				}
			} else {
				LggsUtils.ShowToast(this, "获取图片失败！请重新拍照或选择");
			}
			break;
		case CAMERA_WITH_DATA: // 拍照
			Bitmap photo = BitmapUtils.getSmallBitmap(PHOTO_FILE_PATH + "/"
					+ photoFileName);
			avatarUri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), photo, null, null));
			goCropBitmap(avatarUri, CAMERA_WITH_DATA);
			break;
		case CROP_BITMAP: // 剪切
			Bitmap bitmap = BitmapUtils.getBitmapFromUri(avatarUri, this);
			// Bitmap bitmap = data.getParcelableExtra("data");
			if (bitmap != null) {
				iv_avatar.setImageBitmap(bitmap);
				iv_avatar.setVisibility(View.VISIBLE);
				tv_upload_avatar.setVisibility(View.GONE);
				avatarFile = BitmapUtils.bitmap2File(bitmap, photoFileName);
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
		intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
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
