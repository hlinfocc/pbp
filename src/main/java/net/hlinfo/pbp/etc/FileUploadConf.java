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
	/**
	 * 是否启用相对路径,true情况下返回的文件地址不包含主机部分
	 */
	private boolean relative = false;
	
	/**
	 *  是否启用相对路径,true情况下返回的文件地址不包含主机部分
	 * @return property value of relative
	 */
	public boolean isRelative() {
		return relative;
	}

	/**
	 * 是否启用相对路径,true情况下返回的文件地址不包含主机部分
	 *
	 * @param relative value to be assigned to property relative
	 */
	public void setRelative(boolean relative) {
		this.relative = relative;
	}

	/**
	 *  Getter method for property <b>savePath</b>.
	 * @return property value of savePath
	 */
	public String getSavePath() {
		return savePath;
	}

	/**
	 * Setter method for property <b>savePath</b>.
	 *
	 * @param savePath value to be assigned to property savePath
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 *  Getter method for property <b>baseUrl</b>.
	 * @return property value of baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Setter method for property <b>baseUrl</b>.
	 *
	 * @param baseUrl value to be assigned to property baseUrl
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 *  Getter method for property <b>securitySavePath</b>.
	 * @return property value of securitySavePath
	 */
	public String getSecuritySavePath() {
		return securitySavePath;
	}

	/**
	 * Setter method for property <b>securitySavePath</b>.
	 *
	 * @param securitySavePath value to be assigned to property securitySavePath
	 */
	public void setSecuritySavePath(String securitySavePath) {
		this.securitySavePath = securitySavePath;
	}

	/**
	 *  Getter method for property <b>filePoolPath</b>.
	 * @return property value of filePoolPath
	 */
	public String getFilePoolPath() {
		return filePoolPath;
	}

	/**
	 * Setter method for property <b>filePoolPath</b>.
	 *
	 * @param filePoolPath value to be assigned to property filePoolPath
	 */
	public void setFilePoolPath(String filePoolPath) {
		this.filePoolPath = filePoolPath;
	}

	@Override
	public String toString() {
		return "FileUploadConf [savePath=" + savePath + ", baseUrl=" + baseUrl + ", getSavePath()=" + getSavePath()
				+ ", getBaseUrl()=" + getBaseUrl() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	/**
	 * 允许的文件后缀类型
	 * @return 允许的文件后缀Map
	 */
	public Map<String, String> suffixMap() {
		String fileSuffix = ".pdf,.xls,.xlsx,.doc,.docx,.ppt,.pptx,.zip,.gz,.txt,.rar,.bz2,.xz";
		String imgSuffix = ".gif,.jpg,.jpeg,.png,.bmp";
		String img2Suffix = ".jpg,.jpeg,.png,.bmp";
		String videoSuffix = ".mp4,.avi,.rmvb,.rm,.3gp,.mpeg,.mpg,.mkv,.dat,.asf,.wmv,.flv,.mov,.ogg,.ogm";
		String audioSuffix = ".mp3,.aac,.amr";
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", imgSuffix);
		extMap.put("imageNoGif", img2Suffix);
		extMap.put("file", fileSuffix);
		extMap.put("video", videoSuffix);
		extMap.put("audio", audioSuffix);
		extMap.put("word", ".doc,.docx");
		extMap.put("excel", ".xls,.xlsx");
		extMap.put("ppt", ".ppt,.pptx");
		extMap.put("all", fileSuffix + "," + imgSuffix + "," + videoSuffix + "," + audioSuffix);
		return extMap;
	}
	
	@Bean
	public FilePool filePool() {
		FilePool pool = new NutFilePool(filePoolPath);
		return pool;
	}
}

