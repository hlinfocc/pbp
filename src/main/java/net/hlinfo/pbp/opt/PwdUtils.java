package net.hlinfo.pbp.opt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwdUtils {
	/**
	 * 密码加密，使用spring security
	 * @param pwd
	 * @return
	 */
	public static String passwdEncoder(String pwd) {
		BCryptPasswordEncoder en = new BCryptPasswordEncoder();
		return en.encode(pwd);
	}
	/**
	 * 验证密码，使用spring security
	 * @param pwd
	 * @param hash
	 * @return
	 */
	public static boolean passwdMatches(String pwd,String hash) {
		BCryptPasswordEncoder en = new BCryptPasswordEncoder();
		return en.matches(pwd, hash);
	}
}
