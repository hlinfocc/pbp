package net.hlinfo.pbp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.beetl.sql.core.SQLManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.QueryPages;
import net.hlinfo.opt.Resp;
import net.hlinfo.pbp.entity.AdminRole;
import net.hlinfo.pbp.entity.Role;
import net.hlinfo.pbp.entity.RolePermission;
import net.hlinfo.pbp.opt.dto.PermDTO;
import net.hlinfo.pbp.opt.vo.PermBatchVo;
import net.hlinfo.pbp.opt.vo.RolePermParamVo;
import net.hlinfo.pbp.service.PbpPermissionService;

@Api(tags = "角色管理模块")
@RestController
@RequestMapping("/system/pbp/role")
public class PbpRoleController extends BaseController {
	@Autowired
	private Dao dao;
	
	@Autowired
	private SQLManager sqlManager;
	
	@Autowired
	private PbpPermissionService permissionService;

    @ApiOperation(value = "添加/编辑角色")
    @PostMapping("/addOrUpdate")
    public Resp<Role> addOrUpdate(@Valid @RequestBody Role role) {
        if (Strings.isNotBlank(role.getId())) {
            Role dbRole = dao.fetch(Role.class, role.getId());
            if (dbRole.getLevel() == 1) {
                return Resp.ERROR("该角色等级为系统固定的特殊角色，不能进行操作");
            }
        } else {
            role.setLevel(0);
        }
        if(Strings.isNotBlank(role.getId())) {
	        int count = dao.count(Role.class, Cnd.where("code", "=", role.getCode()).and("id", "!=", role.getId()));
	        if (count > 0) {
	            return Resp.ERROR("该角色代码已被使用");
	        }
        }
        String createId = this.getLoginId();
        role.setCreateId(createId);
        role = (Role) role.insertOrUpdate(dao);
        return Resp.OBJ_O(role);
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/delete")
    public Resp<Integer> delete(@RequestParam("id") String id) {
        int count = dao.count(AdminRole.class, Cnd.where("roleid", "=", id));
        if (count > 0) {
            return Resp.ERROR("不能删除，该角色已被使用");
        }
        Role role = dao.fetch(Role.class, id);
        if (role.getLevel() == 1) {
            return Resp.ERROR("该角色等级为系统固定的特殊角色，不能进行操作");
        }
        int num = role.deletedHard(dao);
        dao.clear(RolePermission.class, Cnd.where("roleid", "=", id));
        return Resp.OBJ_O(num);
    }

    @ApiOperation(value = "角色列表")
    @GetMapping("/list")
    public Resp<QueryPages<List<Role>>> list(@ApiParam("角色名字") @RequestParam(name = "name", required = false, defaultValue = "") String name
            , @ApiParam("角色状态默认为-100, -100全部 0禁用 1启用") @RequestParam(name = "state", defaultValue = "-100") int state
            , @ApiParam("页数") @RequestParam(name = "page", defaultValue = "1") int page
            , @ApiParam("每页显示条数") @RequestParam(name = "limit", defaultValue = "10") int limit) {
        Cnd cnd = Cnd.where("isdelete", "=", 0);
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", "%" + name + "%");
        }
        if (state != -100) {
            cnd.and("state", "=", state);
        }
        String userid = this.getLoginId();
		int type = this.getLoginType();
		System.out.println("type=" + type);
		int maxPow = sqlManager.selectSingle("pbp.account.queryMaxPow", NutMap.NEW().addv("adminid", userid).addv("type", type), Integer.class);
		cnd.and("pow", "<=", maxPow);
        Pager pager = dao.createPager(page, limit);
        pager.setRecordCount(dao.count(Role.class, cnd));
        List<Role> roles = dao.query(Role.class, cnd.asc("sort"), pager);
        return Resp.pages(roles, pager.getRecordCount(),page,limit);
    }
    
    @ApiOperation(value = "给管理员分配角色的表单角色列表")
    @GetMapping("/formList")
    public Resp formList(
    		@ApiParam("分配的管理员id") 
    		@RequestParam(name = "id", defaultValue = "") 
    		String id
    		) {
        Cnd cnd = Cnd.where("isdelete", "=", 0);
        cnd.and("state", "=", 1);
//        String userid=StpUtil.getLoginId().toString().substring(1);
//        AdminRole userRole=dao.fetch(AdminRole.class,Cnd.where("isdelete","=",0).and("adminid","=",userid));
//        String roleid=userRole.getRoleid();
//        Role role=dao.fetch(Role.class,roleid);
//        int pow=role.getPow();
//        cnd.and("pow","<=",pow);
        List<Role> roles = dao.query(Role.class, cnd.asc("sort"));
       
        List<AdminRole> adminRoles = dao.query(AdminRole.class, Cnd.where("adminid", "=", id));
        
        NutMap map = NutMap.NEW();
        map.addv("roles", roles);
        map.addv("adminRoles", adminRoles);
        return Resp.OBJ_Q(map);
    }
    
    @ApiOperation(value = "给角色授权")
    @PostMapping("/authPerm")
    public Resp<RolePermission> authPerm(@Valid @RequestBody RolePermParamVo rap) {
        Cnd cnd = Cnd.where("roleid", "=", rap.getRoleid())
                .and("permid", "=", rap.getPermid());
        RolePermission rp = dao.fetch(RolePermission.class, cnd);
        if (rp != null) {
            dao.delete(rp);
        }
        if (rap.isSelected()) {
            rp = new RolePermission();
            rp.setRoleid(rap.getRoleid());
            rp.setPermid(rap.getPermid());
            rp.setHasBtns(rap.getHasBtns());
            rp.insert(dao);
        }
        return Resp.OBJ_O(rp);
    }

    @ApiOperation(value = "给角色批量授权")
    @PostMapping("/authPermBatch")
    public Resp<List<RolePermission>> authPermBatch(
            @Valid @RequestBody PermBatchVo permBatch
    ) {
        if (Strings.isBlank(permBatch.getRoleid())) {
            return Resp.ERROR("不能删除，该角色已被使用");
        }
        String roleid = permBatch.getRoleid();
        Cnd cnd = Cnd.where("roleid", "=", roleid);
        dao.clear(RolePermission.class, cnd);
        List<RolePermission> lists = new ArrayList<>();

        permBatch.getPermids().forEach((key, item) -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleid(roleid);
            rolePermission.setPermid(key);
            rolePermission.setHasBtns(item);
            rolePermission.insert(dao);
            lists.add(rolePermission);
        });
        return Resp.LIST_O(lists);
    }

    @ApiOperation("查看角色下的权限")
    @GetMapping("/rolePerms")
    public Resp<NutMap> rolePerms(
    		@ApiParam("角色id") 
    		@RequestParam(name="roleid", defaultValue = "") 
    		String roleid) {
    	if(Strings.isBlank(roleid)) {
    		return Resp.ERROR("roleid为空");
    	}
        //该角色使用的权限
        NutMap permMap = permissionService.getPermSetByRoleid(roleid);
        Set<String> permIds = permMap.getAs("permIds", Set.class);
        Map<String, List<String>> permBtnMap = permMap.getAs("permBtnMap", Map.class);
        //所有权限
        List<PermDTO> menus = permissionService.loadPerms("0", permBtnMap);
        NutMap res = NutMap.NEW()
                .addv("menus", menus)
                .addv("hasKeys", permIds);
        return Resp.OBJ_Q(res);
    }

    @ApiOperation("给用户分配角色")
    @GetMapping("/setUserRole")
    public Resp<AdminRole> setUserrole(
            @ApiParam("用户userid") @RequestParam("userid") String adminid
            , @ApiParam("角色id") @RequestParam("roleid") String roleid) {

        Cnd cnd = Cnd.where("userid", "=", adminid).and("roleid", "=", roleid);
        AdminRole userRole = dao.fetch(AdminRole.class, cnd);
        if (userRole == null) {
            userRole = new AdminRole();
            userRole.setRoleId(roleid);
            userRole.setAdminId(adminid);
            userRole.insertOrUpdate(dao);
        }else{
            userRole.setRoleId(roleid);
            userRole.setAdminId(adminid);
            userRole.insertOrUpdate(dao);
        }
        return Resp.OBJ_O(userRole);
    }

    @ApiOperation("查看用户角色")
    @GetMapping("/userRole")
    public Resp<AdminRole> userRole(
            @ApiParam("登录账号(工号/学号) 为空查看全部") @RequestParam(value = "account", defaultValue = "") String account
            , @ApiParam("角色id 为空查看全部") @RequestParam(value = "roleid", defaultValue = "") String roleid) {
        Cnd cnd = Cnd.where("1", "=", 1);
        if (Strings.isNotBlank(account)) {
            cnd = cnd.and("userid", "=", account);
        }
        if (Strings.isNotBlank(roleid)) {
            cnd = cnd.and("roleid", "=", roleid);
        }
        System.out.println(cnd);
        List<AdminRole> userRoles = dao.query(AdminRole.class, cnd);
        return new Resp().ok("获取成功", userRoles);
    }
}

