/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author hlinfo
 *
 */
public class MergeInfo  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("文件名")
    private String filename;
    
    @ApiModelProperty("文件类型")
    private String type;
    
    @ApiModelProperty("文件哈希值")
    private String hash;

	/**
	 *  Getter method for property <b>filename</b>.
	 * @return property value of filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Setter method for property <b>filename</b>.
	 *
	 * @param filename value to be assigned to property filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 *  Getter method for property <b>type</b>.
	 * @return property value of type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter method for property <b>type</b>.
	 *
	 * @param type value to be assigned to property type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 *  Getter method for property <b>hash</b>.
	 * @return property value of hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Setter method for property <b>hash</b>.
	 *
	 * @param hash value to be assigned to property hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
}
