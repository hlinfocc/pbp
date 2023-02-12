package net.hlinfo.pbp.opt.vo;

import io.swagger.annotations.ApiModel;

@ApiModel("KV类")
public class KV {
	private String k;
	private Object v;
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public Object getV() {
		return v;
	}
	public void setV(Object v) {
		this.v = v;
	}
}