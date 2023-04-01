package net.hlinfo.pbp.entity;


import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "files_list",prefix="pbp_")
@ApiModel("文件信息")
@Comment("文件信息")
public class FilesList extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column("name")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="文件名称")
    @ApiModelProperty(value="文件名称")
    private String name;

    @Column("path")
    @ColDefine(type= ColType.VARCHAR,width = 255)
    @Comment(value="文件本地路径")
    @ApiModelProperty(value="文件本地路径")
    private String path;
    
    @Column("url")
    @ColDefine(type= ColType.TEXT)
    @Comment(value="文件访问路径")
    @ApiModelProperty(value="文件访问路径")
    private String url;

    @Column("suffix")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="后缀")
    @ApiModelProperty(value="后缀")
    private String suffix;
    
    @Column("mime_type")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="文件mime类型")
    @ApiModelProperty(value="文件mime类型")
    private String mimeType;

    @Column("size")
    @ColDefine(type= ColType.INT,width = 20)
    @Comment(value="文件大小")
    @ApiModelProperty(value="文件大小")
    private long size;

    @Column("userid")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="上传用户")
    @ApiModelProperty(value="上传用户")
    private String userid;

    @Column("file_hash")
    @ColDefine(type= ColType.VARCHAR,width = 512)
    @Comment(value="文件hash值")
    @ApiModelProperty(value="文件hash值")
    private String fileHash;

	/**
	 *  Getter method for property <b>name</b>.
	 * @return property value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for property <b>name</b>.
	 *
	 * @param name value to be assigned to property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *  Getter method for property <b>path</b>.
	 * @return property value of path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Setter method for property <b>path</b>.
	 *
	 * @param path value to be assigned to property path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 *  Getter method for property <b>url</b>.
	 * @return property value of url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter method for property <b>url</b>.
	 *
	 * @param url value to be assigned to property url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 *  Getter method for property <b>suffix</b>.
	 * @return property value of suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Setter method for property <b>suffix</b>.
	 *
	 * @param suffix value to be assigned to property suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 *  Getter method for property <b>mimeType</b>.
	 * @return property value of mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Setter method for property <b>mimeType</b>.
	 *
	 * @param mimeType value to be assigned to property mimeType
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 *  Getter method for property <b>size</b>.
	 * @return property value of size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Setter method for property <b>size</b>.
	 *
	 * @param size value to be assigned to property size
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 *  Getter method for property <b>userid</b>.
	 * @return property value of userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * Setter method for property <b>userid</b>.
	 *
	 * @param userid value to be assigned to property userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 *  Getter method for property <b>fileHash</b>.
	 * @return property value of fileHash
	 */
	public String getFileHash() {
		return fileHash;
	}

	/**
	 * Setter method for property <b>fileHash</b>.
	 *
	 * @param fileHash value to be assigned to property fileHash
	 */
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

}
