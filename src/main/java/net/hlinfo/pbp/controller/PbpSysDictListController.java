package net.hlinfo.pbp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.json.Json;
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
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.entity.SysDictList;
import net.hlinfo.pbp.entity.SysDictType;
import net.hlinfo.pbp.opt.RedisKey;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.opt.vo.KV;
import net.hlinfo.pbp.service.DictService;

@Api(tags = "数据字典模块")
@RestController
@RequestMapping("/system/pbp/dict")
public class PbpSysDictListController {
	@Autowired
	private Dao dao;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private RedisUtils redisCache;

	@ApiOperation(value="系统数据字典-添加/编辑")
	@PostMapping("/addOrUpdate")
	public Resp<SysDictList> addOrUpdate(@Valid @RequestBody SysDictList data){
		
		if(data.getFieldType() < 1 || data.getFieldType() > 5) {
			return new Resp().error("字段类型不合法[1:普通值，2:单选框, 3:多选值, 4:富文本,5键值对]");
		}
		if(data.getFieldType() == 2 || data.getFieldType() == 3 || data.getFieldType() == 5) {
			try {
				Json.fromJsonAsList(KV.class, data.getFieldValue());
			}catch(Exception e) {
				return new Resp().error("填写的值不正确，格式为[{k, v}...]json字符串");
			}
		}
		
		SysDictType type = dao.fetch(SysDictType.class, data.getFieldTypeId());
		if(type == null) {
			return new Resp().error("字段类型不存在");
		}
		data.setFieldTypeName(type.getTypeName());
		Cnd c = Cnd.where("isdelete", "=", 0).and("field_code", "=", data.getFieldCode());
		if(Strings.isBlank(data.getId())) {
//			data.setId(Funs.genId());
//			data.setCreatetime(new Date());
//			data.setIsdelete(0);
		}else {
			c.and("id", "!=", data.getId());
		}
		int count = dao.count(SysDictList.class, c);
		if(count > 0) {
			return new Resp().error("该字段代码已被使用了");
		}
		//data.setUpdatetime(new Date());
		//data = dao.insertOrUpdate(data);

		SysDictList rs = (SysDictList)data.insertOrUpdate(dao);
		if(rs!=null) {
			redisCache.resetCacheData(RedisKey.SYSDICT+rs.getFieldCode(), rs);
		}
		return Resp.OBJ_O(data);
	}

	@ApiOperation(value = "系统数据字典-软删除")
	@DeleteMapping("/delete")
	public Resp delete(@RequestParam("id") String id
			, HttpServletRequest request) {
		if (Strings.isBlank(id)) {
			return new Resp().error("id不能为空");
		}
		SysDictList tea = dao.fetch(SysDictList.class, id);
		if (tea == null) {
			return new Resp().error("该数据已经被删除了");
		}
		tea.setIsdelete(1);
		int qty = dao.update(tea);
		if(qty>0) {
			redisCache.deleteObject(RedisKey.SYSDICT+tea.getFieldCode());
		}
		return Resp.OBJ_O(qty);
	}
	
	@ApiOperation("系统数据字典-列表")
	@GetMapping("/list")
	public Resp<SysDictList> list(@ApiParam("字段类型不合法[-100:不限, 1:普通值，2:单选框, 3:多选值]")@RequestParam(name="fieldType", defaultValue = "-100") int fieldType
			, @ApiParam("分类ID, 空全部")@RequestParam(name="typeid", defaultValue = "") String typeid
			, @ApiParam("页数")@RequestParam(name="page", defaultValue = "1") int page
			, @ApiParam("每页显示条数")@RequestParam(name="limit", defaultValue = "10") int limit){
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		if(fieldType != -100) {
			cnd.and("field_type", "=", fieldType);
		}
		if(Strings.isNotBlank(typeid)) {
			cnd.and("field_type_id", "=", typeid);
		}
		Pager pager = dao.createPager(page, limit);
		pager.setRecordCount(dao.count(SysDictList.class, cnd));
		List<SysDictList> list = dao.query(SysDictList.class, cnd.asc("field_code"), pager);
		List<NutMap> maps = new ArrayList<>();
		if(list != null) list.forEach(item -> {
			NutMap map = NutMap.WRAP(Json.toJson(item));
//			if(item.getFieldType() == 2 || item.getFieldType() == 3) {
//				map.put("fieldValue", Json.fromJsonAsList(NutMap.class, Json.toJson(item.getFieldValue())));
//			}
			maps.add(map);
		});
		return new Resp().ok("获取成功", new QueryResult(maps, pager));
	}
	

	@ApiOperation("系统数据字典-读取配置")
	@GetMapping("/getDict")
	public Resp<SysDictList> getConfig(@ApiParam("配置分类名称")@RequestParam(name="fieldTypeName", defaultValue = "") String fieldTypeName
			,@ApiParam("字段代码")@RequestParam(name="fieldCode", defaultValue = "") String fieldCode){
		if(Strings.isBlank(fieldCode)) {
			return new Resp().error("字段代码不能为空");
		}
		Cnd cnd=Cnd.where("isdelete", "=", 0);
		if(Strings.isNotBlank(fieldCode)){
			cnd.and("field_code", "=", fieldCode);
		}
		if(Strings.isNotBlank(fieldTypeName)){
			cnd.and("fieldTypeName","=",fieldTypeName);
		}
		SysDictList config = dao.fetch(SysDictList.class
				,cnd);
		NutMap map = NutMap.WRAP(Json.toJson(config));
		if(config.getFieldType() == 2 || config.getFieldType() == 3) {
			map.put("fieldValue", Json.fromJsonAsList(NutMap.class, Json.toJson(config.getFieldValue())));
		}
		return new Resp().ok("获取成功", map);
	}
	
//	@ApiOperation("读取配置(带MD5)")
//	@GetMapping("/getConfigMd5")
//	public Resp<SysDictList> getConfigMd5(@ApiParam("字段代码")@RequestParam(name="fieldCode", defaultValue = "") String fieldCode){
//		if(Strings.isBlank(fieldCode)) {
//			return new Resp().error("字段代码不能为空");
//		}
//		SysDictList config = dao.fetch(SysDictList.class
//				, Cnd.where("isdelete", "=", 0)
//				.and("field_code", "=", fieldCode));
//		NutMap map = NutMap.WRAP(Json.toJson(config));
//		if(config.getFieldType() == 2 || config.getFieldType() == 3) {
//			map.put("fieldValue", Json.fromJsonAsList(NutMap.class, Json.toJson(config.getFieldValue())));
//		}
//		map.put("md5", Lang.md5(Json.toJson(map)));
//		return new Resp().ok("获取成功", map);
//	}
	
	@ApiOperation("系统数据字典-读取字符串配置")
	@GetMapping("/getStrDict")
	public Resp getStrDict(@ApiParam("字段代码")@RequestParam(name="fieldCode", defaultValue = "") String fieldCode){
		if(Strings.isBlank(fieldCode)) {
			return new Resp().error("字段代码不能为空");
		}
		return new Resp().ok("获取成功", dictService.getStrDict(fieldCode));
	}
	
	@ApiOperation("系统数据字典-读取多个字符串配置-普通文本值")
	@GetMapping("/getStrDicts")
	public Resp getStrDicts(@ApiParam("字段代码,多个用逗号(,)好分割")@RequestParam(name="fieldCodes", defaultValue = "") String fieldCodes){
		if(Strings.isBlank(fieldCodes)) {
			return new Resp().error("字段代码不能为空");
		}
		return new Resp().ok("获取成功", dictService.getStrDicts(fieldCodes));
	}
	
	@ApiOperation("系统数据字典-读取数字配置")
	@GetMapping("/getIntDict")
	public Resp getIntConfig(@ApiParam("字段代码")@RequestParam(name="fieldCode", defaultValue = "") String fieldCode){
		if(Strings.isBlank(fieldCode)) {
			return new Resp().error("字段代码不能为空");
		}
		return new Resp().ok("获取成功", dictService.getIntDict(fieldCode));
	}
	
	@ApiOperation("系统数据字典-读取字符串配置")
	@GetMapping("/getDictKVList")
	public Resp<List<KV>> getKvConfig(@ApiParam("字段代码")@RequestParam(name="fieldCode", defaultValue = "") String fieldCode){
		if(Strings.isBlank(fieldCode)) {
			return new Resp().error("字段代码不能为空");
		}

		return new Resp().ok("获取成功"
			, dictService.getKvDicts(fieldCode));
	}
}

