package cn.com.jinke.assist.manager;


import java.io.File;

import cn.com.jinke.assist.booter.ProjectApplication;
import cn.com.jinke.assist.utils.StorageUtil;


/**
 * @Author: lufengwen
 * @Date: 2015年6月19日 上午1:25:39
 * @Description: 存储管理
 */
public class StorageManager {
	// 获取应用程序外部存储目录
	public static String getAppDir() {
		File file = ProjectApplication.getContext().getExternalFilesDir(null);
		if (file == null) {
			file = ProjectApplication.getContext().getFilesDir();
		}

		return file.getAbsolutePath();
	}

	// 获取应用程序外部缓存目录
	public static String getTempDir() {
		File file = ProjectApplication.getContext().getExternalCacheDir();
		if (file == null) {
			file = ProjectApplication.getContext().getCacheDir();
		}
		return StorageUtil.ensureDirExist(file.getAbsolutePath());
	}

	//获取用户头像外存缓存目录
	public static String getAvatarDiskCacheDir() {
		return StorageUtil.ensureDirExist(getAppDir() + "/avatar");
	}

	//获取图片外存缓存目录
	public static String getPictureDiskCacheDir() {
		return StorageUtil.ensureDirExist(getAppDir() + "/picture");
	}
	
	// 获取“保存的图片”目录
    public static String getSavedPhotoDir() {
        return StorageUtil.ensureDirExist(StorageUtil.getExternalStoragePath() + "/Jw Photos");
    }

}
