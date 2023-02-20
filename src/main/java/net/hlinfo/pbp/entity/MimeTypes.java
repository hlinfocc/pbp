package net.hlinfo.pbp.entity;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import io.swagger.annotations.ApiModel;

@Table(value="mime_types",prefix = "pbp")
@ApiModel("mimetypes模块")
@Comment("mimetypes模块")
@TableIndexes({
	@Index(fields= {"contentType"}, unique=false)
})
public class MimeTypes  extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column("content_type")
	@ColDefine(type=ColType.TEXT)
	private String contentType;
	
	@Column("suffix")
	@ColDefine(type=ColType.TEXT)
	private String suffix;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
