package net.hlinfo.pbp.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.checkerframework.checker.units.qual.C;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.FieldMatcher;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
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
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.opt.Resp;
import net.hlinfo.pbp.entity.AdminInfo;
import net.hlinfo.pbp.entity.ArticleCategory;
import net.hlinfo.pbp.entity.ArticleCollection;
import net.hlinfo.pbp.entity.ArticleInfo;
import net.hlinfo.pbp.entity.VisitHistoryRecord;
import net.hlinfo.pbp.opt.vo.ArticleParams;
import net.hlinfo.pbp.opt.vo.KV;
import net.hlinfo.pbp.opt.vo.UpdateBatchVo;
import net.hlinfo.pbp.service.AreacodeService;
import net.hlinfo.pbp.service.ArticleService;
import net.hlinfo.pbp.service.OpLogsService;
import net.hlinfo.pbp.usr.auth.AuthType;

@Api(tags = "文章模块")
@RestController
@RequestMapping("/system/pbp/articleInfo")
@SaCheckPermission({AuthType.Root.PERM,AuthType.Admin.PERM})
@SaCheckLogin
public class PbpArticleInfoController extends BaseController{
	@Autowired
	private Dao dao;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private OpLogsService opLogsService;
	
	@Autowired
	private AreacodeService areacodeService;
	
	@ApiOperation(value="添加/编辑")
	@PostMapping("/addOrUpdate")
	public Resp<ArticleInfo> addOrUpdate(@Valid @RequestBody ArticleInfo data, HttpServletRequest request){
		if(Func.isNotBlank(data.getContent()) && Func.isBlank(data.getTextContent())) {
			data.setTextContent(HtmlUtil.cleanHtmlTag(data.getContent()));
		}
		if(Func.isBlank(data.getSummary()) && Func.isNotBlank(data.getTextContent())) {
			data.setSummary(data.getTextContent().length()>200?data.getTextContent().substring(0, 200):data.getTextContent());
		}
		if(Func.isNotBlank(data.getSummary())) {
			data.setSummary(HtmlUtil.unescape(data.getSummary().replaceAll("\\t|\\n\\r|\\n|\\r|\\s*", "")));
		}
		////处理地址
		switch (data.getLevel()) {
		case 1:
			data.setProvincecode("");
			data.setProvincename(null);
			data.setCitycode("");
			data.setCityname(null);
			data.setCountycode("");
			data.setCountyname(null);
			break;
		case 2:
			data.setCitycode("");
			data.setCityname(null);
			data.setCountycode("");
			data.setCountyname(null);
			break;
		case 3:
			data.setProvincecode("");
			data.setProvincename(null);
			data.setCountycode("");
			data.setCountyname(null);
			break;
		case 4:
			data.setProvincecode("");
			data.setProvincename(null);
			data.setCitycode("");
			data.setCityname(null);
			break;
		default:
			break;
		}
		if(Func.isNotBlank(data.getProvincecode())) {
			data.setProvincename(areacodeService.getAreatitle(data.getProvincecode()));
		}
		if(Func.isNotBlank(data.getCitycode())) {
			data.setCityname(areacodeService.getAreatitle(data.getCitycode()));
		}
		if(Func.isNotBlank(data.getCountycode())) {
			data.setCountyname(areacodeService.getAreatitle(data.getCountycode()));
		}
		
		if(Func.isBlank(data.getTitlePicurl())) {
			data.setTitlePicurl("");
		}
		if(Func.isBlank(data.getFocusPicUrl())) {
			data.setFocusPicUrl("");
		}
		
		if(Func.isNotBlank(data.getPushdate())) {
			DateTime dt = DateUtil.parse(data.getPushdate());
			data.setPushYear(dt.year());
		}
		if(data.getAcids()!=null && data.getAcids().size()>0) {
			List<List<String>> acNames = new ArrayList<List<String>>();
			List<List<String>> codes = new ArrayList<List<String>>();
			for (List<String> acids : data.getAcids()) {
				List<String> acNameItem = new ArrayList<String>();
				List<String> codesItem = new ArrayList<String>();
				for (String id : acids) {
					ArticleCategory category = articleService.getCategory(id);
					acNameItem.add(category.getName());
					codesItem.add(category.getCode());
				}
				acNames.add(acNameItem);
				codes.add(codesItem);
			}
			data.setAcnames(acNames);
			data.setCodes(codes);
		}
		data = (ArticleInfo) data.insertOrUpdateIgnoreNull(dao);
		if(data != null) {
			opLogsService.AdminAddOpLogs("编辑文章", " 文章id："+data.getId(), request);
			return new Resp().ok("操作成功", data);
		}else {
			return new Resp().error("操作失败");
		}
	}
	
	@ApiOperation(value="修改状态")
	@PostMapping("/modifyStatus")
	public Resp<ArticleInfo> modifyStatus(@RequestBody ArticleInfo data, HttpServletRequest request){
		if(Func.isBlank(data.getId()) || data.getStatus()<=0) {
			return new Resp().error("参数不能为空");
		}
		Chain chain = Chain.make("status", data.getStatus());
		int rs = dao.update(ArticleInfo.class, chain, Cnd.where("id", "=", data.getId()));
		if(rs>0) {
			opLogsService.AdminAddOpLogs("修改文章状态", " 文章id："+data.getId()+",状态修改为："+data.getStatus(), request);
			return new Resp().ok("操作成功", data);
		}else {
			return new Resp().error("操作失败");
		}
	}
	
	@ApiOperation(value="批量修改数据")
	@PostMapping("/batchModifyStatus")
	public Resp<ArticleInfo> batchModifyStatus(@RequestBody UpdateBatchVo data, HttpServletRequest request){
		if(data.getIds() == null || data.getIds().size() <= 0
				|| data.getDatas() == null || data.getDatas().size() <= 0) {
			return new Resp().error("参数不能为空");
		}
		Chain chain = null;
		for(int i = 0; i < data.getDatas().size(); i ++){
			KV d = data.getDatas().get(i);
			if(Func.equals(d.getK(), "auditStatus")) {
				continue;
			}
			if(chain == null) {
				chain = Chain.make(d.getK(), d.getV());
			}else {
				chain.add(d.getK(), d.getV());
			}
		}
		
		int rs = dao.update(ArticleInfo.class, chain, Cnd.where("id", "in", data.getIds()));
		if(rs>0) {
			opLogsService.AdminAddOpLogs("批量修改文章数据", " 文章id："+data.getIds().toString(), request);
			return new Resp().ok("操作成功", data);
		}else {
			return new Resp().error("操作失败");
		}
	}

	@ApiOperation(value="查询一条数据")
	@GetMapping("/query")
	@SaIgnore
	public Resp<ArticleInfo> query(@ApiParam(value="文章ID",name="id",required = true) @RequestParam(name="id",required = true) String id
			,@ApiParam(value = "关键词")
			@RequestParam(name="keywords", defaultValue = "") 
			String keywords
			, HttpServletRequest request){
		if(Func.isBlank(id)) {
			return new Resp().error("参数不能为空");
		}
		ArticleInfo obj = dao.fetch(ArticleInfo.class, id);
		if(StpUtil.isLogin() && StpUtil.hasPermissionOr(AuthType.Root.PERM,AuthType.Admin.PERM)) {
			Cnd cnd = Cnd.where("userId", "=", this.getLoginId());
			List<ArticleCollection> acs = dao.query(ArticleCollection.class, cnd);
			List<String> ids = new ArrayList<String> ();
			if(acs != null && acs.size() > 0) {
				acs.forEach(item -> ids.add(item.getArtid()));
			}
			obj.setIsCollect(ids.contains(obj.getId()) ? 1 : 0);
		}
		if(Strings.isNotBlank(keywords)) {
			String keywordsRed = "<red>"+keywords+"</red>";
			if(Func.isNotBlank(obj.getTitle())) {					
				obj.setTitle(obj.getTitle().replace(keywords, keywordsRed));
			}
			if(Func.isNotBlank(obj.getKeywords())) {					
				obj.setKeywords(obj.getKeywords().replace(keywords, keywordsRed));
			}
			if(Func.isNotBlank(obj.getContent())) {					
				obj.setContent(obj.getContent().replace(keywords, keywordsRed));
			}
		}
		return new Resp().ok("获取成功",obj);
	}
	
	@ApiOperation(value="更新浏览量")
	@GetMapping("/visit")
	@SaIgnore
	public Resp<String> visit(@ApiParam(value="id",required = true) @RequestParam(name="id",required = true) String id, HttpServletRequest request){
		if(Func.isBlank(id)) {
			return new Resp().error("参数不能为空");
		}
		ArticleInfo obj = dao.fetch(ArticleInfo.class, id);
		if(obj!=null) {
			VisitHistoryRecord rh = new VisitHistoryRecord();
			rh.setTargetId(id);
			if(StpUtil.isLogin() && StpUtil.hasPermissionOr(AuthType.Member.PERM,AuthType.Merchant.PERM,AuthType.Teacher.PERM,AuthType.Student.PERM,AuthType.Other.PERM)) {
				String memid = StpUtil.getLoginId().toString().split("-")[1];
				rh.setMemid(memid);
			}
			rh.setType(0);
			rh.setMemtype(0);
			rh.setIp(Func.getIpAddr(request));
			rh.setUserAgent(request.getHeader("user-agent"));
			rh.insert(dao);
			dao.update(ArticleInfo.class, Chain.makeSpecial("visit", "+1"), Cnd.where("id","=", id));
			return new Resp().ok("操作成功");
		}
		return new Resp().FAIL();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@ApiOperation(value="POST方式查询列表")
	@PostMapping("/list")
	@SaIgnore
	public Resp<List<ArticleInfo>> postList(@RequestBody ArticleParams ap,HttpServletRequest request){
		if(ap==null) {
			return new Resp().error("参数不能为空");
		}
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		List<String> ids = new ArrayList<String> ();
		/*if(StpUtil.isLogin() && StpUtil.hasPermission(C.LoginType.MEMBER_PERM_NAME)) {
			String[] memids = StpUtil.getLoginIdAsString().split("-");
			Cnd cnd2 = Cnd.where("memid", "=", memids[1]);
			List<ArticleCollection> acs = dao.query(ArticleCollection.class, cnd2);
			if(acs != null && acs.size() > 0) {
				acs.forEach(item -> ids.add(item.getArtid()));
			}
		}*/
		Pager pager = dao.createPager(ap.getPage(), ap.getLimit());
		
		if(ap.getStatus()>0) {
			cnd.and("status", "=", ap.getStatus());
		}else {
			if(ap.getNostatus()>0) {
				cnd.and("status", "!=", ap.getNostatus());
			}
		}
		if(ap.getPushYear()>0) {
			if(ap.getPushYear()==2011) {
				cnd.and("pushYear", "<=", ap.getPushYear());
			}else {
				cnd.and("pushYear", "=", ap.getPushYear());
			}
		}
		if(ap.getAuditStatus()>=0 && ap.getAuditStatus()<=2) {
			cnd.and("auditStatus", "=", ap.getAuditStatus());
		}
		if(ap.getIstop()==0 || ap.getIstop()==1) {
			cnd.and("istop", "=", ap.getIstop());
		}
		if(ap.getHots()==0 || ap.getHots()==1) {
			cnd.and("hots", "=", ap.getHots());
		}
		if(ap.getPushYears()!=null && !ap.getPushYears().isEmpty()) {
			cnd.and("pushYear", "in", ap.getPushYears());
			if(ap.getPushYears().contains("2011")) {
				cnd.and("pushYear", "<=", 2011);
			}
		}
		if(Func.isNotBlank(ap.getPushStartDate()) && Func.isNotBlank(ap.getPushEndDate())) {
			Date endDateObj = Func.Times.getEndOfDay(DateUtil.parse(ap.getPushEndDate()).toJdkDate());
			String endDate = Func.Times.date2String(endDateObj);
			SqlExpressionGroup exp = Cnd.exps(new Static("pushdate::timestamp >= to_timestamp('"+ap.getPushStartDate()+"','yyyy-MM')"));
			exp.and(new Static("pushdate::timestamp <= to_timestamp('"+endDate+"','yyyy-MM-DD HH24:MI:SS')"));
    		cnd.and(exp);
		}
		if(ap.getAcids()!=null && ap.getAcids().size()>0) {
			List<List<String>> acids = new ArrayList<List<String>>();
			acids.add(ap.getAcids());
			cnd.and(new Static("acids @> '"+Jackson.toJSONString(acids)+"'"));
		}
		
		if(Strings.isNotBlank(ap.getKeywords())) {
			if(ap.getSearchField()!=null && ap.getSearchField().size()>0) {
				SqlExpressionGroup exp = null;
				for (int i = 0; i < ap.getSearchField().size(); i++) {
					String item = ap.getSearchField().get(i);
					if(i==0) {
						exp = Cnd.exps(item, "like", "%"+ap.getKeywords()+"%");
					}else {
						exp.or(item,  "like", "%"+ap.getKeywords()+"%");
					}
				}
				cnd.and(exp);
			}else {
				SqlExpressionGroup exp = Cnd.exps("title", "like", "%"+ap.getKeywords()+"%");
				exp.or("textContent",  "like", "%"+ap.getKeywords()+"%");
				exp.or("source",  "like", "%"+ap.getKeywords()+"%");
				exp.or("keywords",  "like", "%"+ap.getKeywords()+"%");
				exp.or("issuedNumber",  "like", "%"+ap.getKeywords()+"%");
	    		cnd.and(exp);
			}
		}
		if(ap.getLevel()==1) {
			cnd.and("level","=",ap.getLevel());
		}else {
			if(ap.getLevel()>1 && ap.getLevel()<=4) {
				cnd.and("level","=",ap.getLevel());
			}
			if(Func.isNotBlank(ap.getProvincecode())) {
				cnd.and("provincecode","=",ap.getProvincecode());
			}
			if(Func.isNotBlank(ap.getCitycode())) {
				cnd.and("citycode","=",ap.getCitycode());
			}
			if(Func.isNotBlank(ap.getCountycode())) {
				cnd.and("countycode","=",ap.getCountycode());
			}
		}
		
		if(ap.getAreaKvs()!=null && !ap.getAreaKvs().isEmpty()) {
			SqlExpressionGroup exp = null;
			for(int i = 0; i < ap.getAreaKvs().size(); i ++){
				KV d = ap.getAreaKvs().get(i);
				
				if(exp == null) {
					exp = Cnd.exps(d.getK(), "=", d.getV());
				}else {
					exp.or(d.getK(),"=", d.getV());
				}
			}
			if(exp!=null) {
				cnd.and(exp);
			}
		}
		
		pager.setRecordCount(dao.count(ArticleInfo.class, cnd));
		
		if(Func.isBlank(ap.getOrderby())) {
			cnd.desc("istop");
			cnd.desc("hots");
			cnd.desc("updatetime");
			cnd.desc("pushdate");
		}else if(Func.equals(ap.getOrderby(), "pushdate") 
				|| Func.equals(ap.getOrderby(), "createtime") 
				|| Func.equals(ap.getOrderby(), "updatetime") 
				|| Func.equals(ap.getOrderby(), "visit")) {
			if(Func.equals(ap.getOrdertype().toLowerCase(), "asc") || Func.equals(ap.getOrdertype().toLowerCase(), "desc")) {
//				cnd.desc("hots");
//				cnd.desc("istop");
				cnd.orderBy(ap.getOrderby(), ap.getOrdertype().toLowerCase());
			}else {
				cnd.desc("istop");
				cnd.desc("hots");
				cnd.desc("updatetime");
				cnd.desc("pushdate");
			}
		}
		FieldMatcher matcher = FieldMatcher.make(null, "^content|text_content$", false);
		List<ArticleInfo> list = dao.query(ArticleInfo.class, cnd,pager,matcher);
		/*if(StpUtil.isLogin() && StpUtil.hasPermission(C.LoginType.MEMBER_PERM_NAME)) {
			if(list != null) list.forEach(item -> item.setCollect(ids.contains(item.getId()) ? 1 : 0));
		}*/
		if(Strings.isNotBlank(ap.getKeywords())) {
			String keywordsRed = "<red>"+ap.getKeywords()+"</red>";
			for(ArticleInfo art:list) {
				if(Func.isNotBlank(art.getTitle())) {					
					art.setTitle(art.getTitle().replace(ap.getKeywords(), keywordsRed));
				}
				if(Func.isNotBlank(art.getKeywords())) {					
					art.setKeywords(art.getKeywords().replace(ap.getKeywords(), keywordsRed));
				}
			}
		}
		if(ap.getQueryIsRead()==1) {
			if(StpUtil.isLogin() && StpUtil.hasPermissionOr(AuthType.Member.PERM,AuthType.Merchant.PERM,AuthType.Teacher.PERM,AuthType.Student.PERM,AuthType.Other.PERM)) {
				String memid = StpUtil.getLoginId().toString().split("-")[1];
				String sqlStr = "select target_id from pbp_visit_history_record where isdelete=0 memid=@memid and type=0";
				 Sql sql = Sqls.create(sqlStr);
				 sql.setParam("memid", memid);
				 sql.setCallback(Sqls.callback.strList());
				 sql.setEntity(dao.getEntity(String.class));
				 dao.execute(sql);
				 List<String> vhlistRecords = sql.getList(String.class);
				 list.forEach(item -> item.setIsRead(vhlistRecords.contains(item.getId()) ? 1 : 0));
			}
		}
		pager.setRecordCount(dao.count(ArticleInfo.class, cnd));
		return new Resp().ok("获取成功").data(new QueryResult(list, pager));
	}
	
	@ApiOperation(value="焦点图列表")
	@GetMapping("/focusPicList")
	public Resp<List<ArticleInfo>> focusPicList(
			@ApiParam("父级分类ID")
			@RequestParam(name="pid", defaultValue = "") 
			String pid,
			@ApiParam("二分类分类ID")
			@RequestParam(name="sid", defaultValue = "") 
			String sid,
			@ApiParam("三级分类分类ID")
			@RequestParam(name="tid", defaultValue = "") 
			String tid,
			@ApiParam(value = "页码",defaultValue = "1")
			@RequestParam(name="page", defaultValue = "1") 
			int page,
			@ApiParam(value = "每页显示条数",defaultValue = "5")
			@RequestParam(name="limit", defaultValue = "5") 
			int limit,
			HttpServletRequest request){
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		cnd.and("status","=",2);
		cnd.and("auditStatus","=",1);
		Pager pager = dao.createPager(page, limit);
		
		List<String> acids = new ArrayList<String>();
		if(Func.isNotBlank(pid)) {
			acids.add(pid);
		}
		if(Func.isNotBlank(sid)) {
			acids.add(sid);
		}
		if(Func.isNotBlank(tid)) {
			acids.add(tid);
		}
		if(acids!=null && acids.size()>0) {
			List<List<String>> acidsarr = new ArrayList<List<String>>();
			acidsarr.add(acids);
			cnd.and(new Static("acids @> '"+Jackson.toJSONString(acidsarr)+"'"));
		}
		
		SqlExpressionGroup exp = Cnd.exps(new Static("focus_pic_url is not null"));
		exp.and("focusPicUrl",  "!=", "");
		cnd.and(exp);
		
		pager.setRecordCount(dao.count(ArticleInfo.class, cnd));
		cnd.limit(page, limit);
		
		cnd.desc("hots");
		cnd.desc("pushdate");
		cnd.desc("createtime");
		
		FieldMatcher matcher = FieldMatcher.make(null, "^content|html|pdfUrl$", false);
		List<ArticleInfo> list = dao.query(ArticleInfo.class, cnd,pager,matcher);
		return new Resp().ok("获取成功").data(new QueryResult(list, pager));
	}
	
	@ApiOperation(value="删除")
	@DeleteMapping("/delete")
	public Resp<ArticleInfo> delete(@RequestParam("id") String id, HttpServletRequest request){
		if(Strings.isBlank(id)) {
			return new Resp().error("id不能为空");
		}
		ArticleInfo obj = dao.fetch(ArticleInfo.class, id);
		if(obj==null) {
			return new Resp().error("删除失败，数据不存在");
		}
		if(obj.getStatus()!=3) {
			return new Resp().error("仅撤稿状态才能删除");
		}
		int res = obj.deletedSoft(dao);
		if(res > 0) {
			opLogsService.AdminAddOpLogs("删除文章", " 文章id："+id, request);
			return new Resp().ok("删除成功");
		}else {
			return new Resp().error("删除失败");
		}
	}
	
	@ApiOperation(value="清空所有撤稿")
	@DeleteMapping("/clear")
	public Resp<String> clear(HttpServletRequest request){
		Chain chain = Chain.make("isdelete", 1);
		int rs = dao.update(ArticleInfo.class, chain, Cnd.where("status", "=", 3));
		if(rs > 0) {
			return new Resp().SUCCESS();
		}else {
			return new Resp().FAIL();
		}
	}
	
	@ApiOperation(value="文章收藏/取消收藏(若文章没有收藏，则收藏，否则取消收藏)")
	@PostMapping("/collect")
	@SaCheckPermission(value= {AuthType.Member.PERM,AuthType.Merchant.PERM,AuthType.Teacher.PERM,AuthType.Student.PERM,AuthType.Other.PERM},mode=SaMode.OR)
	@SaCheckLogin
	public Resp collect(@ApiParam("收藏文章ID")@RequestParam(name="artid", required = false) String artid){
		if(Strings.isBlank(artid)) {
			return new Resp().error("文章id不能为空");
		}
		String[] memids = StpUtil.getLoginIdAsString().split("-");
		Cnd cnd = Cnd.where("memid", "=", memids[1]);
		cnd.and("artid", "=", artid);
		ArticleCollection ac = dao.fetch(ArticleCollection.class, cnd);
		if(ac == null) {
			ac = new ArticleCollection();
			ac.init();
			ac.setUserId(memids[1]);
			ac.setArtid(artid);
			ac = dao.insert(ac);
			return Resp.OBJ_O(ac);
		}else {
			int num = dao.delete(ac);
			return Resp.OBJ_O(num);
		}
	}

	@ApiOperation(value="获取用户收藏的文章数据")
	@GetMapping("/collectList")
	@SaCheckPermission(value= {AuthType.Member.PERM,AuthType.Merchant.PERM,AuthType.Teacher.PERM,AuthType.Student.PERM,AuthType.Other.PERM},mode=SaMode.OR)
	@SaCheckLogin
	public Resp<List<ArticleInfo>> collectList(
			@ApiParam("一级分类ID")
			@RequestParam(name="pid", defaultValue = "") 
			String pid,
			@ApiParam("子分类分类ID")
			@RequestParam(name="sid", defaultValue = "") 
			String sid,
			@ApiParam("三级分类分类ID")
			@RequestParam(name="tid", defaultValue = "") 
			String tid,
			@ApiParam(value = "关键词")
			@RequestParam(name="keywords", defaultValue = "") 
			String keywords,
			@ApiParam(value = "状态：1草稿，2发布，3撤稿, 4归档,0全部",defaultValue = "0")
			@RequestParam(name="status", defaultValue = "0") 
			int status,
			@ApiParam(value = "页码",defaultValue = "1")
			@RequestParam(name="page", defaultValue = "1") 
			int page,
			@ApiParam(value = "每页显示条数",defaultValue = "20")
			@RequestParam(name="limit", defaultValue = "20") 
			int limit
			, HttpServletRequest request){
		
		String[] memids = StpUtil.getLoginIdAsString().split("-");
		Cnd cnd = Cnd.where("memid", "=", memids[1]);
		List<ArticleCollection> acs = dao.query(ArticleCollection.class, cnd);
		Pager pager = dao.createPager(page, limit);
		List<String> ids = new ArrayList<String> ();
		if(acs == null || acs.size() <= 0) {
			return Resp.pages(new ArrayList(), 0,page, limit);
		}else {
			acs.forEach(item -> ids.add(item.getArtid()));
		}
		
		cnd = Cnd.where("isdelete", "=", 0);
		cnd.and("id", "in", ids);
		if(status>0) {
			cnd.and("status", "=", status);
		}
		List<String> acids = new ArrayList<String>();
		if(Func.isNotBlank(pid)) {
			acids.add(pid);
		}
		if(Func.isNotBlank(sid)) {
			acids.add(sid);
		}
		if(Func.isNotBlank(tid)) {
			acids.add(tid);
		}
		if(acids!=null && acids.size()>0) {
			List<List<String>> acidsarr = new ArrayList<List<String>>();
			acidsarr.add(acids);
			cnd.and(new Static("acids @> '"+Jackson.toJSONString(acidsarr)+"'"));
		}
		if(Strings.isNotBlank(keywords)) {
			SqlExpressionGroup exp = Cnd.exps("title", "like", "%"+keywords+"%")
					.or("content", "like", "%"+keywords+"%");
    		cnd.and(exp);
		}
		pager.setRecordCount(dao.count(ArticleInfo.class, cnd));
		cnd.limit(page, limit);
		cnd.desc("hots");
		cnd.desc("pushdate");
		cnd.desc("createtime");
		FieldMatcher matcher = FieldMatcher.make(null, "^content|text_content$", false);
		System.out.println(cnd);
		List<ArticleInfo> list = dao.query(ArticleInfo.class, cnd,pager,matcher);
		
		return new Resp().ok("获取成功").data(new QueryResult(list, pager));
	}
	
	@ApiOperation(value="一键排版")
	@PostMapping("/typeseting")
	public Resp<ArticleInfo> typeseting(@RequestBody ArticleInfo article
			, HttpServletRequest request){
		if(Func.isBlank(article.getContent())) {
			return new Resp().error("文章内容不能为空");
		}
		String html = article.getContent();
		Document doc = Jsoup.parse(html);
		String fontHandleid = "pbpFontHandlerOK40200207223500999";
		if(doc.getElementById(fontHandleid) == null) {
			html = html.replaceAll("font-family:[^><;]*(?:\"\\s+|\\'\\s+|;)", "");
			html = html.replaceAll("font-size:[^><;]*(?:\"\\s+|\\'\\s+|;)", "");
			html = "<div id=\"" + fontHandleid + "\" style=\"font-family: '宋体,SimSun';font-size:12pt\">" + html + "</div>";
		}
//		html = html.replaceAll("<p.*?><\\!\\-\\-\\s+\\[if\\s+gte\\s+mso\\s+9\\]><xml>.*?<\\!\\[endif\\]\\-\\-><\\!\\-\\-StartFragment\\-\\-><\\!\\-\\-EndFragment\\-\\-><\\/p>", "");
		html = ReUtil.delAll("<p><\\!\\-\\-\\s+\\[if\\s+gte\\s+mso\\s+9\\]><xml>(.*?)<\\!\\[endif\\]\\-\\-><\\!\\-\\-StartFragment\\-\\-><\\!\\-\\-EndFragment\\-\\-><\\/p>", html);
		doc = Jsoup.parse(html);
		articleService.typesetingNode(doc);
		article.setContent(doc.getElementsByTag("body").html());
		return new Resp().ok("OK").data(article);
	}
	
	@ApiOperation(value="文章审核")
	@PostMapping("/articleAudit")
	public Resp articleAudit(@RequestBody UpdateBatchVo data
			, HttpServletRequest request) {
		String[] userids = StpUtil.getLoginIdAsString().split("-");
		AdminInfo user = articleService.getUserInfo(userids[1]);
		if(user.getUserLevel()!=0) {
			return new Resp().error("您没有审核权限，仅超级管理员可以审核");
		}
		if(data.getIds() == null || data.getIds().size() <= 0
				|| data.getDatas() == null || data.getDatas().size() <= 0) {
			return new Resp().error("参数不能为空");
		}
		Chain chain = null;
		for(int i = 0; i < data.getDatas().size(); i ++){
			KV d = data.getDatas().get(i);
			if(Func.notequals(d.getK(), "auditStatus") && Func.notequals(d.getK(), "status")) {
				continue;
			}
			if(chain == null) {
				chain = Chain.make(d.getK(), d.getV());
			}else {
				chain.add(d.getK(), d.getV());
			}
		}
		chain.add("auditTime", Func.Times.now());
		int rs = dao.update(ArticleInfo.class, chain, Cnd.where("id", "in", data.getIds()));
		if(rs>0) {
			opLogsService.AdminAddOpLogs("审核文章数据", "文章id："+data.getIds().toString(), request);
			return new Resp().ok("操作成功", data);
		}else {
			return new Resp().error("操作失败");
		}
	}
	
	
	@ApiOperation(value="查重")
	@GetMapping("/checkDuplicate")
	public Resp<List<ArticleInfo>> checkDuplicate(
			@ApiParam(value = "标题")
			@RequestParam(name="title", defaultValue = "") 
			String title,
			@ApiParam(value = "关键词")
			@RequestParam(name="keywords", defaultValue = "") 
			String keywords,
			@ApiParam(value = "标题检索类型，0模糊，1精确",defaultValue = "0")
			@RequestParam(name="titleType", defaultValue = "0") 
			int titleType,
			@ApiParam(value = "页码",defaultValue = "1")
			@RequestParam(name="page", defaultValue = "1") 
			int page,
			@ApiParam(value = "每页显示条数",defaultValue = "50")
			@RequestParam(name="limit", defaultValue = "50") 
			int limit,
			HttpServletRequest request){
		if(Func.isBlank(title)) {
			return new Resp().error("标题不能为空");
		}
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		
		SqlExpressionGroup exp = null;
		if(titleType==1) {
			exp = Cnd.exps("title", "=", title);
			exp.or("docno",  "=", keywords);
			if(Func.isNotBlank(keywords)) {
				exp.or("docno",  "=", keywords);
			}
		}else {
			exp = Cnd.exps("title", "like", "%"+title+"%");
			if(Func.isNotBlank(keywords)) {
				exp.or("docno",  "like", "%"+keywords+"%");
			}
		}
		
		cnd.and(exp);
				
		cnd.desc("istop");
		cnd.desc("hots");
		cnd.desc("pushdate");
		cnd.desc("createtime");
		Pager pager = dao.createPager(page, limit);
		FieldMatcher matcher = FieldMatcher.make(null, "^content|text_content$", false);
		List<ArticleInfo> list = dao.query(ArticleInfo.class, cnd,pager,matcher);
		pager.setRecordCount(dao.count(ArticleInfo.class, cnd));
		if(Strings.isNotBlank(title)) {
			String keywordsRed = "<red>"+title+"</red>";
			for(ArticleInfo art:list) {
				if(Func.isNotBlank(art.getTitle())) {					
					art.setTitle(art.getTitle().replace(title, keywordsRed));
				}
			}
		}
		if(list==null || list.size()<=0) {
			return new Resp().error("未检测到重复数据");
		}
		return new Resp().ok("获取成功").data(new QueryResult(list, pager));
	}

}
