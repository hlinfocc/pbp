package net.hlinfo.pbp.entity;


import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "img_manager",prefix="pbp_")
@Comment("图片管理")
@ApiModel("图片管理")
public class ImgManager extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Column("title")
	@ColDefine(type=ColType.VARCHAR,width = 255)
	@Comment(value="标题")
	@ApiModelProperty(value="标题")
	@NotBlank(message = "标题不能为空")
	private String title;
	
	@Column("imgurl")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="图片地址")
	@ApiModelProperty(value="图片地址")
	@NotBlank(message = "图片地址不能为空")
	private String imgurl;

	@Column("linkurl")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="跳转地址")
	@ApiModelProperty(value="跳转地址")
	private String linkurl;
	
	@Column("imgtype")
	@ColDefine(type=ColType.INT,customType = "integer")
	@Comment(value="类型：0首页轮播图")
	@ApiModelProperty(value="类型：0首页轮播图")
	@Default("0")
	private int imgtype;
	
	@Column("status")
	@ColDefine(type=ColType.INT,customType = "integer")
	@Comment(value="状态：0显示，1不显示")
	@ApiModelProperty(value="状态：0显示，1不显示")
	@Default("0")
	private int status;
	
	@Column("sort")
	@ColDefine(type=ColType.INT,customType = "integer")
	@Comment(value="排序")
	@ApiModelProperty(value="排序")
	@Default("0")
	private int sort;

	/**
	 *  Getter method for property <b>title</b>.
	 * @return property value of title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for property <b>title</b>.
	 *
	 * @param title value to be assigned to property title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *  Getter method for property <b>imgurl</b>.
	 * @return property value of imgurl
	 */
	public String getImgurl() {
		return imgurl;
	}

	/**
	 * Setter method for property <b>imgurl</b>.
	 *
	 * @param imgurl value to be assigned to property imgurl
	 */
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	/**
	 *  Getter method for property <b>linkurl</b>.
	 * @return property value of linkurl
	 */
	public String getLinkurl() {
		return linkurl;
	}

	/**
	 * Setter method for property <b>linkurl</b>.
	 *
	 * @param linkurl value to be assigned to property linkurl
	 */
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	/**
	 *  Getter method for property <b>imgtype</b>.
	 * @return property value of imgtype
	 */
	public int getImgtype() {
		return imgtype;
	}

	/**
	 * Setter method for property <b>imgtype</b>.
	 *
	 * @param imgtype value to be assigned to property imgtype
	 */
	public void setImgtype(int imgtype) {
		this.imgtype = imgtype;
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
	 *  Getter method for property <b>sort</b>.
	 * @return property value of sort
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * Setter method for property <b>sort</b>.
	 *
	 * @param sort value to be assigned to property sort
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}