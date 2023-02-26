/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Jackson;

/**
 * @author hlinfo
 *
 */
public class RolePermParamVo implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("需要授权的角色id")
	@NotBlank(message = "需要授权的角色id不能为空")
	private String roleid;
	
	@ApiModelProperty("给角色授权的权限id")
	@NotBlank(message = "给角色授权的权限id不能为空")
	private String permid;
	
	@ApiModelProperty("拥有的按钮")
	private List<String> hasBtns;
	
	@ApiModelProperty("是否选中: false没选中  true选中")
	private boolean selected;
	
	/**
	 *  Getter method for property <b>roleid</b>.
	 * @return property value of roleid
	 */
	public String getRoleid() {
		return roleid;
	}

	/**
	 * Setter method for property <b>roleid</b>.
	 *
	 * @param roleid value to be assigned to property roleid
	 */
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	/**
	 *  Getter method for property <b>permid</b>.
	 * @return property value of permid
	 */
	public String getPermid() {
		return permid;
	}

	/**
	 * Setter method for property <b>permid</b>.
	 *
	 * @param permid value to be assigned to property permid
	 */
	public void setPermid(String permid) {
		this.permid = permid;
	}

	/**
	 *  Getter method for property <b>hasBtns</b>.
	 * @return property value of hasBtns
	 */
	public List<String> getHasBtns() {
		return hasBtns;
	}

	/**
	 * Setter method for property <b>hasBtns</b>.
	 *
	 * @param hasBtns value to be assigned to property hasBtns
	 */
	public void setHasBtns(List<String> hasBtns) {
		this.hasBtns = hasBtns;
	}

	/**
	 *  Getter method for property <b>selected</b>.
	 * @return property value of selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Setter method for property <b>selected</b>.
	 *
	 * @param selected value to be assigned to property selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
}
