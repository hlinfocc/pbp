package net.hlinfo.pbp.etc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="wechat.login",ignoreInvalidFields=true, ignoreUnknownFields=true)
public class WXLoginConfig {
	/**
	 * 微信公众号APPID
	 */
	public String weiXinAppId;
	/**
	 * 微信公众号APP密钥
	 */
	public String weiXinAppSecret;
	
	/**
	 * 回调地址前缀,结尾不带/，如：https：//jyzdapi.dev.htedu.cc
	 */
	public String redirectUri;
	
	/**
	 * 微信小程序APPID
	 */
	public String miniAppId;
	
	/**
	 * 微信小程序APP密钥
	 */
	public String miniAppSecret;
	
	/**
	 * QQ开放平台APPId
	 */
	public String qqAppId;
	/**
	 * 是否启用模板消息
	 */
	public boolean enableTmplmsg = false;
	/**
	 * 推送消息模板ID
	 */
	public String pushTmplmsgId;
	/**
	 * 留言回复模板ID
	 */
	public String guestBookTmplmsgId;
	/**
	 * H5端地址，包含工程名，末尾不带/
	 */
	public String h5url;
	
	public String getGuestBookTmplmsgId() {
		return guestBookTmplmsgId;
	}

	public void setGuestBookTmplmsgId(String guestBookTmplmsgId) {
		this.guestBookTmplmsgId = guestBookTmplmsgId;
	}

	public String getPushTmplmsgId() {
		return pushTmplmsgId;
	}

	public void setPushTmplmsgId(String pushTmplmsgId) {
		this.pushTmplmsgId = pushTmplmsgId;
	}

	public boolean isEnableTmplmsg() {
		return enableTmplmsg;
	}

	public void setEnableTmplmsg(boolean enableTmplmsg) {
		this.enableTmplmsg = enableTmplmsg;
	}

	public String getH5url() {
		return h5url;
	}

	public void setH5url(String h5url) {
		this.h5url = h5url;
	}

	public String getQqAppId() {
		return qqAppId;
	}

	public void setQqAppId(String qqAppId) {
		this.qqAppId = qqAppId;
	}

	public String getWeiXinAppId() {
		return weiXinAppId;
	}

	public void setWeiXinAppId(String weiXinAppId) {
		this.weiXinAppId = weiXinAppId;
	}

	public String getWeiXinAppSecret() {
		return weiXinAppSecret;
	}

	public void setWeiXinAppSecret(String weiXinAppSecret) {
		this.weiXinAppSecret = weiXinAppSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getMiniAppId() {
		return miniAppId;
	}

	public void setMiniAppId(String miniAppId) {
		this.miniAppId = miniAppId;
	}

	public String getMiniAppSecret() {
		return miniAppSecret;
	}

	public void setMiniAppSecret(String miniAppSecret) {
		this.miniAppSecret = miniAppSecret;
	}
	
}
