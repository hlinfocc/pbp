/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author hlinfo
 *
 */
public class TowFAVo {
	@ApiModelProperty("totp地址")
	private String totpUrl;
	
	@ApiModelProperty("totp地址对应的图片base64数据")
	private String base64Data;
	
	@ApiModelProperty("totp地址对应的二维码地址")
	private String qrcodeUrl;
	
	@ApiModelProperty("秘钥")
	private String key;
	/**
	 *  Getter method for property <b>totpUrl</b>.
	 * @return property value of totpUrl
	 */
	public String getTotpUrl() {
		return totpUrl;
	}
	/**
	 *  Getter method for property <b>qrcodeUrl</b>.
	 * @return property value of qrcodeUrl
	 */
	public String getQrcodeUrl() {
		return qrcodeUrl;
	}
	/**
	 * Setter method for property <b>qrcodeUrl</b>.
	 *
	 * @param qrcodeUrl value to be assigned to property qrcodeUrl
	 */
	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}
	/**
	 * Setter method for property <b>totpUrl</b>.
	 *
	 * @param totpUrl value to be assigned to property totpUrl
	 */
	public void setTotpUrl(String totpUrl) {
		this.totpUrl = totpUrl;
	}
	/**
	 *  Getter method for property <b>base64Data</b>.
	 * @return property value of base64Data
	 */
	public String getBase64Data() {
		return base64Data;
	}
	/**
	 * Setter method for property <b>base64Data</b>.
	 *
	 * @param base64Data value to be assigned to property base64Data
	 */
	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}
	/**
	 *  Getter method for property <b>key</b>.
	 * @return property value of key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Setter method for property <b>key</b>.
	 *
	 * @param key value to be assigned to property key
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
