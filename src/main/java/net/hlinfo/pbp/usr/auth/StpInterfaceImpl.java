package net.hlinfo.pbp.usr.auth;

import java.util.ArrayList;
import java.util.List;

import org.beetl.sql.core.SQLManager;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.ListUtil;
import net.hlinfo.opt.Func;
import net.hlinfo.pbp.entity.Role;

@Component
public class StpInterfaceImpl implements StpInterface {
	@Autowired
	private SQLManager sqlManager;
	
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		//权限
		String id = (String)loginId;
		String[] idArr = id.split("-");
		int type = Func.string2int(idArr[0]);
		String uid = idArr.length==2?idArr[1]:"";
		if(type == AuthType.Admin.TYPE || type == AuthType.Root.TYPE) {
			List<String> perms = handleAdminPerm(uid);
			return perms;
		}else{
			return ListUtil.of(AuthType.getPermName(type));
		}
	}

	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		String id = (String)loginId;
		String[] idArr = id.split("-");
		int type = Func.string2int(idArr[0]);
		String uid = idArr.length==2?idArr[1]:"";
		if(type == AuthType.Admin.TYPE || type == AuthType.Root.TYPE) {
			List<String> roles = handleAdminRole(uid);
			return roles;
		}else{
			return ListUtil.of(AuthType.getRoleName(type));
		}
	}
	
	private List<String> handleAdminPerm(String adminid) {
		List<String> perms = new ArrayList<String>();
		perms.add(AuthType.Admin.PERM);
		NutMap paras = NutMap.NEW();
		paras.addv("adminid", adminid);
		List<NutMap> list = sqlManager.select("pbp.account.queryUserPerm", NutMap.class, paras);
		if(list != null) {
			list.forEach(item -> {
				String code = item.getString("code", "");
				perms.add(code);
				perms.addAll(handleHasBtns(item));
			});
		}
		return perms;
	}
	
	private List<String> handleHasBtns(NutMap item) {
		String code = item.getString("code");
		List<String> perms = new ArrayList<String>();
		String hasBtns = item.getString("hasBtns");
		if(Strings.isNotBlank(hasBtns)) {
			String[] hasBtnsArr = hasBtns.substring(1, hasBtns.length() - 1).split(",");
			for(String btn : hasBtnsArr) {
				if(Strings.isNotBlank(btn)) {
					btn = btn.trim();
					btn = btn.substring(1, btn.length() - 1);
					perms.add(code + ":" + btn);
				}
			}
		}
		return perms;
	}
	
	private List<String> handleAdminRole(String adminid) {
		List<String> roles = new ArrayList<String>();
		roles.add(AuthType.Admin.ROLE);
		NutMap paras = NutMap.NEW();
		paras.addv("adminid", adminid);
		List<Role> list = sqlManager.select("pbp.account.queryUserRole", Role.class, paras);
		if(list != null) {
			list.forEach(item -> {
				if(Strings.isNotBlank(item.getCode())) {
					roles.add(item.getCode());
				}
			});
		}
		return roles;
	}
}
