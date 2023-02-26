/**
 * 
 */
package net.hlinfo.pbp.entity;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hlinfo
 *
 */
@Table(value="article_collection",prefix = "pbp")
@ApiModel("文章收藏表")
@Comment("文章收藏表")
public class ArticleCollection extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**
     * 用户id
     */
	@Column("user_id")
	@ColDefine(type=ColType.VARCHAR, width=36)
	@Comment(value="用户id")
	@ApiModelProperty("用户id")
    private String userId;

    /**
     * 文章id
     */
	@Column("artid")
	@ColDefine(type=ColType.VARCHAR, width=32)
	@Comment(value="文章id")
	@ApiModelProperty("文章id")
    private String artid;

	/**
	 *  Getter method for property <b>userId</b>.
	 * @return property value of userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter method for property <b>userId</b>.
	 *
	 * @param userId value to be assigned to property userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 *  Getter method for property <b>artid</b>.
	 * @return property value of artid
	 */
	public String getArtid() {
		return artid;
	}

	/**
	 * Setter method for property <b>artid</b>.
	 *
	 * @param artid value to be assigned to property artid
	 */
	public void setArtid(String artid) {
		this.artid = artid;
	}

}

