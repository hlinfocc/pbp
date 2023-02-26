/**
 * 
 */
package net.hlinfo.pbp.opt.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;


public class AdminLoginResultDTO extends LoginResultDTO {
	@ApiModelProperty("菜单信息")
	private List<PermDTO> menus;

	/**
	 *  Getter method for property <b>menus</b>.
	 * @return property value of menus
	 */
	public List<PermDTO> getMenus() {
		return menus;
	}

	/**
	 * Setter method for property <b>menus</b>.
	 *
	 * @param menus value to be assigned to property menus
	 */
	public void setMenus(List<PermDTO> menus) {
		this.menus = menus;
	}
}
