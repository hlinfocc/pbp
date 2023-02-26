/**
 * 
 */
package net.hlinfo.pbp.opt.dto;

import cn.dev33.satoken.stp.SaTokenInfo;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.pbp.entity.AccountInfo;

public class LoginResultDTO {
	@ApiModelProperty("用户信息")
	private AccountInfo accountInfo;
	
	@ApiModelProperty("登录信息，token等")
	private SaTokenInfo tokenInfo;
	
	@ApiModelProperty("登录成功页面")
	private String successUrl;
	/**
	 *  Getter method for property <b>successUrl</b>.
	 * @return property value of successUrl
	 */
	public String getSuccessUrl() {
		return successUrl;
	}
	/**
	 * Setter method for property <b>successUrl</b>.
	 *
	 * @param successUrl value to be assigned to property successUrl
	 */
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	/**
	 *  Getter method for property <b>accountInfo</b>.
	 * @return property value of accountInfo
	 */
	public AccountInfo getAccountInfo() {
		return accountInfo;
	}
	/**
	 * Setter method for property <b>accountInfo</b>.
	 *
	 * @param accountInfo value to be assigned to property accountInfo
	 */
	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	/**
	 *  Getter method for property <b>tokenInfo</b>.
	 * @return property value of tokenInfo
	 */
	public SaTokenInfo getTokenInfo() {
		return tokenInfo;
	}
	/**
	 * Setter method for property <b>tokenInfo</b>.
	 *
	 * @param tokenInfo value to be assigned to property tokenInfo
	 */
	public void setTokenInfo(SaTokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}
}
