package net.hlinfo.pbp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.HmacHashFunction;
import com.warrenstrange.googleauth.KeyRepresentation;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;

import net.hlinfo.pbp.opt.vo.TowFAVo;

@Service
public class Auth2FAService {
		
	private GoogleAuthenticator authenticator;
	/**
	 * 初始化GoogleAuthenticator对象
	 */
	private void init() {
		if(authenticator==null) {
			GoogleAuthenticatorConfigBuilder config = new GoogleAuthenticatorConfigBuilder();
			config.setKeyRepresentation(KeyRepresentation.BASE32);
			config.setHmacHashFunction(HmacHashFunction.HmacSHA1);
			authenticator = new GoogleAuthenticator(config.build());
		}
	}
	
	/**
	 * 获取totp地址
	 * @param areaCode
	 * @return 2FA信息对象
	 */
	public TowFAVo genTotpAuthURL(String issuer, String accountName) {
		this.init();
		TowFAVo rs2fa = new TowFAVo();
		GoogleAuthenticatorKey secretKey = authenticator.createCredentials();
		rs2fa.setKey(secretKey.getKey());
		String url = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, accountName, secretKey);
		rs2fa.setTotpUrl(url);
		QrConfig qrConfig = new QrConfig(300,300);
		String base64 = QrCodeUtil.generateAsBase64(url, qrConfig, ImgUtil.IMAGE_TYPE_JPEG);
		rs2fa.setBase64Data(base64);
		String fmt = "/system/pbp/captcha/qrcode?data=%s";
		rs2fa.setQrcodeUrl(String.format(fmt,internalURLEncode(url)));
		return rs2fa;
	}
	/**
	 * 生成基于时间的一次性密码
	 * @param secretKey 秘钥
	 * @return 基于时间的一次性密码
	 */
	public int getTotpPassword(String secretKey) {
		this.init();
		int oneTimePassword = authenticator.getTotpPassword(secretKey);
		return oneTimePassword;
	}
	/**
	 * 验证一次性密码是否有效
	 * @param secretKey 秘钥
	 * @param oneTimePassword 一次性密码
	 * @return 是否有效
	 */
	public boolean authorize(String secretKey,int oneTimePassword) {
		this.init();
		 boolean isValid = authenticator.authorize(secretKey, oneTimePassword);
		return isValid;
	}
	
	private static String internalURLEncode(String s)
    {
        try
        {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("UTF-8 encoding is not supported by URLEncoder.", e);
        }
    }
	
}
