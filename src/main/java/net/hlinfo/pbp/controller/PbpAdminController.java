package net.hlinfo.pbp.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.HashUtils;
import net.hlinfo.opt.QueryPages;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.opt.pager.MPager;
import net.hlinfo.pbp.entity.AdminInfo;
import net.hlinfo.pbp.etc.EnvConfig;
import net.hlinfo.pbp.opt.PwdUtils;
import net.hlinfo.pbp.opt.RedisKey;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.opt.dto.AdminLoginResultDTO;
import net.hlinfo.pbp.opt.dto.PermDTO;
import net.hlinfo.pbp.opt.vo.LoginParam;
import net.hlinfo.pbp.opt.vo.ResetPwdParam;
import net.hlinfo.pbp.service.PbpPermissionService;
import net.hlinfo.pbp.usr.auth.AuthType;
import net.hlinfo.pbp.usr.auth.AuthType.Admin;

@Api(tags = "管理员模块")
@RestController
@RequestMapping("/system/pbp/admin")
public class PbpAdminController extends BaseController {
	@Autowired
	private Dao dao;
	
	@Autowired
	private RedisUtils redisCache;
	
	@Autowired
	private EnvConfig env;
	private PbpPermissionService permissionService;

	@ApiOperation(value="管理员添加|编辑")
	@PostMapping("/save")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<AdminInfo> save(@Valid @RequestBody AdminInfo data){
		Cnd cnd = Cnd.where("isdelete","=",0).and("account","=",data.getAccount());
		if(Func.isNotBlank(data.getId())) {
			cnd.and("id","!=",data.getId());
		}
		int qty = dao.count(Admin.class,cnd);
		if(qty>0) {
			return new Resp<AdminInfo>().error("账号不能重复");
		}
		data = (AdminInfo)data.insertOrUpdateIgnoreNull(dao);
		if(data != null) {
			return new Resp<AdminInfo>().ok("保存成功", data);
		}else {
			return new Resp<AdminInfo>().error("保存失败");
		}
	}

	@ApiOperation(value = "管理员删除")
	@DeleteMapping("/delete")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp delete(@RequestParam("id") String id
			, HttpServletRequest request) {
		if (Func.isBlank(id)) {
			return new Resp().error("id不能为空");
		}
		AdminInfo obj = dao.fetch(AdminInfo.class, id);
		if (obj == null) {
			return new Resp().error("该数据已经被删除了");
		}
		int num = obj.deletedSoft(dao);
		return Resp.OBJ_O(num);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation("管理员列表")
	@GetMapping("/list")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<QueryPages<AdminInfo>> list(@ApiParam("姓名查找")@RequestParam(name="keywords", defaultValue = "") String keywords
			, @ApiParam("状态：-1全部 0 启用 1禁用")@RequestParam(name="status", defaultValue = "-1") int status
			, @ApiParam("【可选】用户级别：-1全部,其他请自定义")@RequestParam(name="userLevel", defaultValue = "-1") int userLevel
			, @ApiParam("【可选】用户类型：-1全部,其他请自定义")@RequestParam(name="userType", defaultValue = "-1") int userType
			, @ApiParam("页数")@RequestParam(name="page", defaultValue = "1") int page
			, @ApiParam("每页显示条数")@RequestParam(name="limit", defaultValue = "20") int limit
			, HttpServletRequest request){
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		MPager pager = new MPager(page, limit);
		if(Strings.isNotBlank(keywords)) {
			SqlExpressionGroup exp = Cnd.exps("realName", "like", "%"+keywords+"%")
					.or("account","like","%"+keywords+"%")
					.or("phone","like","%"+keywords+"%");
			cnd.and(exp);
		}
		if(status==0 || status==1) {
			cnd.and("status", "=", status);
		}
		if(userLevel>=0) {
			cnd.and("userLevel", "=", userLevel);
		}
		if(userType>=0) {
			cnd.and("userType", "=", userType);
		}
		pager.setRecordCount(dao.count(AdminInfo.class, cnd));
		List<AdminInfo> list = dao.query(AdminInfo.class, cnd.limit(page, limit).desc("createtime"));
//		opLogsService.AdminAddOpLogs("获取管理员列表", "共查询数据："+list.size()+"条", request);
		return new Resp().ok("获取成功", new QueryPages<AdminInfo>(list,pager));
	}
	
	@ApiOperation(value = "账号密码登陆",notes = "密码用sm2加密")
	@PostMapping("/login")
	public Resp<AdminLoginResultDTO> accountLogin(@RequestBody LoginParam dto
			, HttpServletRequest request) {
		if (Strings.isBlank(dto.getPwd()) || Strings.isBlank(dto.getAccount())) {
			return Resp.ERROR("出错了，参数不能为空");
		}
		String verifycodekey = Func.isBlank(dto.getTime())?Lang.getIP(request):(Lang.getIP(request)+":"+dto.getTime());
		String key = RedisKey.VERIFYCODE + verifycodekey;
		String code = redisCache.getObject(key);
		
		if(env.isprod()) {
			if(Func.isBlank(code) || Func.notequals(code.toLowerCase(), dto.getVerifyCode().toLowerCase())) {
				return new Resp().error("验证码不正确，请重新输入");
			}
		}
		//获取SM2密钥
		String publicKeyStr = redisCache.getObject("sm2PublicKey:"+Func.Times.nowDateBasic());
		String privateKeyStr = redisCache.getObject("sm2PrivateKey:"+Func.Times.nowDateBasic());
		if(Func.isBlank(publicKeyStr) || Func.isBlank(privateKeyStr)) {
			return Resp.FAIL("加密密钥过期，请刷新后重试");
		}
		String ip = Func.getIpAddr(request);
		int aeq = redisCache.getCacheInt("accountErrorQty:"+ip);
//		long count=stringRedisTemplate.boundValueOps("accountErrorQtyTimes:"+ip).increment(1);
		
		if(aeq>5 && redisCache.hashKeys("accountErrorQtyTimes:"+ip)) {
			return new Resp().error("尝试超过失败次数限制，请于15分钟后再试");
		}
		
		AdminInfo admin = dao.fetch(AdminInfo.class
			, Cnd.where("account", "=", dto.getAccount()).and("isdelete", "=", 0));
		if (admin == null) {
			aeq +=1;
			if(aeq>5) {
				redisCache.resetCacheData("accountErrorQty:"+ip, aeq,15);
				redisCache.setObject("accountErrorQtyTimes:"+ip, aeq, 15, TimeUnit.MINUTES);
			}else {
				redisCache.setObject("accountErrorQty:"+ip, aeq);
			}
			return new Resp().error("账号或密码错误");
		}
		//SM2解密
		SM2 sm2 = SmUtil.sm2(privateKeyStr, publicKeyStr);
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(dto.getPwd(), KeyType.PrivateKey));
		
		if (!PwdUtils.passwdMatches(HashUtils.sm3(decryptStr), admin.getPassword())) {
			aeq +=1;
			if(aeq>5) {
				redisCache.resetCacheData("accountErrorQty:"+ip, aeq,15);
				redisCache.setObject("accountErrorQtyTimes:"+ip, aeq, 15, TimeUnit.MINUTES);
			}else {
				redisCache.setObject("accountErrorQty:"+ip, aeq);
			}
			return new Resp().error("账号或密码错误");
		}
		if (admin.getStatus() == 1) {
			return new Resp().error("账号已经被禁止登陆");
		}
		admin.updateLoginInfo(request);
		admin.updated();
		int rs = dao.updateIgnoreNull(admin);
		
		if(rs>0) {
			redisCache.deleteObject("accountErrorQty:"+ip);
			redisCache.deleteObject("accountErrorQtyTimes:"+ip);
		}
		int userType;
		if(admin.getUserType()==0) {
			userType = AuthType.Root.TYPE;
			StpUtil.login(AuthType.Root.TYPE + "-" + admin.getId());
		}else {
			userType = AuthType.Admin.TYPE;
			StpUtil.login(AuthType.Admin.TYPE + "-" + admin.getId());
		}
		AdminLoginResultDTO result = new AdminLoginResultDTO();
		result.setAccountInfo(admin);
		
		NutMap permMap = permissionService.getPermSetByUserid(userType, result.getAccountInfo().getId());
		Set<String> permIds = permMap.getAs("permIds", Set.class);
		Map<String, List<String>> permBtnMap = permMap.getAs("permBtnMap", Map.class);
		List<PermDTO> permVo = permissionService.loadLeftMenu(permIds, permBtnMap, "0");
		result.setMenus(permVo);
		result.setTokenInfo(StpUtil.getTokenInfo());
		result.setSuccessUrl(permissionService.getLoginSuccessRoutePath(admin.getId()));
		
//		opLogsService.AdminAddOpLogs("后台管理员登录", "用户id："+admin.getId(),admin.getAccount(), request);
		redisCache.resetCacheData(RedisKey.ADMININFO+admin.getId(), admin);
		return Resp.OK("登陆成功",result);
	}
	
	@ApiOperation("账号退出")
	@PostMapping("/logout")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<NutMap> logout(HttpServletRequest request) {
		String id = this.getLoginId();
		int userType = 0;
		if(StpUtil.hasPermission(AuthType.Root.PERM)) {
			userType = AuthType.Root.TYPE;
		}else {
			userType = AuthType.Admin.TYPE;
		}
//		opLogsService.AdminAddOpLogs("后台管理员退出", "用户id："+member.getId(),member.getAccount(), request);
		StpUtil.logout(userType + "-" + id);
		return Resp.OK("退出成功");
	}
	
	@ApiOperation(value="重置本人密码")
	@PostMapping("/resetPwd")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<AdminInfo> resetPwd(@Valid @RequestBody ResetPwdParam obj, HttpServletRequest request){
		if(Strings.isBlank(obj.getOldPwd())) {
			return new Resp().error("旧密码不能空");
		}
		//获取SM2密钥
		String publicKeyStr = redisCache.getObject("sm2PublicKey:"+Func.Times.nowDateBasic());
		String privateKeyStr = redisCache.getObject("sm2PrivateKey:"+Func.Times.nowDateBasic());
		if(Func.isBlank(publicKeyStr) || Func.isBlank(privateKeyStr)) {
			return Resp.FAIL("加密密钥过期，请刷新后重试");
		}
		//SM2解密
		SM2 sm2 = SmUtil.sm2(privateKeyStr, publicKeyStr);
		String decryptOldPwd = StrUtil.utf8Str(sm2.decryptFromBcd(obj.getOldPwd(), KeyType.PrivateKey));
		String decryptNowPwd = StrUtil.utf8Str(sm2.decryptFromBcd(obj.getNewPwd(), KeyType.PrivateKey));
		
		AdminInfo  userInfo = dao.fetch(AdminInfo.class, obj.getId());
		if(!PwdUtils.passwdMatches(HashUtils.sm3(decryptOldPwd), userInfo.getPassword())) {
			return new Resp().error("旧密码错误，请重新输入");
		}
		userInfo.setPassword(PwdUtils.passwdEncoder(HashUtils.sm3(decryptNowPwd)));
		int n =  dao.updateIgnoreNull(userInfo);
		if(n > 0) {
//			opLogsService.AdminAddOpLogs("重置管理员密码", "用户ID："+obj.getId(), request);
			return new Resp().ok("保存成功");
		}else {
			return new Resp().error("保存失败");
		}
	}
	
	@ApiOperation(value="重置用户的密码")
	@PostMapping("/resetUserPwd")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<AdminInfo> resetUserPwd(@Valid @RequestBody ResetPwdParam obj, HttpServletRequest request){
		//获取SM2密钥
		String publicKeyStr = redisCache.getObject("sm2PublicKey:"+Func.Times.nowDateBasic());
		String privateKeyStr = redisCache.getObject("sm2PrivateKey:"+Func.Times.nowDateBasic());
		if(Func.isBlank(publicKeyStr) || Func.isBlank(privateKeyStr)) {
			return Resp.FAIL("加密密钥过期，请刷新后重试");
		}
		//SM2解密
		SM2 sm2 = SmUtil.sm2(privateKeyStr, publicKeyStr);
		String decryptNowPwd = StrUtil.utf8Str(sm2.decryptFromBcd(obj.getNewPwd(), KeyType.PrivateKey));
		AdminInfo  userInfo = dao.fetch(AdminInfo.class, obj.getId());
		userInfo.setPassword(PwdUtils.passwdEncoder(HashUtils.sm3(decryptNowPwd)));
		int n =  dao.updateIgnoreNull(userInfo);
		if(n > 0) {
//			opLogsService.AdminAddOpLogs("重置管理员密码", "用户ID："+obj.getId(), request);
			return new Resp().ok("保存成功");
		}else {
			return new Resp().error("保存失败");
		}
	}
	
	@ApiOperation(value = "根据ID获取信息")
	@GetMapping("/byid")
	@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
    public Resp<AdminInfo> byid(@RequestParam(name = "id")String id){
		AdminInfo data = dao.fetch(AdminInfo.class, id);
		return Resp.OBJ_Q(data);
    }
}

