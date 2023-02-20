/**
 * 
 */
package net.hlinfo.pbp.service;

import java.math.BigDecimal;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.entity.SysDictList;
import net.hlinfo.pbp.opt.RedisKey;
import net.hlinfo.pbp.opt.vo.KV;


/**
 * @author hlinfo
 *
 */
@Service
public class DictService {
	@Autowired
	private Dao dao;
	
	@Autowired
	private RedisUtils redisCache;
	
	/**
	 * 获取数据字典信息
	 * @param fieldCode 字段代码
	 * @return 数据字典对象
	 */
	public SysDictList getDict(String fieldCode){
		SysDictList dict = redisCache.getObject(RedisKey.SYSDICT+fieldCode);
		if(dict == null) {
			dict = dao.fetch(SysDictList.class
				, Cnd.where("isdelete", "=", 0)
				.and("field_code", "=", fieldCode));
			redisCache.setObject(RedisKey.SYSDICT+fieldCode, dict);
		}
		return dict;
	}
	/**
	 * 获取数据字典字段值
	 * @param fieldCode 字段代码
	 * @return 字段值
	 */
	public String getStrDict(String fieldCode) {
		SysDictList dict = redisCache.getObject(RedisKey.SYSDICT+fieldCode);
		if(dict == null) {
			dict = dao.fetch(SysDictList.class
				, Cnd.where("isdelete", "=", 0)
				.and("field_code", "=", fieldCode));
			redisCache.setObject(RedisKey.SYSDICT+fieldCode, dict);
		}
		return dict != null && dict.getFieldType() == 1 ? dict.getFieldValue() : "";
	}
	/**
	 * 获取数据字典字段值集合
	 * @param fieldCode 字段代码,多个用,逗号隔开
	 * @return 字段值集合
	 */
	public NutMap getStrDicts(String fieldCodes) {
		if(Func.isBlank(fieldCodes)) {
			return null;
		}
		NutMap map = NutMap.NEW();
		String[] fieldCodeArr = fieldCodes.split(",");
		for(int i = 0; i < fieldCodeArr.length; i ++) {
			String str = getStrDict(fieldCodeArr[i]);
			map.addv(fieldCodeArr[i], str);
		}
		return map;
	}
	/**
	 * 获取数据字典整型字段值
	 * @param fieldCode 字段代码
	 * @return 整型字段值
	 */
	public int getIntDict(String fieldCode) {
		SysDictList dict = redisCache.getObject(RedisKey.SYSDICT+fieldCode);
		if(dict == null) {
			dict = dao.fetch(SysDictList.class
				, Cnd.where("isdelete", "=", 0)
				.and("field_code", "=", fieldCode));
			redisCache.setObject(RedisKey.SYSDICT+fieldCode, dict);
		}
		String v = dict != null && dict.getFieldType() == 1 ? dict.getFieldValue() : "";
		if(Func.isNotBlank(v)) {
			try {
				return Func.string2int(v);
			}catch(Exception e) {
				
			}
		}
		return 0;
	}
	/**
	 * 获取数据字典整型字段值
	 * @param fieldCode 字段代码
	 * @return 整型字段值
	 */
	public long getLongDict(String fieldCode) {
		SysDictList dict = redisCache.getObject(RedisKey.SYSDICT+fieldCode);
		if(dict == null) {
			dict = dao.fetch(SysDictList.class
					, Cnd.where("isdelete", "=", 0)
					.and("field_code", "=", fieldCode));
			redisCache.setObject(RedisKey.SYSDICT+fieldCode, dict);
		}
		String v = dict != null && dict.getFieldType() == 1 ? dict.getFieldValue() : "";
		if(Func.isNotBlank(v)) {
			try {
				return Func.string2Long(v);
			}catch(Exception e) {
				
			}
		}
		return 0;
	}
	/**
	 * 获取数据字典BigDeciaml型字段值
	 * @param fieldCode 字段代码
	 * @return BigDeciaml型字段值
	 */
	public BigDecimal getBigDeciamlDict(String fieldCode) {
		SysDictList dict = redisCache.getObject(RedisKey.SYSDICT+fieldCode);
		if(dict == null) {
			dict = dao.fetch(SysDictList.class
				, Cnd.where("isdelete", "=", 0)
				.and("field_code", "=", fieldCode));
			redisCache.setObject(RedisKey.SYSDICT+fieldCode, dict);
		}
		String v = dict != null && dict.getFieldType() == 1 ? dict.getFieldValue() : "";
		if(Func.isNotBlank(v)) {
			try {
				return new BigDecimal(v);
			}catch(Exception e) {
				
			}
		}
		return new BigDecimal(0);
	}
	/**
	 * 获取数据字典字段值并转化为list,仅限2单选[k, v]值，3多选[k, v]值,5键值对[{k:'',v:''}]
	 * @param fieldCode 字段代码
	 * @return <pre>List&lt;KV&gt;型字段值</pre>
	 */
	public List<KV> getKvDicts(String fieldCode){
		SysDictList dict = redisCache.getObject(RedisKey.SYSDICT+fieldCode);
		if(dict == null) {
			dict = dao.fetch(SysDictList.class
				, Cnd.where("isdelete", "=", 0)
				.and("field_code", "=", fieldCode));
			redisCache.setObject(RedisKey.SYSDICT+fieldCode, dict);
		}
		if(dict == null) return null;
		if(dict.getFieldType() == 2 || dict.getFieldType() == 3 || dict.getFieldType() == 5) {
			return Jackson.toList(dict.getFieldValue(), KV.class);
		}else {
			return null;
		}
	}
}
