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

@Table(value = "role",prefix="pbp_")
@ApiModel("角色表")
@Comment("角色表")
public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column("name")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment(value="名称")
	@ApiModelProperty(value="名称")
	@NotBlank(message =  "名称不能为空")
	private String name;
	
	@Column("code")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment(value="角色代码")
	@ApiModelProperty(value="角色代码")
	@NotBlank(message =  "角色代码不能为空")
	private String code;
	
	
	@Column("level")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="角色级别： 0普通角色 1系统固定角色，禁止删除")
	@ApiModelProperty(value="角色级别： 0普通角色 1系统固定角色，禁止删除")
	private int level;
	
	
	@Column("status")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="状态  0 启用 1禁用")
	@ApiModelProperty(value="状态  0 启用 1禁用")
	@Default("0")
	private int status;
	
	@Column("pow")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="角色权重 用于角色分配管理，权重大的可以分配比它权重小的角色")
	@ApiModelProperty(value="角色权重 用于角色分配管理，权重大的可以分配比它权重小的角色")
	@Default("0")
	private int pow;
	
	@Column("route_path")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="登录成功后跳转的地址/路由")
	@ApiModelProperty(value="登录成功后跳转的地址/路由")
	private String routePath;

	@Column("create_id")
	@ColDefine(type=ColType.VARCHAR, width=32)
	@Comment(value="创建人ID")
	@ApiModelProperty(value="创建人ID")
	private String createId;
	
	@Column("remarks")
	@ColDefine(type=ColType.VARCHAR, width=500)
	@Comment(value="备注 也就是描述")
	@ApiModelProperty(value="备注 也就是描述")
	private String remarks;
	
	@Column("sort")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="排序")
	@ApiModelProperty(value="排序")
	private int sort;

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
	 *  Getter method for property <b>pow</b>.
	 * @return property value of pow
	 */
	public int getPow() {
		return pow;
	}

	/**
	 * Setter method for property <b>pow</b>.
	 *
	 * @param pow value to be assigned to property pow
	 */
	public void setPow(int pow) {
		this.pow = pow;
	}

	/**
	 *  Getter method for property <b>routePath</b>.
	 * @return property value of routePath
	 */
	public String getRoutePath() {
		return routePath;
	}

	/**
	 * Setter method for property <b>routePath</b>.
	 *
	 * @param routePath value to be assigned to property routePath
	 */
	public void setRoutePath(String routePath) {
		this.routePath = routePath;
	}

	/**
	 *  Getter method for property <b>createId</b>.
	 * @return property value of createId
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * Setter method for property <b>createId</b>.
	 *
	 * @param createId value to be assigned to property createId
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 *  Getter method for property <b>remarks</b>.
	 * @return property value of remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Setter method for property <b>remarks</b>.
	 *
	 * @param remarks value to be assigned to property remarks
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
