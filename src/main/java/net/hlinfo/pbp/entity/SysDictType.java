package net.hlinfo.pbp.entity;


import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "sts_dict_type",prefix="pbp_")
@Comment("系统数据字典分类表")
@ApiModel("系统数据字典分类表")
public class SysDictType extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column("type_name")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="分类名字")
	@ApiModelProperty(value="分类名字")
	@NotBlank(message = "名字不能为空")
	private String typeName;
	
	@Column("type_sort")
	@ColDefine(type = ColType.INT)
	@Comment(value="显示排序")
	@ApiModelProperty(value="显示排序")
	private int typeSort;

	/**
	 *  Getter method for property <b>typeName</b>.
	 * @return property value of typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Setter method for property <b>typeName</b>.
	 *
	 * @param typeName value to be assigned to property typeName
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 *  Getter method for property <b>typeSort</b>.
	 * @return property value of typeSort
	 */
	public int getTypeSort() {
		return typeSort;
	}

	/**
	 * Setter method for property <b>typeSort</b>.
	 *
	 * @param typeSort value to be assigned to property typeSort
	 */
	public void setTypeSort(int typeSort) {
		this.typeSort = typeSort;
	}

}
