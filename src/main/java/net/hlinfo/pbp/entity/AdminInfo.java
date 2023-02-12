package net.hlinfo.pbp.entity;


import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value="admin_info",prefix="pbp")
@ApiModel("管理员信息")
@Comment("管理员信息")
@TableIndexes({
	@Index(fields = {"account"}, unique = true)
})
public class AdminInfo extends AccountInfo{
	@ExcelIgnore
	private static final long serialVersionUID = 1L;
	
	@Column("phone")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="手机号")
	@ApiModelProperty(value="手机号")
	@NotBlank(message = "手机号不能为空")
	@ExcelProperty("手机号")
	private String phone;
	
	@Column("dep_name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="所在部门")
	@ApiModelProperty(value="所在部门")
	@ExcelProperty("所在部门")
	private String depName;
	
	@Column("dep_id")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="所在部门ID")
	@ApiModelProperty(value="所在部门ID")
	@ExcelIgnore
	private String depId;
	
    @Column("email")
    @ColDefine(type = ColType.TEXT)
    @Comment(value = "电子邮件")
    @ApiModelProperty(value = "电子邮件")
    @ExcelProperty("电子邮件")
    private String email;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
