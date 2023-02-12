package net.hlinfo.pbp.entity;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Func;
import net.hlinfo.pbp.opt.GenderConverter;

public class AccountInfo extends BaseEntity {
	@ExcelIgnore
	private static final long serialVersionUID = 1L;

	/**
	* 登录帐号
	*/
	@Column("account")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="登录帐号")
	@ApiModelProperty(value="登录帐号")
	@ExcelProperty("登录帐号")
	private String account;
	
	@Column("real_name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="姓名")
	@ApiModelProperty(value="姓名")
	@ExcelProperty("姓名")
	private String realName;
	
	@Column("sex")
	@ColDefine(type = ColType.INT, width = 1)
	@Comment(value = "性别:[1男   0女]")
	@ApiModelProperty(value = "性别:[1男   0女]")
	@ExcelProperty(value = "性别", converter = GenderConverter.class)
	private int sex;
	
	@Column("password")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="密码")
	@ApiModelProperty(value="密码(前端需sm2加密)")
	@JsonProperty(access = Access.WRITE_ONLY) //密码不发送到前端，但是前端可以传过来
	@ExcelProperty("密码")
	private String password;
	
	@Column("status")
	@ColDefine(notNull=false,type=ColType.INT, width=3,customType = "integer")
	@Comment(value="状态 0 启用 1禁用")
	@ApiModelProperty(value="状态 0 启用 1禁用")
	@Default("0")
	@ExcelIgnore
	private int status;
	
	@Column("user_level")
	@ColDefine(notNull=false,type=ColType.INT, width=3,customType = "integer")
	@Comment(value="用户级别")
	@ApiModelProperty(value="用户级别")
	@Default("0")
	@ExcelIgnore
	private int userLevel;
	
	@Column("user_type")
	@ColDefine(notNull=false,type=ColType.INT, width=3,customType = "integer")
	@Comment(value="用户类型")
	@ApiModelProperty(value="用户类型")
	@Default("0")
	@ExcelIgnore
	private int userType;

	@Column("avatar")
	@ColDefine(type = ColType.TEXT)
	@Comment(value = "头像地址")
	@ApiModelProperty(value = "头像地址")
	@ExcelIgnore
	private String avatar;
	
	@Column("last_login_time")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="上一次登陆时间")
	@ApiModelProperty(value="上一次登陆时间")
	@ExcelIgnore
	private String lastLoginTime;
	
	@Column("last_login_ip")
	@ColDefine(type=ColType.VARCHAR, width=128)
	@Comment(value="上一次登陆ip")
	@ApiModelProperty(value="上一次登陆ip")
	@ExcelIgnore
	private String lastLoginIp;
	
	@Column("that_login_time")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="这一次登陆时间")
	@ApiModelProperty(value="这一次登陆时间")
	@ExcelIgnore
	private String thatLoginTime;
	
	@Column("that_login_ip")
	@ColDefine(type=ColType.VARCHAR, width=128)
	@Comment(value="这一次登陆ip")
	@ApiModelProperty(value="这一次登陆ip")
	@ExcelIgnore
	private String thatLoginIp;

	public void updateLoginInfo(HttpServletRequest request) {
		this.setLastLoginIp(this.getThatLoginIp());
		this.setLastLoginTime(this.getThatLoginTime());
		this.setThatLoginIp(Func.getIpAddr(request));
		this.setThatLoginTime(Func.Times.now());
		this.updated();
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getThatLoginTime() {
		return thatLoginTime;
	}

	public void setThatLoginTime(String thatLoginTime) {
		this.thatLoginTime = thatLoginTime;
	}

	public String getThatLoginIp() {
		return thatLoginIp;
	}

	public void setThatLoginIp(String thatLoginIp) {
		this.thatLoginIp = thatLoginIp;
	}
}
