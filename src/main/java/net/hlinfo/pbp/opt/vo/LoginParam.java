/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 登录参数
 * @author hlinfo
 */
public class LoginParam {
	@ApiModelProperty("登录帐号(退出的时候只需要传帐号就可以了)")
	private String account;
	
	@ApiModelProperty("密码")
	private String pwd;
	
	@ApiModelProperty("验证码")
	private String verifyCode;
	
	@ApiModelProperty("验证码生成时间戳")
	private String time;
	
	@ApiModelProperty("openId登录使用")
	private String openId;

	/**
	 *  Getter method for property <b>account</b>.
	 * @return property value of account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Setter method for property <b>account</b>.
	 *
	 * @param account value to be assigned to property account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 *  Getter method for property <b>pwd</b>.
	 * @return property value of pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * Setter method for property <b>pwd</b>.
	 *
	 * @param pwd value to be assigned to property pwd
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 *  Getter method for property <b>verifyCode</b>.
	 * @return property value of verifyCode
	 */
	public String getVerifyCode() {
		return verifyCode;
	}

	/**
	 * Setter method for property <b>verifyCode</b>.
	 *
	 * @param verifyCode value to be assigned to property verifyCode
	 */
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	/**
	 *  Getter method for property <b>time</b>.
	 * @return property value of time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Setter method for property <b>time</b>.
	 *
	 * @param time value to be assigned to property time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 *  Getter method for property <b>openId</b>.
	 * @return property value of openId
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * Setter method for property <b>openId</b>.
	 *
	 * @param openId value to be assigned to property openId
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
