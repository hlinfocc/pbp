/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

/**
 * 修改密码参数
 * @author hlinfo
 */
public class ResetPwdParam {
	@ApiModelProperty("用户ID")
	@NotEmpty(message = "用户ID不能为空")
	private String id;
	
	@ApiModelProperty("旧密码(需sm2加密)")
	private String oldPwd;
	
	@ApiModelProperty("新密码(需sm2加密)")
	@NotEmpty(message = "新密码不能为空")
	private String newPwd;

	/**
	 *  Getter method for property <b>id</b>.
	 * @return property value of id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter method for property <b>id</b>.
	 *
	 * @param id value to be assigned to property id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 *  Getter method for property <b>oldPwd</b>.
	 * @return property value of oldPwd
	 */
	public String getOldPwd() {
		return oldPwd;
	}

	/**
	 * Setter method for property <b>oldPwd</b>.
	 *
	 * @param oldPwd value to be assigned to property oldPwd
	 */
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	/**
	 *  Getter method for property <b>newPwd</b>.
	 * @return property value of newPwd
	 */
	public String getNewPwd() {
		return newPwd;
	}

	/**
	 * Setter method for property <b>newPwd</b>.
	 *
	 * @param newPwd value to be assigned to property newPwd
	 */
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
