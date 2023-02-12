package net.hlinfo.pbp.opt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("权限meta")
public class PermMeta {
	
	@ApiModelProperty("设置该路由的图标,在常规图标中拷贝即可")
	private String icon;
	
	@ApiModelProperty("如果设置为false，则不会在breadcrumb面包屑中显示")
	private boolean breadcrumb;
	
	@ApiModelProperty("如果设置为true，则不会被 <keep-alive> 缓存(默认 false)")
	private boolean noKeepAlive;
	
	@ApiModelProperty("")
	private boolean invisible;
	
	@ApiModelProperty("")
	private boolean isdisable;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(boolean breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public boolean isNoKeepAlive() {
		return noKeepAlive;
	}

	public void setNoKeepAlive(boolean noKeepAlive) {
		this.noKeepAlive = noKeepAlive;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean isIsdisable() {
		return isdisable;
	}

	public void setIsdisable(boolean isdisable) {
		this.isdisable = isdisable;
	}	
}
