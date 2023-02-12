package net.hlinfo.pbp.entity;

import java.util.List;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.pbp.opt.vo.PermMeta;
import net.hlinfo.pbp.usr.adaptor.JsonbToListValueAdaptor;
import net.hlinfo.pbp.usr.adaptor.PermMetaAdaptor;

@ApiModel("权限")
@Comment("权限")
@Table(value="permission",prefix="pbp")
@TableIndexes({
	@Index(unique = false, fields = {"code"}),
	@Index(unique = false, fields = {"pid"}),
	@Index(unique = false, fields = {"name"})
})
public class Permission extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	@Column("pid")
	@ColDefine(type=ColType.VARCHAR, width=32)
	@Comment(value="所属父权限，第一层为0")
	@ApiModelProperty("所属父权限，第一层为0")
	@Default(value = "0")
	private String pid;
	
	@Column("name")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="名称")
	@ApiModelProperty("名称")
	private String name;
	
	@Column("router")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="前端router")
	@ApiModelProperty("前端router")
	private String router;
	
	@Column("code")
	@ColDefine(type=ColType.TEXT)
	@Comment(value="权限code,唯一")
	@ApiModelProperty("权限code,唯一")
	private String code;
	
	@Column("create_id")
	@ColDefine(type=ColType.VARCHAR, width=32)
	@Comment(value="添加该条记录的人员id")
	@ApiModelProperty("添加该条记录的人员id")
	private String createId;
	
	@Column("sort")
	@ColDefine(type=ColType.INT, width=10)
	@Comment(value="排序")
	@ApiModelProperty("排序")
	private int sort;
	
	@Column("status")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="状态 1禁用 0启用")
	@ApiModelProperty("状态 1禁用 0启用")
	@Default("0")
	private int status;
	
	@Column("level")
	@ColDefine(type=ColType.INT, width=3)
	@Comment(value="0普通权限菜单 1特殊权限,禁用删除")
	@ApiModelProperty("0普通权限菜单 1特殊权限,禁用删除")
	private int level;
	
	@Column("remark")
	@ColDefine(type=ColType.VARCHAR, width=255)
	@Comment(value="备注")
	@ApiModelProperty("备注")
	private String remark;
	
	@Column("path")
	@ColDefine(type=ColType.VARCHAR, width=500)
	@Comment(value="路由权限的path")
	@ApiModelProperty("路由权限的path")
	private String path;
	
	@Column("need_auth")
	@ColDefine(type=ColType.BOOLEAN)
	@Comment(value="是否需要授权：false不需要 true需要")
	@ApiModelProperty("是否需要授权：false不需要 true需要")
	@Default("true")
	private boolean needAuth;

	@Column("component_str")
	@ColDefine(type=ColType.VARCHAR)
	@Comment(value="动态组件")
	@ApiModelProperty("动态组件")
	private String componentStr;
	
	@Column("hidden")
	@ColDefine(type=ColType.BOOLEAN)
	@Comment(value="设置 true 的时候该路由不会再侧边栏出现")
	@ApiModelProperty("设置 true 的时候该路由不会再侧边栏出现")
	private boolean hidden;
	
	@Column("meta")
	@ColDefine(type=ColType.TEXT, adaptor = PermMetaAdaptor.class)
	@Comment(value="前段VUE meta")
	@ApiModelProperty("前段VUE meta")
	private PermMeta meta;
	
	@Column("btns")
	@ColDefine(type=ColType.PSQL_JSON,customType = "jsonb", adaptor = JsonbToListValueAdaptor.class)
	@Comment(value="菜单按钮")
	@ApiModelProperty("菜单按钮")
	private List<String> btns;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}

	public String getComponentStr() {
		return componentStr;
	}

	public void setComponentStr(String componentStr) {
		this.componentStr = componentStr;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public PermMeta getMeta() {
		return meta;
	}

	public void setMeta(PermMeta meta) {
		this.meta = meta;
	}

	public List<String> getBtns() {
		return btns;
	}

	public void setBtns(List<String> btns) {
		this.btns = btns;
	}

}
