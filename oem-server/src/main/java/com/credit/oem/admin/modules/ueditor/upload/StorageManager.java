package com.credit.oem.admin.modules.ueditor.upload;


import com.credit.oem.admin.modules.ueditor.define.AppInfo;
import com.credit.oem.admin.modules.ueditor.define.BaseState;
import com.credit.oem.admin.modules.ueditor.define.State;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Component
@ConfigurationProperties(prefix="nginx")
public class StorageManager {
	static Logger logger = LoggerFactory.getLogger(StorageManager.class);
	public static final int BUFFER_SIZE = 8192;
	
	private static String fileurl;

	public static String getFileurl() {
		return fileurl;
	}

	public static void setFileurl(String fileurl) {
		StorageManager.fileurl = fileurl;
	}

	public static int getBufferSize() {
		return BUFFER_SIZE;
	}

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			logger.error("",ioe);
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo( "size", data.length );
		state.putInfo( "title", file.getName() );
		return state;
	}

	public static State saveFileByInputStream(HttpServletRequest request, InputStream is, String path, String picName,
                                              long maxSize) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile(tmpFile, path+"/"+picName);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;

		} catch (IOException e) {
			logger.error("",e);
		}
		return new BaseState(false, AppInfo.IO_ERROR);

//		State state = null;
//		File tmpFile = getTmpFile();
//		byte[] dataBuf = new byte[ 2048 ];
//
//		try {
//			//???????????????
//			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//	        int rc = 0;
//	        while ((rc = is.read(dataBuf, 0, 100)) > 0) {
//	            swapStream.write(dataBuf, 0, rc);
//	        }
//
//	        dataBuf = swapStream.toByteArray();
//	        swapStream.flush();
//	        swapStream.close();
//
//			if (tmpFile.length() > maxSize) {
//				tmpFile.delete();
//				return new BaseState(false, AppInfo.MAX_SIZE);
//			}
//			//??????DFS???????????????????????????
//			//:TODO
//			/**
//			 * ????????????????????????????????????????????????????????????
//			 */
////			UploadResult result = baseFileService.upload(dataBuf, picName, "OM", null);
//
//			boolean success = true;
//			//??????????????????
//			if (success) {
//				state = new BaseState(true);
//				state.putInfo( "size", tmpFile.length() );
//				state.putInfo( "title", "");//?????????????????????
//				state.putInfo( "group", "");//??????group????????????
//				state.putInfo( "url", "");//???????????????url????????????
//				tmpFile.delete();
//			}else{
//				state = new BaseState(false, 4);
//				tmpFile.delete();
//			}
//
//			return state;
//
//		} catch (IOException e) {
//		}
//		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path, String picName) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			//state = saveTmpFile(tmpFile, path);
			//???????????????????????????????????????
//			InputStream in = new FileInputStream(tmpFile);
//			UploadUtils uploadUtils = new UploadUtils();
//			boolean success = uploadUtils.uploadFile(in, path, picName);
			boolean success = true;
			
			//??????????????????
			if (success) {
				state = new BaseState(true);
				state.putInfo( "size", tmpFile.length() );
				state.putInfo( "title", tmpFile.getName() );
				tmpFile.delete();
			}else{
				state = new BaseState(false, 4);
				tmpFile.delete();
			}

			return state;
		} catch (IOException e) {
			logger.error("",e);
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);

		if (targetFile.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}
		try {
			FileUtils.moveFile(tmpFile, targetFile);
		} catch (IOException e) {
			logger.error("",e);
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true);
		state.putInfo( "size", targetFile.length() );
		state.putInfo( "title", targetFile.getName() );
		
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}
}
