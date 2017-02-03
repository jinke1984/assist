package cn.com.jinke.assist.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhaojian
 * @time 下午4:05:16
 * @date 2014-11-3
 * @class_name MD5Utils.java
 */
public class MD5Utils
{

	/**
	 * 默认的密码字符串组合，用来将字节转换�? 16 进制表示的字�?,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * @param s
	 * @return
	 */
	public static String getMD5(String s)
	{
		return getMD5String(s.getBytes());
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5(File file) throws IOException
	{
		InputStream fis = null;
		try
		{
			MessageDigest l_messagedigest = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0)
			{
				l_messagedigest.update(buffer, 0, numRead);
			}
			return bufferToHex(l_messagedigest.digest());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fis != null)
				fis.close();
		}
		return "";
	}

	private static String getMD5String(byte[] bytes)
	{
		try
		{
			MessageDigest l_messagedigest = MessageDigest.getInstance("MD5");
			l_messagedigest.update(bytes);
			return bufferToHex(l_messagedigest.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	private static String bufferToHex(byte bytes[])
	{
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n)
	{
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++)
		{
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer)
	{
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
