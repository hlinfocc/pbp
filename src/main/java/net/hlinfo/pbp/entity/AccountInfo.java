package net.hlinfo.pbp.entity;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Func;

@ApiModel("账号信息基础类")
public class AccountInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Column("account")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="登录帐号")
	@ApiModelProperty(value="登录帐号")
	private String account;
	
	@Column("real_name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="姓名")
	@ApiModelProperty(value="姓名")
	private String realName;
	
	@Column("sex")
	@ColDefine(type = ColType.INT, width = 1)
	@Comment(value = "性别:[1男   0女]")
	@ApiModelProperty(value = "性别:[1男   0女]")
	private int sex;
	
	@Column("password")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="密码")
	@ApiModelProperty(value="密码(前端需sm2加密)")
	@JsonProperty(access = Access.WRITE_ONLY) //密码不发送到前端，但是前端可以传过来
	private String password;
	
	@Column("phone")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="手机号")
	@ApiModelProperty(value="手机号")
	@NotBlank(message = "手机号不能为空")
	private String phone;
	
	@Column("status")
	@ColDefine(notNull=false,type=ColType.INT, width=3,customType = "integer")
	@Comment(value="状态 0 启用 1禁用")
	@ApiModelProperty(value="状态 0 启用 1禁用")
	@Default("0")
	private int status;
	
	@Column("user_level")
	@ColDefine(notNull=false,type=ColType.INT, width=3,customType = "integer")
	@Comment(value="用户级别,默认为0")
	@ApiModelProperty(value="用户级别,默认为0")
	@Default("0")
	private int userLevel;
	
	@Column("user_type")
	@ColDefine(notNull=false,type=ColType.INT, width=3,customType = "integer")
	@Comment(value="用户类型,默认为0")
	@ApiModelProperty(value="用户类型,默认为0")
	@Default("0")
	private int userType;

	@Column("avatar")
	@ColDefine(type = ColType.TEXT)
	@Comment(value = "头像地址")
	@ApiModelProperty(value = "头像地址")
	private String avatar;
	
	@Column("last_login_time")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="上一次登陆时间")
	@ApiModelProperty(value="上一次登陆时间")
	private String lastLoginTime;
	
	@Column("last_login_ip")
	@ColDefine(type=ColType.VARCHAR, width=128)
	@Comment(value="上一次登陆ip")
	@ApiModelProperty(value="上一次登陆ip")
	private String lastLoginIp;
	
	@Column("that_login_time")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="这一次登陆时间")
	@ApiModelProperty(value="这一次登陆时间")
	private String thatLoginTime;
	
	@Column("that_login_ip")
	@ColDefine(type=ColType.VARCHAR, width=128)
	@Comment(value="这一次登陆ip")
	@ApiModelProperty(value="这一次登陆ip")
	private String thatLoginIp;
	
	@Column("pwd_modify_time")
	@ColDefine(notNull=false, type=ColType.DATETIME, width=25)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Comment(value="密码最后修改时间")
	@ApiModelProperty(value="密码最后修改时间")
	private Date pwdModifyTime;
	
	/**
	 * 更新上一次登录信息
	 * @param request request对象
	 */
	public void updateLoginInfo(HttpServletRequest request) {
		this.setLastLoginIp(this.getThatLoginIp());
		this.setLastLoginTime(this.getThatLoginTime());
		this.setThatLoginIp(Func.getIpAddr(request));
		this.setThatLoginTime(Func.Times.now());
		this.updated();
	}

	/**
	 *  Getter method for property <b>account</b>.
	 * @return property value of account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Setter method for property <b>account</b>.
	 *
	 * @param account value to be assigned to property account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 *  Getter method for property <b>realName</b>.
	 * @return property value of realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * Setter method for property <b>realName</b>.
	 *
	 * @param realName value to be assigned to property realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 *  Getter method for property <b>sex</b>.
	 * @return property value of sex
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * Setter method for property <b>sex</b>.
	 *
	 * @param sex value to be assigned to property sex
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 *  Getter method for property <b>password</b>.
	 * @return property value of password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter method for property <b>password</b>.
	 *
	 * @param password value to be assigned to property password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 *  Getter method for property <b>phone</b>.
	 * @return property value of phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Setter method for property <b>phone</b>.
	 *
	 * @param phone value to be assigned to property phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	 *  Getter method for property <b>userLevel</b>.
	 * @return property value of userLevel
	 */
	public int getUserLevel() {
		return userLevel;
	}

	/**
	 * Setter method for property <b>userLevel</b>.
	 *
	 * @param userLevel value to be assigned to property userLevel
	 */
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	/**
	 *  Getter method for property <b>userType</b>.
	 * @return property value of userType
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * Setter method for property <b>userType</b>.
	 *
	 * @param userType value to be assigned to property userType
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	/**
	 *  Getter method for property <b>avatar</b>.
	 * @return property value of avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Setter method for property <b>avatar</b>.
	 *
	 * @param avatar value to be assigned to property avatar
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 *  Getter method for property <b>lastLoginTime</b>.
	 * @return property value of lastLoginTime
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * Setter method for property <b>lastLoginTime</b>.
	 *
	 * @param lastLoginTime value to be assigned to property lastLoginTime
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 *  Getter method for property <b>lastLoginIp</b>.
	 * @return property value of lastLoginIp
	 */
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	/**
	 * Setter method for property <b>lastLoginIp</b>.
	 *
	 * @param lastLoginIp value to be assigned to property lastLoginIp
	 */
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	/**
	 *  Getter method for property <b>thatLoginTime</b>.
	 * @return property value of thatLoginTime
	 */
	public String getThatLoginTime() {
		return thatLoginTime;
	}

	/**
	 * Setter method for property <b>thatLoginTime</b>.
	 *
	 * @param thatLoginTime value to be assigned to property thatLoginTime
	 */
	public void setThatLoginTime(String thatLoginTime) {
		this.thatLoginTime = thatLoginTime;
	}

	/**
	 *  Getter method for property <b>thatLoginIp</b>.
	 * @return property value of thatLoginIp
	 */
	public String getThatLoginIp() {
		return thatLoginIp;
	}

	/**
	 * Setter method for property <b>thatLoginIp</b>.
	 *
	 * @param thatLoginIp value to be assigned to property thatLoginIp
	 */
	public void setThatLoginIp(String thatLoginIp) {
		this.thatLoginIp = thatLoginIp;
	}

	/**
	 *  Getter method for property <b>pwdModifyTime</b>.
	 * @return property value of pwdModifyTime
	 */
	public Date getPwdModifyTime() {
		return pwdModifyTime;
	}

	/**
	 * Setter method for property <b>pwdModifyTime</b>.
	 *
	 * @param pwdModifyTime value to be assigned to property pwdModifyTime
	 */
	public void setPwdModifyTime(Date pwdModifyTime) {
		this.pwdModifyTime = pwdModifyTime;
	}

}
