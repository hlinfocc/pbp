package net.hlinfo.pbp.entity;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;


import io.swagger.annotations.ApiModel;

@Table(value = "oplogs",prefix="pbp_")
@Comment("操作日志")
@ApiModel("操作日志")
public class Oplogs extends BaseEntity{
	private static final long serialVersionUID = 1L;

	@Column("account")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="操作账号")
	private String account;
	
	@Column("mokuai")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="操作模块")
	private String module;
	
	@Column("pager")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="操作页面")
	private String pager;
	
	@Column("opfunc")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="操作功能")
	private String opfunc;
	
	@Column("content")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="日志内容")
	private String content;
	
	@Column("remark")
	@ColDefine(notNull=false, type=ColType.TEXT)
	@Comment(value="备注")
	private String remark;
	
	@Column("ip")
	@ColDefine(notNull=false, type=ColType.VARCHAR,width=50)
	@Comment(value="ip")
	private String ip;

	/**
	 *  Getter method for property <b>account</b>.
	 * @return property value of account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Setter method for property <b>account</b>.
	 *
	 * @param account value to be assigned to property account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 *  Getter method for property <b>module</b>.
	 * @return property value of module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Setter method for property <b>module</b>.
	 *
	 * @param module value to be assigned to property module
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 *  Getter method for property <b>pager</b>.
	 * @return property value of pager
	 */
	public String getPager() {
		return pager;
	}

	/**
	 * Setter method for property <b>pager</b>.
	 *
	 * @param pager value to be assigned to property pager
	 */
	public void setPager(String pager) {
		this.pager = pager;
	}

	/**
	 *  Getter method for property <b>opfunc</b>.
	 * @return property value of opfunc
	 */
	public String getOpfunc() {
		return opfunc;
	}

	/**
	 * Setter method for property <b>opfunc</b>.
	 *
	 * @param opfunc value to be assigned to property opfunc
	 */
	public void setOpfunc(String opfunc) {
		this.opfunc = opfunc;
	}

	/**
	 *  Getter method for property <b>content</b>.
	 * @return property value of content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter method for property <b>content</b>.
	 *
	 * @param content value to be assigned to property content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 *  Getter method for property <b>remark</b>.
	 * @return property value of remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Setter method for property <b>remark</b>.
	 *
	 * @param remark value to be assigned to property remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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

}