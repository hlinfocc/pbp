package net.hlinfo.pbp.entity;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;

@Table(value = "admin_role",prefix="pbp_")
@ApiModel("管理员角色")
@Comment("管理员角色")
public class AdminRole extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column("role_id")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=32)
	@Comment(value="角色ID")
	private String roleId;
	
	@Column("admin_id")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=32)
	@Comment(value="管理员ID")
	private String adminId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

}
