package net.hlinfo.pbp.entity;


import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "files_list",prefix="pbp")
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
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="文件本地路径")
    @ApiModelProperty(value="文件本地路径")
    private String path;
    
    @Column("url")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="文件访问路径")
    @ApiModelProperty(value="文件访问路径")
    private String url;

    @Column("suffix")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="后缀")
    @ApiModelProperty(value="后缀")
    private String suffix;
    
    @Column("content_type")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="后缀")
    @ApiModelProperty(value="后缀")
    private String contentType;

    @Column("size")
    @ColDefine(type= ColType.INT)
    @Comment(value="文件大小")
    @ApiModelProperty(value="文件大小")
    private long size;

    @Column("userid")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="上传用户")
    @ApiModelProperty(value="上传用户")
    private String userid;

    @Column("file_hash")
    @ColDefine(type= ColType.VARCHAR)
    @Comment(value="文件hash值")
    @ApiModelProperty(value="文件hash值")
    private String fileHash;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}
}
