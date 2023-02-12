package net.hlinfo.pbp.etc.initd;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.random.R;
import org.nutz.trans.Trans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollectionUtil;
import net.hlinfo.pbp.entity.AdminInfo;
import net.hlinfo.pbp.entity.AdminRole;
import net.hlinfo.pbp.entity.Permission;
import net.hlinfo.pbp.entity.Role;
import net.hlinfo.pbp.entity.RolePermission;
import net.hlinfo.pbp.opt.PwdUtils;
import net.hlinfo.pbp.opt.vo.PermMeta;

@Component
public class PbpInitConfig {
	@Autowired
	private Dao dao;
	
	@PostConstruct
	public void init() {
		int count = dao.count(Permission.class, null);
		if(count <= 0) {
			initPermission();
		}
		
		count = dao.count(Role.class, null);
		if(count <= 0) {
			initRole();
		}
		
		count = dao.count(AdminInfo.class, null);
		if(count <= 0) {
			initAdmin();
		}
	}

	private void initPermission() {
		List<Permission> perms = new ArrayList<>();
		Permission permHome = createPermission("0", "home", "首页", 
				"adminHome", "pages/home/AdminHome", "bank", 0, null);
		perms.add(permHome);
		
		Permission permConfig = createPermission("0", "configManager", "全局配置", 
				"configManager", "", "setting", 1, null);
		perms.add(permConfig);
		perms.add(createPermission(permConfig.getId(), "configTypeManager", "数据字典分类管理", 
				"configTypeManager", "pages/config/type/list", "", 0, CollectionUtil.newArrayList("添加", "编辑", "删除", "查询")));
		perms.add(createPermission(permConfig.getId(), "configManager", "数据字典管理", 
				"configManager", "pages/config/list", "", 1, CollectionUtil.newArrayList("添加", "编辑", "删除", "查询")));
		
		Permission permSystem = createPermission("0", "systemManager", "系统管理", 
				"sysManager", "", "setting", 100, null);
		perms.add(permSystem);
		perms.add(createPermission(permSystem.getId(), "menuManager", "菜单管理", 
				"permList", "pages/sys/Menu", "", 0, CollectionUtil.newArrayList("添加", "编辑", "删除", "查询")));
		perms.add(createPermission(permSystem.getId(), "roleManager", "角色管理", 
				"roleList", "pages/sys/Role", "", 1, CollectionUtil.newArrayList("添加", "编辑", "删除", "授权", "查询")));
		perms.add(createPermission(permSystem.getId(), "adminManager", "管理员管理", 
				"adminManager",  "pages/sys/Admin", "", 2, CollectionUtil.newArrayList("添加", "编辑", "删除", "关联角色", "查询")));
		
		dao.insert(perms);
	}
	
	private void initRole() {
		Role role = new Role();
		role.init();
		role.setCreateId("system");
		role.setCode("root");
		role.setName("超级管理员");
		role.setPow(999);
		role.setLevel(1);
		role.setStatus(0);
		List<RolePermission> rps = new ArrayList<RolePermission>();
		List<Permission> perms = dao.query(Permission.class, null);
		for(Permission perm : perms) {
			RolePermission rp = new RolePermission();
			rp.init();
			rp.setRoleid(role.getId());
			rp.setPermid(perm.getId());
			rp.setHasBtns(perm.getBtns());
			rps.add(rp);
		}
		Trans.exec(()->{
			dao.insert(role);
			dao.insert(rps);
		});
	}
	
	private void initAdmin() {
		AdminInfo adminInfo = new AdminInfo();
		adminInfo.init();
		adminInfo.setAccount("root");
		adminInfo.setPassword(PwdUtils.passwdEncoder("123456"));
		adminInfo.setRealName("超级管理员");
		adminInfo.setStatus(0);
		
		Role role = dao.fetch(Role.class, Cnd.where("code", "=", "root"));
		AdminRole ar = new AdminRole();
		ar.init();
		ar.setAdminId(adminInfo.getId());
		ar.setRoleId(role.getId());
		Trans.exec(()->{
			dao.insert(adminInfo);
			dao.insert(ar);
		});
	}
	
	private Permission createPermission(
			String pid, String code, String name, 
			String router, String component, 
			String icon, int sort, 
			List<String> btns
		) {
		Permission perm = new Permission();
		perm.init();
		perm.setName(name);
		perm.setCode(code);
		perm.setComponentStr(component);
		
		perm.setCreateId("system");
		perm.setHidden(false);
		perm.setLevel(1);
		
		PermMeta meta = new PermMeta();
		meta.setIcon(icon);
		meta.setBreadcrumb(true);
		meta.setNoKeepAlive(true);
		meta.setInvisible(false);
		meta.setIsdisable(false);
		perm.setMeta(meta);
		
		perm.setNeedAuth(true);
		perm.setRouter(router);
		perm.setPid(pid);
		perm.setSort(sort);
		perm.setStatus(0);
		perm.setBtns(btns);
		
		return perm;
	}
}
