package net.hlinfo.pbp.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Resp;
import net.hlinfo.pbp.entity.ImgManager;
import net.hlinfo.pbp.usr.auth.AuthType;

@Api(tags = "图片管理")
@RestController
@RequestMapping("/system/pbp/imgManager")
public class PbpImgManagerController  extends BaseController {
	@Autowired
	private Dao dao;
	
	@ApiOperation(value="添加/编辑")
	@PostMapping("/addOrUpdate")
	@SaCheckPermission(value= {AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<ImgManager> addOrUpdate(@Valid @RequestBody ImgManager data){
		if(Strings.isBlank(data.getId())) {
			data.init();
		}
		/*
		 * if(data.getImgtype()==1 || data.getImgtype()==2) {
		 * if(data.getStatus()==0) {
		 * Chain chain = Chain.make("status", 1);
		 * Cnd cnd = Cnd.where("isdelete", "=", 0);
		 * cnd.and("imgtype","=",data.getImgtype());
		 * dao.update(ImgManager.class, chain, cnd);
		 * }else {
		 * Cnd cnd = Cnd.where("isdelete", "=", 0);
		 * cnd.and("imgtype","=",data.getImgtype());
		 * int bgc = dao.count(ImgManager.class,cnd);
		 * if(bgc==1) {
		 * return new Resp().error("背景图至少需保留一张开启^_^");
		 * }
		 * }
		 * }
		 */
		data.setUpdateTime(new Date());
		data = dao.insertOrUpdate(data);
		if(data != null) {
			return new Resp().ok("保存成功", data);
		}else {
			return new Resp().error("保存失败");
		}
	}
	
	
	
	@ApiOperation(value="删除")
	@RequestMapping(value = {"/delete"},method = {RequestMethod.DELETE,RequestMethod.GET})
	@SaCheckPermission(value= {AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp delete(@RequestParam("id") String id){
		ImgManager obj = dao.fetch(ImgManager.class, id);
		if(obj==null) {
			return new Resp().error("删除失败，待删除内容不存在");
		}
		/*if((obj.getImgtype()==1 || obj.getImgtype()==2) && obj.getStatus()==0) {
			Cnd cnd = Cnd.where("isdelete", "=", 0);
			cnd.and("imgtype","=",obj.getImgtype());
			int bg = dao.count(ImgManager.class,cnd);
			if(bg==1) {
				return new Resp().error("背景图至少需保留一张开启^_^");
			}
		}*/
		int num = dao.delete(ImgManager.class, id);
		if(num > 0) {
			return new Resp().ok("删除成功");
		}else {
			return new Resp().error("删除失败");
		}
	}
	
	@ApiOperation("列表")
	@GetMapping("/list")
	@SaCheckPermission(value= {AuthType.Root.PERM,AuthType.Admin.PERM})
	@SaCheckLogin
	public Resp<List<ImgManager>> list(@ApiParam("类型：0首页轮播图") @RequestParam(value="imgtype",defaultValue = "-1") int imgtype){
		Cnd cnd =  Cnd.where("isdelete", "=", 0);
		if(imgtype>-1) {
			cnd.and("imgtype", "=", imgtype);
		}
		cnd.asc("imgtype");
		cnd.asc("sort");
		List<ImgManager> list = dao.query(ImgManager.class,cnd);
		return new Resp().ok("获取成功", list);
	}
}
