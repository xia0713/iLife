package com.szgs.bbs.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.szgs.bbs.common.Constans;

public class BitmapUtils {

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 30, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//		int options = 100;
//		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//			baos.reset();// 重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//			options -= 10;// 每次都减少10
//		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 根据uri来获取对应的bitmap
	 * 
	 * @param uri
	 * @param mContext
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存bitmap为file（jpeg）
	 * 
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static File bitmap2File(Bitmap bitmap, String fileName) {
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constans.DIR + "/image";
		File dir = new File(file_path);
		if (!dir.exists())
			dir.mkdirs();
		File file = new File(dir, fileName);
		try {
			FileOutputStream fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm, degree);
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;

	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * 
	 * @return degree 旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
}
