package cn.com.jinke.assist.utils;

/**
 * 
 * @author jinke
 */
public enum EImageType
{
	/** .jpg */
	JPEG(0),
	/** .png */
	PNG(1),
	/** .png */
	ROUNDED_IMAGE(2),
	/** Head*/
	HEAD(3);

	private final int mType;

	private EImageType(int type)
	{
		mType = type;
	}

	public int toValue()
	{
		return mType;
	}
}
