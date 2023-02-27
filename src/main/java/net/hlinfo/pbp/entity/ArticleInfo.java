package net.hlinfo.pbp.entity;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.lang.util.NutMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import net.hlinfo.pbp.usr.adaptor.JsonbToListListValueAdaptor;
import net.hlinfo.pbp.usr.adaptor.ListNutMapJsonAdaptor;

@Table(value = "article_info",prefix = "pbp")
@Comment("通用文章表")
@ApiModel("通用文章表")
@TableIndexes({
	@Index(fields = {"acids"},unique=false),
	@Index(fields = {"status"},unique=false),
	@Index(fields = {"hots"},unique=false),
	@Index(fields = {"contentType"},unique=false),
	@Index(fields = {"visit"},unique=false),
	@Index(fields = {"auditStatus"},unique=false),
	@Index(fields = {"istop"},unique=false),
	@Index(fields = {"codes"},unique=false)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	@Column("acids")
	@ColDefine(type=ColType.PSQL_JSON,customType = "jsonb", adaptor = JsonbToListListValueAdaptor.class)
	@Comment(value="文章类别id [[id_p1, id_p2, ...],[id1,id2,...]]")
	@ApiModelProperty(value="文章类别id [[id_p1, id_p2, ...],[id1,id2,...]]")
	@NotEmpty(message = "请选择文章类别")
	private List<List<String>> acids;
	
	@Column("acnames")
	@ColDefine(type=ColType.PSQL_JSON,customType = "jsonb", adaptor = JsonbToListListValueAdaptor.class)
	@Comment(value="文章类别名称 格式同ID")
	@ApiModelProperty(value="文章类别名称 格式同ID]")
	private List<List<String>> acnames;
	
	@Column("codes")
	@ColDefine(type=ColType.PSQL_JSON,customType = "jsonb", adaptor = JsonbToListListValueAdaptor.class)
	@Comment(value="文章类别代码 格式同ID")
	@ApiModelProperty(value="文章类别代码 格式同ID]")
	private List<List<String>> codes;
	
	@Column("title")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="文章标题")
	@ApiModelProperty(value="文章标题")
	@NotBlank(message = "文章标题不能为空")
	private String title;
	
	@Column("title_pic_url")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="标题图")
	@ApiModelProperty(value="标题图")
	private String titlePicUrl;
	
	@Column("focus_pic_url")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="焦点图,一般情况只要标题图即可")
	@ApiModelProperty(value="焦点图,一般情况只要标题图即可")
	private String focusPicUrl;
	
	@Column("rbeditor")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="责任编辑")
	@ApiModelProperty(value="责任编辑")
	private String rbeditor;
	
	@Column("pushdate")
	@ColDefine(type=ColType.VARCHAR, width=50)
	@Comment(value="发布时间")
	@ApiModelProperty(value="发布时间")
	@NotBlank(message = "发布时间不能为空")
	private String pushdate;
	
	@Column("push_year")
	@ColDefine(type=ColType.INT,customType = "integer")
	@Comment(value="发文年度")
	@ApiModelProperty(value="发文年度")
	private int pushYear;
	
	@Column("status")
	@ColDefine(type=ColType.INT)
	@Comment(value="状态：1草稿，2发布，3撤稿, 4归档")
	@ApiModelProperty(value="状态：1草稿，2发布，3撤稿, 4归档")
	private int status;
	
	@Column("hots")
	@ColDefine(type=ColType.INT)
	@Comment(value="热门：0正常 1热门")
	@ApiModelProperty(value="热门：0正常 1热门")
	@Default("0")
	private int hots;
	
	@Column("hots_start_date")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="热门开始时间")
	@ApiModelProperty(value="热门开始时间")
	private String hotsStartDate;
	
	@Column("hots_end_date")
	@ColDefine(type=ColType.VARCHAR, width=25)
	@Comment(value="热门结束时间")
	@ApiModelProperty(value="热门结束时间")
	private String hotsEndDate;
	
	@Column("visit")
	@ColDefine(type=ColType.INT)
	@Comment(value="浏览量")
	@ApiModelProperty(value="浏览量")
	@Default("0")
	private int visit;
	
	@Column("create_id")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="创建人ID")
	@ApiModelProperty(value="创建人ID")
	private String createId;
	
	@Column("source")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="来源")
	@ApiModelProperty(value="来源")
	private String source;
	
	@Column("source_url")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="来源url")
	@ApiModelProperty(value="来源url")
	private String sourceUrl;
	
	@Column("keywords")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="关键词")
	@ApiModelProperty(value="关键词")
	private String keywords;
	
	@Column("summary")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="摘要")
	@ApiModelProperty(value="摘要")
	private String summary;
	
	@Column("content_type")
	@ColDefine(type=ColType.INT,customType = "integer")
	@Comment(value="内容类型，0是图文，1是pdf,2跳转链接")
	@ApiModelProperty(value="内容类型，0是图文，1是pdf,2跳转链接")
	@Default("0")
	private int contentType;
	
	@Column("link_url")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="PDF地址或链接地址，仅contentType为1或者2是有效")
	@ApiModelProperty(value="PDF地址或链接地址，仅contentType为1或者2是有效")
	private String linkUrl;
	
	@Column("text_content")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="文章纯文本内容，不包含html标签")
	@ApiModelProperty(value="文章纯文本内容，不包含html标签")
	private String textContent;
	
	@Column("content")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="文章内容，包含html标签")
	@ApiModelProperty(value="文章内容，包含html标签")
	private String content;
	
	@Column("level")
	@ColDefine(type=ColType.INT)
	@Comment(value="地区级别：[1-4]=>[国家级，省, 市/州, 区/县]")
	@ApiModelProperty(value="地区级别：[1-4]=>[国家级，省, 市/州, 区/县]")
	private int level;
	
	@Column("provincecode")
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment(value="省代码")
	@ApiModelProperty(value="省代码")
	private String provincecode;
	
	@Column("provincename")
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment(value="省名字")
	@ApiModelProperty(value="省名字")
	private String provincename;
	
	@Column("citycode")
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment(value="市代码")
	@ApiModelProperty(value="市代码")
	private String citycode;
	
	@Column("cityname")
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment(value="市名字")
	@ApiModelProperty(value="市名字")
	private String cityname;
	
	@Column("countycode")
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment(value="区代码")
	@ApiModelProperty(value="区代码")
	private String countycode;
	
	@Column("countyname")
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment(value="区名字")
	@ApiModelProperty(value="区名字")
	private String countyname;
	
	@Column("audit_status")
	@ColDefine(type=ColType.INT,customType = "integer")
	@Comment(value="审核状态，0未审核，1通过，2不通过")
	@ApiModelProperty(value="审核状态，0未审核，1通过，2不通过")
	@Default("0")
	private int auditStatus;
	
	@Column("audit_uid")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="审核人ID")
	@ApiModelProperty(value="审核人ID")
	private String auditUid;
	
	@Column("audit_name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="审核人")
	@ApiModelProperty(value="审核人")
	private String auditName;
	
	@Column("audit_time")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="审核时间")
	@ApiModelProperty(value="审核时间")
	private String auditTime;
	
	@Column("istop")
	@ColDefine(type=ColType.INT)
	@Comment(value="是否置顶：0不置顶 1置顶")
	@ApiModelProperty(value="是否置顶：0不置顶 1置顶")
	@Default("0")
	private int istop;
	
	@Column("issued_number")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="文号")
	@ApiModelProperty(value="文号")
	private String issuedNumber;
	
	@Column("extended_field")
	@ColDefine(type=ColType.PSQL_JSON,customType = "jsonb", adaptor = ListNutMapJsonAdaptor.class)
	@Comment(value="自定义扩展字段信息")
	@ApiModelProperty(value="自定义扩展字段信息")
	private NutMap extendedField;
	
	@ApiModelProperty(value="【查询用】是否已读，1已读，0 未读,-1未查询是否已读")
	private int isRead = -1;
	
	@ApiModelProperty(value="【查询用】是否收藏，1已收藏，0 未收藏,-1未查询是否已收藏")
	private int isCollect = -1;

	/**
	 *  Getter method for property <b>acids</b>.
	 * @return property value of acids
	 */
	public List<List<String>> getAcids() {
		return acids;
	}

	/**
	 * Setter method for property <b>acids</b>.
	 *
	 * @param acids value to be assigned to property acids
	 */
	public void setAcids(List<List<String>> acids) {
		this.acids = acids;
	}

	/**
	 *  Getter method for property <b>acnames</b>.
	 * @return property value of acnames
	 */
	public List<List<String>> getAcnames() {
		return acnames;
	}

	/**
	 * Setter method for property <b>acnames</b>.
	 *
	 * @param acnames value to be assigned to property acnames
	 */
	public void setAcnames(List<List<String>> acnames) {
		this.acnames = acnames;
	}

	/**
	 *  Getter method for property <b>codes</b>.
	 * @return property value of codes
	 */
	public List<List<String>> getCodes() {
		return codes;
	}

	/**
	 * Setter method for property <b>codes</b>.
	 *
	 * @param codes value to be assigned to property codes
	 */
	public void setCodes(List<List<String>> codes) {
		this.codes = codes;
	}

	/**
	 *  Getter method for property <b>title</b>.
	 * @return property value of title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for property <b>title</b>.
	 *
	 * @param title value to be assigned to property title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *  Getter method for property <b>titlePicUrl</b>.
	 * @return property value of titlePicUrl
	 */
	public String getTitlePicUrl() {
		return titlePicUrl;
	}

	/**
	 * Setter method for property <b>titlePicUrl</b>.
	 *
	 * @param titlePicUrl value to be assigned to property titlePicUrl
	 */
	public void setTitlePicUrl(String titlePicUrl) {
		this.titlePicUrl = titlePicUrl;
	}

	/**
	 *  Getter method for property <b>focusPicUrl</b>.
	 * @return property value of focusPicUrl
	 */
	public String getFocusPicUrl() {
		return focusPicUrl;
	}

	/**
	 * Setter method for property <b>focusPicUrl</b>.
	 *
	 * @param focusPicUrl value to be assigned to property focusPicUrl
	 */
	public void setFocusPicUrl(String focusPicUrl) {
		this.focusPicUrl = focusPicUrl;
	}

	/**
	 *  Getter method for property <b>rbeditor</b>.
	 * @return property value of rbeditor
	 */
	public String getRbeditor() {
		return rbeditor;
	}

	/**
	 * Setter method for property <b>rbeditor</b>.
	 *
	 * @param rbeditor value to be assigned to property rbeditor
	 */
	public void setRbeditor(String rbeditor) {
		this.rbeditor = rbeditor;
	}

	/**
	 *  Getter method for property <b>pushdate</b>.
	 * @return property value of pushdate
	 */
	public String getPushdate() {
		return pushdate;
	}

	/**
	 * Setter method for property <b>pushdate</b>.
	 *
	 * @param pushdate value to be assigned to property pushdate
	 */
	public void setPushdate(String pushdate) {
		this.pushdate = pushdate;
	}

	/**
	 *  Getter method for property <b>pushYear</b>.
	 * @return property value of pushYear
	 */
	public int getPushYear() {
		return pushYear;
	}

	/**
	 * Setter method for property <b>pushYear</b>.
	 *
	 * @param pushYear value to be assigned to property pushYear
	 */
	public void setPushYear(int pushYear) {
		this.pushYear = pushYear;
	}

	/**
	 *  Getter method for property <b>status</b>.
	 * @return property value of status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Setter method for property <b>status</b>.
	 *
	 * @param status value to be assigned to property status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 *  Getter method for property <b>hots</b>.
	 * @return property value of hots
	 */
	public int getHots() {
		return hots;
	}

	/**
	 * Setter method for property <b>hots</b>.
	 *
	 * @param hots value to be assigned to property hots
	 */
	public void setHots(int hots) {
		this.hots = hots;
	}

	/**
	 *  Getter method for property <b>hotsStartDate</b>.
	 * @return property value of hotsStartDate
	 */
	public String getHotsStartDate() {
		return hotsStartDate;
	}

	/**
	 * Setter method for property <b>hotsStartDate</b>.
	 *
	 * @param hotsStartDate value to be assigned to property hotsStartDate
	 */
	public void setHotsStartDate(String hotsStartDate) {
		this.hotsStartDate = hotsStartDate;
	}

	/**
	 *  Getter method for property <b>hotsEndDate</b>.
	 * @return property value of hotsEndDate
	 */
	public String getHotsEndDate() {
		return hotsEndDate;
	}

	/**
	 * Setter method for property <b>hotsEndDate</b>.
	 *
	 * @param hotsEndDate value to be assigned to property hotsEndDate
	 */
	public void setHotsEndDate(String hotsEndDate) {
		this.hotsEndDate = hotsEndDate;
	}

	/**
	 *  Getter method for property <b>visit</b>.
	 * @return property value of visit
	 */
	public int getVisit() {
		return visit;
	}

	/**
	 * Setter method for property <b>visit</b>.
	 *
	 * @param visit value to be assigned to property visit
	 */
	public void setVisit(int visit) {
		this.visit = visit;
	}

	/**
	 *  Getter method for property <b>createId</b>.
	 * @return property value of createId
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * Setter method for property <b>createId</b>.
	 *
	 * @param createId value to be assigned to property createId
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 *  Getter method for property <b>source</b>.
	 * @return property value of source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Setter method for property <b>source</b>.
	 *
	 * @param source value to be assigned to property source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 *  Getter method for property <b>sourceUrl</b>.
	 * @return property value of sourceUrl
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}

	/**
	 * Setter method for property <b>sourceUrl</b>.
	 *
	 * @param sourceUrl value to be assigned to property sourceUrl
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	/**
	 *  Getter method for property <b>keywords</b>.
	 * @return property value of keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Setter method for property <b>keywords</b>.
	 *
	 * @param keywords value to be assigned to property keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 *  Getter method for property <b>summary</b>.
	 * @return property value of summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Setter method for property <b>summary</b>.
	 *
	 * @param summary value to be assigned to property summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 *  Getter method for property <b>contentType</b>.
	 * @return property value of contentType
	 */
	public int getContentType() {
		return contentType;
	}

	/**
	 * Setter method for property <b>contentType</b>.
	 *
	 * @param contentType value to be assigned to property contentType
	 */
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	/**
	 *  Getter method for property <b>linkUrl</b>.
	 * @return property value of linkUrl
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * Setter method for property <b>linkUrl</b>.
	 *
	 * @param linkUrl value to be assigned to property linkUrl
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 *  Getter method for property <b>textContent</b>.
	 * @return property value of textContent
	 */
	public String getTextContent() {
		return textContent;
	}

	/**
	 * Setter method for property <b>textContent</b>.
	 *
	 * @param textContent value to be assigned to property textContent
	 */
	public void setTextContent(String textContent) {
		this.textContent = textContent;
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
	 *  Getter method for property <b>level</b>.
	 * @return property value of level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Setter method for property <b>level</b>.
	 *
	 * @param level value to be assigned to property level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 *  Getter method for property <b>provincecode</b>.
	 * @return property value of provincecode
	 */
	public String getProvincecode() {
		return provincecode;
	}

	/**
	 * Setter method for property <b>provincecode</b>.
	 *
	 * @param provincecode value to be assigned to property provincecode
	 */
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}

	/**
	 *  Getter method for property <b>provincename</b>.
	 * @return property value of provincename
	 */
	public String getProvincename() {
		return provincename;
	}

	/**
	 * Setter method for property <b>provincename</b>.
	 *
	 * @param provincename value to be assigned to property provincename
	 */
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	/**
	 *  Getter method for property <b>citycode</b>.
	 * @return property value of citycode
	 */
	public String getCitycode() {
		return citycode;
	}

	/**
	 * Setter method for property <b>citycode</b>.
	 *
	 * @param citycode value to be assigned to property citycode
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	/**
	 *  Getter method for property <b>cityname</b>.
	 * @return property value of cityname
	 */
	public String getCityname() {
		return cityname;
	}

	/**
	 * Setter method for property <b>cityname</b>.
	 *
	 * @param cityname value to be assigned to property cityname
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	/**
	 *  Getter method for property <b>countycode</b>.
	 * @return property value of countycode
	 */
	public String getCountycode() {
		return countycode;
	}

	/**
	 * Setter method for property <b>countycode</b>.
	 *
	 * @param countycode value to be assigned to property countycode
	 */
	public void setCountycode(String countycode) {
		this.countycode = countycode;
	}

	/**
	 *  Getter method for property <b>countyname</b>.
	 * @return property value of countyname
	 */
	public String getCountyname() {
		return countyname;
	}

	/**
	 * Setter method for property <b>countyname</b>.
	 *
	 * @param countyname value to be assigned to property countyname
	 */
	public void setCountyname(String countyname) {
		this.countyname = countyname;
	}

	/**
	 *  Getter method for property <b>auditStatus</b>.
	 * @return property value of auditStatus
	 */
	public int getAuditStatus() {
		return auditStatus;
	}

	/**
	 * Setter method for property <b>auditStatus</b>.
	 *
	 * @param auditStatus value to be assigned to property auditStatus
	 */
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	/**
	 *  Getter method for property <b>auditUid</b>.
	 * @return property value of auditUid
	 */
	public String getAuditUid() {
		return auditUid;
	}

	/**
	 * Setter method for property <b>auditUid</b>.
	 *
	 * @param auditUid value to be assigned to property auditUid
	 */
	public void setAuditUid(String auditUid) {
		this.auditUid = auditUid;
	}

	/**
	 *  Getter method for property <b>auditName</b>.
	 * @return property value of auditName
	 */
	public String getAuditName() {
		return auditName;
	}

	/**
	 * Setter method for property <b>auditName</b>.
	 *
	 * @param auditName value to be assigned to property auditName
	 */
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	/**
	 *  Getter method for property <b>auditTime</b>.
	 * @return property value of auditTime
	 */
	public String getAuditTime() {
		return auditTime;
	}

	/**
	 * Setter method for property <b>auditTime</b>.
	 *
	 * @param auditTime value to be assigned to property auditTime
	 */
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	/**
	 *  Getter method for property <b>istop</b>.
	 * @return property value of istop
	 */
	public int getIstop() {
		return istop;
	}

	/**
	 * Setter method for property <b>istop</b>.
	 *
	 * @param istop value to be assigned to property istop
	 */
	public void setIstop(int istop) {
		this.istop = istop;
	}

	/**
	 *  Getter method for property <b>issuedNumber</b>.
	 * @return property value of issuedNumber
	 */
	public String getIssuedNumber() {
		return issuedNumber;
	}

	/**
	 * Setter method for property <b>issuedNumber</b>.
	 *
	 * @param issuedNumber value to be assigned to property issuedNumber
	 */
	public void setIssuedNumber(String issuedNumber) {
		this.issuedNumber = issuedNumber;
	}

	/**
	 *  Getter method for property <b>extendedField</b>.
	 * @return property value of extendedField
	 */
	public NutMap getExtendedField() {
		return extendedField;
	}

	/**
	 * Setter method for property <b>extendedField</b>.
	 *
	 * @param extendedField value to be assigned to property extendedField
	 */
	public void setExtendedField(NutMap extendedField) {
		this.extendedField = extendedField;
	}

	/**
	 *  Getter method for property <b>isRead</b>.
	 * @return property value of isRead
	 */
	public int getIsRead() {
		return isRead;
	}

	/**
	 * Setter method for property <b>isRead</b>.
	 *
	 * @param isRead value to be assigned to property isRead
	 */
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	/**
	 *  Getter method for property <b>isCollect</b>.
	 * @return property value of isCollect
	 */
	public int getIsCollect() {
		return isCollect;
	}

	/**
	 * Setter method for property <b>isCollect</b>.
	 *
	 * @param isCollect value to be assigned to property isCollect
	 */
	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}
}
