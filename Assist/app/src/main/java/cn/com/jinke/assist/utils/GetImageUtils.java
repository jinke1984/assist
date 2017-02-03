package cn.com.jinke.assist.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Collection of utility functions used in this package.
 */
public class GetImageUtils
{
//	private final static String TAG = "Utils";

	public static final String ACTION_NEW_PICTURE = "camera.action.NEW_PICTURE";

	public static final String DEFAULT_TEMP_DIR = GetImageUtils.getSDCardDir();

	public static final String DEFAULT_TEMP_FILE_NAME = "image_temp.jpg";

	public static final int ORIENTATION_ROTATE_0 = 0;

	public static final int ORIENTATION_ROTATE_90 = 90;

	public static final int ORIENTATION_ROTATE_180 = 180;

	public static final int ORIENTATION_ROTATE_270 = 270;

	public static final int DEFAULT_OUTPUT_X = 200;

	public static final int DEFAULT_OUTPUT_Y = 200;

	public static final int DEFAULT_OUTPUT_QUALITY = 75;

	public static final int MIN_SIDE = 100;

	private GetImageUtils()
	{
	}

	private static float sPixelDensity = 1;

	private static int sScreenWidth = 240;

	private static int sScreenHeight = 320;

	public static void initialize(Context context)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		sPixelDensity = metrics.density;
		sScreenWidth = metrics.widthPixels;
		sScreenHeight = metrics.heightPixels;
	}

	public static int dpToPixel(int dp)
	{
		return Math.round(sPixelDensity * dp);
	}

	public static int getScreenWidth()
	{
		return sScreenWidth;
	}

	public static int getScreenHeight()
	{
		return sScreenHeight;
	}

	// ========================================================================================
	// TODO ͼ�����?

	public static Bitmap rotate(Bitmap bitmap, int degrees)
	{
		return rotateAndMirror(bitmap, degrees, false);
	}

	public static Bitmap rotateAndMirror(Bitmap aBitmap, int aDegrees, boolean aMirror)
	{
		if ((aDegrees != 0 || aMirror) && aBitmap != null)
		{
			Matrix m = new Matrix();
			// Mirror first.
			// horizontal flip + rotation = -rotation + horizontal flip
			if (aMirror)
			{
				m.postScale(-1, 1);
				aDegrees = (aDegrees + 360) % 360;
				if (aDegrees == 0 || aDegrees == 180)
				{
					m.postTranslate(aBitmap.getWidth(), 0);
				}
				else if (aDegrees == 90 || aDegrees == 270)
				{
					m.postTranslate(aBitmap.getHeight(), 0);
				}
				else
				{
					throw new IllegalArgumentException("Invalid degrees=" + aDegrees);
				}
			}
			if (aDegrees != 0)
			{
				// clockwise
				m.postRotate(aDegrees, (float) aBitmap.getWidth() / 2, (float) aBitmap.getHeight() / 2);
			}

			try
			{
				Bitmap b2 = Bitmap.createBitmap(aBitmap, 0, 0, aBitmap.getWidth(), aBitmap.getHeight(), m, true);
				if (aBitmap != b2)
				{
					aBitmap.recycle();
					aBitmap = b2;
				}
			}
			catch (OutOfMemoryError ex)
			{
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return aBitmap;
	}

	public static void setPictureExifDegree(String aPath, int aDegree) throws Exception
	{
		ExifInterface exifInterface = new ExifInterface(aPath);
		switch (aDegree)
		{
			case ORIENTATION_ROTATE_0:
				exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
				break;

			case ORIENTATION_ROTATE_90:
				exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
				break;

			case ORIENTATION_ROTATE_180:
				exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
				break;

			case ORIENTATION_ROTATE_270:
				exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_270));
				break;

			default:
				return;
		}
		exifInterface.saveAttributes();
	}


	public static int getPictureExifDegree(String aPath)
	{
		ExifInterface exifInterface = null;
		try
		{
			exifInterface = new ExifInterface(aPath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return ORIENTATION_ROTATE_0;
		}
		int degree = ORIENTATION_ROTATE_0;
		int rotate = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		switch (rotate)
		{
			case ExifInterface.ORIENTATION_NORMAL:
				degree = ORIENTATION_ROTATE_0;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = ORIENTATION_ROTATE_90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = ORIENTATION_ROTATE_180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = ORIENTATION_ROTATE_270;
				break;
		}
		return degree;
	}

	public static void rotateCanvas(Canvas aCanvas, int aWidth, int aHeight, int rotation)
	{
		aCanvas.translate(aWidth / 2, aHeight / 2);
		aCanvas.rotate(rotation);
		if (((rotation / 90) & 0x01) == 0)
		{
			aCanvas.translate(-aWidth / 2, -aHeight / 2);
		}
		else
		{
			aCanvas.translate(-aHeight / 2, -aWidth / 2);
		}
	}


	// public static boolean saveImage(Bitmap aBitmap, String aStrFileName, int aQuality) {
	// return CompressImage.save(aBitmap, aStrFileName, aQuality);
	// }

	// public static final Bitmap getCompressBitmap(String aFilePath, int aMaxWidth, int maxHeight) {
	// return CompressImage.getCompressBitmap(aFilePath, aMaxWidth, maxHeight);
	// }

	// ========================================================================================
	// TODO ��ѧ����

	public static int checkXBoundary(Rect aBoundary, int aX)
	{
		if (aX >= aBoundary.left && aX <= aBoundary.right)
		{
			return 0;
		}
		if (aX <= aBoundary.left)
		{
			return -1;
		}
		return 1;
	}

	public static int checkYBoundary(Rect aBoundary, int aY)
	{
		if (aY >= aBoundary.top && aY <= aBoundary.bottom)
		{
			return 0;
		}
		if (aY <= aBoundary.top)
		{
			return -1;
		}
		return 1;
	}

	public static int checkXBoundary(RectF aRectA, RectF aRectB)
	{
		if (aRectA.left >= aRectA.right || aRectA.top >= aRectA.bottom || aRectB.left >= aRectB.right || aRectB.top >= aRectB.bottom)
		{
			return -1;
		}

		return checkBoundary(aRectA.left, aRectB.left, aRectA.right, aRectB.right);
	}

	public static int checkYBoundary(RectF aRectA, RectF aRectB)
	{
		if (aRectA.left >= aRectA.right || aRectA.top >= aRectA.bottom || aRectB.left >= aRectB.right || aRectB.top >= aRectB.bottom)
		{
			return -1;
		}

		return checkBoundary(aRectA.top, aRectB.top, aRectA.bottom, aRectB.bottom);
	}

	private static int checkBoundary(float aLowerBoundaryA, float aLowerBoundaryB, float aUpperBoundaryA, float aUpperBoundaryB)
	{
		if (aLowerBoundaryA <= aLowerBoundaryB && aUpperBoundaryA >= aUpperBoundaryB)
		{
			return 0;
		}
		if (aLowerBoundaryA > aLowerBoundaryB && aUpperBoundaryA < aUpperBoundaryB)
		{
			return 1;
		}
		if (aUpperBoundaryA < aUpperBoundaryB)
		{
			return 2;
		}
		if (aLowerBoundaryA > aLowerBoundaryB)
		{
			return 3;
		}

		return 4;
	}

	public static boolean checkXSize(RectF aRectA, RectF aRectB)
	{
		return aRectA.width() >= aRectB.width();
	}

	public static boolean checkYSize(RectF rectA, RectF rectB)
	{
		return rectA.height() >= rectB.height();
	}

	public static float distance(MotionEvent aEvent)
	{
		return PointF.length(aEvent.getX(1) - aEvent.getX(0), aEvent.getY(1) - aEvent.getY(0));
	}

	public static PointF midpoint(MotionEvent aEvent)
	{
		return new PointF((aEvent.getX(1) + aEvent.getX(0)) / 2f, (aEvent.getY(1) + aEvent.getY(0)) / 2f);
	}

	public static float rotationAngle(MotionEvent event)
	{
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		float lAngle = (float) Math.toDegrees(radians);
		if (lAngle < 0)
		{
			lAngle = 360 + lAngle;
		}
		return lAngle;
	}

	public static void rotateRectangle(Rect aRect, int aWidth, int aHeight, int aRotation)
	{
		if (aRotation == 0 || aRotation == 360)
			return;

		int lWidth = aRect.width();
		int lHeight = aRect.height();
		switch (aRotation)
		{
			case 90:
			{
				int left = aRect.left;
				aRect.left = aRect.top;
				aRect.top = aHeight - (left + lWidth);
				aRect.right = aRect.left + lHeight;
				aRect.bottom = aRect.top + lWidth;
				return;
			}
			case 180:
			{
				aRect.left = aWidth - (aRect.left + lWidth);
				aRect.top = aHeight - (aRect.top + lHeight);
				aRect.right = aRect.left + lWidth;
				aRect.bottom = aRect.top + lHeight;
				return;
			}
			case 270:
			{
				int top = aRect.top;
				aRect.top = aRect.left;
				aRect.left = aWidth - (top + lHeight);
				aRect.right = aRect.left + lHeight;
				aRect.bottom = aRect.top + lWidth;
				return;
			}
			default:
				throw new AssertionError("rotate error.");
		}
	}

	public static void rotateRectangle(Rect aRect, int aRotation)
	{
		if (aRotation % 90 != 0)
		{
			return;
		}

		int w = aRect.width();
		int h = aRect.height();
		if (((aRotation / 90) & 0x01) == 1)
		{
			aRect.right = aRect.left + h;
			aRect.bottom = aRect.top + w;
		}
	}

	public static float getScaleX(Matrix aMatrix)
	{
		float[] values = new float[9];
		aMatrix.getValues(values);

		float scaleX = values[Matrix.MSCALE_X];
		float skewX = values[Matrix.MSKEW_X];

		if (scaleX * skewX != 0)
		{
			throw new AssertionError("error scale.");
		}

		return scaleX == 0 ? Math.abs(skewX) : Math.abs(scaleX);
	}

	public static float getScaleY(Matrix aMatrix)
	{
		float[] values = new float[9];
		aMatrix.getValues(values);

		float scaleY = values[Matrix.MSCALE_Y];
		float skewY = values[Matrix.MSKEW_Y];

		if (scaleY * skewY != 0)
		{
			throw new AssertionError("error scale.");
		}

		return scaleY == 0 ? Math.abs(skewY) : Math.abs(scaleY);
	}

	public static void setMatrixScale(Matrix aMatrix, float aScaleX, float aScaleY, float aPointX, float aPointY)
	{
		aMatrix.postScale(aScaleX / getScaleX(aMatrix), aScaleY / getScaleY(aMatrix), aPointX, aPointY);
	}

	public static int getAngleFromMatrix(Matrix aMatrix)
	{
		float[] values = new float[9];
		aMatrix.getValues(values);

		float scaleX = values[Matrix.MSCALE_X];
		float skewX = values[Matrix.MSKEW_X];
		float scaleY = values[Matrix.MSCALE_Y];
		float skewY = values[Matrix.MSKEW_Y];

		// Log.d(TAG, "| " + scaleX + " " + skewX + " |");
		// Log.d(TAG, "| " + skewY + " " + scaleY + " |");

		if (scaleX == 0)
		{
			if (skewX > 0)
			{
				return ORIENTATION_ROTATE_270;
			}
			else if (skewX < 0)
			{
				return ORIENTATION_ROTATE_90;
			}
		}
		else if (skewY == 0)
		{
			if (scaleY > 0)
			{
				return ORIENTATION_ROTATE_0;
			}
			else if (scaleY < 0)
			{
				return ORIENTATION_ROTATE_180;
			}
		}

		throw new AssertionError("error angle.");
	}

	public static float sin(int angle)
	{
		return (float) Math.sin(Math.toRadians(angle));
	}

	// ========================================================================================
	// TODO �ļ�����

	public static boolean sdCardAvailable()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static String getSDCardDir()
	{
		return Environment.getExternalStorageDirectory().getPath();
	}

	// ========================================================================================
	// TODO ��������


	public static int getDeviceOrientation(float aSensorX, float aSensorY)
	{
		if (aSensorX >= Math.abs(aSensorY))
			return ORIENTATION_ROTATE_0;
		else if (aSensorY >= Math.abs(aSensorX))
			return ORIENTATION_ROTATE_90;
		else if (Math.abs(aSensorX) >= Math.abs(aSensorY))
			return ORIENTATION_ROTATE_180;
		else if (Math.abs(aSensorY) >= Math.abs(aSensorX))
			return ORIENTATION_ROTATE_270;
		else
			return ORIENTATION_ROTATE_0;
	}

	// public static boolean isShowCutShadow() {
	// return AppShareData.getInstance().getBoolean(BaContants.KEY_ASSIST, false);
	// }
	//
	// public static void setShowCutShadow(boolean aShowCutShadow) {
	// AppShareData.getInstance().setData(BaContants.KEY_ASSIST, aShowCutShadow);
	// }

	public static void broadcastNewPicture(Context context, Uri uri)
	{
		context.sendBroadcast(new Intent(ACTION_NEW_PICTURE, uri));
	}
}
