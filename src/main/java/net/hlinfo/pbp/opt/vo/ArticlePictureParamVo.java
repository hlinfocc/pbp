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
@ApiModel("标题图|焦点图列表数据参数")
public class ArticlePictureParamVo {
	@ApiModelProperty("分类ID,格式为：[\"一级分类ID\",\"二级分类分类ID\",\"三级分类分类ID\",...]")
	private List<String> acids = new ArrayList<String>();
	
	@ApiModelProperty(value = "类型：1标题图，2焦点图",example = "1")
	private int pictureType = 1;
	
	@ApiModelProperty(value = "页码",example = "1")
	private int page = 1;
	
	@ApiModelProperty(value = "每页显示条数",example = "10")
	private int limit = 10;

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
	 *  Getter method for property <b>pictureType</b>.
	 * @return property value of pictureType
	 */
	public int getPictureType() {
		return pictureType;
	}

	/**
	 * Setter method for property <b>pictureType</b>.
	 *
	 * @param pictureType value to be assigned to property pictureType
	 */
	public void setPictureType(int pictureType) {
		this.pictureType = pictureType;
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
