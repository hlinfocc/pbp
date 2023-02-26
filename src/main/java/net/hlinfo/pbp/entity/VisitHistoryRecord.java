package net.hlinfo.pbp.entity;

import javax.validation.constraints.NotBlank;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table(value = "visit_history_record",prefix = "pbp")
@Comment("访问历史记录表")
@ApiModel("访问历史记录表")
public class VisitHistoryRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Column("target_id")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="访问目标id")
	@NotBlank(message = "访问目标id不能为空")
	@ApiModelProperty(value="访问目标id")
	private String targetId;
	
	@Column("memid")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="访问人员id，0表示匿名未登录访问的")
	@ApiModelProperty(value="访问人员id，0表示匿名未登录访问的")
	@Default("0")
	private String memid;
	
	@Column("memtype")
	@ColDefine(type=ColType.INT)
	@Comment(value="用户类型：-1匿名未登录用户，0普通用户")
	@ApiModelProperty(value="用户类型：-1匿名未登录用户，0普通用户")
	@Default(value="0")
	private int memtype;
	
	@Column("type")
	@ColDefine(type=ColType.INT)
	@Comment(value="类型：0文章,其他请自定义")
	@ApiModelProperty(value="类型：0文章,其他请自定义")
	@Default(value="0")
	private int type;

	@Column(value="ip")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment(value="访问ip")
	@ApiModelProperty(value="访问ip")
	private String ip;
	
	@Column(value="user_agent")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="浏览器UA")
	@ApiModelProperty(value="浏览器UA")
	private String userAgent;

	/**
	 *  Getter method for property <b>userAgent</b>.
	 * @return property value of userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Setter method for property <b>userAgent</b>.
	 *
	 * @param userAgent value to be assigned to property userAgent
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 *  Getter method for property <b>targetId</b>.
	 * @return property value of targetId
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * Setter method for property <b>targetId</b>.
	 *
	 * @param targetId value to be assigned to property targetId
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 *  Getter method for property <b>memid</b>.
	 * @return property value of memid
	 */
	public String getMemid() {
		return memid;
	}

	/**
	 * Setter method for property <b>memid</b>.
	 *
	 * @param memid value to be assigned to property memid
	 */
	public void setMemid(String memid) {
		this.memid = memid;
	}

	/**
	 *  Getter method for property <b>memtype</b>.
	 * @return property value of memtype
	 */
	public int getMemtype() {
		return memtype;
	}

	/**
	 * Setter method for property <b>memtype</b>.
	 *
	 * @param memtype value to be assigned to property memtype
	 */
	public void setMemtype(int memtype) {
		this.memtype = memtype;
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
	 *  Getter method for property <b>ip</b>.
	 * @return property value of ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Setter method for property <b>ip</b>.
	 *
	 * @param ip value to be assigned to property ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 *  Getter method for property <b>serialversionuid</b>.
	 * @return property value of serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
