/**
 * 
 */
package net.hlinfo.pbp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.beetl.sql.core.SQLManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.pbp.entity.Permission;
import net.hlinfo.pbp.opt.dto.PermDTO;


@Service
public class PbpPermissionService {
	@Autowired
	private Dao dao;
	
	@Autowired
	private SQLManager beetlSqlManager;
	
	/**
	 * 加载权限数据
	 * @param pid 父级ID
	 * @param permBtnMap 权限菜单的按钮
	 * @return 权限数据
	 */
	public List<PermDTO> loadPerms(String pid,
								  Map<String, List<String>> permBtnMap){
		Cnd cnd = Cnd.where("isdelete", "=", 0)
			.and("pid",  "=", pid);
		List<Permission> list = dao.query(Permission.class, cnd.asc("sort"));
		if(list == null) {
			return null;
		}
		List<PermDTO> perms = new ArrayList<PermDTO>();
		for(Permission item:list) {
			PermDTO vo = Jackson.toJavaObject(Jackson.toJSONString(item), PermDTO.class);
			if(permBtnMap != null) {
				vo.setHasBtns(permBtnMap.get(vo.getId()));
			}
			vo.setChildren(loadPerms(vo.getId(), permBtnMap));
			perms.add(vo);
		}
		return perms;
	}
	/**
	 * 加载权限子集
	 * @param cnd 条件
	 * @param permBtnMap 权限菜单的按钮
	 * @return 带子集的权限信息
	 */
	public List<PermDTO> loadPerms(Cnd cnd,
								  Map<String, List<String>> permBtnMap){
		List<Permission> list = dao.query(Permission.class, cnd.asc("sort"));
		if(list == null) {
			return null;
		}
		List<PermDTO> perms = new ArrayList<PermDTO>();
		for(Permission item:list) {
			PermDTO vo = Jackson.toJavaObject(Jackson.toJSONString(item), PermDTO.class);
			if(permBtnMap != null) {
				vo.setHasBtns(permBtnMap.get(vo.getId()));
			}
			vo.setChildren(loadPerms(vo.getId(), permBtnMap));
			perms.add(vo);
		}
		return perms;
	}
	/**
	 * 根据角色id获取权限授权的权限
	 * @param roleid
	 * @return { 
	 * 		permIds: 权限菜单的IDs, 
	 * 		permBtnMap: 权限菜单的按钮
	 * }
	 */
	public NutMap getPermSetByRoleid(String roleid){
		System.out.println("roleid=="+roleid);
		NutMap para = NutMap.NEW().addv("roleid", roleid);
		List<NutMap> permList = beetlSqlManager.select("pbp.account.queryRolePermid", NutMap.class, para);
		Set<String> permIds = new HashSet<String>();
		Map<String, List<String>> permBtnMap = new HashMap<>();
		permList.forEach(perm -> {
			String permid = perm.getString("permid");
			permIds.add(permid);
			
			if(Func.isNotBlank(perm.getString("hasBtns"))) {
				List<String> hasBtns = Jackson.toList(perm.getString("hasBtns"), String.class);
				if(permBtnMap.containsKey(permid)) {
					List<String> existBtns = permBtnMap.get(permid);
					if(existBtns != null && existBtns.size() > 0) {
						hasBtns.addAll(existBtns);
						hasBtns = CollectionUtil.newArrayList(CollectionUtil.newHashSet(hasBtns));
					}
				}
				permBtnMap.put(permid, hasBtns);
			}
		});
		return NutMap.NEW()
			.addv("permIds", permIds)
			.addv("permBtnMap", permBtnMap);
	}
	
	/**
	 * 获取用户可用的权限id和btns
	 * @param userid
	 * @return { 
	 * 		permIds: 权限菜单的IDs, 
	 * 		permBtnMap: 权限菜单的按钮
	 * }
	 */
	public NutMap getPermSetByUserid(int type, String userid){
		NutMap para = NutMap.NEW()
				.addv("userid", userid);
		List<NutMap> permList = beetlSqlManager.select("pbp.account.queryUserPermid", NutMap.class, para);
		Set<String> permIds = new HashSet<String>();
		Map<String, List<String>> permBtnMap = new HashMap<>();
		permList.forEach(perm -> {
			String permid = perm.getString("permid");
			permIds.add(permid);
			
			if(Func.isNotBlank(perm.getString("hasBtns"))) {
				List<String> hasBtns = Jackson.toList(perm.getString("hasBtns"), String.class);
				if(permBtnMap.containsKey(permid)) {
					List<String> existBtns = permBtnMap.get(permid);
					if(existBtns != null && existBtns.size() > 0) {
						hasBtns.addAll(existBtns);
						hasBtns = CollectionUtil.newArrayList(CollectionUtil.newHashSet(hasBtns));
					}
				}
				permBtnMap.put(permid, hasBtns);
			}
		});
		return NutMap.NEW()
			.addv("permIds", permIds)
			.addv("permBtnMap", permBtnMap);
	}
	
	/**
	 * 加载左侧菜单
	 * @param permIds 菜单ID集合
	 * @param permBtnMap 拥有的权限集合
	 * @return 菜单信息
	 */
	public List<PermDTO> loadLeftMenu(
			Set<String> permIds,
			Map<String, List<String>> permBtnMap,
			String pid){
		if(permIds == null || permIds.size() <= 0) {
			return null;
		}
		
		Cnd cnd = Cnd.where("isdelete", "=", 0)
			.and("pid",  "=", pid)
			.and("state",  "=", 1)
			.and("id", "in", permIds);
		
		List<Permission> list = dao.query(Permission.class, cnd.asc("sort"));
		if(list == null) {
			return null;
		}
		List<PermDTO> perms = new ArrayList<PermDTO>();
		for(Permission item:list) {
			PermDTO vo = Jackson.toJavaObject(Jackson.toJSONString(item), PermDTO.class);
			if(permBtnMap != null) {
				vo.setHasBtns(permBtnMap.get(vo.getId()));
			}
			vo.setChildren(loadLeftMenu(permIds, permBtnMap,vo.getId()));
			perms.add(vo);
		}
		return perms;
	}
	/**
	 * 获取登录成功后跳转的地址/路由
	 * @param id 用户ID
	 * @return 登录成功后跳转的地址/路由
	 */
	public String getLoginSuccessRoutePath(String id) {
		String queryLoginSuccessUrl = beetlSqlManager.selectSingle("pbp.account.queryLoginSuccessUrl", NutMap.NEW().addv("adminid", id), String.class);
		return queryLoginSuccessUrl;
	}
}
