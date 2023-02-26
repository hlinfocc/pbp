/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Jackson;

/**
 * @author hlinfo
 *
 */
public class PermBatchVo implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="角色ID")
	@NotBlank(message ="角色ID不能为空")
	private String roleid;

	@ApiModelProperty(value="权限数组 array[permid][hasBtns] hasBtns-array")
	private Map<String,List<String>> permids;

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
	 *  Getter method for property <b>permids</b>.
	 * @return property value of permids
	 */
	public Map<String, List<String>> getPermids() {
		return permids;
	}

	/**
	 * Setter method for property <b>permids</b>.
	 *
	 * @param permids value to be assigned to property permids
	 */
	public void setPermids(Map<String, List<String>> permids) {
		this.permids = permids;
	}

	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
}
