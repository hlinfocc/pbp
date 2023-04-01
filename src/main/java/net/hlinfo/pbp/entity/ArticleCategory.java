package net.hlinfo.pbp.entity;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "article_category",prefix = "pbp_")
@Comment("通用文章分类表")
@ApiModel("通用文章分类表")
@TableIndexes({
	@Index(fields = {"code"},unique=true)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleCategory extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Column("pid")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="父级ID,一级为0")
	@ApiModelProperty(value="父级ID,一级为0")
	@Default(value="0")
	private String pid;
	
	@Column("name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="类别名称")
	@NotBlank(message = "类别名称不能为空")
	@ApiModelProperty(value="类别名称")
	private String name;
	
	@Column("code")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="类别代码,二级分类code前自动加上一级code,如：FAQ表示常见问题,FAQ-PAY表示常见问题下的支付")
	@NotBlank(message = "类别代码不能为空")
	@ApiModelProperty(value="类别代码,如：FAQ表示常见问题")
	private String code;
	
	@Column("form_template_type")
	@ColDefine(type=ColType.INT)
	@Comment(value="表单模板:0普通")
	@ApiModelProperty(value="表单模板:0普通")
	private int formTemplateType;
	
	@Column("sort")
	@ColDefine(type=ColType.INT)
	@Comment(value="显示排序")
	@ApiModelProperty(value="显示排序")
	@Default(value="0")
	private int sort;
	
	@Column("status")
	@ColDefine(type=ColType.INT)
	@Comment(value="状态：0启用 1禁用")
	@ApiModelProperty(value="状态：0启用 1禁用")
	@Default(value="0")
	private int status;
	
	@Column("isdisplay")
	@ColDefine(type=ColType.INT)
	@Comment(value="是否在导航显示 0不显示，1显示")
	@ApiModelProperty(value="是否在导航显示 0不显示，1显示")
	@Default(value="0")
	private int isdisplay;
	
	@Column("locked")
	@ColDefine(type=ColType.INT)
	@Comment(value="是否锁定：0否 1锁定（锁定后不能删除，修改类别代码）")
	@ApiModelProperty(value="是否锁定：0否 1锁定（锁定后不能删除，修改类别代码）")
	@Default(value="0")
	private int locked;
	
	@Column("single_content")
	@ColDefine(type=ColType.INT)
	@Comment(value="是否单篇正文：0否 1是（单篇正文只能有一篇发布状态文章）")
	@ApiModelProperty(value="是否单篇正文：0否 1是（单篇正文只能有一篇发布状态文章）")
	@Default(value="0")
	private int singleContent;
	
	@ApiModelProperty(value="【查询用】子分类")
	private List<ArticleCategory> children;

	/**
	 *  Getter method for property <b>isdisplay</b>.
	 * @return property value of isdisplay
	 */
	public int getIsdisplay() {
		return isdisplay;
	}

	/**
	 * Setter method for property <b>isdisplay</b>.
	 *
	 * @param isdisplay value to be assigned to property isdisplay
	 */
	public void setIsdisplay(int isdisplay) {
		this.isdisplay = isdisplay;
	}

	/**
	 *  Getter method for property <b>pid</b>.
	 * @return property value of pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * Setter method for property <b>pid</b>.
	 *
	 * @param pid value to be assigned to property pid
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 *  Getter method for property <b>name</b>.
	 * @return property value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for property <b>name</b>.
	 *
	 * @param name value to be assigned to property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *  Getter method for property <b>code</b>.
	 * @return property value of code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Setter method for property <b>code</b>.
	 *
	 * @param code value to be assigned to property code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	

	/**
	 *  Getter method for property <b>formTemplateType</b>.
	 * @return property value of formTemplateType
	 */
	public int getFormTemplateType() {
		return formTemplateType;
	}

	/**
	 * Setter method for property <b>formTemplateType</b>.
	 *
	 * @param formTemplateType value to be assigned to property formTemplateType
	 */
	public void setFormTemplateType(int formTemplateType) {
		this.formTemplateType = formTemplateType;
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
	 *  Getter method for property <b>locked</b>.
	 * @return property value of locked
	 */
	public int getLocked() {
		return locked;
	}

	/**
	 * Setter method for property <b>locked</b>.
	 *
	 * @param locked value to be assigned to property locked
	 */
	public void setLocked(int locked) {
		this.locked = locked;
	}

	/**
	 *  Getter method for property <b>singleContent</b>.
	 * @return property value of singleContent
	 */
	public int getSingleContent() {
		return singleContent;
	}

	/**
	 * Setter method for property <b>singleContent</b>.
	 *
	 * @param singleContent value to be assigned to property singleContent
	 */
	public void setSingleContent(int singleContent) {
		this.singleContent = singleContent;
	}

	/**
	 *  Getter method for property <b>children</b>.
	 * @return property value of children
	 */
	public List<ArticleCategory> getChildren() {
		return children;
	}

	/**
	 * Setter method for property <b>children</b>.
	 *
	 * @param children value to be assigned to property children
	 */
	public void setChildren(List<ArticleCategory> children) {
		this.children = children;
	}
}
