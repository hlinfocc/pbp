package net.hlinfo.pbp.opt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwdUtils {
	/**
	 * 密码加密，使用spring-security-crypto
	 * @param pwd
	 * @return
	 */
	public static String passwdEncoder(String pwd) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		return bcpe.encode(pwd);
	}
	/**
	 * 验证密码，使用spring-security-crypto
	 * @param pwd
	 * @param hash
	 * @return
	 */
	public static boolean passwdMatches(String pwd,String hash) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		return bcpe.matches(pwd, hash);
	}
}
