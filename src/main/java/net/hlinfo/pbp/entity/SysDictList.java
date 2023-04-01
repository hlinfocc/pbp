package net.hlinfo.pbp.entity;


import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "sts_dict_list",prefix="pbp_")
@Comment("系统数据字典列表")
@ApiModel("系统数据字典列表")
public class SysDictList extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Column("field_type_id")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="分类ID")
	@ApiModelProperty(value="分类ID")
	@NotBlank(message = "分类ID不能为空")
	private String fieldTypeId;
	
	@Column("field_type_name")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="分类名称")
	@ApiModelProperty(value="分类名称")
	private String fieldTypeName;
	
	@Column("field_name")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="字段名称")
	@ApiModelProperty(value="字段名称")
	@NotBlank(message = "字段名称不能为空")
	private String fieldName;
	
	@Column("field_code")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="字段代码")
	@ApiModelProperty(value="字段代码")
	@NotBlank(message = "字段代码不能为空")
	private String fieldCode;
	
	@Column("field_value")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="字段值,普通文本值就是一个普通字符串，单选多选值为一个json对象,格式为[{k, v}, {k, v}]")
	@ApiModelProperty(value="字段值,普通文本值就是一个普通字符串，单选多选值为一个json对象,格式为[{k, v}, {k, v}]")
	@NotBlank(message = "字段值不能为空")
	private String fieldValue;
	
	@Column("field_type")
	@ColDefine(type = ColType.INT)
	@Comment(value="字段类型[1:普通文本值，2:单选[k, v]值, 3:多选[k, v]值，4:富文本,5键值对[{k:'',v:''}]")
	@ApiModelProperty(value="字段类型[1:普通文本值，2:单选[k, v]值, 3:多选[k, v]值，4:富文本,5键值对[{k:'',v:''}]")
	private int fieldType;
	
	@Column("group_sort")
	@ColDefine(type=ColType.INT,width = 5)
	@Comment(value="分类组内排序")
	@ApiModelProperty(value="分类组内排序")
	private int groupSort;

	/**
	 *  Getter method for property <b>fieldTypeId</b>.
	 * @return property value of fieldTypeId
	 */
	public String getFieldTypeId() {
		return fieldTypeId;
	}

	/**
	 * Setter method for property <b>fieldTypeId</b>.
	 *
	 * @param fieldTypeId value to be assigned to property fieldTypeId
	 */
	public void setFieldTypeId(String fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}

	/**
	 *  Getter method for property <b>fieldTypeName</b>.
	 * @return property value of fieldTypeName
	 */
	public String getFieldTypeName() {
		return fieldTypeName;
	}

	/**
	 * Setter method for property <b>fieldTypeName</b>.
	 *
	 * @param fieldTypeName value to be assigned to property fieldTypeName
	 */
	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	/**
	 *  Getter method for property <b>fieldName</b>.
	 * @return property value of fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Setter method for property <b>fieldName</b>.
	 *
	 * @param fieldName value to be assigned to property fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 *  Getter method for property <b>fieldCode</b>.
	 * @return property value of fieldCode
	 */
	public String getFieldCode() {
		return fieldCode;
	}

	/**
	 * Setter method for property <b>fieldCode</b>.
	 *
	 * @param fieldCode value to be assigned to property fieldCode
	 */
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	/**
	 *  Getter method for property <b>fieldValue</b>.
	 * @return property value of fieldValue
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * Setter method for property <b>fieldValue</b>.
	 *
	 * @param fieldValue value to be assigned to property fieldValue
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 *  Getter method for property <b>fieldType</b>.
	 * @return property value of fieldType
	 */
	public int getFieldType() {
		return fieldType;
	}

	/**
	 * Setter method for property <b>fieldType</b>.
	 *
	 * @param fieldType value to be assigned to property fieldType
	 */
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 *  Getter method for property <b>groupSort</b>.
	 * @return property value of groupSort
	 */
	public int getGroupSort() {
		return groupSort;
	}

	/**
	 * Setter method for property <b>groupSort</b>.
	 *
	 * @param groupSort value to be assigned to property groupSort
	 */
	public void setGroupSort(int groupSort) {
		this.groupSort = groupSort;
	}
}
