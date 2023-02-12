package net.hlinfo.pbp.entity;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.pbp.usr.adaptor.JsonbToListValueAdaptor;

@Table(value = "role_permission",prefix="pbp")
@ApiModel("角色权限")
@Comment("角色权限")
public class RolePermission extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column("roleid")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=32)
	@Comment(value="角色ID")
	private String roleid;
	
	@Column("permid")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=32)
	@Comment(value="权限ID")
	private String permid;
	
	@Column("has_btns")
	@ColDefine(type=ColType.PSQL_JSON,customType = "jsonb", adaptor = JsonbToListValueAdaptor.class)
	@Comment(value="该角色拥有的菜单按钮")
	@ApiModelProperty("该角色拥有的菜单按钮")
	private List<String> hasBtns;

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getPermid() {
		return permid;
	}

	public void setPermid(String permid) {
		this.permid = permid;
	}

	public List<String> getHasBtns() {
		return hasBtns;
	}

	public void setHasBtns(List<String> hasBtns) {
		this.hasBtns = hasBtns;
	}

}
