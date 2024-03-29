package net.hlinfo.pbp.entity;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import net.hlinfo.opt.Jackson;

@Table(value = "areacode",prefix = "pbp_")
@Comment("地址表")
@ApiModel("地址表")
public class Areacode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Name
	@Column("code")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=20)
	@Comment(value="行政区划代码")
	private String areaCode;
	
	@Column("area_name")
	@ColDefine(notNull=true, type=ColType.TEXT)
	@Comment(value="名称")
	private String areaName;
	
	@Column("type")
	@ColDefine(notNull=true, type=ColType.INT, width=5)
	@Comment(value="父级ID")
	private int type;
	
	@Column("parent_code")
	@ColDefine(notNull=true, type=ColType.VARCHAR, width=20)
	@Comment(value="父级ID")
	private String parentCode;
	
	@Column("area_name_alias")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="名称别名")
	private String areaNameAlias;
	
	@Column("display")
	@ColDefine(notNull=true, type=ColType.INT, customType = "integer")
	@Comment(value="是否显示，0显示，1不显示")
	@Default("0")
	private int display;

	/**
	 *  Getter method for property <b>areaCode</b>.
	 * @return property value of areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * Setter method for property <b>areaCode</b>.
	 *
	 * @param areaCode value to be assigned to property areaCode
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 *  Getter method for property <b>areaName</b>.
	 * @return property value of areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * Setter method for property <b>areaName</b>.
	 *
	 * @param areaName value to be assigned to property areaName
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 *  Getter method for property <b>type</b>.
	 * @return property value of type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setter method for property <b>type</b>.
	 *
	 * @param type value to be assigned to property type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 *  Getter method for property <b>parentCode</b>.
	 * @return property value of parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * Setter method for property <b>parentCode</b>.
	 *
	 * @param parentCode value to be assigned to property parentCode
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 *  Getter method for property <b>areaNameAlias</b>.
	 * @return property value of areaNameAlias
	 */
	public String getAreaNameAlias() {
		return areaNameAlias;
	}

	/**
	 * Setter method for property <b>areaNameAlias</b>.
	 *
	 * @param areaNameAlias value to be assigned to property areaNameAlias
	 */
	public void setAreaNameAlias(String areaNameAlias) {
		this.areaNameAlias = areaNameAlias;
	}

	/**
	 *  Getter method for property <b>display</b>.
	 * @return property value of display
	 */
	public int getDisplay() {
		return display;
	}

	/**
	 * Setter method for property <b>display</b>.
	 *
	 * @param display value to be assigned to property display
	 */
	public void setDisplay(int display) {
		this.display = display;
	}

	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}

}
