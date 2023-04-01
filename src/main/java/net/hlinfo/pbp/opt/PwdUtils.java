package net.hlinfo.pbp.opt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwdUtils {
	/**
	 * 密码加密，使用spring-security-crypto
	 * @param pwd 明文密码
	 * @return 加密后的密码（hash值）
	 */
	public static String passwdEncoder(String pwd) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		return bcpe.encode(pwd);
	}
	/**
	 * 验证密码，使用spring-security-crypto
	 * @param pwd 明文密码
	 * @param hash 密码hash值
	 * @return true验证通过
	 */
	public static boolean passwdMatches(String pwd,String hash) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		return bcpe.matches(pwd, hash);
	}
}
