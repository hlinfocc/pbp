package net.hlinfo.pbp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.beetl.sql.core.SQLManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.lang.Strings;
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
import net.hlinfo.pbp.entity.Permission;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.opt.dto.PermDTO;
import net.hlinfo.pbp.service.PbpPermissionService;

@Api(tags = "权限管理模块")
@RestController
@RequestMapping("/system/pbp/permission")
public class PbpPermissionController extends BaseController {
	@Autowired
	private Dao dao;
	
	@Autowired
	private PbpPermissionService permissionService;

	@ApiOperation(value="添加/编辑权限")
	@PostMapping("/addOrUpdate")
	public Resp<Permission> addOrUpdate(@Valid @RequestBody Permission permission){
		permission = (Permission) permission.insertOrUpdate(dao);
		return Resp.OBJ_O(permission);
	}
	
	@ApiOperation(value="删除权限")
	@DeleteMapping("/delete")
	public Resp<Integer> delete(@RequestParam("id") String id){
		if(Strings.isBlank(id)) {
			return Resp.ERROR("ID不能为空");
		}
		Permission perm = dao.fetch(Permission.class, id);
		if(perm.getLevel() == 1) {
			return Resp.ERROR("特殊权限，禁止删除");
		}
		
		int count = dao.count(Permission.class, Cnd.where("isdelete", "=", 0)
				.and("pid", "=", id));
		if(count > 0) {
			return Resp.ERROR("删除失败,该权限下还有子权限");
		}
		int num = dao.delete(Permission.class, id);
		return Resp.OBJ_O(num);
	}
	
	@ApiOperation("权限列表")
	@GetMapping("/list")
	public Resp<QueryResult> list(@ApiParam("权限名字-只支持2级")@RequestParam(name="name", required = false, defaultValue = "") String name
		, @ApiParam("权限类型: 0路由权限 1api接口权限 -100全部 ")@RequestParam(name="type", defaultValue = "-100") int type
		, @ApiParam("权限状态默认为-100, -100全部 0禁用 1启用")@RequestParam(name="state", defaultValue = "-100") int state){
		
		Cnd cnd = Cnd.where("isdelete", "=", 0)
				.and("pid",  "=", "0");
		if(Strings.isNotBlank(name)) {
			List<Permission> permissionList = dao.query(Permission.class,Cnd.where("name", "like", "%"+name+"%"));
			List<String> inStr=new ArrayList<>();
			if(permissionList.size()>0){
				permissionList.forEach(item->{
					inStr.add(item.getId());
					if(Strings.isNotBlank(item.getPid())){
						inStr.add(item.getPid());
					}
				});
				cnd.and(Cnd.cri().where().andInStrList("id",inStr));
			}
		}
		List<PermDTO> vos = permissionService.loadPerms(cnd, null);
		return new Resp().ok("获取成功", vos);
	}
	
	@ApiOperation("表单中的权限选择列表")
	@GetMapping("/formPermList")
	public Resp<List<PermDTO>> formPermList(
			@ApiParam("父id")@RequestParam(name="pid", defaultValue = "0") String pid){
		List<PermDTO> perms = permissionService.loadPerms("0", null);
		return Resp.LIST_Q(perms);
	}
}

