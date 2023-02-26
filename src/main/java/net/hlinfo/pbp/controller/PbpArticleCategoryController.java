package net.hlinfo.pbp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.cri.Static;
import org.nutz.lang.Strings;
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
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.opt.Resp;
import net.hlinfo.pbp.entity.ArticleCategory;
import net.hlinfo.pbp.entity.ArticleInfo;
import net.hlinfo.pbp.service.ArticleService;
import net.hlinfo.pbp.usr.auth.AuthType;

@Api(tags = "文章分类模块")
@RestController
@RequestMapping("/system/pbp/articleCategory")
@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
@SaCheckLogin
public class PbpArticleCategoryController{
	@Autowired
	private Dao dao;
	
	@Autowired
	private ArticleService articleService;
	
	@ApiOperation(value="添加/编辑")
	@PostMapping("/addOrUpdate")
	public Resp<ArticleCategory> addOrUpdate(@Valid @RequestBody ArticleCategory data){
		if(data == null) {
			return Resp.ERROR("数据为空");
		}
		if(Func.isBlank(data.getId()) && Func.notequals(data.getPid(),"0")) {
			ArticleCategory ac = dao.fetch(ArticleCategory.class, data.getPid());
			if(ac==null) {
				return Resp.ERROR("请选择正确的父级分类");
			}
			data.setCode(ac.getCode()+"-"+data.getCode());
		}
		Cnd cnd = Cnd.where("code", "=", data.getCode());
		if(Func.isNotBlank(data.getId())) {
			cnd.and("id","!=",data.getId());
		}
		int artc = dao.count(ArticleCategory.class, cnd);
		if(artc>0) {
			return new Resp().error("分类代码不能重复");
		}
		int rs = 0;
		if(Strings.isNotBlank(data.getId())) {
			rs  = data.updateIgnoreNull(dao);
		}else {
			ArticleCategory rsdata = (ArticleCategory) data.insert(dao);
			if(rsdata!=null) {
				data = rsdata;
				rs =1;
			}
		}
		if(rs>0) {
			return new Resp().SUCCESS().data(data);
		}else {
			return new Resp().FAIL();
		}
	}
	
	@ApiOperation(value = "列表")
	@GetMapping(value = "/list")
	@SaIgnore
	public Resp<Object> list(
			@ApiParam("状态：-1全部,0启用,1禁用")
			@RequestParam(name="status", defaultValue = "-1") 
			int status,
			
			@ApiParam("在导航显示 0不显示，1显示,-1全部")
			@RequestParam(name="isdisplay", defaultValue = "-1") 
			int isdisplay,
			
			@ApiParam("分页页数: page和limit有一个小于等于0则为不分页,不分页时包含子分类")
			@RequestParam(name="page", defaultValue = "1") 
			int page,
			
			@ApiParam("分页大小: page和limit有一个小于等于0则为不分页,不分页时包含子分类")
			@RequestParam(name="limit", defaultValue = "20") 
			int limit
		) {
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		if(status != -1) {
			cnd.and("status", "=", status);
		}
		cnd.asc("sort");
		if(page <= 0 || limit <= 0) {
			List<ArticleCategory> list = articleService.articleCategoryList("0", status,isdisplay);
			return Resp.OK("获取成功", list);
		}else {
			Pager pager = dao.createPager(page, limit);
			int count = dao.count(ArticleCategory.class, cnd);
			pager.setRecordCount(count);
			List<ArticleCategory> list = dao.query(ArticleCategory.class, cnd, pager);
			return Resp.OK("获取成功", new QueryResult(list, pager));
		}
	}
	
	@ApiOperation(value = "删除")
	@DeleteMapping(value = "/delete")
	public Resp<List<ArticleCategory>> delete(
			@ApiParam("ID")
			@RequestParam(name="id", defaultValue = "") 
			String id
		) {
		if(Strings.isBlank(id)) {
			return Resp.ERROR("ID为空");
		}
		Cnd cnd = Cnd.where("isdelete", "=", 0).and("acid", "=", id);
		int count = dao.count(ArticleInfo.class, cnd);
		if(count > 0) {
			return Resp.ERROR("该分类下有内容，不能删除");
		}
		List<String> acids = new ArrayList<String>();
		acids.add(id);
		List<List<String>> acidsarr = new ArrayList<List<String>>();
		acidsarr.add(acids);
		Cnd cnd2 = Cnd.where("isdelete", "=", 0);
		cnd2.and(new Static("acids @> '"+Jackson.toJSONString(acidsarr)+"'"));
		int qty = dao.count(ArticleInfo.class,cnd2);
		if(qty>0) {
			return Resp.ERROR("该分类下有文章，不能删除");
		}
		ArticleCategory data = dao.fetch(ArticleCategory.class, id);
		if(data == null) {
			return Resp.ERROR("删除不存在或者已经被删了");
		}
		if(data.getLocked()==1) {
			return Resp.ERROR("该分类禁止删除，若不再使用，设置状态禁用即可");
		}
		data.setCode(data.getCode()+"_"+Func.Times.nowNumberFull());
		int rs = data.deletedSoft(dao);
		return rs>0?Resp.OK("删除成功"):Resp.ERROR("删除失败");
	}
	
}
