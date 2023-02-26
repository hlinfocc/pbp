/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hlinfo
 *
 */
@ApiModel("文章查询参数")
public class ArticleParams {
	@ApiModelProperty("文章分类ID列表，['一级分类','二级级分类','三级分类',...]")
	private List<String> acids;
	
	@ApiModelProperty(value = "关键词")
	private String keywords;
	
	@ApiModelProperty(value = "搜索字段")
	private List<String> searchField;
	
	@ApiModelProperty(value="[1-4]=>[国家级，省, 市/区, 县]")
	private int level;
	
	@ApiModelProperty(value = "省代码")
	private String provincecode;
	
	@ApiModelProperty(value = "市州代码")
	private String citycode;
	
	@ApiModelProperty(value = "区县代码")
	private String countycode;
	
	@ApiModelProperty(value = "地区筛选")
	private List<KV> areaKvs;
	
	@ApiModelProperty(value = "排序字段：pushdate,createtime")
	private String orderby;
	
	@ApiModelProperty(value = "排序方式：desc,asc")
	private String ordertype;
	
	@ApiModelProperty(value = "状态：1草稿，2发布，3撤稿, 4归档,0全部")
	private int status;
	
	@ApiModelProperty(value = "排除的状态：1草稿，2发布，3撤稿, 4归档,0不排除")
	private int nostatus;
	
	@ApiModelProperty(value = "发文年度")
	private int pushYear;
	
	@ApiModelProperty(value = "发文年度")
	private List<Integer> pushYears;
	
	@ApiModelProperty(value = "发文开始时间")
	private String pushStartDate;
	
	@ApiModelProperty(value = "发文结束时间")
	private String pushEndDate;
	
	@ApiModelProperty(value = "审核状态，0未审核，1通过，2不通过,-1全部")
	private int auditStatus = 1;
	
	@ApiModelProperty(value = "是否置顶：0不置顶 1置顶,-1全部")
	private int istop = -1;
	
	@ApiModelProperty(value = "是否热门：0正常 1热门,-1全部")
	private int hots = -1;
	
	@ApiModelProperty(value = "是否查询已读未读信息：0不查询 1查询(需登录方可)")
	private int queryIsRead = 0;
	
	@ApiModelProperty(value = "页码")
	private int page = 1;
	
	@ApiModelProperty(value = "每页显示条数")
	private int limit =20;

	/**
	 *  Getter method for property <b>queryIsRead</b>.
	 * @return property value of queryIsRead
	 */
	public int getQueryIsRead() {
		return queryIsRead;
	}

	/**
	 * Setter method for property <b>queryIsRead</b>.
	 *
	 * @param queryIsRead value to be assigned to property queryIsRead
	 */
	public void setQueryIsRead(int queryIsRead) {
		this.queryIsRead = queryIsRead;
	}

	/**
	 *  Getter method for property <b>acids</b>.
	 * @return property value of acids
	 */
	public List<String> getAcids() {
		return acids;
	}

	/**
	 * Setter method for property <b>acids</b>.
	 *
	 * @param acids value to be assigned to property acids
	 */
	public void setAcids(List<String> acids) {
		this.acids = acids;
	}

	/**
	 *  Getter method for property <b>keywords</b>.
	 * @return property value of keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Setter method for property <b>keywords</b>.
	 *
	 * @param keywords value to be assigned to property keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 *  Getter method for property <b>searchField</b>.
	 * @return property value of searchField
	 */
	public List<String> getSearchField() {
		return searchField;
	}

	/**
	 * Setter method for property <b>searchField</b>.
	 *
	 * @param searchField value to be assigned to property searchField
	 */
	public void setSearchField(List<String> searchField) {
		this.searchField = searchField;
	}

	/**
	 *  Getter method for property <b>level</b>.
	 * @return property value of level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Setter method for property <b>level</b>.
	 *
	 * @param level value to be assigned to property level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 *  Getter method for property <b>provincecode</b>.
	 * @return property value of provincecode
	 */
	public String getProvincecode() {
		return provincecode;
	}

	/**
	 * Setter method for property <b>provincecode</b>.
	 *
	 * @param provincecode value to be assigned to property provincecode
	 */
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	/**
	 *  Getter method for property <b>citycode</b>.
	 * @return property value of citycode
	 */
	public String getCitycode() {
		return citycode;
	}

	/**
	 * Setter method for property <b>citycode</b>.
	 *
	 * @param citycode value to be assigned to property citycode
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	/**
	 *  Getter method for property <b>countycode</b>.
	 * @return property value of countycode
	 */
	public String getCountycode() {
		return countycode;
	}

	/**
	 * Setter method for property <b>countycode</b>.
	 *
	 * @param countycode value to be assigned to property countycode
	 */
	public void setCountycode(String countycode) {
		this.countycode = countycode;
	}

	/**
	 *  Getter method for property <b>areaKvs</b>.
	 * @return property value of areaKvs
	 */
	public List<KV> getAreaKvs() {
		return areaKvs;
	}

	/**
	 * Setter method for property <b>areaKvs</b>.
	 *
	 * @param areaKvs value to be assigned to property areaKvs
	 */
	public void setAreaKvs(List<KV> areaKvs) {
		this.areaKvs = areaKvs;
	}

	/**
	 *  Getter method for property <b>orderby</b>.
	 * @return property value of orderby
	 */
	public String getOrderby() {
		return orderby;
	}

	/**
	 * Setter method for property <b>orderby</b>.
	 *
	 * @param orderby value to be assigned to property orderby
	 */
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	/**
	 *  Getter method for property <b>ordertype</b>.
	 * @return property value of ordertype
	 */
	public String getOrdertype() {
		return ordertype;
	}

	/**
	 * Setter method for property <b>ordertype</b>.
	 *
	 * @param ordertype value to be assigned to property ordertype
	 */
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	/**
	 *  Getter method for property <b>status</b>.
	 * @return property value of status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Setter method for property <b>status</b>.
	 *
	 * @param status value to be assigned to property status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 *  Getter method for property <b>nostatus</b>.
	 * @return property value of nostatus
	 */
	public int getNostatus() {
		return nostatus;
	}

	/**
	 * Setter method for property <b>nostatus</b>.
	 *
	 * @param nostatus value to be assigned to property nostatus
	 */
	public void setNostatus(int nostatus) {
		this.nostatus = nostatus;
	}

	/**
	 *  Getter method for property <b>pushYear</b>.
	 * @return property value of pushYear
	 */
	public int getPushYear() {
		return pushYear;
	}

	/**
	 * Setter method for property <b>pushYear</b>.
	 *
	 * @param pushYear value to be assigned to property pushYear
	 */
	public void setPushYear(int pushYear) {
		this.pushYear = pushYear;
	}

	/**
	 *  Getter method for property <b>pushYears</b>.
	 * @return property value of pushYears
	 */
	public List<Integer> getPushYears() {
		return pushYears;
	}

	/**
	 * Setter method for property <b>pushYears</b>.
	 *
	 * @param pushYears value to be assigned to property pushYears
	 */
	public void setPushYears(List<Integer> pushYears) {
		this.pushYears = pushYears;
	}

	/**
	 *  Getter method for property <b>pushStartDate</b>.
	 * @return property value of pushStartDate
	 */
	public String getPushStartDate() {
		return pushStartDate;
	}

	/**
	 * Setter method for property <b>pushStartDate</b>.
	 *
	 * @param pushStartDate value to be assigned to property pushStartDate
	 */
	public void setPushStartDate(String pushStartDate) {
		this.pushStartDate = pushStartDate;
	}

	/**
	 *  Getter method for property <b>pushEndDate</b>.
	 * @return property value of pushEndDate
	 */
	public String getPushEndDate() {
		return pushEndDate;
	}

	/**
	 * Setter method for property <b>pushEndDate</b>.
	 *
	 * @param pushEndDate value to be assigned to property pushEndDate
	 */
	public void setPushEndDate(String pushEndDate) {
		this.pushEndDate = pushEndDate;
	}

	/**
	 *  Getter method for property <b>auditStatus</b>.
	 * @return property value of auditStatus
	 */
	public int getAuditStatus() {
		return auditStatus;
	}

	/**
	 * Setter method for property <b>auditStatus</b>.
	 *
	 * @param auditStatus value to be assigned to property auditStatus
	 */
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	/**
	 *  Getter method for property <b>istop</b>.
	 * @return property value of istop
	 */
	public int getIstop() {
		return istop;
	}

	/**
	 * Setter method for property <b>istop</b>.
	 *
	 * @param istop value to be assigned to property istop
	 */
	public void setIstop(int istop) {
		this.istop = istop;
	}

	/**
	 *  Getter method for property <b>hots</b>.
	 * @return property value of hots
	 */
	public int getHots() {
		return hots;
	}

	/**
	 * Setter method for property <b>hots</b>.
	 *
	 * @param hots value to be assigned to property hots
	 */
	public void setHots(int hots) {
		this.hots = hots;
	}

	/**
	 *  Getter method for property <b>page</b>.
	 * @return property value of page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Setter method for property <b>page</b>.
	 *
	 * @param page value to be assigned to property page
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 *  Getter method for property <b>limit</b>.
	 * @return property value of limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Setter method for property <b>limit</b>.
	 *
	 * @param limit value to be assigned to property limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
