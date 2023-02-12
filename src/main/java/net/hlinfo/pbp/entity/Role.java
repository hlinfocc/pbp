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

@Table(value = "role",prefix="pbp")
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPow() {
		return pow;
	}

	public void setPow(int pow) {
		this.pow = pow;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
