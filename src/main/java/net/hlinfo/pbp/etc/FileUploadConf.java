package net.hlinfo.pbp.etc;

import java.util.HashMap;
import java.util.Map;

import org.nutz.filepool.FilePool;
import org.nutz.filepool.NutFilePool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="upload",ignoreInvalidFields=true, ignoreUnknownFields=true)
public class FileUploadConf {
	/**
	 * 普通文件保存目录
	 */
	private String savePath;
	/**
	 * 普通文件保存目录访问地址
	 */
	private String baseUrl;
	/**
	 * 安全文件保存目录，该目录不设置访问地址，文件加密存储，解密后方可预览
	 */
	private String securitySavePath;
	/**
	 * 文件池地址
	 */
	private String filePoolPath = "/tmp/hlinfo/filePool";
	
	public String getFilePoolPath() {
		return filePoolPath;
	}
	public void setFilePoolPath(String filePoolPath) {
		this.filePoolPath = filePoolPath;
	}
	public String getSecuritySavePath() {
		return securitySavePath;
	}
	public void setSecuritySavePath(String securitySavePath) {
		this.securitySavePath = securitySavePath;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	@Override
	public String toString() {
		return "FileUploadConf [savePath=" + savePath + ", baseUrl=" + baseUrl + ", getSavePath()=" + getSavePath()
				+ ", getBaseUrl()=" + getBaseUrl() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	/**
	 * 文件类型
	 * @return
	 */
	public Map<String, String> extMap() {
		String fileext = ".pdf,.xls,.xlsx,.doc,.docx,.zip,.gz,.txt,.rar,.bz2,.xz";
		String imgext = ".gif,.jpg,.jpeg,.png,.bmp";
		String img2ext = ".jpg,.jpeg,.png,.bmp";
		String videoext = ".mp4,.avi,.rmvb,.rm,.3gp,.mpeg,.mpg,.mkv,.dat,.asf,.wmv,.flv,.mov,.ogg,.ogm";
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", imgext);
		extMap.put("imageNoGif", img2ext);
		extMap.put("file", fileext);
		extMap.put("videoext", videoext);
		extMap.put("all", fileext+","+imgext+","+videoext);
		extMap.put("word", ".doc,.docx");
		return extMap;
	}
	
	@Bean
	public FilePool filePool() {
		FilePool pool = new NutFilePool(filePoolPath);
		return pool;
	}
}

