/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("批量修改数据参数")
public class UpdateBatchVo implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("ID集合")
	@NotEmpty(message = "ID不能为空")
	private List<String> ids;
	
	@ApiModelProperty("数据集合，K为字段属性，V为值")
	@NotEmpty(message = "数据集合不能为空")
	private List<KV> datas;
	/**
	 *  Getter method for property <b>ids</b>.
	 * @return property value of ids
	 */
	public List<String> getIds() {
		return ids;
	}
	/**
	 * Setter method for property <b>ids</b>.
	 *
	 * @param ids value to be assigned to property ids
	 */
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	/**
	 *  Getter method for property <b>datas</b>.
	 * @return property value of datas
	 */
	public List<KV> getDatas() {
		return datas;
	}
	/**
	 * Setter method for property <b>datas</b>.
	 *
	 * @param datas value to be assigned to property datas
	 */
	public void setDatas(List<KV> datas) {
		this.datas = datas;
	}
	
}
