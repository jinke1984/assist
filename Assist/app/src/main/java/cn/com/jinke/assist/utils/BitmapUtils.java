package cn.com.jinke.assist.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.jinke.assist.booter.ProjectApplication;


public class BitmapUtils {

	public static final String PERSION_SAVE_PATH = Environment.getExternalStorageDirectory() + "/PROJECT/IMAGE/";
	private BitmapUtils() {
	}

	private static int m_screenWidth = -1;
	private static int m_screenHeight = -1;
	private static final int OPTIONS_NONE = 0x0;
	private static final int OPTIONS_SCALE_UP = 0x1;
	private static DisplayMetrics m_displayMetrics;

	/**
	 * Constant used to indicate we should recycle the input in
	 * {@link #extractThumbnail(Bitmap, int, int, int)} unless the output is the
	 * input.
	 */
	public static final int OPTIONS_RECYCLE_INPUT = 0x2;

	/**
	 * Creates a centered bitmap of the desired size.
	 * 
	 * @param source
	 *            original bitmap source
	 * @param width
	 *            targeted width
	 * @param height
	 *            targeted height
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
		return extractThumbnail(source, width, height, OPTIONS_NONE);
	}

	/**
	 * Creates a centered bitmap of the desired size.
	 * 
	 * @param source
	 *            original bitmap source
	 * @param width
	 *            targeted width
	 * @param height
	 *            targeted height
	 * @param options
	 *            options used during thumbnail extraction
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height,
			int options) {
		if (source == null) {
			return null;
		}

		float scale;
		if (source.getWidth() < source.getHeight()) {
			scale = width / (float) source.getWidth();
		} else {
			scale = height / (float) source.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap thumbnail = transform(matrix, source, width, height,
				OPTIONS_SCALE_UP | options);
		return thumbnail;
	}

	/**
	 * Transform source Bitmap to targeted width and height.
	 */
	private static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, int options) {
		if (source == null || source.isRecycled()) {
			return null;
		}

		boolean scaleUp = (options & OPTIONS_SCALE_UP) != 0;
		boolean recycle = (options & OPTIONS_RECYCLE_INPUT) != 0;

		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1 = null;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			try {
				b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
						source.getHeight(), scaler, true);
			} catch (OutOfMemoryError error) {
				// TODO: handle exception
				error.printStackTrace();
			}
		} else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		if (b1 == null) // modify by zj 2013-05-10
		{
			return null;
		}
		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = null;
		try {
			b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
					targetHeight);
		} catch (OutOfMemoryError e) {
			b2 = null;
			return b2;
		}

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}
		return b2;
	}

	/**
	 * ��ȡѹ����ͼƬ�����ͼƬ��������ѹ��?
	 * 
	 * @param a_bitmapSrc
	 * @param a_iMaxNumOfPixels
	 * @return
	 */
	public static Bitmap getLowMemoryBitmap(String a_bitmapSrc,
			int a_iMaxNumOfPixels, boolean mFlag) {
		return processedBitmap(a_bitmapSrc, -1, a_iMaxNumOfPixels, mFlag);
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static Bitmap processedBitmap(String a_strBitmapSrc,
			int a_iMinSideLength, int a_iMaxNumOfPixels, boolean mFlag) {
		// PhonePlusData.log("yn", "strpath: "+ a_strBitmapSrc
		// +"   minlength: "+a_iMinSideLength
		// +"  numPixxels: "+a_iMaxNumOfPixels);
		if (a_strBitmapSrc == null)
			return null;

		// if (PhonePlusData.ms_iLogLevel != -1)
		// {
		// try
		// {
		// Bitmap bitmap = BitmapFactory.decodeFile(a_strBitmapSrc);
		// PhonePlusData.log(
		// TAG,
		// "computeSampleSize start height:" + bitmap.getHeight() + ",width:" +
		// bitmap.getWidth() +
		// ",memory:" + bitmap.getRowBytes()
		// * bitmap.getHeight() / 1024 + "KB");
		// }
		// catch (OutOfMemoryError e)
		// {
		// // TODO: handle exception
		// }
		// catch (Exception e)
		// {
		// // TODO: handle exception
		// }
		// }

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(a_strBitmapSrc, opts);
		int initialSize = computeInitialSampleSize(opts, a_iMinSideLength,
				a_iMaxNumOfPixels);
		int roundedSize;
		if ((opts.outWidth > opts.outHeight * 2)
				|| (opts.outHeight > opts.outWidth * 2)) {
			roundedSize = 2;
		} else {
			if (initialSize <= 8) {
				roundedSize = 1;
				while (roundedSize < initialSize) {
					roundedSize <<= 1;
				}
			} else {
				roundedSize = (initialSize + 7) / 8 * 8;
			}
		}
		opts.inSampleSize = roundedSize;
		opts.inJustDecodeBounds = false;
		opts.inPurgeable = true;
		if (mFlag) {
			opts.inPreferredConfig = Config.RGB_565;
		} else {
			opts.inPreferredConfig = Config.ARGB_8888;
		}

		Bitmap bitmap = null;
		if (roundedSize == 1) {
			try {
				bitmap = BitmapFactory.decodeFile(a_strBitmapSrc, opts);
			} catch (OutOfMemoryError e) {
				// PhonePlusData.log("zj",
				// "get bitmap is outofmemory---------->one");
			}
		} else {
			try {
				bitmap = BitmapFactory.decodeFile(a_strBitmapSrc, opts);
			} catch (OutOfMemoryError e) {
				// PhonePlusData.log("zj",
				// "get bitmap is outofmemory---------->two");
			}
		}
		// PhonePlusData.log("zj", "get lowmemoryis : " + a_strBitmapSrc +
		// "  bitmap: " + bitmap);
		return bitmap;
	}

	private static int computeInitialSampleSize(
			BitmapFactory.Options a_oOptions, int a_iMinSideLength,
			int a_iMaxNumOfPixels) {
		double w = a_oOptions.outWidth;
		double h = a_oOptions.outHeight;

		int lowerBound = (a_iMaxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / a_iMaxNumOfPixels));
		int upperBound = (a_iMinSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / a_iMinSideLength),
				Math.floor(h / a_iMinSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((a_iMaxNumOfPixels == -1) && (a_iMinSideLength == -1)) {
			return 1;
		} else if (a_iMinSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * 
	 * @param path
	 *            ͼƬ����·��
	 * @return degree��ת�ĽǶ�
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
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
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * ��ȡ�Ҷ�ͼƬ
	 * 
	 * @param original
	 *            ԭʼͼƬ
	 * @return �Ҷȴ�����ͼƬ
	 */
	public static Bitmap toGrayscale(Bitmap original) {
		int width, height;
		height = original.getHeight();
		width = original.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(original, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * ���ݸ߿������ͼ�?
	 * 
	 * @param bitmap
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int viewWidth,
			int viewHeight) {
		if (bitmap == null || viewWidth == 0 || viewHeight == 0) {
			return bitmap;
		}
		try {
			int mapWidth = bitmap.getWidth();
			int mapHeigh = bitmap.getHeight();
			float scale = 0;

			if ((float) mapWidth / (float) mapHeigh < (float) viewWidth
					/ (float) viewHeight) {
				scale = (float) viewWidth / (float) mapWidth;
			} else {
				scale = (float) viewHeight / (float) mapHeigh;
			}

			// PhonePlusData.log("zdx", "scaleBitmap:" + scale + "  mapWidth :"
			// + mapWidth + "  mapHeigh:" + mapHeigh + "  viewWidth:" +
			// viewWidth
			// + "  viewHeight:" + viewHeight);

			bitmap = Bitmap.createScaledBitmap(bitmap,
					(int) (mapWidth * scale), (int) (mapHeigh * scale), true);

			if (bitmap != null) {
				// PhonePlusData.log("zdx", "scaleBitmap��new width:" +
				// bitmap.getWidth() + "    new height:" + bitmap.getHeight());
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static Bitmap decodeSampledFile(String path, int reqWidth,
			int reqHeight, boolean isStretched) {
		if (TextUtils.isEmpty(path) || reqWidth <= 0 || reqHeight <= 0) {
			return null;
		}

		final BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);

		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds = false;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inPreferredConfig = Config.RGB_565; // Android
														// 4.0��ʼ��������Ч����ʹ���ã�Ҳ�ǰ�
														// ARGB_8888 ����

		Bitmap bitmap = null;
		try {
			// TODO�� �ж�ͼƬ����ת�Ƕ�
			int degree = BitmapUtils.readPictureDegree(path);
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap no_bitmap = BitmapFactory.decodeFile(path, opts);
			bitmap = Bitmap.createBitmap(no_bitmap, 0, 0, no_bitmap.getWidth(),
					no_bitmap.getHeight(), matrix, true);
			if (bitmap != null) {
				if (isStretched) {
					bitmap = extractThumbnail(bitmap, reqWidth, reqHeight);
				} else {
					float scale = getScaleRate(bitmap.getWidth(),
							bitmap.getHeight(), reqWidth, reqHeight);
					bitmap = extractThumbnail(bitmap,
							(int) (scale * bitmap.getWidth()),
							(int) (scale * bitmap.getHeight()));
				}
			}
		} catch (Exception e) {
			// PhonePlusData.log("dly", "decodeSampledFile:" + e.getMessage());
		}
		return bitmap;
	}

	/**
	 * ����inSampleSize
	 * 
	 * @param oldValue
	 * @return
	 */
	private static int adjustInSampleSize(int oldValue) {
		if (oldValue > 1 && oldValue < 2) {
			return 2;
		} else if ((oldValue > 2 && oldValue < 4)
				|| (oldValue > 4 && oldValue < 6)) {
			return 4;
		} else if ((oldValue > 6 && oldValue < 8)
				|| (oldValue > 8 && oldValue < 12)) {
			return 8;
		} else if ((oldValue > 12 && oldValue < 16)
				|| (oldValue > 16 && oldValue < 24)) {
			return 16;
		} else if ((oldValue > 24 && oldValue < 32)
				|| (oldValue > 32 && oldValue < 48)) {
			return 32;
		} else {
			return oldValue;
		}
	}
	
	public static int getScreenWidth() {
		if (m_screenWidth == -1) {
			initDisplayMetrics();
			m_screenWidth = m_displayMetrics.widthPixels;
		}
		return m_screenWidth;
	}
	
	private static void initDisplayMetrics()
	{
		if (m_displayMetrics == null)
		{
			m_displayMetrics = new DisplayMetrics();
			WindowManager wndMgr = (WindowManager) ProjectApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
			wndMgr.getDefaultDisplay().getMetrics(m_displayMetrics);
		}
	}

	public static int getScreenHeight() {
		if (m_screenHeight == -1) {
			initDisplayMetrics();
			m_screenHeight = m_displayMetrics.heightPixels;
		}
		return m_screenHeight;
	}

	public static Bitmap decodeFitScreenFile(String path) {
		int width = getScreenWidth();
		int height = getScreenHeight();
		return decodeSampledFile(path, width, height, false);
	}

	public static Bitmap decodeSampledFile(Resources res, int resId,
			int reqWidth, int reqHeight, boolean isStretched) {
		if (res == null || resId == 0 || reqWidth == 0 || reqHeight == 0) {
			return null;
		}

		final BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, opts);

		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds = false;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inPreferredConfig = Config.RGB_565;

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(res, resId, opts);
			if (bitmap != null) {
				if (isStretched) {
					bitmap = extractThumbnail(bitmap, reqWidth, reqHeight);
				} else {
					float scale = getScaleRate(bitmap.getWidth(),
							bitmap.getHeight(), reqWidth, reqHeight);
					bitmap = extractThumbnail(bitmap,
							(int) (scale * bitmap.getWidth()),
							(int) (scale * bitmap.getHeight()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	// /**
	// * ����BitmapFactory.Options������inSampleSize�����ű�����ֵ
	// *
	// * @param options
	// * BitmapFactory.Options ����
	// * @param reqWidth
	// * ���ſ��?
	// * @param reqHeight
	// * ���Ÿ߶�
	// * @return inSampleSize�����ű�����ֵ
	// */
	// public static int calculateInSampleSize(BitmapFactory.Options options,
	// int reqWidth, int
	// reqHeight)
	// {
	// final int height = options.outHeight;
	// final int width = options.outWidth;
	// int inSampleSize = 1;
	//
	// if (height > reqHeight || width > reqWidth)
	// {
	// final int heightRatio = Math.round((float) height / (float) reqHeight);
	// final int widthRatio = Math.round((float) width / (float) reqWidth);
	//
	// inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	// }
	// return adjustInSampleSize(inSampleSize);
	// }

	public static float getScaleRate(int srcWidth, int srcHeight, int dstWidth,
			int dstHeight) {
		if (srcWidth > dstWidth || srcHeight > dstHeight) {
			float scaleWidth = (float) dstWidth / (float) srcWidth;
			float scaleHeight = (float) dstHeight / (float) srcHeight;
			return Math.min(scaleWidth, scaleHeight);
		}
		return 1;
	}

	// *- NEW METHOD -* \\
	// *******************************************************************************************************************************************
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	/**
	 * ͼƬ�ü�������ü��ɹ���ԭͼ�񽫱��ͷţ����򷵻�ԭͼ��?
	 * 
	 * @param src
	 *            ԭͼ
	 * @param width
	 *            �ü����?
	 * @param height
	 *            �ü��߶�
	 * @return
	 */
	public static Bitmap cutBitmap(Bitmap src, int width, int height) {
		if (src == null) {
			throw new RuntimeException("catBitmap��ԭͼ����Ϊnull��������");
		}

		if (width <= 0 || height <= 0) {
			throw new RuntimeException(
					"catBitmap��ͼƬ�ü�����Ȼ��߸߶Ȳ���С��?0����������");
		}

		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();

		float scale = 1f;

		if ((float) srcWidth / (float) srcHeight > (float) width
				/ (float) height) {
			scale = (float) height / (float) srcHeight;
		} else {
			scale = (float) width / (float) srcWidth;
		}

		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		matrix.postTranslate(-(srcWidth * scale - width) / 2, -(srcHeight
				* scale - height) / 2);

		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, matrix, paint);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		if (bitmap == null) {
			bitmap = src;
		} else if (bitmap != src) {
			src.recycle();
		}

		return bitmap;
	}

	/**
	 * ����һ���µ������ε�Բ��ͼƬ���������ʧ�ܣ�����null��
	 * 
	 * @param bitmap
	 *            ԭλͼ
	 * @param radius
	 *            Բ�ǰ뾶
	 * @return
	 */
	public static Bitmap getRoundedSquareBitmap(Bitmap bitmap, float radius) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		return getRoundedSquareBitmap(bitmap, width < height ? width : height,
				radius);
	}

	/**
	 * ����һ���µ������ε�Բ��ͼƬ���������ʧ�ܣ�����null��
	 * 
	 * @param bitmap
	 *            ԭλͼ
	 * @param radius
	 *            Բ�ǰ뾶
	 * @return
	 */
	public static Bitmap getRoundedSquareBitmap(Bitmap bitmap, int sideLength,
			float radius) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		final boolean useWidth = width < height;
		int sideLengthSrc = useWidth ? width : height;

		final int offsetX = (int) (useWidth ? 0
				: (width - sideLengthSrc) / 2.0f);
		final int offsetY = (int) (useWidth ? (height - sideLengthSrc) / 2.0f
				: 0);

		final Rect workingArea = new Rect(offsetX, offsetY, offsetX
				+ sideLengthSrc, offsetY + sideLengthSrc);

		return getRoundedBitmap(bitmap, workingArea, sideLength, sideLength,
				radius);
	}

	/**
	 * ����һ���µ�Բ��ͼƬ���������ʧ�ܣ�����null��
	 * 
	 * @param bitmap
	 *            ԭλͼ
	 * @param workingArea
	 *            �������򡣿���Ϊnull��Ϊnullʱ����Ϊ����ͼƬ
	 * @param outputWidth
	 *            ������
	 * @param outputHeight
	 *            ����߶�?
	 * @param radius
	 *            Բ�ǰ뾶
	 * @return
	 */
	public static Bitmap getRoundedBitmap(Bitmap bitmap, Rect workingArea,
			int outputWidth, int outputHeight, float radius) {
		if (bitmap == null || outputWidth <= 0 || outputHeight <= 0
				|| radius < 0 || (workingArea != null && workingArea.isEmpty())) {
			return null;
		}
		Bitmap result = null;
		try {
			result = Bitmap.createBitmap(outputWidth, outputHeight,
					Config.ARGB_8888);

			Canvas canvas = new Canvas(result);
			final Paint paint = new Paint();

			final Rect dst = new Rect(0, 0, outputWidth, outputHeight);

			final int color = 0xff808080;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(new RectF(dst), radius, radius, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			canvas.drawBitmap(bitmap, workingArea == null ? new Rect(0, 0,
					bitmap.getWidth(), bitmap.getHeight()) : workingArea, dst,
					paint);

			if (result != bitmap) {
				// bitmap = result;
				return result;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			BitmapUtils.releaseBitmap(result);
		}

		return null;
	}

	/**
	 * ʹ��{@link Config#RGB_565}��ʽ���ļ�·������ͼ��
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int maxWidth, int maxHeight) {
		return decodeSampledBitmapFromFile(filename, maxWidth, maxHeight,
				Config.RGB_565);
	}

	/**
	 * ���ļ�·������ͼ��
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int maxWidth, int maxHeight, Config config) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		BitmapFactory.decodeFile(filename, options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth,
				maxHeight);
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = config;

		try {
			return BitmapFactory.decodeFile(filename, options);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap getJPG(String aFilePath) {
		return getImage(aFilePath, Config.RGB_565);
	}

	public static Bitmap getPNG(String aFilePath) {
		return getImage(aFilePath, Config.ARGB_8888);
	}

	/**
	 * DrawableתBitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	private static Bitmap getImage(String aFilePath, Config config) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = config;
		File lFile = new File(aFilePath);
		if (lFile != null && lFile.exists() && lFile.isFile()) {
			boolean error = false;
			try {
				Bitmap lBitmap = BitmapFactory.decodeFile(aFilePath, options);
				if (lBitmap != null) {
					return lBitmap;
				} else {
					error = true;
				}
			} catch (Exception e) {
				error = true;
			} catch (OutOfMemoryError oom) {

			} finally {
				if (error && lFile != null && lFile.exists()) {
					lFile.delete();
				}
			}
		}
		return null;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int maxWidth, int maxHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (width > maxWidth || height > maxHeight) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) maxHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) maxWidth);
			}

			final float totalPixels = width * height;

			final float maxTotalPixels = maxWidth * maxHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels) {
				inSampleSize++;
			}
		}
		return adjustInSampleSize(inSampleSize);
	}

	public static Bitmap decodeSampledBitmapFromFileByPixels(String filename,
			int baseLen, Config config) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		BitmapFactory.decodeFile(filename, options);
		float scale = options.outHeight * 1f / options.outWidth;
		if (scale < 1) {
			scale = options.outWidth * 1f / options.outHeight;
		}
		options.inSampleSize = calculateInSampleSizeByPixels(options,
				(int) (baseLen * baseLen * scale));
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = config == null ? Config.RGB_565
				: config;

		try {
			return BitmapFactory.decodeFile(filename, options);
		} catch (Throwable e) {
			return null;
		}
	}

	public static int calculateInSampleSizeByPixels(
			BitmapFactory.Options options, int pixels) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (width * height > pixels) {
			final float totalPixels = width * height;

			while (totalPixels / (inSampleSize * inSampleSize) > pixels) {
				inSampleSize++;
			}
		}
		return adjustInSampleSize(inSampleSize);
	}

	public static void releaseBitmap(Bitmap... bitmaps) {
		try {
			for (Bitmap bm : bitmaps) {
				if (bm != null) {
					if (!bm.isRecycled())
						bm.recycle();
					bm = null;
				}
			}
			bitmaps = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean saveImage(EImageType imageType, String savePath,
			String lTempFilePath, int width, int height) {
		FileOutputStream fos = null;
		Bitmap bitmap = null;
		Bitmap saveBitmap = null;
		byte[] buffer = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(lTempFilePath, options);
			options.inSampleSize = BitmapUtils.calculateInSampleSize(options,
					width, height);

			int offSetX = 0, offSetY = 0;
			// if (imageType == EImageType.JPEG || imageType == EImageType.PNG)
			// {
			// ��ʼ����ͼƬ�ߴ�
			float scaleOld, scaleNew;
			scaleOld = (float) options.outWidth / options.outHeight;
			scaleNew = (float) width / height;

			int cutHeight = options.outHeight / options.inSampleSize;
			int cutWidth = options.outWidth / options.inSampleSize;

			if (scaleOld > scaleNew) {
				width = cutHeight * width / height;
				height = cutHeight;
				offSetX = (width - cutWidth) / 2;
			} else {
				height = cutWidth * height / width;
				width = cutWidth;
				offSetY = (cutWidth * height / width - cutHeight) / 2;
			}

			// }

			// ���ö�ȡͼƬ��С��
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inDither = false;
			buffer = new byte[16384];
			options.inTempStorage = buffer;

			bitmap = BitmapFactory.decodeFile(lTempFilePath, options);

			if (bitmap != null) {
				switch (imageType) {
				default:
				case JPEG:
				case PNG:
					if (offSetX != 0 || offSetY != 0) {
						saveBitmap = Bitmap.createBitmap(bitmap,
								Math.abs(offSetX), Math.abs(offSetY), width,
								height);
					} else {
						saveBitmap = bitmap;
					}
					CompressFormat lCompressFormat = imageType == EImageType.JPEG ? CompressFormat.JPEG
							: CompressFormat.PNG;
					fos = new FileOutputStream(savePath);
					boolean result = saveBitmap.compress(lCompressFormat, 75,
							fos);
					fos.flush();
					fos.close();
					return result;
				case ROUNDED_IMAGE:
					// Bitmap tempBitmap = Bitmap.createScaledBitmap(bitmap,
					// width, height, true);
					Bitmap tempBitmap = Bitmap
							.createBitmap(bitmap, Math.abs(offSetX),
									Math.abs(offSetY), width, height);
					float fRound = tempBitmap.getWidth() / 12;
					saveBitmap = Bitmap.createBitmap(tempBitmap.getWidth(),
							tempBitmap.getHeight(), Config.ARGB_8888);
					Canvas canvas = new Canvas(saveBitmap);
					final int color = 0xff424242;
					final Paint paint = new Paint();
					final Rect rect = new Rect(0, 0, tempBitmap.getWidth(),
							tempBitmap.getHeight());
					final RectF rectF = new RectF(rect);
					paint.setAntiAlias(true);
					canvas.drawARGB(0, 0, 0, 0);
					paint.setColor(color);
					canvas.drawRoundRect(rectF, fRound, fRound, paint);
					paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
					canvas.drawBitmap(tempBitmap, rect, rect, paint);
					BitmapUtils.releaseBitmap(tempBitmap);
					fos = new FileOutputStream(savePath);
					boolean result2 = saveBitmap.compress(CompressFormat.PNG,
							75, fos);
					fos.flush();
					fos.close();
					return result2;
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			BitmapUtils.releaseBitmap(saveBitmap);
			if (buffer != null) {
				buffer = null;
			}
		}
		return false;
	}

	public static final boolean saveImage(Bitmap aBitmap, int maxLen,
			String aFilePath) {
		if (aBitmap == null || aBitmap.isRecycled()) {
			return false;
		}
		int width = aBitmap.getWidth();
		int height = aBitmap.getHeight();
		if (aBitmap.getWidth() > maxLen) {
			width = maxLen;
			height = width * aBitmap.getHeight() / aBitmap.getWidth();
		} else if (aBitmap.getHeight() > maxLen) {
			height = maxLen;
			width = height * aBitmap.getWidth() / aBitmap.getHeight();
		}
		Bitmap bm = Bitmap.createScaledBitmap(aBitmap, width, height, false);
		return saveImage(bm, aFilePath);
	}

	public static final boolean saveImage(Bitmap aBitmap, String aFilePath,
			CompressFormat aFormat) {
		boolean result = false;
		if (aBitmap != null && !aBitmap.isRecycled()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(aFilePath);
				result = aBitmap.compress(aFormat, 75, fos);
				fos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}
	
	public static final boolean saveImage(Bitmap aBitmap, String aFilePath) {
		return saveImage(aBitmap, aFilePath, CompressFormat.JPEG);
	}

}
