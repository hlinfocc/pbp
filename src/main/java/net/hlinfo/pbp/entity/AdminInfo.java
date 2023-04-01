package net.hlinfo.pbp.entity;


import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value="admin_info",prefix="pbp_")
@ApiModel("管理员信息")
@Comment("管理员信息")
@TableIndexes({
	@Index(fields = {"account"}, unique = true)
})
public class AdminInfo extends AccountInfo{
	private static final long serialVersionUID = 1L;
	
	@Column("dep_name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="所在部门")
	@ApiModelProperty(value="所在部门")
	private String depName;
	
	@Column("dep_id")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="所在部门ID")
	@ApiModelProperty(value="所在部门ID")
	private String depId;
	
    @Column("email")
    @ColDefine(type = ColType.TEXT)
    @Comment(value = "电子邮件")
    @ApiModelProperty(value = "电子邮件")
    private String email;

	/**
	 *  Getter method for property <b>depName</b>.
	 * @return property value of depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * Setter method for property <b>depName</b>.
	 *
	 * @param depName value to be assigned to property depName
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 *  Getter method for property <b>depId</b>.
	 * @return property value of depId
	 */
	public String getDepId() {
		return depId;
	}

	/**
	 * Setter method for property <b>depId</b>.
	 *
	 * @param depId value to be assigned to property depId
	 */
	public void setDepId(String depId) {
		this.depId = depId;
	}

	/**
	 *  Getter method for property <b>email</b>.
	 * @return property value of email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter method for property <b>email</b>.
	 *
	 * @param email value to be assigned to property email
	 */
	public void setEmail(String email) {
		this.email = email;
	}


}
