/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import java.util.ArrayList;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hlinfo
 *
 */
@ApiModel("获取用户收藏的文章数据参数")
public class ArticleCollectionParamVo {
	@ApiModelProperty("分类ID,格式为：[\"一级分类ID\",\"二级分类分类ID\",\"三级分类分类ID\",...]")
	private List<String> acids = new ArrayList<String>();
	
	@ApiModelProperty(value = "关键词")
	private String keywords;
	
	@ApiModelProperty(value = "状态：1草稿，2发布，3撤稿; 4归档;0全部",example = "0")
	private int status = 0;
	
	@ApiModelProperty(value = "页码",example = "1")
	private int page = 1;
	
	@ApiModelProperty(value = "每页显示条数",example = "20")
	private int limit = 20;

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
