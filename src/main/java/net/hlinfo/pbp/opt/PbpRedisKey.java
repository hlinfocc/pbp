package net.hlinfo.pbp.opt;

public class PbpRedisKey {
	
	/**
	 * 验证码 <br>
	 */
	public static final String VERIFYCODE = "verifyCode:";
	/**
	 * 验证码密钥，加入密钥作为混淆
	 */
	public static String VERIFYCODE_KEY = "9eac95ca-cca7-11ed-b316-aba22fcee160";
    
    /** 验证码 失效时间 秒*/
    public static final int VERIFYCODE_EXPIRE = 2*60;
    /**
     * 下载
     */
    public static final String DOWNLOAD = "download:";
    /**
     * 导出所有
     */
    public static final String EXPORTALL = "exportAll:";
    /**
     * 管理员信息
     */
    public static final String ADMININFO = "adminInfo:";
    /**
     * member信息
     */
    public static final String MEMBERINFO = "memberInfo:";
    public static final String SYSDICT = "SYS_DICT:";
    
}
