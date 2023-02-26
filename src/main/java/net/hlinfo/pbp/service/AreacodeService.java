package net.hlinfo.pbp.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.Static;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import net.hlinfo.opt.Jackson;
import net.hlinfo.pbp.entity.Areacode;

@Service
public class AreacodeService {
	@Autowired
	private Dao dao;
	
	public List<Areacode> getProv() {
		List<Areacode> list = dao.query(Areacode.class, Cnd.where("parent_code", "=", "1").asc("code"));
		return list;
	}
	

	/**
	 * 根据地址码获取地址名称
	 * @param areaCode
	 * @return
	 */
	public String getAreatitle(String areaCode) {
		Areacode area = dao.fetch(Areacode.class
				, Cnd.where("areaCode", "=", areaCode));
		if(area == null) {
			return "";
		}else {
			return area.getAreaTitle();
		}
	}
	
	/**
	 * 根据地址码获取地址名称
	 * @param areaCode
	 * @return
	 */
	public Areacode getAreacode(String areaCode) {
		Areacode area = dao.fetch(Areacode.class
				, Cnd.where("areaCode", "=", areaCode));
		return area;
	}
	
	/**
	 * 根据地址码获取地址名称
	 * @param areaCode
	 * @return
	 */
	public String[] getAreacode(String[] areaCode) {
		if(areaCode == null) {
			return null;
		}
		String[] areaName = new String[areaCode.length];
		for(int i = 0; i < areaCode.length; i ++) {
			areaName[i] = getAreatitle(areaCode[i]);
		}
		return areaName;
	}
	
	/**
	 * 根据父地址码和地址名称获取地址代码
	 * @param pAreaCode
	 * @param title
	 * @return
	 */
	public String getAreacode(String pAreaCode, String title) {
		// TODO Auto-generated method stub
		Areacode areacode = dao.fetch(Areacode.class
				, Cnd.where("areaParent", "=", pAreaCode)
				.and("areaTitle", "=", title));
		if(areacode != null) {
			return areacode.getAreaCode();
		}else {
			return "";
		}
	}
	
	/**
	 * 根据父地址码和地址名称反响获取地址代码
	 * @param pAreaCode 父级
	 * @param title 比如贵州省 -> 那去数据库里查贵州的，
	 * @return
	 */
	public String getAreacodeFLike(String pAreaCode, String title) {
		// TODO Auto-generated method stub
		Cnd cnd = Cnd.where("areaParent", "=", pAreaCode)
			.and(new Static("'" + title + "' like concat('%', area_name, '%')"));
		Areacode areacode = dao.fetch(Areacode.class
			, cnd);
		if(areacode != null) {
			return areacode.getAreaCode();
		}else {
			return "";
		}
	}
	
	/**
	 * 根据经纬度获取省市区地址
	 * @param key
	 * @param lng
	 * @param latpublic final Logger log = LoggerFactory.getLogger(
	 * @return map={
	 * 	success: true, //成功是否
	 * 	msg: '消息',
	 * 	data: {
	 * 		province: '',
	 * 		city: '',
	 * 		county: ''
	 * }
	 */
	public NutMap getAddrByLnglat(String key, String lng, String lat){
		String url = "https://restapi.amap.com/v3/geocode/regeo?";
		url += "key=" + key;
		url += "&location="+lng+"," + lat;
		Response resp = Http.get(url);
		NutMap map = Json.fromJson(NutMap.class, resp.getContent("utf-8"));
		try {
			if(map.getInt("status") == 0) {
				return NutMap.NEW().addv("success", false)
					.addv("msg", map.getString("info"));
			}else {
				NutMap ac = map.getAs("regeocode", NutMap.class)
						.getAs("addressComponent", NutMap.class);
				String province = ac.getString("province", "");
				String city = "";
				try {
					city = ac.getString("city", "");
				}catch(Exception e) {
					//直辖市有问题
					city = "市辖区";
				}
				String district = ac.getString("district", "");
				
				String detailAddr;
				try {
					detailAddr = map.getAs("regeocode", NutMap.class)
							.getString("formatted_address", "");
					detailAddr = detailAddr.replace(province + city + district, "");
				}catch(Exception e) {
					detailAddr = "";
				}
				
				NutMap res = NutMap.NEW()
					.addv("province", province)
					.addv("city", city)
					.addv("county", district)
					.addv("detailAddr", detailAddr);
				return NutMap.NEW().addv("success", true)
						.addv("msg", map.getString("info"))
						.addv("data", res);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return NutMap.NEW().addv("success", false)
					.addv("msg", "[" + map.getString("info") + "] - [" + e.getMessage() + "]");
		}
	}
	/**
	 * 逆地址编码——腾讯地图
	 @param key 地图API key
	 @param lng 经度
	 @param lat 纬度
	 @return
	 */
	public NutMap geocoderByQQMap(String key, String lng, String lat){
		String url = "https://apis.map.qq.com/ws/geocoder/v1/?";
		url += "key=" + key;
		url += "&location="+lat+"," + lng;
		url += "&get_poi=0";
		Response resp = Http.get(url);
		JsonNode json = Jackson.toJsonObject(resp.getContent("utf-8"));
		try {
			if(json.get("status").asInt() != 0) {
				return NutMap.NEW().addv("success", false)
					.addv("msg", json.get("message").asText());
			}else {
				NutMap res = NutMap.NEW()
					.addv("location", json.findPath("location"))
					.addv("address", json.findPath("address"))
					.addv("addressComponent", json.findPath("address_component"))
					.addv("adInfo", json.findPath("ad_info"));
				return NutMap.NEW().addv("success", true)
						.addv("msg", json.get("message").asText())
						.addv("data", res);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return NutMap.NEW().addv("success", false)
					.addv("msg", "[" + json.get("message").asText() + "] - [" + e.getMessage() + "]");
		}
	}
}
