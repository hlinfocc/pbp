package net.hlinfo.pbp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.hlinfo.opt.Func;
import net.hlinfo.pbp.entity.MimeTypes;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.usr.auth.AuthType;

@Api(tags = "Mime类型管理模块")
@RestController
@RequestMapping("/system/pbp/mimeTypes")
public class PbpMimeTypesController {
	@Autowired
	private Dao dao;
	
	@ApiOperation(value="添加/编辑")
	@PostMapping("/addOrUpdate")
	@SaCheckPermission({AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp addOrUpdate(@Valid @RequestBody MimeTypes at){
		Cnd cnd = Cnd.where("isdelete", "=", 0)
				.and("content_type", "=", at.getContentType());
		if(Func.isNotBlank(at.getId())) {
			cnd.and("id", "!=", at.getId());
		}
		MimeType data = dao.fetch(MimeType.class, cnd);
		if(data != null) {
			return Resp.ERROR("ContentType已存在").data(data);
		}
		at = (@Valid MimeTypes) at.insertOrUpdate(dao);
		return Resp.OBJ_O(at);
	}

	@ApiOperation(value = "软删除")
	@DeleteMapping("/delete")
	public Resp delete(@RequestParam("id") String id
			, HttpServletRequest request) {
		if (Strings.isBlank(id)) {
			return new Resp().error("id不能为空");
		}
		MimeTypes tea = dao.fetch(MimeTypes.class, id);
		if (tea == null) {
			return new Resp().error("该数据已经被删除了");
		}
		int num = tea.deletedSoft(dao);
		return Resp.OBJ_O(num);
	}
	
	@ApiOperation(value = "byid")
	@GetMapping("/byid")
	public Resp byid(@RequestParam("id") String id
			, HttpServletRequest request) {
		if (Strings.isBlank(id)) {
			return new Resp().error("id不能为空");
		}
		MimeTypes tea = dao.fetch(MimeTypes.class, id);
		
		return Resp.OBJ_Q(tea);
	}
	
	@ApiOperation(value = "ContentType查询")
	@GetMapping("/byContentType")
	public Resp byContentType(@RequestParam("contentType") String contentType
			, HttpServletRequest request) {
		if (Strings.isBlank(contentType)) {
			return new Resp().error("contentType不能为空");
		}
		Cnd cnd = Cnd.where("isdelete", "=", 0)
				.and("content_type", "=", contentType);
		MimeTypes tea = dao.fetch(MimeTypes.class, cnd);
		return Resp.OBJ_Q(tea);
	}
	
	@ApiOperation("查询")
	@GetMapping("/list")
	@SaCheckPermission({AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<List<MimeType>> list(){
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		List<MimeType> list = dao.query(MimeType.class, cnd.asc("sort"));
		return Resp.LIST_Q(list);
	}
	
	@ApiOperation("生成初始化代码")
	@GetMapping("/genInitCode")
//	@SaCheckPermission({UserType.Admin.permName})
	public Resp<String> genInitCode(){
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		List<MimeTypes> list = dao.query(MimeTypes.class, cnd);
		String code = "String[][] mimeTypeArr = {";
		if(list != null) {
			for(int i = 0; i < list.size(); i ++){
				code += "\t\"" + list.get(i).getContentType() + "\", \"" + list.get(i).getSuffix() + "\"";
				if(i < list.size() - 2) {
					code += ",";
				}
				code += "\n";
			}
		}
		code += "}";
		return Resp.OBJ_Q(code);
	}
	
}

