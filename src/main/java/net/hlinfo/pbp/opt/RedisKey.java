package net.hlinfo.pbp.opt;

public class RedisKey {
	
	/**
	 * 验证码 <br>
	 */
	public static final String VERIFYCODE = "verifyCode:";
    
    /** 验证码 失效时间 秒*/
    public static final int VERIFYCODE_EXPIRE = 60*5;
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
