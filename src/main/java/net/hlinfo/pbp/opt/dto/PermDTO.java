/**
 * 
 */
package net.hlinfo.pbp.opt.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.pbp.entity.Permission;


public class PermDTO extends Permission {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("前段menu组件的title")
	private String title;
	
	@ApiModelProperty("有的按钮权限")
	private List<String> hasBtns;
	
	@ApiModelProperty("子权限")
	private List<PermDTO> children;

	/**
	 *  Getter method for property <b>title</b>.
	 * @return property value of title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for property <b>title</b>.
	 *
	 * @param title value to be assigned to property title
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 *  Getter method for property <b>children</b>.
	 * @return property value of children
	 */
	public List<PermDTO> getChildren() {
		return children;
	}

	/**
	 * Setter method for property <b>children</b>.
	 *
	 * @param children value to be assigned to property children
	 */
	public void setChildren(List<PermDTO> children) {
		this.children = children;
	}
}
