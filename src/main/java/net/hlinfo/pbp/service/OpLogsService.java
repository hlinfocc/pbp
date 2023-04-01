/**
 * 
 */
package net.hlinfo.pbp.service;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cn.dev33.satoken.stp.StpUtil;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.entity.AdminInfo;
import net.hlinfo.pbp.entity.Oplogs;
import net.hlinfo.pbp.opt.PbpRedisKey;

/**
 * 操作日志处理
 */
@Service
public class OpLogsService {
	public static final Logger log = LoggerFactory.getLogger(OpLogsService.class);
	@Autowired
	private Dao dao;
	
	@Autowired
	private RedisUtils redisCache;
	
	/**
	 * 操作日志
	 * @param account 账号
	 * @param mokuai 模块
	 * @param pager 页面
	 * @param gongneng 功能
	 * @param logcontent 内容
	 * @param remark 备注
	 * @param request HttpServletRequest对象
	 */
	public void addOpLogs(String account,String mokuai,String pager,String gongneng,String logcontent,String remark,HttpServletRequest request) {
		try {
			Oplogs logs = new Oplogs();
			logs.init();
			logs.setAccount(account);
			logs.setModule(mokuai);
			logs.setPager(pager);
			logs.setOpfunc(gongneng);
			logs.setContent(logcontent);
			logs.setRemark(remark);
			logs.setIp(Func.getIpAddr(request));
			dao.insert(logs);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	/**
	 * admin账号操作日志
	 * @param gongneng
	 * @param logcontent
	 * @param request HttpServletRequest对象
	 */
	public void AdminAddOpLogs(String gongneng,String logcontent,HttpServletRequest request) {
		AdminInfo userInfo = new AdminInfo();
		
		try {
			String loginId =  StpUtil.getLoginId()==null?"":StpUtil.getLoginId().toString().split("-")[1];
			userInfo = redisCache.getObject(PbpRedisKey.ADMININFO+loginId);
			if(userInfo==null) {
				userInfo = dao.fetch(AdminInfo.class,loginId);
				if(userInfo!=null) {
					redisCache.setObject(PbpRedisKey.ADMININFO+loginId, userInfo);
				}
			}
			Oplogs logs = new Oplogs();
			logs.init();
			logs.setAccount((userInfo==null||Func.isBlank(userInfo.getAccount()))?"":userInfo.getAccount());
			logs.setModule("后台管理");
			logs.setPager("");
			logs.setOpfunc(gongneng);
			logs.setContent(logcontent);
			logs.setRemark("");
			logs.setIp(Func.getIpAddr(request));
			dao.insert(logs);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
	}
	/**
	 * 添加操作日志
	 * @param gongneng 功能
	 * @param logcontent 内容
	 * @param pager 页面
	 * @param request HttpServletRequest对象
	 */
	public void AdminAddOpLogs(String gongneng,String logcontent,String pager,HttpServletRequest request) {
		AdminInfo userInfo = new AdminInfo();
		
		try {
			String loginId =  StpUtil.getLoginId()==null?"":StpUtil.getLoginId().toString().split("-")[1];
			userInfo = redisCache.getObject(PbpRedisKey.ADMININFO+loginId);
			if(userInfo==null) {
				userInfo = dao.fetch(AdminInfo.class,loginId);
				if(userInfo!=null) {
					redisCache.setObject(PbpRedisKey.ADMININFO+loginId, userInfo);
				}
			}
			Oplogs logs = new Oplogs();
			logs.init();
			logs.setAccount((userInfo==null||Func.isBlank(userInfo.getAccount()))?"":userInfo.getAccount());
			logs.setModule("后台管理");
			logs.setPager("");
			logs.setOpfunc(gongneng);
			logs.setContent(logcontent);
			logs.setRemark("");
			logs.setIp(Func.getIpAddr(request));
			dao.insert(logs);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
	}
	/**
	 * 添加操作日志
	 * @param gongneng 功能
	 * @param logcontent 内容
	 * @param pager 页面
	 * @param remark 备注
	 * @param request HttpServletRequest对象
	 */
	public void AdminAddOpLogs(String gongneng,String logcontent,String pager,String remark,HttpServletRequest request) {
		AdminInfo userInfo = new AdminInfo();
		
		try {
			String loginId =  StpUtil.getLoginId()==null?"":StpUtil.getLoginId().toString().split("-")[1];
			userInfo = redisCache.getObject(PbpRedisKey.ADMININFO+loginId);
			if(userInfo==null) {
				userInfo = dao.fetch(AdminInfo.class,loginId);
				if(userInfo!=null) {
					redisCache.setObject(PbpRedisKey.ADMININFO+loginId, userInfo);
				}
			}
			Oplogs logs = new Oplogs();
			logs.init();
			logs.setAccount((userInfo==null||Func.isBlank(userInfo.getAccount()))?"":userInfo.getAccount());
			logs.setModule("后台管理");
			logs.setPager("");
			logs.setOpfunc(gongneng);
			logs.setContent(logcontent);
			logs.setRemark(remark);
			logs.setIp(Func.getIpAddr(request));
			dao.insert(logs);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	/**
	 * 添加操作日志
	 * @param gongneng 功能
	 * @param logcontent 内容
	 * @param account 账号
	 * @param pager 页面
	 * @param remark 备注
	 * @param request HttpServletRequest对象
	 */
	public void AdminAddOpLogs(String gongneng,String logcontent,String account,String pager,String remark,HttpServletRequest request) {
		try {
			Oplogs logs = new Oplogs();
			logs.init();
			logs.setAccount(account);
			logs.setModule("后台管理");
			logs.setPager(pager);
			logs.setOpfunc(gongneng);
			logs.setContent(logcontent);
			logs.setRemark(remark);
			logs.setIp(Func.getIpAddr(request));
			dao.insert(logs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
		}
	}
	

}
