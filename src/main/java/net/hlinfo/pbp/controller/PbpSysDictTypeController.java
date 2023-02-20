package net.hlinfo.pbp.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.hlinfo.opt.Func;
import net.hlinfo.pbp.entity.SysDictList;
import net.hlinfo.pbp.entity.SysDictType;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.usr.auth.AuthType;

@Api(tags = "数据字典分类模块")
@RestController
@RequestMapping("/system/pbp/dict/type")
public class PbpSysDictTypeController {
	@Autowired
	private Dao dao;
	

	@ApiOperation(value="配置分类-添加/编辑")
	@PostMapping("/addOrUpdate")
	@SaCheckPermission({AuthType.Admin.PERM})
	public Resp<SysDictType> addOrUpdate(@Valid @RequestBody SysDictType at){
		boolean isSuccess = true;
		if(Func.isBlank(at.getId())) {
			at.setId(Func.longuuid()+"");
			at.setCreateTime(new Date());
			at.setIsdelete(0);
			at.setUpdateTime(new Date());
			isSuccess = dao.insert(at) != null;
		}else {
			at.setUpdateTime(new Date());
			Chain data = Chain.make("field_type_name", at.getTypeName());
			Cnd cnd = Cnd.where("field_type_id", "=", at.getId());
			dao.update(SysDictList.class, data, cnd);
			isSuccess = dao.updateIgnoreNull(at) > 0;
		}
		
		if(isSuccess) {
			return new Resp<SysDictType>().ok("保存成功", at);
		}else {
			return new Resp<SysDictType>().error("保存失败");
		}
	}

	@ApiOperation(value = "配置分类-软删除")
	@DeleteMapping("/delete")
	public Resp delete(@RequestParam("id") String id
			, HttpServletRequest request) {
		if (Strings.isBlank(id)) {
			return new Resp().error("id不能为空");
		}
		int qty = dao.count(SysDictList.class,Cnd.where("isdelete","=",0).and("fieldTypeId","=",id));
		if(qty>0) {
			return new Resp().error("该分类下存在数据，禁止删除");
		}
		SysDictType tea = dao.fetch(SysDictType.class, id);
		if (tea == null) {
			return new Resp().error("该数据已经被删除了");
		}
		tea.setIsdelete(1);
		int num = dao.update(tea);
		return Resp.OBJ_O(num);
	}
	
	@ApiOperation("配置分类-查询")
	@GetMapping("/list")
	@SaCheckPermission({AuthType.Admin.PERM})
	public Resp<SysDictType> list(){
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		List<SysDictType> list = dao.query(SysDictType.class, cnd.asc("type_sort"));
		return new Resp().ok("获取成功").data(list);
	}
}

